<template>
  <el-container>
    <el-header class="nav-container">
      <manager-navbar nav-index="manage"/>
    </el-header>
    <el-container>
      <sidebar :items="manageRouters" class="sidebar-container" />
      <el-main id="manager-main-container">
        <app-main />
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import { mapGetters } from 'vuex'
import { ManagerNavbar, Sidebar, AppMain } from './components'

export default {
  name: 'AdminLayout',
  components: {
    ManagerNavbar,
    Sidebar,
    AppMain
  },
  data() {
    return {
      routerItems: []
    }
  },

  computed: {
    ...mapGetters(['manage', 'manageRouters'])
  },

  mounted() {},

  methods: {
    navClick(index) {}
  }
}
</script>

<style lang="scss" scoped>
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

#manager-main-container {
  max-height: 100%;
  padding: 0px;
}

.el-container {
  overflow: hidden;
  height: 100%;
}
</style>
