<template>
  <div class="setting-logic-form">
    <el-button
      class="add-btn"
      @click="handleToSet">
      点击配置
    </el-button>

    <el-dialog
      :visible.sync="dialogVisible"
      :before-close="handleCloseDialog"
      :close-on-click-modal="false"
      title="添加逻辑表单规则"
      width="500px"
      class="edit-dialog">
      <div>
        <div class="edit-tips">
          选择选项后，才会显示所设置的其他字段
        </div>

        <div class="edit-table">
          <flexbox
            align="center"
            justify="flex-start"
            class="edit-table__header row">
            <div class="label">选项内容</div>
            <flexbox-item class="content">显示字段</flexbox-item>
          </flexbox>
          <div
            v-if="list.length > 0 && fieldLibArr.length > 0"
            class="edit-table__body">
            <flexbox
              v-for="(item, index) in list"
              :key="index"
              align="center"
              justify="flex-start"
              class="row">
              <div class="label">{{ item.name }}</div>
              <flexbox-item class="content">
                <el-select
                  v-model="item.code"
                  placeholder="请选择"
                  multiple>
                  <el-option
                    v-for="(item, childIndex) in fieldLibArr"
                    :key="childIndex"
                    :label="item.formType === 'desc_text' ? '描述文字' : (item.name || '未命名')"
                    :value="item.code" />
                </el-select>
              </flexbox-item>
            </flexbox>
          </div>
        </div>
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
import { isEmpty } from '@/utils/types'

export default {
  name: 'SettingLogicForm',
  props: {
    field: {
      type: Object,
      required: true
    },
    fieldList: { // 所有字段
      type: Array,
      required: true
    },
    point: { // 字段坐标
      type: Array,
      required: true
    }
  },
  data() {
    return {
      list: [],
      dialogVisible: false
    }
  },
  computed: {
    fieldLibArr() {
      const arr = []
      this.fieldList.forEach((father, fatherIndex) => {
        father.forEach((child, childIndex) => {
          if (child.code !== this.field.code) {
            arr.push(child)
          }
        })
      })
      return arr
    },
    allFormAssistCode() {
      return this.fieldLibArr.map(o => o.code)
    }
  },
  watch: {
    field: {
      handler() {
        // remark null 普通  options_type 逻辑表单
        if (this.field.remark !== 'options_type') {
          // 普通单选多选
          this.list = this.field.setting.map(o => {
            return {
              name: o,
              value: null
            }
          })
        } else {
          // 逻辑表单单选多选
          let data = {}
          if (this.field.optionsData) {
            data = this.field.optionsData || {}
          } else {
            try {
              data = JSON.parse(this.field.options) || {}
            } catch (e) {
              this.list = this.field.setting.map(o => {
                return {
                  name: o,
                  value: null
                }
              })
              // 如果异常
              this.$set(this.field, 'remark', null)
              this.$set(this.field, 'optionsData', null)
              this.$set(this.field, 'options', this.field.setting.join(','))
              return
            }
          }
          this.list = Object.keys(data).map(key => {
            return {
              name: key,
              value: isEmpty(data[key]) ? [] : data[key]
            }
          })
        }
      },
      deep: true,
      immediate: true
    },
    allFormAssistCode: {
      handler() {
        if (this.field.remark === 'options_type') {
          // 某个字段被删除后，则同时删除其对应的逻辑关联
          this.list.forEach(item => {
            const codes = []
            if (item.value) {
              item.value.forEach(code => {
                if (this.allFormAssistCode.includes(code)) {
                  codes.push(code)
                }
              })
            }
            item.value = codes
          })

          const optionsData = {}
          this.list.forEach(o => {
            optionsData[o.name] = o.value
          })
          this.$set(this.field, 'optionsData', optionsData)
        }
      },
      deep: true,
      immediate: true
    }
  },
  methods: {
    handleToSet() {
      this.dialogVisible = true
    },

    handleCloseDialog() {
      // 关闭弹窗，触发watch更新
      this.$set(this.field, '_remark', '')
      this.$nextTick(() => {
        delete this.field._remark
      })
      this.dialogVisible = false
    },

    handleDialogConfirm() {
      const optionsData = {}
      this.list.forEach(o => {
        optionsData[o.name] = o.value
      })
      const len = this.list.filter(o => !isEmpty(o.value)).length
      if (len !== 0) {
        // 保存逻辑表单关系
        this.$set(this.field, 'remark', 'options_type')
        this.$set(this.field, 'optionsData', optionsData)
        const optionsStr = JSON.stringify(optionsData)
        this.$set(this.field, 'options', optionsStr)
      } else {
        // 如果没有配置逻辑表单则置普通类型
        this.$set(this.field, 'remark', null)
        this.$set(this.field, 'optionsData', null)
        this.$set(this.field, 'options', this.field.setting.join(','))
      }

      this.handleCloseDialog()
    }
  }
}
</script>

<style scoped lang="scss">
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

.edit-dialog {
  .edit-tips {
    font-size: 14px;
    color: #999999;
    background-color: #fafbfb;
    border-top: 1px solid #E6E6E6;
    border-bottom: 1px solid #E6E6E6;
    padding: 5px 20px;
  }
  ::v-deep .el-dialog__body {
    padding: 0;
  }

  .edit-table {
    height: 300px;
    padding: 0 15px;
    overflow-y: auto;

    .row {
      border-bottom: 1px solid #E6E6E6;
      &:last-child {
        border-bottom: 0 none;
      }
      .label {
        width: 180px;
        padding: 0 15px;
        overflow: hidden;
        white-space: nowrap;
        text-overflow: ellipsis;
      }
      .content {
        padding-right: 15px;
        .el-select {
          width: 100%;
        }
      }
    }
    .edit-table__header {
      width: 100%;
      height: 36px;
      color: #999999;
    }
    .edit-table__body {
      .row {
        padding: 6px 0;
      }
    }
  }
}
</style>
