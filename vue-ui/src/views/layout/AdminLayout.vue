<template>
  <el-container>
    <el-header class="nav-container">
      <manager-navbar app-code="manage"/>
    </el-header>
    <el-container>
      <sidebar :items="manageRouters" class="sidebar-container" />
      <el-main id="manager-main-container">
        <app-main />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed } from 'vue'
import { useStore } from 'vuex'
import { AppMain, ManagerNavbar, Sidebar } from './components'

// 使用 store
const store = useStore()

// 计算属性
const manageRouters = computed(() => store.getters.manageRouters)

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
