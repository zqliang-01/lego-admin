<template>
  <el-container>
    <el-header class="nav-container">
      <navbar app-code="home"/>
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
import { mapGetters } from 'vuex'
import { Navbar, Sidebar, AppMain } from './components'

export default {
  name: 'HomeLayout',
  components: {
    Navbar,
    Sidebar,
    AppMain
  },
  computed: {
    ...mapGetters(['name'])
  },
  data() {
    return {}
  },
  created() {
    document.title = this.name + ' - 首页'
  },
  methods: {
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
