<template>
  <div class="navbar">
    <img
      v-src="logoUrl"
      :key="logo"
      class="logo"
      @click="enterMainPage" >
    <div class="nav-items-container">
      <el-menu
        :default-active="navActiveIndex"
        mode="horizontal"
        active-text-color="#2362FB"
        @select="navItemsClick">
        <el-menu-item
          v-for="(item, index) in showItems"
          :key="index"
          :index="item.code">
          <i :class="item.icon | iconPre" style="fontSize: 17px;margin-right: 0.3em;" />
          <span>{{ item.name }}</span>
        </el-menu-item>
        <el-menu-item ref="navManagerMenu" index="other">
          <i :class="'grid' | iconPre" />
        </el-menu-item>
      </el-menu>
    </div>

    <el-badge
      :value="unreadNums.announceCount"
      :hidden="!unreadNums.announceCount || unreadNums.announceCount == 0"
      :max="99">
      <i
        :class="'announcement' | iconPre"
        @click="checkMessageDetail(true)"/>
    </el-badge>

    <el-badge
      :value="unreadNums.allCount"
      :hidden="!unreadNums.allCount || unreadNums.allCount == 0"
      :max="99">
      <i
        :class="'bell' | iconPre"
        @click="checkMessageDetail(false)"/>
    </el-badge>

    <system-message
      :visible.sync="sysMessageShow"
      :unread-nums="unreadNums"
      :only-announcement="mesOnlyAnnouncement"
      @update-count="sendSystemUnreadNum"/>

    <el-dropdown
      trigger="click"
      @command="moreMenuClick">
      <div
        class="user-container">
        <template v-if="userInfo && Object.keys(userInfo).length > 0">
          <xr-avatar
            :name="userInfo.name"
            :size="32"
            :src="userInfo.imageCode"
            class="user-img" />
        </template>
        <i class="el-icon-caret-bottom mark"/>
      </div>
      <el-dropdown-menu
        slot="dropdown"
        class="el-dropdown-unarrow" >
        <el-dropdown-item
          v-for="(item, index) in moreMenu"
          :key="index"
          :command="item.command"
          :divided="item.divided"
          :icon="item.icon | iconPre"
          :disabled="item.disabled"
        >{{ item.label }}</el-dropdown-item>
        <div
          v-if="manageAuth"
          class="handel-box">
          <el-button
            type="primary"
            class="handel-button"
            @click="enterSystemSet()">企业管理后台</el-button>
        </div>
      </el-dropdown-menu>
    </el-dropdown>

    <nav-manager
      v-if="navManagerShow"
      ref="navManager"
      :collapse="collapse"
      :top-module="items"
      @change="getTopHeaderModule"
      @select="navManagerSelect" />
  </div>
</template>

<script>
import { filePreviewUrl, systemMessageUnreadCountAPI } from '@/api/common'
import { headerModelListAPI } from '@/api/config'
import SystemMessage from './SystemMessage'
import NavManager from './NavManager'

import { mapGetters } from 'vuex'
import { Loading } from 'element-ui'
import { on, off } from '@/utils/dom'

