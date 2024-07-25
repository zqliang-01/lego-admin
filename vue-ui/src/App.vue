<template>
  <div id="app">
    <router-view class="router-view" />
  </div>
</template>

<script>
import cache from '@/utils/cache'

export default {
  name: 'App',
  mounted() {
    this.addDocumentVisibilityChange()
    this.setMinHeight()
  },
  methods: {
    addDocumentVisibilityChange() {
      // 网页当前状态判断
      // hidden,
      var state, visibilityChange
      if (typeof document.hidden !== 'undefined') {
        // hidden = 'hidden'
        visibilityChange = 'visibilitychange'
        state = 'visibilityState'
      } else if (typeof document.mozHidden !== 'undefined') {
        // hidden = 'mozHidden'
        visibilityChange = 'mozvisibilitychange'
        state = 'mozVisibilityState'
      } else if (typeof document.msHidden !== 'undefined') {
        // hidden = 'msHidden'
        visibilityChange = 'msvisibilitychange'
        state = 'msVisibilityState'
      } else if (typeof document.webkitHidden !== 'undefined') {
        // hidden = 'webkitHidden'
        visibilityChange = 'webkitvisibilitychange'
        state = 'webkitVisibilityState'
      }
      // 添加监听器，在title里显示状态变化
      document.addEventListener(visibilityChange, () => {
        if (document[state] == 'visible') {
          if (cache.updateAxiosHeaders() && this.$route.name === 'login') {
            window.location.reload()
          }
        }
      }, false)
    },

    setMinHeight() {
      this.$nextTick(() => {
        const dpr = window.devicePixelRatio || 1
        const clientWidth = document.body.clientWidth
        const dom = document.getElementById('app')
        if (dpr !== 1 && clientWidth > 1600) {
          dom.style.minHeight = '800px'
        } else if (dpr === 1 && clientWidth > 1600) {
          dom.style.minWidth = '1650px'
        } else {
          // dom.style.minWidth = '1200px'
          dom.style.minHeight = '605px'
        }
      })
    }
  }
}
</script>

<style>
#app {
  position: relative;
  min-width: 1200px;
  min-height: 500px;
}
.el-menu-item,
.el-submenu .el-submenu__title{
  padding-left: 14px !important;
}
body .StatisticsPerformanceDetailTable.el-table th > .cell{
  white-space: inherit;
  text-overflow: inherit;
  line-height: 18px;
}
</style>
