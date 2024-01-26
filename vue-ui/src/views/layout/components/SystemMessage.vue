<template>
  <div
    v-show="visible"
    class="full-container">
    <slide-view
      v-if="showDetail"
      :show-close="false"
      :no-listener-class="['bell-message-hook']"
      class="d-view"
      @close="hiddenView">
      <div class="sm-main">
        <div class="sm-main__hd">
          <span class="title">{{ title }}</span>
          <el-button
            v-if="permissionSave && onlyAnnouncement"
            icon="el-icon-plus"
            class="notice-btn"
            type="text"
            @click="createNotice">新建公告</el-button>
        </div>

        <flexbox
          v-if="!onlyAnnouncement"
          class="menu"
          wrap="wrap">
          <el-badge
            v-for="(item, index) in menuList"
            :key="index"
            :value="unreadNums[item.countKey]"
            :hidden="!unreadNums[item.countKey] || unreadNums[item.countKey] == 0"
            :max="99">
            <el-button
              :class="{'is-current': menuLabel == item.label}"
              type="primary"
              @click.native="menuClick(item.label)">{{ item.name }}</el-button>
          </el-badge>
        </flexbox>

        <div
          :style="{ height: contentHeight }"
          class="sm-main__bd">
          <div
            v-infinite-scroll="getList"
            :key="scrollKey"
            infinite-scroll-distance="100"
            infinite-scroll-disabled="scrollDisabled">
            <message-cell
              v-for="(item, index) in list"
              :key="index"
              :data="item"
              :data-index="index"
              @detail="checkCRMDetail"
              @download="downloadError"
              @read="readMessageClick"
              @delete="deleteMessageClick"/>
          </div>
          <p
            v-if="loading"
            class="scroll-bottom-tips">加载中...</p>
          <p
            v-if="noMore"
            class="scroll-bottom-tips">没有更多了</p>
        </div>

        <flexbox class="sm-main__ft">
          <div class="switch-read">
            <el-switch
              v-model="isUnRead"
              @change="refreshList"/>
            <span class="switch-read__title">仅显示未读消息</span>
          </div>
          <el-dropdown
            trigger="click"
            @command="handleCommand">
            <i
              class="el-icon-more more" />
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item
                icon="el-icon-check"
                command="read">{{ `全部${currentMenu.label == 'all' ? '' : currentMenu.name}标记为已读` }}</el-dropdown-item>
              <el-dropdown-item
                :icon="'s-delete' | iconPre"
                command="delete">{{ `删除${currentMenu.name}已读消息` }}</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </flexbox>
      </div>
    </slide-view>

  </div>
</template>

<script>
import {
  systemMessageListAPI,
  systemMessageReadAPI,
  systemMessageReadAllAPI,
  systemMessageClearAPI,
  systemMessageDeleteByIdAPI } from '@/api/common'
import SlideView from '@/components/SlideView'
import MessageCell from './MessageCell'

import { getMaxIndex } from '@/utils/index'
import { mapGetters } from 'vuex'

