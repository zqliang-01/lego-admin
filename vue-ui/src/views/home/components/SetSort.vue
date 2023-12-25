<template>
  <el-dialog
    ref="legoDialog"
    :visible="visible"
    :append-to-body="true"
    :close-on-click-modal="false"
    title="仪表盘模块设置"
    custom-class="no-padding-dialog"
    width="650px"
    @close="handleCancel">
    <div class="handle-box">
      <div class="title">拖动以下模块可设置模块位置，点击开启/关闭按钮可设置模块是否在仪表盘显示，点击保存按钮即可生效。</div>
      <flexbox
        align="stretch"
        class="section">
        <div class="left">
          <draggable
            v-model="sortLeft"
            :group="{ name: 'sort'}"
            v-bind="{ forceFallback: false }"
            class="draggable-box">
            <set-sort-item
              v-for="(item, index) in sortLeft"
              :key="index"
              :title="item.title"
              :icon="item.icon | iconPre"
              :icon-color="item.iconColor"
              :img="item.image"
              class="content"
            >
              <el-switch
                slot="header"
                v-model="item.show"/>
            </set-sort-item>
          </draggable>
        </div>
        <div class="right">
          <draggable
            v-model="sortRight"
            :group="{ name: 'sort'}"
            v-bind="{ forceFallback: false }"
            class="draggable-box">
            <set-sort-item
              v-for="(item, index) in sortRight"
              :key="index"
              :title="item.title"
              :icon="item.icon | iconPre"
              :icon-color="item.iconColor"
              :img="item.image"
              class="content"
            >
              <el-switch
                slot="header"
                v-model="item.show"/>
            </set-sort-item>
          </draggable>
        </div>
      </flexbox>
    </div>
    <span
      slot="footer"
      class="dialog-footer">
      <el-button @click.native="handleCancel">取消</el-button>
      <el-button
        type="primary"
        @click.native="handleConfirm">保存</el-button>
    </span>
  </el-dialog>
</template>

<script>
import SetSortItem from './SetSortItem'
import draggable from 'vuedraggable'

import ElDialogLoadingMixin from '@/mixins/ElDialogLoading'

export default {
  // 设置仪表盘排序
  name: 'SetSort',
  components: {
    SetSortItem,
    draggable
  },
  mixins: [ElDialogLoadingMixin],
  props: {
    visible: {
      type: Boolean,
      required: true,
      default: false
    },
    chartList: {
      type: Array,
      default: () => {
        return []
      }
    }
  },
  data() {
    return {
      loading: false,
      sortLeft: [],
      sortRight: []
    }
  },
  watch: {
    chartList: {
      handler() {
        this.sortLeft = []
        this.sortRight = []
        this.chartList.forEach(chart => {
          if (chart.position == 'left') {
            this.sortLeft.push(chart)
          } else {
            this.sortRight.push(chart)
          }
        })
      },
      deep: true,
      immediate: true
    }
  },
  created() {
    this.getModelSort()
  },
  methods: {
    getModelSort() {

    },

    /**
     * 取消选择
     */
    handleCancel() {
      this.$emit('update:visible', false)
    },

    handleConfirm() {
      const params = {}
      params.left = this.sortLeft
      params.right = this.sortRight
      this.$message.success('修改成功！')
      this.$emit('update:visible', false)
    }
  }
}
</script>

<style lang="scss" scoped>
.handle-box {
  background-color: #efefef;
  color: #333;
  font-size: 12px;
  padding: 15px;
  .title {
    color: #999;
  }
}

.section {
  margin-top: 12px;
  height: 40vh;
  overflow: auto;
  user-select: none;

  .left {
    width: calc(60.5% - 12px);
    margin-right: 12px;
  }
  .right {
    width: 39.5%;
  }

  .content {
    height: 100px;

    /deep/ .el-switch {
      position: absolute;
      right: 0;
      top: 0;
    }
  }

  .content + .content {
    margin-top: 12px;
  }
}

.draggable-box {
  height: 100%;
}
</style>
