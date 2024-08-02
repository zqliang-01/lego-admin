<template>
  <el-dialog
    :visible.sync="visible"
    :close-on-click-modal="false"
    title="高级筛选"
    width="900px"
    @close="handleCancel">
    <div style="margin-bottom: 10px;">筛选条件</div>
    <el-form
      id="filter-container"
      class="filter-container">
      <el-form-item>
        <div v-for="(formItem, index) in filterForm" :key="index">
          <el-row :key="index">
            <el-col :span="8">
              <el-select
                v-model="formItem.fieldCode"
                placeholder="请选择要筛选的字段名"
                filterable
                @change="fieldChange(formItem)"
                @focus="fieldFocus">
                <el-option
                  v-for="item in fieldList"
                  :key="item.fieldCode"
                  :label="item.name"
                  :value="item.fieldCode"/>
              </el-select>
            </el-col>
            <template v-if="showCalCondition(formItem.formType)">
              <el-col :span="1">&nbsp;</el-col>
              <el-col :span="4">
                <el-select
                  v-model="formItem.type"
                  placeholder="请选择范围"
                  @change="conditionChange($event, formItem)">
                  <el-option
                    v-for="item in getConditionByFormType(formItem.formType)"
                    :key="item.type"
                    :label="item.label"
                    :value="item.type"/>
                </el-select>
              </el-col>
            </template>
            <el-col :span="1">&nbsp;</el-col>
            <el-col :span="getValueSpan(formItem.formType)" style="position: relative;">
              <template v-if="['isNotNull', 'isNull'].includes(formItem.type)">
                &nbsp;
              </template>
              <template v-else>
                <field
                  :item="formItem"
                  :index="index"
                  :field-form="formItem.fieldForm"
                  @change="handleFieldChange" />
              </template>
            </el-col>
            <el-col :span="1" class="delete">
              <i class="el-icon-error delete-btn" @click="handleDelete(index)"/>
            </el-col>
          </el-row>
        </div>
      </el-form-item>
    </el-form>
    <el-button
      type="text"
      @click="handleAdd">+ 添加筛选条件</el-button>
    <div
      v-if="saveScene"
      class="save">
      <el-checkbox v-model="saveChecked">保存为场景</el-checkbox>
      <el-input
        v-show="saveChecked"
        v-model.trim="saveName"
        :maxlength="10"
        class="name"
        placeholder="请输入场景名称，最多10个字符"/>
    </div>
    <div
      slot="footer"
      class="dialog-footer">
      <el-button @click="handleCancel">取 消</el-button>
      <el-button
        type="primary"
        @click="handleConfirm">确 定</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { objDeepCopy } from '@/utils'
import { isEmpty, isArray } from '@/utils/types'
import FilterMixin from '../mixins/LegoFilter'

export default {
  name: 'LegoFilterForm',
  mixins: [FilterMixin],
  props: {
    saveScene: {
      type: Boolean,
      default: true
    }
  },
  data() {
    return {
      saveChecked: false // 展示场景
    }
  },
  watch: {
    dialogVisible: {
      handler(val) {
        if (val) {
          this.filterForm = objDeepCopy(this.filterObj.form)
          if (this.filterForm.length === 0) {
            this.filterForm.push({
              name: '',
              fieldCode: '',
              fieldForm: {},
              formType: '',
              type: '',
              setting: []
            })
          }
          this.saveChecked = false
          this.saveName = null
        }
        this.visible = this.dialogVisible
      },
      deep: true,
      immediate: true
    },
    form() {
      this.$nextTick(() => {
        var container = document.getElementById('filter-container')
        container.scrollTop = container.scrollHeight
      })
    }
  },
  methods: {
    handleConfirm() {
      if (this.saveChecked && isEmpty(this.saveName)) {
        this.$message.error('场景名称不能为空！')
        return
      }
      const formItems = []
      const sceneList = []
      for (let i = 0; i < this.filterForm.length; i++) {
        const formItem = this.filterForm[i]
        if (isEmpty(formItem.fieldCode)) {
          this.$message.error('筛选的字段名称不能为空！')
          return
        }
        const value = formItem.fieldForm[formItem.fieldCode]
        if (!['isNull', 'isNotNull'].includes(formItem.type) && isEmpty(value)) {
          this.$message.error('筛选内容不能为空！')
          return
        }
        if (formItem.formType != 'entity') {
          formItem.value = value
        }
        sceneList.push({
          fieldCode: formItem.fieldCode,
          formType: formItem.formType,
          type: formItem.type,
          value: formItem.value,
          values: value
        })
        let values = []
        if (!isArray(value) && !isEmpty(value)) {
          values = [value]
        }
        formItems.push({
          fieldCode: formItem.fieldCode,
          formType: formItem.formType,
          type: formItem.type,
          values: values
        })
      }
      const data = {
        scenes: sceneList,
        form: this.filterForm,
        filterList: formItems,
        saveChecked: this.saveChecked,
        saveName: this.saveName
      }
      this.$emit('filter', data)
    }
  }
}
</script>

<style lang="scss" scoped>
::v-deep .el-dialog__body {
  padding: 10px 20px;
}

::v-deep .el-form-item__label {
  width: 100%;
  text-align: left;
}
.filter-container {
  max-height: 300px;
  overflow-y: auto;
}

.save {
  margin-top: 10px;
  .name {
    width: 300px;
    margin-left: 10px;
    ::v-deep .el-input__inner {
      height: 32px;
    }
  }
  .save-setting {
    margin-top: 20px;
  }
}

.el-form-item {
  margin-bottom: 0;
}

.el-row {
  margin-bottom: 20px;
  .delete-btn {
    margin-left: 15px;
    color: #bbb;
    cursor: pointer;
  }
  .el-select,
  .el-date-editor {
    width: 100%;
    ::v-deep .el-range__icon {
      line-height: 26px;
    }
  }

  .el-input-number {
    ::v-deep input {
      text-align: left;
      padding: 0 8px;
    }
  }

  .date-range-content {
    position: absolute;
    left: 30px;
    right: 30px;
    top: 4px;
    height: 30px;
    line-height: 30px;
    background: white;
    font-size: 13px;
    cursor: pointer;
  }
}

.warning-info {
  width: 100%;
  font-size: 14px;
  color: #f56c6c;
  margin-top: 10px;
  .desc {
    padding-left: 8px;
  }
}
</style>