export default {
  // 导航头部系统消息
  name: 'SystemMessage',
  components: {
    SlideView,
    MessageCell
  },
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    // 仅公告
    onlyAnnouncement: {
      type: Boolean,
      default: false
    },
    unreadNums: Object
  },
  data() {
    return {
      showTodayDetail: false,
      todayDetailData: {},
      showDetail: false,
      // 1 任务 2 日志 3 oa审批 4公告 5 日程 6 客户管理
      menuLabel: 'all',

      // 公告
      announcementAddShow: false,

      // 列表
      list: [],
      loading: false,
      noMore: false,
      scrollKey: Date.now(),
      page: 1,
      isUnRead: false, // 仅展示未读

      showFullDetail: false, // 查看相关客户管理详情
      relationID: '', // 相关ID参数
      relationCrmType: '' // 相关类型
    }
  },
  computed: {
    ...mapGetters(['oa', 'crm', 'project']),
    permissionSave() {
      return this.oa && this.oa.announcement && this.oa.announcement.save
    },
    title() {
      if (this.onlyAnnouncement) {
        return this.unreadNums.announceCount && this.unreadNums.announceCount > 0 ? `公告（${this.unreadNums.announceCount}）` : '公告'
      }
      return this.unreadNums.allCunt > 0 ? `通知（${this.unreadNums.allCunt}）` : '通知'
    },

    scrollDisabled() {
      return this.loading || this.noMore
    },

    labelValue() {
      if (this.onlyAnnouncement) {
        return 4
      }
      return this.menuLabel == 'all' ? '' : this.menuLabel
    },

    contentHeight() {
      if (this.menuList.length > 7) {
        return 'calc(100% - 110px)'
      }
      return 'calc(100% - 155px)'
    },

    menuList() {
      const menuList = [{
        name: '全部',
        label: 'all',
        countKey: 'allCount'
      }]

      if ((this.oa && this.oa.taskExamine) || this.project) {
        menuList.push({
          name: '任务',
          label: 1,
          countKey: 'taskCount'
        })
      }

      if (this.oa && this.oa.log) {
        menuList.push({
          name: '日志',
          label: 2,
          countKey: 'logCount'
        })
      }

      if (this.crm) {
        menuList.push({
          name: '客户管理',
          label: 6,
          countKey: 'crmCount'
        })
      }

      if (this.oa && this.oa.calendar) {
        menuList.push({
          name: '日程',
          label: 5,
          countKey: 'eventCount'
        })
      }

      return menuList
    },

    currentMenu() {
      return this.menuList.find(item => {
        return item.label === this.menuLabel
      })
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
    document.body.appendChild(this.$el)
    this.$el.addEventListener('click', this.handleDocumentClick, false)
  },

  beforeDestroy() {
    // remove DOM node after destroy
    if (this.$el && this.$el.parentNode) {
      this.$el.parentNode.removeChild(this.$el)
      this.$el.removeEventListener('click', this.handleDocumentClick, false)
    }
  },
  methods: {
    /**
     * 日程事件
     */
    todayHandle() {
      this.$bus.emit('handleSuccess')
      this.showTodayDetail = false
    },

    /**
     * 新建公告
     */
    createNotice() {
      this.announcementAddShow = true
    },

    announcementSubmiteSuccess() {
      setTimeout(() => {
        this.refreshList()
        this.$emit('update-count')
      }, 1000)
    },

    /**
     * 身体
     */

    /**
     * 菜单点击
     */
    menuClick(label) {
      this.menuLabel = label
      this.refreshList()
    },

    /**
     * 刷新列表
     */
    refreshList() {
      this.page = 1
      this.list = []
      this.noMore = false
      this.scrollKey = Date.now()
    },

    /**
     * 获取列表
     */
    getList() {
      this.loading = true
      const params = {
        page: this.page,
        limit: 15,
        label: this.labelValue
      }
      if (this.isUnRead) {
        params.isRead = 0
      }
      systemMessageListAPI(params)
        .then(res => {
          this.loading = false
          if (this.labelValue == params.label) {
            if (!this.noMore) {
              if (this.page == 1) {
                this.list = res.data.list
              } else {
                this.list = this.list.concat(res.data.list)
              }
              this.page++
            }
            this.noMore = res.data.lastPage
          } else {
            this.refreshList()
          }
        })
        .catch(() => {
          this.noMore = true
          this.loading = false
        })
    },



    /**
     * 查看详情
     */
    checkCRMDetail(type, id, dataIndex, data) {
      if (type === 'schedule') {
        this.relationID = id
        if (data.content) {
          this.todayDetailData = JSON.parse(data.content)
        }
        this.showTodayDetail = true
      } else {
        this.relationID = id
        this.relationCrmType = type
        this.showFullDetail = true
      }
    },

    /**
     * 下载错误数据
     */
    downloadError(messageId) {
    },

    /**
     * 读取消息
     */
    readMessageClick(messageId, index) {
      systemMessageReadAPI({ messageId })
        .then(res => {
          this.list[index].isRead = 1
          this.$emit('update-count')
        })
        .catch(() => {
        })
    },

    deleteMessageClick(messageId, index) {
      this.$confirm('确定删除这条消息?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          systemMessageDeleteByIdAPI(messageId)
            .then(res => {
              this.list.splice(index, 1)
              this.$emit('update-count')
              this.$message.success('操作成功')
            })
            .catch(() => {})
        })
        .catch(() => {
          this.$message({
            type: 'info',
            message: '已取消操作'
          })
        })
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
      systemMessageReadAllAPI({
        label: this.labelValue
      })
        .then(res => {
          this.list.forEach(item => {
            item.isRead = 1
          })
          this.$emit('update-count')
        })
        .catch(() => {
        })
    },

    /**
     * 全部删除
     */
    allDeleteClick() {
      if (this.currentMenu) {
        const name = this.currentMenu.label == 'all' ? '' : this.currentMenu.name
        this.$confirm(`确定删除全部${name}已读消息?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
          .then(() => {
            systemMessageClearAPI({
              label: this.labelValue
            })
              .then(res => {
                this.refreshList()
                this.$emit('update-count')
                this.$message.success('操作成功')
              })
              .catch(() => {})
          })
          .catch(() => {
            this.$message({
              type: 'info',
              message: '已取消操作'
            })
          })
      }
    },

    hiddenView() {
      this.showDetail = false
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
