<template>
  <div
    v-loading="loading"
    class="jurisdiction-box">
    <div>
      <div class="jurisdiction-edit">
        <el-button
          v-if="currentData.code"
          class="xr-btn--red"
          type="primary"
          @click="handleDelete">
          删除
        </el-button>
        <el-button
          type="primary"
          @click="handleSubmit">
          保存
        </el-button>
      </div>
      <div class="jurisdiction-content">
        <el-form
          ref="createForm"
          :model="fieldForm"
          :rules="fieldRule"
          :validate-on-rule-change="false"
          class="form"
          label-position="top">
          <lego-create-sections title="基本信息">
            <form-items
              v-for="(children, index) in baseFieldList"
              :key="index"
              :field-form="fieldForm"
              :field-list="children"
              @change="handleChangeValue"
            />
          </lego-create-sections>
          <Attribute
            :currentData="currentData"
            :fieldForm="fieldForm"
            :fieldRule="fieldRule"
            :selected="selected"
            :tableCode="tableCode"/>
        </el-form>
      </div>
    </div>
  </div>
</template>
<script>
import { mapGetters } from 'vuex'
import LegoCreateSections from '@/components/Lego/LegoCreateSections'
import FormItems from '@/components/Common/Form/FormItems'
import Attribute from './Attribute'

