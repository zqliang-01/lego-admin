<template>
  <div id="app">
    <router-view class="router-view" />
  </div>
</template>

<script setup>
import cache from '@/utils/cache'
import { nextTick, onMounted } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()

// 监听文档可见性变化
const addDocumentVisibilityChange = () => {
  // 网页当前状态判断
  let state, visibilityChange
  if (typeof document.hidden !== 'undefined') {
    visibilityChange = 'visibilitychange'
    state = 'visibilityState'
  } else if (typeof document.mozHidden !== 'undefined') {
    visibilityChange = 'mozvisibilitychange'
    state = 'mozVisibilityState'
  } else if (typeof document.msHidden !== 'undefined') {
    visibilityChange = 'msvisibilitychange'
    state = 'msVisibilityState'
  } else if (typeof document.webkitHidden !== 'undefined') {
    visibilityChange = 'webkitvisibilitychange'
    state = 'webkitVisibilityState'
  }

  // 添加监听器，在title里显示状态变化
  document.addEventListener(visibilityChange, () => {
    if (document[state] === 'visible') {
      if (cache.updateAxiosHeaders() && route.name === 'login') {
        window.location.reload()
      }
    }
  }, false)
}

// 设置最小高度
const setMinHeight = async() => {
  await nextTick()
  const dpr = window.devicePixelRatio || 1
  const clientWidth = document.body.clientWidth
  const dom = document.getElementById('app')
  if (dpr !== 1 && clientWidth > 1600) {
    dom.style.minHeight = '800px'
  } else if (dpr === 1 && clientWidth > 1600) {
    dom.style.minWidth = '1650px'
  } else {
    dom.style.minHeight = '605px'
  }
}

// 生命周期钩子
onMounted(() => {
  addDocumentVisibilityChange()
  setMinHeight()
})
</script>

<style>
#app {
  position: relative;
  min-width: 1200px;
  min-height: 500px;
}

/* Element Plus 样式更新 */
.el-menu-item,
.el-sub-menu .el-sub-menu__title {
  padding-left: 14px !important;
}

/* 统计表格样式 */
body .StatisticsPerformanceDetailTable.el-table th > .cell {
  white-space: inherit;
  text-overflow: inherit;
  line-height: 18px;
}
</style>
