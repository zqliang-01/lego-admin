<template>
  <div
    v-show="visible"
    class="full-container">
    <template>
      <slide-view
        :visible.sync="showDetail"
        :show-close="false"
        :no-listener-class="['lego-announcement']"
        class="d-view"
        @close="showDetail = false">
        <div class="sm-main">
          <div class="sm-main__hd">
            <span class="title">公告</span>
          </div>
          <div :style="{ height: contentHeight }" class="sm-main__bd">
            <div
              v-infinite-scroll="getList"
              :key="scrollKey"
              infinite-scroll-distance="15"
              infinite-scroll-disabled="scrollDisabled">
              <message-cell
                v-for="(item, index) in dataList"
                :key="index"
                :message-data="item"
                :data-index="index"
                @detail="checkDetail"
                @read="readMessageClick"
                @delete="deleteMessageClick"/>
            </div>
            <p v-if="loading" class="scroll-bottom-tips">加载中...</p>
            <p v-if="noMore" class="scroll-bottom-tips">没有更多了</p>
          </div>

          <flexbox class="sm-main__ft">
            <div class="switch-read">
              <el-switch
                v-model="isUnRead"
                @change="refreshList"/>
              <span class="switch-read__title">仅显示未读公告</span>
            </div>
            <el-dropdown
              trigger="click"
              @command="handleCommand">
              <i class="el-icon-more more" />
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item
                  icon="el-icon-check"
                  command="read">{{ `全部公告标记为已读` }}</el-dropdown-item>
                <el-dropdown-item
                  :icon="'s-delete' | iconPre"
                  command="delete">{{ `删除已读公告` }}</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </flexbox>
        </div>
      </slide-view>
    </template>
  </div>
</template>

<script>
import {
  noticeMessageListAPI,
  noticeMessageReadAPI,
  noticeMessageReadAllAPI,
  noticeMessageDeleteAPI,
  noticeMessageDeleteAllAPI } from '@/api/noticeMessage'
import SlideView from '@/components/Layout/SlideView'
import MessageCell from './NoticeMessageCell'
import { getMaxIndex } from '@/utils/index'