export default {
  components: {
    SystemMessage,
    NavManager
  },
  props: {
    navIndex: {
      type: [Number, String],
      default: 0
    }
  },
  data() {
    return {
      unreadNums: {
        allCount: 0,
        announceCount: 0,
        crmCount: 0,
        examineCount: 0,
        eventCount: 0,
        logCount: 0,
        taskCount: 0
      },
      mesOnlyAnnouncement: false,
      sysMessageShow: false,
      intervalId: null,
      type: 0,
      items: [],
      homePage: {
        code: 'home',
        name: '首页',
        icon: 'customer',
        path: '/home'
      },
      showItems: [],
      hiddenItems: [],
      navManagerShow: false,
      isNewest: false
    }
  },
  computed: {
    ...mapGetters([
      'userInfo',
      'lang',
      'logo',
      'navActiveIndex',
      'collapse',
      'allAuth',
      'headerModule'
    ]),
    logoUrl() {
      if (this.logo) {
        return filePreviewUrl + this.logo
      }
      return require('@/assets/img/logo.png')
    },
    moreMenu() {
      return [{
        command: 'baseInfo',
        divided: false,
        label: '基本信息',
        icon: 'user'
      },
      {
        command: 'logOut',
        divided: false,
        label: '退出登录',
        icon: 'logout'
      }
      ]
    },
    manageAuth() {
      if (this.allAuth) {
        return Object.prototype.hasOwnProperty.call(this.allAuth, 'manage')
      }
      return false
    }
  },
  watch: {
    navManagerShow(val) {
      if (val == false) {
        this.changeNavIndex()
      }
    }
  },
  mounted() {
    on(document, 'click', this.handleDocumentClick)

    window.onresize = () => {
      this.changeMenu(document.documentElement.clientWidth)
    }

    // 消息数
    this.getSystemUnreadNum('visible')

    this.$bus.on('document-visibility', (state) => {
      this.getSystemUnreadNum(state)
    })
  },
  beforeDestroy() {
    this.$bus.off('document-visibility')
    if (this.intervalId) {
      clearInterval(this.intervalId)
      this.intervalId = null
    }
    off(document, 'click', this.handleDocumentClick)
  },
  created() {
    this.getTopHeaderModule()
  },
  methods: {
    /**
     * 获取置顶头
     */
    getTopHeaderModule() {
      headerModelListAPI().then(res => {
        this.items = res.data
        this.items.unshift(this.homePage)
        this.changeMenu(document.documentElement.clientWidth)
      })
    },

    changeMenu(clintWidth) {
      this.showItems = []
      this.hiddenItems = []
      for (let index = 0; index < this.items.length; index++) {
        const element = this.items[index]
        if (clintWidth - 450 - (index + 1) * 130 > 0) {
          this.showItems.push(element)
        } else {
          this.hiddenItems.push(element)
        }
      }
      this.changeNavIndex()
    },

    changeNavIndex() {
      let isHidden = true
      this.showItems.forEach(item => {
        if (this.navIndex === item.code) {
          isHidden = false
        }
      })
      if (this.navActiveIndex !== this.navIndex) {
        this.$store.commit('SET_NAVACTIVEINDEX', this.navIndex)
        if (isHidden) {
          this.navItemsClick(this.navIndex)
        }
      }
    },

    navItemsClick(code) {
      if (code === 'other') {
        this.navManagerShow = !this.navManagerShow
      } else if (code === 'user') {
        this.navManagerShow = false
        code = 'other'
      } else {
        this.navManagerShow = false
        this.$router.push('/' + code)
      }
      this.$store.commit('SET_NAVACTIVEINDEX', code)
      this.$emit('nav-items-click', code)
    },
    enterSystemSet() {
      this.$router.push({
        name: 'manage'
      })
    },

    /**
     * 获取系统未读消息数
     */
    getSystemUnreadNum(state) {
      if (this.intervalId) {
        clearInterval(this.intervalId)
        this.intervalId = null
      }
      if (state == 'visible') {
        this.sendSystemUnreadNum()
        this.intervalId = setInterval(() => {
          this.sendSystemUnreadNum()
        }, 600000)
      }
    },

    sendSystemUnreadNum() {
      systemMessageUnreadCountAPI()
        .then(res => {
          this.unreadNums = res.data
        })
        .catch(() => {
          if (this.intervalId) {
            clearInterval(this.intervalId)
            this.intervalId = null
          }
        })
    },

    /**
     * 有客户权限点击logo 进入仪表盘
     */
    enterMainPage() {
      this.$router.push('/')
    },

    moreMenuClick(command) {
      if (command == 'baseInfo') {
        this.$router.push({
          name: 'person'
        })
      } else if (command == 'logOut') {
        this.$confirm('退出登录？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
          .then(() => {
            var loading = Loading.service({
              target: document.getElementById('#app')
            })
            this.$store
              .dispatch('LogOut')
              .then(() => {
                location.reload()
                loading.close()
              })
              .catch(() => {
                location.reload()
                loading.close()
              })
          })
          .catch(() => {})
      }
    },

    /**
     * 控制导航管理隐藏
     */
    handleDocumentClick(e) {
      if (!this.$refs.navManagerMenu ||
        !this.$refs.navManager ||
        this.$refs.navManagerMenu.$el.contains(e.target) ||
        this.$refs.navManager.$el.contains(e.target)) {
        return
      }
      this.navManagerShow = false
    },

    navManagerSelect({ path, code }) {
      this.navManagerShow = false
      this.$router.push(path)
      const index = this.headerModule.findIndex(module => module.code === code)
      let navActiveIndex = code
      if (index === -1) {
        navActiveIndex = 'other'
      }
      this.$store.commit('SET_NAVACTIVEINDEX', navActiveIndex)
    },

    /**
     * 查看消息详情
     */
    checkMessageDetail(onlyAnnouncement) {
      this.mesOnlyAnnouncement = onlyAnnouncement
      this.sysMessageShow = true
    }
  }
}
</script>

<style lang="scss" scoped>
.navbar {
  height: 60px;
  min-height: 60px;
  background-color: white;
  display: flex;
  align-items: center;
  position: relative;
  padding: 0 30px;
  .logo {
    width: 150px;
    height: 40px;
    display: block;
    flex-shrink: 0;
    margin-right: 15px;
    background-color: white;
    cursor: pointer;
  }
  .nav-items-container {
    flex: 1;
    display: flex;
    min-width: 500px;
    height: 100%;
    overflow-x: auto;
    line-height: 60px;
    font-size: 15px;
  }

  .user-container {
    display: flex;
    align-items: center;
    flex-shrink: 0;
    cursor: pointer;
    .user-img {
      margin-right: 8px;
    }
    .mark {
      font-size: 15px;
      color: #aaaaaa;
    }
  }

  .user-container:hover {
    .mark {
      color: #2486e4;
    }
  }
}

// 菜单
.el-menu {
  overflow: hidden;

  ::v-deep .el-submenu__icon-arrow {
    display: none;
  }

  ::v-deep .el-submenu {
    .el-submenu__title {
      i {
        color: #5c6075;
      }
    }
  }

  ::v-deep .el-submenu.is-active {
    .el-submenu__title {
      i {
        color: $xr-color-primary;
      }
    }
  }
}

.el-menu.el-menu--horizontal {
  border-bottom: none;
}

.el-menu--horizontal .el-menu-item {
  padding: 0;
  margin: 0 20px !important;
  font-size: 16px;
  font-weight: 500;
  color: #2a304d;
  i {
    color: #5c6075;
  }
}

.el-menu-item:hover {
  i {
    color: $xr-color-primary;
  }
}

.el-menu--horizontal .el-menu-item:not(.is-disabled):hover {
  color: $xr-color-primary;
}

.el-menu-item.is-active {
  border-width: 3px;
  i {
    color: $xr-color-primary;
  }
}

// 右侧操作
.handel-box {
  padding: 0 15px;
  border-top: 1px solid #EBEEF5;
  padding-top: 10px;
  .handel-button {
    width: 100%;
    border-radius: $xr-border-radius-base;
    border-color: #2362fb;
    background-color: #2362fb;
  }
}

.hr-top {
  margin-top: 8px;
  border-top: 1px solid #f4f4f4;
  padding-top: 3px;
}

.nav-lang {
  cursor: pointer;
  color: #888;
  padding: 20px;
  &:hover {
    color: #2362fb;
  }
  &.active {
    font-weight: bold;
    color: #2362fb;
  }
}

// 系统消息
.wk-announcement,
.wk-bell {
  color: #9DA9C2;
  cursor: pointer;
  font-size: 20px;
}

.el-badge {
  margin-right: 20px;
}

.wk-announcement:hover,
.wk-bell:hover {
  color: $xr-color-primary;
}

.el-dropdown-menu {
  ::v-deep .popper__arrow {
    display: none;
  }
}

.top-btn {
  display: flex;
  flex-direction: row;
  justify-content: flex-start;
  align-items: center;
  color: $xr-color-primary;
  .gift {
    color: $xr-color-primary;
    margin-right: 5px;
  }
}

.wk-update-tips {
  font-size: 12px;
  text-align: center;
  color: #bbb;
  margin-bottom: 10px;

  .el-badge {
    margin-left: 5px;
    margin-right: 0;
  }

  .el-button {
    padding: 2px 5px;
    font-size: 12px;
  }
}
</style>

