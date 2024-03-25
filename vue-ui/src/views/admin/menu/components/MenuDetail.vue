<template>
  <div
    v-loading="loading"
    class="jurisdiction-box">
    <div class="jurisdiction-edit">
      <el-button
        v-if="manage.permission.add && operationType === 'update'"
        icon="el-icon-plus"
        @click="handleAdd">
        新增子菜单
      </el-button>
      <el-button
        v-if="operationType === 'add'"
        class="xr-btn--orange"
        type="primary"
        @click="resetForm">
        取消
      </el-button>
      <el-button
        v-if="showDelete"
        class="xr-btn--red"
        type="primary"
        @click="handleDelete">
        删除
      </el-button>
      <el-button
        v-if="manage.permission.update || manage.permission.add"
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
        <create-sections title="基本信息">
          <form-items
            v-for="(children, index) in baseFieldList"
            :key="index"
            :field-form="fieldForm"
            :field-list="children"
            @change="handleChangeValue"
          />
        </create-sections>
      </el-form>
      <extend-detail
        v-if="menuType === 'menu'"
        :form-code="formCode"
      />
    </div>
  </div>
</template>
<script>
import { mapGetters } from 'vuex'
import CreateSections from '@/components/CreateSections'
import FormItems from '@/components/NewCom/Form/FormItems'
import ExtendDetail from './ExtendDetail'
import { isEmpty } from '@/utils/types'
import { getFormFieldValue, showFormErrorMessage } from '@/components/NewCom/Form/utils'
import GenerateRulesMixin from '@/components/Mixins/GenerateRules'
import {
  permissionTypeListAPI,
  permissionRouteTypeListAPI,
  permissionModifyAPI,
  permissionAddAPI,
  permissionDeleteAPI } from '@/api/admin/permission'
import { customFormSimpleListAPI } from '@/api/admin/formField'

export default {
  components: {
    CreateSections,
    FormItems,
    ExtendDetail
  },
  mixins: [GenerateRulesMixin],
  props: {
    menuList: {
      type: Array,
      default: () => {
        return []
      }
    },
    menuData: {
      type: Object,
      default: () => {
        return {}
      }
    }
  },
  computed: {
    ...mapGetters(['manage']),
    requestAPI() {
      return this.operationType === 'update' ? permissionModifyAPI : permissionAddAPI
    },
    showDelete() {
      return this.operationType === 'update' && this.manage.permission.delete && !isEmpty(this.menuData)
    }
  },
  data() {
    return {
      loading: false,
      operationType: 'update',
      menuType: '',
      formCode: '',
      permissionTypes: [],
      permissionRouteTypes: [],
      formList: [],
      baseFieldList: [
        [
          { fieldCode: 'parentCode', name: '上级菜单', formType: 'structure', stylePercent: 100 }
        ],
        [
          { fieldCode: 'code', name: '编码', formType: 'text', unique: true, required: true, tipType: 'tooltip', inputTips: '编码格式：模块_一级菜单_二级菜单' },
          { fieldCode: 'name', name: '名称', formType: 'text', required: true }
        ],
        [
          { fieldCode: 'icon', name: '图标', formType: 'icon' },
          { fieldCode: 'type', name: '类型', formType: 'select', precisions: 1, required: true }
        ],
        [
          { fieldCode: 'form', name: '表单', formType: 'select', clearable: true },
          { fieldCode: 'routeType', name: '路由类型', formType: 'select', precisions: 1, tipType: 'tooltip', inputTips: '模块路由定义，动态路由页面受后台控制' }
        ],
        [
          { fieldCode: 'sn', name: '序号', formType: 'number' }
        ]
      ],
      fieldForm: {},
      fieldRule: {}
    }
  },
  watch: {
    menuData: {
      handler() {
        this.resetForm()
      },
      deep: true,
      immediate: true
    }
  },
  mounted() {
    permissionTypeListAPI().then(res => {
      this.permissionTypes = res.data
      this.resetForm()
    })
    permissionRouteTypeListAPI().then(res => {
      this.permissionRouteTypes = res.data
      this.resetForm()
    })
    customFormSimpleListAPI().then(res => {
      this.formList = res.data
      this.resetForm()
    })
  },
  methods: {
    resetForm() {
      this.menuType = this.menuData.type ? this.menuData.type.code : ''
      this.formCode = this.menuData.form ? this.menuData.form.code : ''
      this.operationType = 'update'
      this.baseFieldList.forEach(fields => {
        fields.forEach(field => {
          if (field.fieldCode === 'parentCode') {
            field.setting = this.menuList
          }
          if (field.fieldCode === 'form') {
            field.setting = this.formList
          }
          if (field.fieldCode === 'type') {
            field.setting = this.permissionTypes
          }
          if (field.fieldCode === 'routeType') {
            field.setting = this.permissionRouteTypes
            this.$set(field, 'disabled', this.menuType !== 'menu')
          }
          if (field.fieldCode === 'code' && this.operationType !== 'add') {
            field.disabled = true
          }
          this.fieldRule[field.fieldCode] = this.getRules(field)
          field.value = this.menuData[field.fieldCode]
          this.$set(this.fieldForm, field.fieldCode, getFormFieldValue(field, false))
        })
      })
    },
    handleChangeValue(field, index, value) {
      if (field.fieldCode === 'type') {
        this.menuType = value
      }
      if (field.fieldCode === 'form') {
        this.formCode = value
      }
    },
    handleSubmit() {
      this.loading = true
      this.$refs.createForm.validate(valid => {
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
      this.formCode = ''
      this.operationType = 'add'
      this.baseFieldList.forEach(fields => {
        fields.forEach(field => {
          if (field.fieldCode === 'parentCode') {
            field.value = this.menuData.code
            this.$set(this.fieldForm, field.fieldCode, this.menuData.code)
          } else {
            if (field.fieldCode === 'code') {
              field.disabled = false
            }
            field.value = ''
            this.$set(this.fieldForm, field.fieldCode, '')
          }
        })
      })
    },
    handleDelete() {
      if (!this.menuData) {
        this.$message.error('请选择需要删除的菜单！')
        return
      }
      this.$confirm('此操作将永久删除[' + this.menuData.name + ']菜单，是否继续?', '提示', {
        type: 'warning'
      }).then(() => {
        this.loading = true
        permissionDeleteAPI(this.menuData.code).then(res => {
          this.loading = false
          this.$message.success('删除成功！')
          this.$emit('deleteSuccess')
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
