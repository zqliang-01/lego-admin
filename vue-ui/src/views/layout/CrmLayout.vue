<template>
  <el-container>
    <el-header class="nav-container">
      <navbar
        nav-index="crm"
        @nav-items-click="navClick"/>
    </el-header>
    <el-container>
      <sidebar
        :items="crmRouters"
        :add-offset="quickAddOffset"
        create-button-title="快速创建"
        main-router="crm"
        @select="handleSelect">
        <div
          slot="add"
          class="quick-add">
          <div class="quick-add-content">
            <p
              v-for="(item, index) in quickAddList"
              :key="index"
              @click="addSkip(item)">
            <i :class="['wk', 'wk-' + item.icon]"/><span>{{ item.label }}</span></p>
          </div>
        </div>
      </sidebar>
      <el-main
        id="crm-main-container"
        style="padding: 0;">
        <app-main/>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import { Navbar, Sidebar, AppMain } from './components'

import { mapGetters } from 'vuex'

export default {
  name: 'CrmLayout',

  components: {
    Navbar,
    Sidebar,
    AppMain
  },

  data() {
    return {
      isCreate: false,
      createAction: null,
      createType: ''
    }
  },

  computed: {
    ...mapGetters(['crmRouters']),
    // 快捷添加
    quickAddList() {
      return []
    },
    quickAddOffset() {
      return Math.round(this.quickAddList.length / 2) * 25
    }
  },

  created() {
  },

  mounted() {},

  methods: {
    navClick(index) { },

    addSkip(item) {
      this.createAction = {
        type: 'save',
        id: '',
        data: {}
      }
      this.createType = item.index
      this.isCreate = true
    },

    /**
     * 菜单钢鞭
     */
    handleSelect() {
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