export default {
  name: 'SystemMessage',
  components: {
    SlideView,
    MessageCell
  },
  props: {
    visible: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      showDetail: false,
      dataList: [],
      loading: false,
      noMore: false,
      scrollKey: Date.now(),
      pageIndex: 1,
      isUnRead: false
    }
  },
  computed: {
    scrollDisabled() {
      return this.loading || this.noMore
    },
    contentHeight() {
      return 'calc(100% - 155px)'
    }
  },
  watch: {
    visible: {
      handler(val) {
        this.showDetail = val
        if (val) {
          this.$el.style.zIndex = getMaxIndex()
          this.refreshList()
        }
      },
      immediate: true
    },
    showDetail(val) {
      if (!val) {
        setTimeout(() => {
          this.$emit('update:visible', false)
        }, 350)
      }
    }
  },
  mounted() {
    this.$nextTick(() => {
      document.body.appendChild(this.$el)
      this.$el.addEventListener('click', this.handleDocumentClick, false)
    })
  },

  beforeDestroy() {
    if (this.$el && this.$el.parentNode) {
      this.$el.parentNode.removeChild(this.$el)
      this.$el.removeEventListener('click', this.handleDocumentClick, false)
    }
  },
  methods: {

    /**
     * 菜单点击
     */
    menuClick(code) {
      this.refreshList()
    },

    /**
     * 刷新列表
     */
    refreshList() {
      this.pageIndex = 1
      this.dataList = []
      this.noMore = false
      this.scrollKey = Date.now()
    },

    /**
     * 获取列表
     */
    getList() {
      this.loading = true
      const params = {
        pageIndex: this.pageIndex,
        pageSize: 15
      }
      if (this.isUnRead) {
        params.readed = 0
      }
      noticeMessageListAPI(params)
        .then(res => {
          this.loading = false
          if (!this.noMore) {
            if (this.pageIndex == 1) {
              this.dataList = res.data.result
            } else {
              this.dataList = this.dataList.concat(res.data.result)
            }
            this.pageIndex++
          }
          this.noMore = res.data.result.length === 0
        })
        .catch(() => {
          this.noMore = true
          this.loading = false
        })
    },

    /**
     * 查看详情
     */
    checkDetail(id, dataIndex, data) {
    },

    /**
     * 读取公告
     */
    readMessageClick(messageId, index) {
      noticeMessageReadAPI(messageId).then(res => {
        this.dataList[index].readed = true
        this.$emit('update-count')
      }).catch(() => {})
    },

    deleteMessageClick(messageId, index) {
      this.$confirm('确定删除这条公告?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        noticeMessageDeleteAPI(messageId).then(res => {
          this.dataList.splice(index, 1)
          this.$emit('update-count')
          this.$message.success('操作成功')
        }).catch(() => {

        })
      }).catch(() => { })
    },

    handleCommand(action) {
      if (action === 'delete') {
        this.allDeleteClick()
      } else {
        this.allMarkDoneClick()
      }
    },

    /**
     * 全部标记完成
     */
    allMarkDoneClick() {
      noticeMessageReadAllAPI().then(() => {
        this.dataList.forEach(item => { item.readed = true })
        this.$emit('update-count')
      }).catch(() => {})
    },

    /**
     * 全部删除
     */
    allDeleteClick() {
      this.$confirm(`确定删除全部已读公告?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        noticeMessageDeleteAllAPI().then(() => {
          this.refreshList()
          this.$emit('update-count')
          this.$message.success('操作成功')
        }).catch(() => {})
      }).catch(() => {
        this.$message({ type: 'info', message: '已取消操作' })
      })
    },
    handleDocumentClick(e) {
      e.stopPropagation()
      if (this.$el == e.target) {
        this.showDetail = false
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.full-container {
  position: fixed;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  overflow: auto;
  margin: 0;
  background-color: rgba(0, 0, 0, 0.3);
}

.d-view {
  position: fixed;
  width: 530px;
  top: 0px;
  bottom: 0px;
  right: 0px;
  background-color: white;
  ::v-deep .el-card__body {
    height: 100%;
  }
}

.sm-main {
  height: 100%;
  position: relative;

  &__hd {
    padding: 0 20px;
    height: 50px;
    line-height: 50px;
    font-size: 14px;
    color: #333;
    background-color: #f7f8fa;

    .title {
      font-weight: 600;
    }

    .notice-btn {
      float: right;
      margin-top: 10px;
    }
  }

  &__bd {
    overflow-y: auto;
  }

  &__ft {
    position: absolute;
    left: 0;
    right: 0;
    bottom: -1px;
    height: 50px;
    background-color: #f7f8fa;
    padding: 0 15px;

    .switch-read {
      flex: 1;
      &__title {
        font-size: 13px;
        margin-left: 10px;
        color: #333;
      }
    }

    .more {
      flex-shrink: 0;
      cursor: pointer;
    }

    .more:hover {
      color: $xr-color-primary;
    }
  }
}

// 菜单
.menu {
  overflow-x: auto;
  overflow-y: hidden;
  padding-right: 15px;
  margin-bottom: 15px;
  // line-height: 60px;
  border-top: 1px solid $xr-border-color-base;
  // border-bottom: 1px solid $xr-border-color-base;

  .el-button {
    color: #666;
    margin-left: 15px;
    background-color: $xr--background-color-base;
    border-color: $xr--background-color-base;
    font-size: 12px;
    height: 28px;
    border-radius: 14px;
    ::v-deep i {
      font-size: 12px;
      margin-right: 5px;
    }
  }

  .el-button--primary.is-current,
  .el-button--primary:hover {
    background: $xr-color-primary;
    border-color: $xr-color-primary;
    color: #ffffff;
  }

  .el-badge {
    margin-top: 15px;
  }
}


.scroll-bottom-tips {
  margin: 15px 0 65px;
}

.el-badge {
  ::v-deep .el-badge__content.is-fixed {
    z-index: 2;
  }
}
</style>
