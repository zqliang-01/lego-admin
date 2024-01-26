<template>
  <el-container>
    <el-header class="nav-container">
      <navbar
        nav-index="user"
        @nav-items-click="navClick"/>
    </el-header>
    <el-container>
      <el-main
        id="main-container"
        style="padding:15px;">
        <app-main/>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import { Navbar, AppMain } from './components'
import { mapGetters } from 'vuex'

export default {
  name: 'UserLayout',
  components: {
    Navbar,
    AppMain
  },
  data() {
    return {}
  },
  computed: {
    ...mapGetters(['navActiveIndex'])
  },
  methods: {
    navClick(index) {}
  }
}
</script>

<style lang="scss" scoped>
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
