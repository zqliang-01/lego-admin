<template>
  <el-dialog
    v-loading="loading"
    :visible.sync="visible"
    :append-to-body="true"
    :close-on-click-modal="false"
    title="场景管理"
    width="700px"
    @close="handleCancel">
    <div class="scene-name">您可通过拖拽管理标签</div>
    <flexbox class="scene-list">
      <div class="scene-list-box">
        <flexbox class="scene-list-head">
          <el-checkbox
            :indeterminate="isleftIndeterminate"
            v-model="checkleftAll"
            @change="handleleftCheckAllChange"/>
          <div class="scene-list-head-name">显示的标签</div>
          <div class="scene-list-head-detail">{{ leftCheckItems.length + '/' + checkedLeftData.length }}</div>
        </flexbox>
        <div class="scene-list-body">
          <draggable
            v-model="checkedLeftData"
            :move="leftMove"
            v-bind="{group: 'list',forceFallback:false, fallbackClass:'draggingStyle'}"
            style="min-height: 100px;"
            @end="leftMoveEnd">
            <flexbox
              v-for="(item, index) in checkedLeftData"
              :key="index"
              class="list-item">
              <template v-if="item.isSystem != 1">
                <div
                  :class="{'default-mark-active': false}"
                  class="default-mark"/>
                <el-checkbox
                  v-model="item.check"
                  class="list-item-check"
                  @change="leftCheckItemChange"/>
                <div class="list-item-name">{{ item.name }}</div>
                <div class="list-item-handle">
                  <i
                    class="el-icon-edit"
                    @click="itemHandle('edit', item, index)"/>
                  <i
                    class="el-icon-delete"
                    @click="itemHandle('delete', item, index)"/>
                </div>
              </template>
            </flexbox>
          </draggable>
        </div>
      </div>
      <div class="scene-middle-list">
        <el-button
          :class="{'scene-middle-button-select':rightCheckItems.length > 0}"
          :disabled="rightCheckItems.length == 0"
          @click="changePositon('left')">
          <i class="el-icon-arrow-left scene-middle-icon"/>
        </el-button>
        <el-button
          :class="{'scene-middle-button-select':leftCheckItems.length > 0}"
          :disabled="leftCheckItems.length == 0"
          class="scene-middle-right-button"
          @click="changePositon('right')">
          <i class="el-icon-arrow-right scene-middle-icon"/>
        </el-button>
      </div>
      <div class="scene-list-box">
        <flexbox class="scene-list-head">
          <el-checkbox
            :indeterminate="isrightIndeterminate"
            v-model="checkrightAll"
            @change="handlerightCheckAllChange"/>
          <div class="scene-list-head-name">隐藏的标签</div>
          <div class="scene-list-head-detail">{{ rightCheckItems.length + '/' + checkedRightData.length }}</div>
        </flexbox>
        <div class="scene-list-body">
          <draggable
            v-model="checkedRightData"
            :move="rightMove"
            v-bind="{group: 'list',forceFallback:false, fallbackClass:'draggingStyle'}"
            style="min-height: 100px;"
            @end="rightMoveEnd">
            <flexbox
              v-for="(item, index) in checkedRightData"
              :key="index"
              class="list-item">
              <el-checkbox
                v-model="item.check"
                style="margin-left:9px;"
                class="list-item-check"
                @change="rightCheckItemChange"/>
              <div class="list-item-name">{{ item.name }}</div>
            </flexbox>
          </draggable>
        </div>
      </div>
    </flexbox>

    <div class="handle-bar">
      <div
        class="handle-bar-add"
        @click="addAndEditScene('add',{})">+ 新建场景</div>
    </div>

    <div
      slot="footer"
      class="dialog-footer">
      <el-button @click.native="handleCancel">取消</el-button>
      <el-button
        v-debounce="handleConfirm"
        type="primary">保存</el-button>
    </div>
    <scene-create
      :form-code="formCode"
      :field-list="fieldList"
      :dialog-visible.sync="showCreateScene"
      :name="filterName"
      :filter-obj="filterObj"
      :edit_id="filterEditId"
      @save-success="editSuccess"/>
  </el-dialog>
</template>

<script type="text/javascript">
import {
  sceneListAPI,
  sceneDeleteAPI,
  sceneVisibleModifyAPI
} from '@/api/scene'
import draggable from 'vuedraggable'
import SceneCreate from './SceneCreate' // 新建编辑场景