import {
  genTableColumnAddAPI,
  genTableColumnModifyAPI,
  genTableColumnDeleteAPI
} from '@/api/admin/genTableColumn'
import { customFieldTypeListAPI } from '@/api/admin/formField'
import { getFormFieldValue, showFormErrorMessage } from '@/components/Common/Form/utils'
import GenerateRulesMixin from '@/components/Mixins/GenerateRules'
export default {
  components: {
    LegoCreateSections,
    Attribute,
    FormItems
  },
  mixins: [GenerateRulesMixin],
  props: {
    currentData: {
      type: Object,
      default: () => {
        return {}
      }
    },
    selected: {
      type: Array,
      default: () => {
        return []
      }
    },
    tableCode: String
  },
  computed: {
    ...mapGetters(['manage']),
    requestAPI() {
      return this.operationType === 'update' ? genTableColumnModifyAPI : genTableColumnAddAPI
    }
  },
  data() {
    return {
      loading: false,
      operationType: 'update',
      formTypeList: [],
      baseFieldList: [
        [
          { fieldCode: 'name', name: '字段列名', formType: 'text', required: true, inputTips: '一般与数据库表字段命名一致', tipType: 'tooltip' },
          { fieldCode: 'comment', name: '字段描述', formType: 'text', required: true }
        ],
        [
          { fieldCode: 'dataType', name: '物理类型', formType: 'text', disabled: true, inputTips: '数据库字段类型', tipType: 'tooltip' },
          { fieldCode: 'javaField', name: 'java属性', formType: 'text', required: true, inputTips: '生成JAVA代码的属性名称', tipType: 'tooltip' }
        ],
        [
          { fieldCode: 'formType', name: '字段类型', formType: 'select', required: true, filterable: true, inputTips: '对应自定义表单内的类型，用于定义字段生成代码的类型', tipType: 'tooltip' },
          { fieldCode: 'javaFieldType', name: 'java类型', formType: 'text', editable: true, disabled: true, inputTips: '生成JAVA代码的属性类型', tipType: 'tooltip' },
          { fieldCode: 'relativeTable', name: '关联表', formType: 'entity', fieldList: [
            { fieldCode: 'code', name: '表名', formType: 'select', width: '150' },
            { fieldCode: 'name', name: '功能名称', formType: 'text', width: '150' },
            { fieldCode: 'comment', name: '描述', formType: 'text', width: '150' },
            { fieldCode: 'packageName', name: '包名', formType: 'text', width: '150' },
            { fieldCode: 'appCode', name: '模块编码', formType: 'text', width: '100' },
            { fieldCode: 'className', name: '类名', formType: 'text', width: '100' },
            { fieldCode: 'permissionCode', name: '菜单编码', formType: 'text', width: '100' }
          ],
          searchKey: 'code', queryApi: '/back-end/sys-gen-table/list', editable: false, required: true, inputTips: '如果为ID外键字段则选择关联的其他表信息', tipType: 'tooltip' }
        ],
        [
          { fieldCode: 'sn', name: '序号', formType: 'text', stylePercent: '50', inputTips: '序号越小越靠前', tipType: 'tooltip' },
          { fieldCode: 'required', name: '必填', formType: 'boolean', stylePercent: '25', inputTips: '对应数据库的非空字段', tipType: 'tooltip' },
          { fieldCode: 'unique', name: '唯一', formType: 'boolean', stylePercent: '25', inputTips: '唯一字段一般对应数据库的唯一索引，表示不能重复录入的值', tipType: 'tooltip' }
        ]
      ],
      fieldForm: {},
      fieldRule: {}
    }
  },
  watch: {
    currentData: {
      handler() {
        this.resetForm()
      },
      deep: true
    }
  },
  mounted() {
    customFieldTypeListAPI().then(res => {
      this.formTypeList = res.data
      this.resetForm()
    })
  },
  methods: {
    resetForm() {
      if (this.currentData.code) {
        this.operationType = 'update'
      } else {
        this.operationType = 'add'
        this.$set(this.fieldForm, 'tableCode', this.tableCode)
      }
      this.$set(this.fieldForm, 'code', this.currentData.code)
      this.baseFieldList.forEach(fields => {
        fields.forEach(field => {
          this.fieldRule[field.fieldCode] = this.getRules(field)
          this.$set(field, 'value', this.currentData[field.fieldCode])
          if (field.fieldCode === 'formType') {
            field.setting = this.formTypeList
            if (field.value === 'entity') {
              this.resetFormType('javaFieldType', 'relativeTable')
            } else {
              this.resetFormType('relativeTable', 'javaFieldType')
            }
          }
          this.$set(this.fieldForm, field.fieldCode, getFormFieldValue(field, false))
        })
      })
    },
    resetFormType(fieldCode, showFieldCode) {
      this.baseFieldList.forEach(fields => {
        fields.forEach(field => {
          if (field.fieldCode === fieldCode) {
            field.editable = false
          }
          if (field.fieldCode === showFieldCode) {
            field.editable = true
          }
        })
      })
    },
    handleChangeValue(field, index, value) {
      if (field.fieldCode === 'formType') {
        if (value === 'entity') {
          this.resetFormType('javaFieldType', 'relativeTable')
        } else {
          this.resetFormType('relativeTable', 'javaFieldType')
        }
        const item = this.formTypeList.find(formType => formType.code === value)
        if (item) {
          this.$set(this.fieldForm, 'javaFieldType', item.type)
        }
      }
    },
    handleSubmit() {
      this.loading = true
      const createForm = this.$refs.createForm
      createForm.validate(valid => {
        if (!valid) {
          this.loading = false
          showFormErrorMessage(createForm)
          return false
        }
        this.requestAPI(this.fieldForm).then(() => {
          this.loading = false
          this.$message.success('提交成功！')
          this.$emit('success')
        }).catch(() => {
          this.loading = false
        })
      })
    },
    handleAdd() {
      this.operationType = 'add'
      this.baseFieldList.forEach(fields => {
        fields.forEach(field => {
          if (field.fieldCode === 'code') {
            this.$set(field, 'disabled', this.operationType === 'update')
          }
          field.value = ''
          this.$set(this.fieldForm, field.fieldCode, '')
        })
      })
    },
    handleDelete() {
      if (!this.currentData) {
        this.$message.error('请选择需要删除的字段！')
        return
      }
      this.$confirm('此操作将永久删除[' + this.currentData.name + ']字段，是否继续?', '提示', {
        type: 'warning'
      }).then(() => {
        this.loading = true
        genTableColumnDeleteAPI(this.currentData.code).then(res => {
          this.loading = false
          this.$message.success('删除成功！')
          this.$emit('success')
        }).catch(() => {
          this.loading = false
        })
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.jurisdiction-box {
  padding: 10px;
  overflow: auto;
  position: relative;
}
.jurisdiction-edit {
  text-align: right;
  padding: 10px 30px;
  position: absolute;
  top: 10;
  right: 20px;
  z-index: 3;
}
.jurisdiction-content {
  padding-top: 10px;
  position: relative;
}
</style>
