<template>
  <el-container>
    <el-header class="nav-container">
      <navbar nav-index="oa"/>
    </el-header>
    <el-container>
      <sidebar
        :items="currentRouters"
        main-router="oa">
      </sidebar>
      <el-main id="main-container" style="padding: 0;">
        <app-main/>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import { Navbar, Sidebar, AppMain } from './components'
import { mapGetters } from 'vuex'

export default {
  name: 'TableFormLayout',

  components: {
    Navbar,
    Sidebar,
    AppMain
  },

  data() {
    return {
      appCode: '',
      createShow: false
    }
  },

  computed: {
    ...mapGetters([
      'menuRouters',
      'allAuth'
    ]),
    currentRouters() {
      return this.menuRouters.oa
    },
    quickAddOffset() {
      return Math.round(this.quickAddList.length / 2) * 25
    }
  },
  mounted() {
    this.appCode = this.$route.params.model
  },
  methods: {
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
