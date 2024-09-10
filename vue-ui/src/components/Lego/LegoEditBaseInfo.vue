<template>
  <div v-loading="loading" class="b-cont">
    <div v-if="showSaveButton" class="b-cont__handle">
      <el-button @click.native="editCancel">取消</el-button>
      <el-button v-debounce="editConfirm" type="primary">保存</el-button>
    </div>
    <lego-sections
      title="基本信息"
      class="b-cells"
      content-height="auto">
      <el-form
        :model="editForm"
        :rules="currentEditRules"
        :ref="`editForm`"
        :validate-on-rule-change="false"
        class="el-form--flex"
        label-position="left"
        label-width="100px">
        <el-form-item
          v-for="(item, index) in fieldList"
          :key="index"
          :prop="item.fieldCode"
          :class="[{'is-block': isBlockShowField(item)}, `is-${item.formType}`]">
          <span v-if="!isBlockShowField(item)" slot="label">
            {{ item.name }}
          </span>
          <field
            v-if="item.isEdit"
            :item="item"
            :index="index"
            :field-form="editForm" />
          <flexbox
            v-else
            align="stretch"
            style="width: 100%;"
            class="form-item__value">
            <field-view
              :item="item"
              :form-type="item.formType"
              :value="item.value"
              @clickEntity="handleEntityClick"/>
            <i
              v-if="canEdit(item)"
              :class="'edit form-item__edit' | iconPre"
              @click.stop="editClick(item, index)" />
          </flexbox>
        </el-form-item>
      </el-form>
    </lego-sections>
    <lego-sections
      title="系统信息"
      class="b-cells"
      content-height="auto">
      <el-form
        class="el-form--flex"
        label-position="left"
        label-width="100px">
        <el-form-item
          v-for="(item, index) in systemFieldList"
          :key="index"
          :prop="item.fieldCode"
          :class="[{'is-block': isBlockShowField(item)}, `is-${item.formType}`]">
          <span slot="label">
            {{ item.name }}
          </span>
          <flexbox
            align="stretch"
            style="width: 100%;"
            class="form-item__value">
            <field-view
              :item="item"
              :form-type="item.formType"
              :value="item.value" />
          </flexbox>
        </el-form-item>
      </el-form>
    </lego-sections>
  </div>
</template>

<script>
import LegoSections from './LegoSections'
import FieldView from '../Common/Form/FieldView'
import Field from '../Common/Form/Field'
import { objDeepCopy } from '@/utils'
import { getMenuAuth } from '@/utils/auth'
import { getFormFieldValue } from '../Common/Form/utils'
import GenerateRulesMixin from '../Mixins/GenerateRules'

export default {
  name: 'LegoditBaseInfo',
  components: {
    LegoSections,
    FieldView,
    Field
  },
  mixins: [GenerateRulesMixin],
  props: {
    menuCode: String,
    formCode: String,
    detailCode: [String, Number],
    fieldList: Array,
    systemFieldList: Array
  },
  computed: {
    auth() {
      return getMenuAuth(this.menuCode)
    }
  },
  data() {
    return {
      loading: false,
      // 编辑
      showSaveButton: false,
      editRules: {}, // 全字段规则
      currentEditRules: {}, // 当前编辑字段规则
      editForm: {}
    }
  },
  inject: ['rootTabs'],
  watch: {
    fieldList: {
      handler() {
        if (this.fieldList) {
          this.editCancel()
        }
      },
      deep: true,
      immediate: true
    },
    'rootTabs.currentName'(val) {
      if (val === 'LegoEditBaseInfo') {
        console.log(val)
      }
    }
  },
  created() {
    this.fieldList.forEach(field => {
      this.editRules[field.fieldCode] = this.getRules(objDeepCopy(field))
    })
  },
  methods: {
    canEdit(item) {
      return !item.unique && !item.simpleType
    },
    /**
     * 是整行展示字段
     */
    isBlockShowField(item) {
      return item.simpleType
    },

    /**
     * 点击编辑按钮
     */
    editClick(item, index) {
      this.$set(this.editForm, item.fieldCode, getFormFieldValue(item))
      this.$set(item, 'isEdit', true)
      this.$set(this.currentEditRules, item.fieldCode, this.editRules[item.fieldCode] || [])
      this.showSaveButton = true
    },

    /**
     * 点击取消
     */
    editCancel() {
      if (this.$refs.editForm && this.$refs.editForm.clearValidate) {
        this.$refs.editForm.clearValidate()
      }

      this.$nextTick(() => {
        this.fieldList.forEach(item => {
          item.isEdit = false
        })

        this.currentEditRules = {}
        this.editForm = {}
        this.showSaveButton = false
      })
    },

    /**
     * 点击保存
     */
    editConfirm() {
      this.$refs.editForm.validate(valid => {
        if (valid) {
          this.editForm['code'] = this.detailCode
          this.fieldList.forEach(field => {
            if (!this.editForm.hasOwnProperty(field.fieldCode) && !field.simpleType) {
              this.editForm[field.fieldCode] = getFormFieldValue(field)
            }
          })
          this.$emit('handle', { type: 'update', data: this.editForm }) // 刷新数据
        }
      })
    },
    handleEntityClick(data) {
      this.$emit('clickEntity', data)
    }
  }
}
</script>

<style lang="scss" scoped>
.b-cont {
  position: relative;
  padding: 15px;
  height: 100%;
  overflow-y: auto;
  overflow-y: overlay;

  &__handle {
    position: absolute;
    text-align: right;
    right: 20px;
    width: 100%;
    z-index: 3;
  }
}

.section {
  margin-top: 0;
  ::v-deep .content {
    overflow: hidden;
  }
}

.el-input-number {
  width: 100%;
  ::v-deep .el-input__inner {
    text-align: left;
    padding: 0 8px;
  }
}

.b-cells + .b-cells {
  margin-top: 25px;
}

.b-cell {
  padding: 0 10px;
}

.el-form--flex {
  margin: 20px 10px 0;
  ::v-deep .el-form-item {
    padding: 0 40px 0 15px;
    margin-bottom: 13px;
    max-width: 100%;
    .el-form-item__content {
      position: relative;
      min-height: 40px;
      line-height: 1.5;
    }

    .el-form-item__label {
      color: #777;
      font-size: 13px;
      line-height: 1.5;
    }

    &:hover {
      .form-item__edit{
        display: inline;
      }
    }

    &.is-desc_text {
      .el-form-item__content {
        margin-left: 0 !important;
      }
    }
  }
}

.form-item__value {
  font-size: 13px;
  color: #333;
  line-height: 1.5;
  min-height: 22px;
  white-space: pre-wrap;
  word-wrap: break-word;
  word-break: break-all;
  .field-view {
    width: 0;
    flex: 1;
  }
}

.form-item__edit {
  margin-left: 5px;
  font-size: 14px;
  color: #999;
  cursor: pointer;
  display: none;
  flex-shrink: 0;

  &:hover {
    color: $xr-color-primary;
  }
}

.can-check {
  color: $xr-color-primary !important;
  cursor: pointer;
}

.is-block {
  flex-basis: 100% !important;
}

.b-cell-b {
  width: auto;
  .b-cell-name {
    width: 100px;
    margin-right: 10px;
    font-size: 13px;
    flex-shrink: 0;
    color: #777;
  }
  .b-cell-value {
    font-size: 13px;
    color: #333;
    line-height: 30px;
    white-space: pre-wrap;
    word-wrap: break-word;
    word-break: break-all;
  }
}
</style>