export default {
  name: 'SceneSet', // 场景 设置
  components: {
    draggable,
    SceneCreate
  },
  props: {
    fieldList: {
      type: Array,
      required: true,
      default: () => {
        return []
      }
    },
    dialogVisible: {
      type: Boolean,
      required: true,
      default: false
    },
    formCode: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      loading: false,
      visible: false,
      isleftIndeterminate: false, // 标注头部是多选框效果
      checkleftAll: false, // 关联全选操作多选框

      checkedLeftData: [], // 数据源
      leftCheckItems: [], // 选择的数据源

      isrightIndeterminate: false,
      checkrightAll: false,

      checkedRightData: [],
      rightCheckItems: [],

      moveItem: {}, // 移动中的item

      /** 添加 编辑 场景 */
      showCreateScene: false, // 展示场景添加
      filterObj: { form: [] }, // 筛选确定数据
      filterName: '',
      filterEditId: '' // 编辑id
    }
  },
  computed: {},
  watch: {
    dialogVisible: {
      handler(val) {
        this.visible = val
        if (val) {
          this.getSceneList()
        }
      },
      deep: true,
      immediate: true
    }
  },
  mounted() {},
  methods: {
    getSceneList() {
      sceneListAPI({
        formCode: this.formCode
      })
        .then(res => {
          this.checkedLeftData = res.data.filter(item => item.enable).map(item => {
            item.check = false
            item.data = !item.data ? {} : JSON.parse(item.data)
            return item
          })
          this.checkedRightData = res.data.filter(item => !item.enable).map(item => {
            item.check = false
            item.data = !item.data ? {} : JSON.parse(item.data)
            return item
          })
        })
        .catch(() => {})
    },
    editSuccess() {
      this.getSceneList()
      this.$emit('save-success')
    },
    /**
     * 确定选择
     */
    handleConfirm() {
      this.loading = true
      sceneVisibleModifyAPI({
        codes: this.checkedLeftData.map(item => item.code),
        hiddenCodes: this.checkedRightData.map(item => item.code)
      }).then(res => {
        this.loading = false
        this.$message.success('更新成功！')
        this.handleCancel()
        this.$emit('save-success')
      }).catch(() => {
        this.loading = false
      })
    },
    /** 事项操作 */
    itemHandle(type, item, index) {
      if (type === 'edit') {
        this.addAndEditScene('edit', item)
      }
      if (type === 'delete') {
        this.$confirm('确定删除场景[' + item.name + ']?', '提示', {
          type: 'warning'
        }).then(() => {
          this.loading = true
          sceneDeleteAPI(item.code).then(() => {
            this.$message.success('删除成功！')
            this.loading = false
          }).catch(() => {
            this.loading = false
          })
        })
      }
    },
    /** 添加编辑场景 */
    addAndEditScene(type, data) {
      if (type == 'edit') {
        this.filterObj = { form: data.data }
        this.filterName = data.name
        this.filterEditId = data.code
      } else {
        this.filterObj = { form: [] }
        this.filterEditId = ''
        this.filterName = ''
      }
      this.showCreateScene = true
    },
    /**
     * 取消选择
     */
    handleCancel() {
      this.visible = false
      this.$emit('update:dialogVisible', false)
    },
    /** 拖拽操作 */
    rightMoveEnd(evt) {
      this.moveItem.check = false
      this.leftCheckItemChange()
      this.rightCheckItemChange()
    },
    rightMove(obj) {
      this.moveItem = obj.draggedContext.element
    },
    leftMoveEnd(evt) {
      this.moveItem.check = false
      this.leftCheckItemChange()
      this.rightCheckItemChange()
    },
    leftMove(obj) {
      this.moveItem = obj.draggedContext.element
    },
    // 选择全部
    handleleftCheckAllChange(value) {
      if (value) {
        this.isleftIndeterminate = false
      }
      this.checkedLeftData.forEach(item => {
        item.check = item.isSystem == 1 ? false : value
      })

      this.leftCheckItems = value ? this.checkedLeftData.filter(item => {
        return item.check
      }) : []
    },
    leftCheckItemChange() {
      this.leftCheckItems = this.checkedLeftData.filter(item => {
        return item.check == true
      })
      if (this.leftCheckItems.length > 0) {
        if (this.leftCheckItems.length == this.checkedLeftData.length) {
          this.checkleftAll = true
          this.isleftIndeterminate = false
        } else {
          this.checkleftAll = false
          this.isleftIndeterminate = true
        }
      } else {
        this.checkleftAll = false
        this.isleftIndeterminate = false
      }
    },
    // 选择全部
    handlerightCheckAllChange(value) {
      if (value) {
        this.isrightIndeterminate = false
      }
      this.checkedRightData.forEach(item => {
        item.check = value
      })
      this.rightCheckItems = value ? this.checkedRightData : []
    },
    rightCheckItemChange() {
      this.rightCheckItems = this.checkedRightData.filter(item => {
        return item.check == true
      })
      if (this.rightCheckItems.length > 0) {
        if (this.rightCheckItems.length == this.checkedRightData.length) {
          this.checkrightAll = true
          this.isrightIndeterminate = false
        } else {
          this.checkrightAll = false
          this.isrightIndeterminate = true
        }
      } else {
        this.checkrightAll = false
        this.isrightIndeterminate = false
      }
    },
    // 按钮操作
    changePositon(type) {
      var self = this
      // 从右往左
      if (type == 'left') {
        this.checkedRightData = this.checkedRightData.filter(item => {
          var remove = false
          self.rightCheckItems.forEach(element => {
            if (item.code == element.code) {
              remove = true
            }
          })
          return !remove
        })

        this.rightCheckItems.forEach(element => {
          element.check = false
          self.checkedLeftData.push(element)
        })

        this.rightCheckItems = []
        this.isrightIndeterminate = false
        this.checkrightAll = false

        // 刷新左侧效果
        this.leftCheckItemChange()
        this.rightCheckItemChange()
      } else {
        this.checkedLeftData = this.checkedLeftData.filter(item => {
          var remove = false
          self.leftCheckItems.forEach(element => {
            if (item.code == element.code) {
              remove = true
            }
          })
          return !remove
        })

        this.leftCheckItems.forEach(element => {
          element.check = false
          self.checkedRightData.push(element)
        })

        this.leftCheckItems = []
        this.isleftIndeterminate = false
        this.checkleftAll = false

        // 刷新右侧效果
        this.leftCheckItemChange()
        this.rightCheckItemChange()
      }
    }
  }
}
</script>
<style rel="stylesheet/scss" lang="scss" scoped>
.scene-name {
  font-size: 12px;
  padding-bottom: 5px;
  color: #aaa;
}

