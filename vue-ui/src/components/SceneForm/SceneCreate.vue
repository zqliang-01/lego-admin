<template>
  <el-dialog
    :title="edit_id ? '编辑场景' : '新建场景'"
    :visible.sync="visible"
    :close-on-click-modal="false"
    :append-to-body="true"
    width="800px"
    @close="handleCancel">
    <div class="scene-name-container">
      <div class="scene-name">场景名称</div>
      <el-input
        v-model.trim="saveName"
        :maxlength="10"
        class="scene-input"
        placeholder="请输入场景名称，最多10个字符"/>
    </div>
    <div class="scene-name">筛选条件</div>
    <el-form
      id="scene-filter-container"
      class="filter-container">
      <el-form-item>
        <div v-for="(formItem, index) in filterForm">
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
                  :field-from="formItem.fieldForm"
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
      slot="footer"
      class="dialog-footer">
      <el-button @click="handleCancel">取 消</el-button>
      <el-button
        v-debounce="handleConfirm"
        type="primary">确 定</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { crmSceneAddAPI, crmSceneModifyAPI } from '@/api/scene'

import { objDeepCopy } from '@/utils'
import { isEmpty } from '@/utils/types'
import FilterMixin from '../FilterForm/FilterMixin'
export default {
  name: 'SceneCreate',
  mixins: [FilterMixin],
  props: {
    formCode: {
      type: String,
      default: ''
    },
    name: {
      type: String,
      default: ''
    },
    edit_id: {
      type: String,
      default: ''
    }
  },
  computed: {
    requestAPI() {
      return isEmpty(this.edit_id) ? crmSceneAddAPI : crmSceneModifyAPI
    }
  },
  watch: {
    dialogVisible: {
      handler(val) {
        if (val) {
          this.saveName = this.name
          this.filterForm = objDeepCopy(this.filterObj.form)
          this.filterForm.forEach(formItem => {
            this.$set(formItem, 'fieldForm', { [formItem.fieldCode]: formItem.values })
            const field = this.fieldList.find(field => field.fieldCode === formItem.fieldCode)
            if (field) {
              this.$set(formItem, 'setting', field.setting)
              if (formItem.formType === 'entity') {
                this.$set(formItem, 'request', field.request)
                this.$set(formItem, 'relativeForm', field.relativeForm)
              }
            }
          })
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
        }
        this.visible = this.dialogVisible
      },
      deep: true,
      immediate: true
    },

    form() {
      this.$nextTick(() => {
        var container = document.getElementById('scene-filter-container')
        container.scrollTop = container.scrollHeight
      })
    }
  },
  methods: {
    handleConfirm() {
      if (isEmpty(this.saveName)) {
        this.$message.error('场景名称不能为空！')
        return
      }
      const sceneList = []
      for (let i = 0; i < this.filterForm.length; i++) {
        const formItem = this.filterForm[i]
        if (isEmpty(formItem.fieldCode)) {
          this.$message.error('筛选的字段名称不能为空！')
          return
        }
        if (['isNull', 'isNotNull'].includes(formItem.type)) {
          continue
        }
        const value = formItem.fieldForm[formItem.fieldCode]
        if (isEmpty(value)) {
          this.$message.error('筛选内容不能为空！')
          return
        }
        sceneList.push({
          fieldCode: formItem.fieldCode,
          formType: formItem.formType,
          type: formItem.type,
          value: formItem.value,
          values: value
        })
      }
      this.requestAPI({
        code: this.edit_id,
        enable: true,
        name: this.saveName,
        formCode: this.filterFormCode,
        data: JSON.stringify(sceneList)
      }).then(res => {
        this.$message.success('操作成功')
        this.$emit('save-success')
        this.handleCancel()
      })
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
  max-height: 200px;
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

.scene-name-container {
  padding-bottom: 15px;
  .scene-input {
    width: 300px;
  }
}
.scene-name {
  margin-bottom: 10px;
}
</style>
