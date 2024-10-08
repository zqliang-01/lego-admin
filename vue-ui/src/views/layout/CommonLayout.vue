<template>
  <el-container>
    <el-header class="nav-container">
      <navbar :app-code="appCode"/>
    </el-header>
    <el-container>
      <sidebar
        :main-router="appCode"
        :items="currentRouters"
        :add-offset="quickAddOffset"
        :show-create-button="showQuickAddButton"
        create-button-title="快速创建">
        <div
          v-if="showQuickAddButton"
          slot="add"
          class="quick-add">
          <div class="quick-add-content">
            <p
              v-for="(item, index) in quickAddList"
              :key="index"
              @click="addSkip(item)">
            <i :class="item.icon | iconPre"/><span>{{ item.title }}</span></p>
          </div>
        </div>
      </sidebar>
      <el-main id="main-container" style="padding: 0;">
        <app-main/>
      </el-main>
    </el-container>
    <lego-all-create
      :visible.sync="createShow"
      :form-code="formCode"
      @close="createShow = false"
      @handle="actionHandle"
    />
  </el-container>
</template>

<script>
import { Navbar, Sidebar, AppMain, Welcome } from './components'
import { isObject } from '@/utils/types'
import { mapGetters } from 'vuex'

export default {
  name: 'CommonLayout',
  props: {
    currentAppCode: String
  },
  components: {
    Navbar,
    Sidebar,
    AppMain,
    Welcome
  },
  data() {
    return {
      formCode: '',
      appCode: '',
      quickAddList: [],
      createShow: false
    }
  },

  computed: {
    ...mapGetters([
      'menuRouters',
      'navActiveIndex',
      'allAuth'
    ]),
    currentRouters() {
      return this.menuRouters[this.appCode]
    },
    appName() {
      if (this.appCode) {
        return this.allAuth[this.appCode].title
      }
      return ''
    },
    quickAddOffset() {
      return Math.round(this.quickAddList.length / 2) * 25
    },
    showQuickAddButton() {
      return this.quickAddList.length > 0
    }
  },
  watch: {
    $route(to, from) {
      this.init()
    }
  },
  mounted() {
    this.init()
  },
  methods: {
    init() {
      const urls = this.$route.path.split('/')
      this.appCode = this.$route.params.model || this.navActiveIndex
      if (urls.length > 1 && urls[1] !== this.appCode) {
        this.appCode = urls[1]
      }
      if (this.currentAppCode) {
        this.appCode = this.currentAppCode
      }
      if (this.appCode) {
        this.quickAddList = []
        this.addQuickAddMenu(this.allAuth[this.appCode])
        this.quickAddList.sort(function(a, b) {
          return a.sn - b.sn
        })
      }
    },
    addSkip(item) {
      this.formCode = item.formCode
      this.createShow = true
    },
    actionHandle(data) {
      if (data.type === 'save-success') {
        this.$message.success('创建成功！')
        return
      }
    },
    addQuickAddMenu(obj) {
      let hasChildren = false
      for (const key in obj) {
        if (isObject(obj[key])) {
          hasChildren = true
          this.addQuickAddMenu(obj[key])
        }
      }
      if (!hasChildren && obj && obj.formCode && obj.add) {
        this.quickAddList.push({
          sn: obj.sn,
          title: obj.title,
          icon: obj.icon,
          formCode: obj.formCode
        })
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import './styles/common.scss';
.el-container {
  min-height: 0;
  height: 100%;
}
.aside-container {
  position: relative;
  background-color: #2d3037;
  box-sizing: border-box;
}

.nav-container {
  padding: 0;
  box-shadow: 0px 1px 2px #dbdbdb;
  z-index: 100;
  min-width: 1200px;
}
</style>