.default-mark {
  width: 4px;
  height: 4px;
  border-radius: 2px;
  margin-right: 5px;
}
.default-mark-active {
  background-color: $xr-color-primary;
}

.scene-list {
  user-select: none;

  .scene-list-box {
    flex: 1;
    border: 1px solid $xr-border-line-color;
    border-radius: 2px;
    height: 300px;
    position: relative;
    padding: 15px;
  }
  .scene-middle-list {
    width: 50px;
    text-align: center;
    button {
      border: 1px solid #cccccc;
      width: 34px;
      height: 34px;
      border-radius: 17px;
      background-color: #f5f7fa;
    }
    .scene-middle-icon {
      color: #cccccc;
      font-size: 14px;
    }
    .scene-middle-right-button {
      margin-top: 15px;
      margin-left: 0;
    }

    .scene-middle-button-select {
      border: 1px solid $xr-color-primary !important;
      background-color: $xr-color-primary !important;
      .scene-middle-icon {
        color: white !important;
      }
    }
  }
  .scene-list-head {
    padding-bottom: 15px;
    padding-left: 9px;
    border-bottom: 1px dashed $xr-border-line-color;
    font-size: 13px;
    .scene-list-head-name {
      color: #333;
      flex: 1;
      margin: 0 8px;
    }

    .scene-list-head-detail {
      color: #aaa;
    }
  }
  .scene-list-body {
    padding: 5px 0;
    height: 240px;
    position: relative;
    overflow-y: auto;
  }

  .list-item {
    padding: 5px 0;
    background-color: white;
    height: 26px;
    cursor: pointer;
    .list-item-check {
      margin-right: 8px;
    }
    .list-item-name {
      font-size: 13px;
      color: #333;
      flex: 1;
    }
    .list-item-handle {
      flex-shrink: 0;
      display: none;
      i {
        color: #aaa;
        font-size: 12px;
        margin-right: 5px;
      }
      i:hover {
        color: $xr-color-primary;
      }
    }
  }

  .list-item:hover {
    .list-item-handle {
      display: block;
    }
  }
}

.handle-bar {
  font-size: 12px;
  .handle-bar-add {
    cursor: pointer;
    color: #2362FB;
    padding: 10px;
  }
  .handle-bar-save {
    position: relative;
    height: 30px;
    button {
      float: right;
      margin-right: 10px;
    }
  }
}
</style>
