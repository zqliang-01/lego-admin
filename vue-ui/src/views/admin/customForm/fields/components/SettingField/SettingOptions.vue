<template>
  <div class="setting-options">
    <flexbox align="center" justify="center">
      <div class="add-other-btn" @click="handleUpdateAll">
        批量编辑
        <el-tooltip
          content="配置表单布局，可以单行多字段排布"
          effect="dark"
          placement="top">
          <i :class="'help lego-help-tips' | iconPre" style="margin-left: 3px;"/>
        </el-tooltip>
      </div>
      <flexbox-item />
    </flexbox>
    <draggable
      :list="optionsList"
      v-bind="dragConfig"
      @sort="handleChange">
      <div
        v-for="(item, index) in optionsList"
        :key="index"
        class="option-item">
        <el-row :gutter="20">
          <el-col :span="9">
            <el-input
              v-model="item.code"
              placeholder="编码"
              @change="handleChange"/>
          </el-col>
          <el-col :span="15">
            <el-input
              v-model="item.name"
              placeholder="名称"
              @change="handleChange">
              <flexbox slot="suffix">
                <div :class="'grid' | iconPre" class="el-input__icon drag-hook" />
                <el-button
                  :class="'icon-bin' | iconPre"
                  type="text"
                  class="el-input__icon"
                  @click="handleDelete(index)" />
              </flexbox>
            </el-input>
          </el-col>
        </el-row>
      </div>
    </draggable>
    <el-button
      class="add-btn"
      @click="handleAdd">
      <i class="el-icon-plus" /> 添加新选项
    </el-button>

    <el-dialog
      :visible.sync="dialogVisible"
      :before-close="handleCloseDialog"
      title="批量编辑"
      width="500px"
      class="edit-dialog">
      <div>
        <div class="edit-tips">
          每行内容对应一个选项，点击完成后，逻辑表单设置将失效
        </div>
        <json-editor v-model="dialogContentVal" :show-btns="false" :expanded-on-start="true" mode="code" />
      </div>
      <div slot="footer">
        <el-button @click="handleCloseDialog">
          取 消
        </el-button>
        <el-button
          type="primary"
          @click="handleDialogConfirm">
          确 定
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import draggable from 'vuedraggable'
import { isEmpty, isArray } from '@/utils/types'
import { guid } from '@/utils'
import JsonEditor from 'vue-json-editor'

export default {
  name: 'SettingOptions',
  components: {
    draggable,
    JsonEditor
  },
  props: {
    field: {
      type: Object,
      required: true
    },
    isTableChild: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      optionsList: [],
      dialogVisible: false,
      dialogContentVal: ''
    }
  },
  computed: {
    dragConfig() {
      return {
        group: guid(),
        forceFallback: false,
        fallbackClass: 'draggingStyle',
        handle: '.drag-hook',
        filter: '.el-input__inner',
        preventOnFilter: false
      }
    }
  },
  watch: {
    field: {
      handler(newVal, oldVal) {
        if (isEmpty(this.field.setting)) {
          this.$set(this.field, 'setting', [{ name: '选项1', code: '1' }, { name: '选项2', code: '2' }, { name: '选项3', code: '3' }])
        }
        if (!oldVal || newVal.options !== oldVal.options) {
          this.optionsList = this.field.setting.map(o => {
            return o
          })
        }
      },
      deep: true,
      immediate: true
    }
  },
  methods: {
    /**
     * 修改、排序
     */
    handleChange(newValue, oldValue) {
      if (!this.checkOptionData(this.optionsList)) {
        this.optionsList = this.field.setting
        return
      }
      this.field.setting = this.optionsList
      this.$set(this.field, 'setting', this.field.setting)
      this.$nextTick(() => {
        this.checkDefaultValue()
      })
    },
    /**
     * 删除
     * @param index
     */
    handleDelete(index) {
      this.optionsList.splice(index, 1)
      this.field.setting.splice(index, 1)
      this.$set(this.field, 'setting', this.field.setting)
      this.checkDefaultValue()
    },
    /**
     * 添加
     */
    handleAdd() {
      const val = this.getAddValue(this.optionsList.length + 1)
      this.optionsList.push({
        code: val,
        name: '选项' + val
      })
      this.handleChange()
    },

    /**
     * 点击批量编辑
     */
    handleUpdateAll() {
      this.dialogContentVal = this.optionsList
      this.dialogVisible = true
    },

    /**
     * 关闭弹窗
     */
    handleCloseDialog() {
      this.dialogVisible = false
    },

    /**
     * 批量编辑
     */
    handleDialogConfirm() {
      if (!this.checkOptionData(this.dialogContentVal)) {
        return
      }
      this.optionsList = this.dialogContentVal
      this.handleChange()
      this.handleCloseDialog()
    },

    checkOptionData(options) {
      const obj = {}
      let arr = options.filter(o => isEmpty(o.code))
      if (arr.length && arr.length > 0) {
        this.$message.error('存在编码为空的选项内容，保存失败！')
        return false
      }
      arr = options.filter(o => {
        if (obj[o.code]) {
          return false
        }
        obj[o.code] = true
        return true
      })
      if (arr.length && arr.length !== options.length) {
        this.$message.error('存在编码重复的选项内容，保存失败！')
        return false
      }
      return true
    },

    getAddValue(index) {
      const findRes = this.optionsList.find(o => o.code === `${index}`)
      if (findRes) {
        return this.getAddValue(index + 1)
      }
      return `${index}`
    },

    /**
     * 选项变化后修改默认值
     */
    checkDefaultValue() {
      if (!isEmpty(this.field.defaultValue)) {
        if (isArray(this.field.defaultValue)) {
          const arr = []
          this.field.defaultValue.forEach(o => {
            const findRes = this.optionsList.find(item => item.code === o)
            if (findRes) arr.push(o)
          })
          this.$set(this.field, 'defaultValue', [...arr])
        } else {
          const findRes = this.optionsList.find(item => item.code === this.field.defaultValue)
          if (!findRes) {
            this.$set(this.field, 'defaultValue', null)
          }
        }
      }
    }
  }
}
</script>

<style scoped lang="scss">
.option-item {
  margin: 5px 0;

  .el-input__icon {
    font-size: 14px;
    color: #999999;
  }
  .drag-hook {
    cursor: move;
  }

  &.other-item {
    .lego-icon-bin {
      cursor: pointer;
    }
  }
}

.add-btn {
  width: 100%;
  height: 34px;
  font-size: 14px;
  color: #666666;
  border: 1px dashed $xr-border-color-base;
  border-radius: $xr-border-radius-base;
  background-color: #f8f8f8;
  cursor: pointer;
}

.add-other-btn {
  font-size: 14px;
  color: #666666;
  cursor: pointer;
  display: inline-block;
  margin-top: 8px;
}

.edit-dialog {
  .edit-tips {
    font-size: 14px;
    //color: #999999;
    color: #ecb971;
    background-color: #fafbfb;
    border-top: 1px solid #E6E6E6;
    border-bottom: 1px solid #E6E6E6;
    padding: 5px 20px;
  }
  .el-textarea {
    margin: 10px 0;
  }
  ::v-deep .el-textarea__inner {
    border: 0 none;
    padding: 10px 20px;
  }
  ::v-deep .el-dialog__body {
    padding: 0;
  }
}

</style>
