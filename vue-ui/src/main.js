import 'babel-polyfill'
import Vue from 'vue'

import 'normalize.css/normalize.css' // A modern alternative to CSS resets

// 配置信息
import config from '@/config'
window.SystemConfig = config
Vue.prototype.SystemConfig = config

import ElementUI from 'element-ui'
Vue.use(ElementUI)
import {
  Message
} from 'element-ui'
Vue.prototype.$message.success = function(msg) {
  return Message({
    message: msg,
    duration: 1500,
    type: 'success'
  })
}
Vue.prototype.$message.error = function(msg) {
  return Message({
    message: msg,
    duration: 1500,
    type: 'error'
  })
}

import 'element-ui/lib/theme-chalk/index.css'
import 'el-bigdata-table'

import '@/styles/index.scss' // global css

import App from './App'
import router from './router'
import store from './store'

import cache from '@/utils/cache'
cache.loadingCache()

import '@/permission' // permission control
import 'vue2-animate/dist/vue2-animate.min.css'

/** 事件传递 */
import VueBus from 'vue-bus'
Vue.use(VueBus)

/** 常用flex组件 */
import {
  Flexbox,
  FlexboxItem
} from '@/components/Flexbox'
Vue.component('flexbox', Flexbox)
Vue.component('flexbox-item', FlexboxItem)
import XrAvatar from '@/components/XrAvatar'
Vue.component('xr-avatar', XrAvatar)

import FileUpload from '@/components/FileUpload/index.js'
Vue.use(FileUpload)
import FileSelect from '@/components/NewCom/File/Select/main.js'
Vue.use(FileSelect)
import PreviewFile from '@/components/PreviewFile/main.js'
Vue.use(PreviewFile)

/** 懒加载图片 */
import VueSrc from './directives/src'
Vue.directive('src', VueSrc)

import * as filters from './filters' // global filters
// 注册全局过滤器
Object.keys(filters).forEach(key => {
  Vue.filter(key, filters[key])
})
import vueNumeralFilterInstaller from './filters/vueNumeralFilter'
Vue.use(vueNumeralFilterInstaller, { locale: 'chs' })

// 处理时间的过滤器
Vue.use(require('vue-moment'))
import moment from 'moment'
moment.locale('zh_cn')

// 限制数据数值
import inputLimit from './directives/inputLimit'
Vue.use(inputLimit)
// 自定义全局点击空白处组件
import clickoutside from './directives/clickoutside'
Vue.directive('clickoutside', clickoutside)
import elClickoutside from './directives/elClickoutside'
Vue.directive('elclickoutside', elClickoutside)
import clipboard from './directives/clipboard'
Vue.directive('clipboard', clipboard)

import empty from './directives/empty'
Vue.use(empty)
import debounce from './directives/clickDebounce'
Vue.use(debounce)

// 表情
import {
  emoji
} from './utils/emoji'
Vue.prototype.emoji = emoji

Vue.config.productionTip = false
// 加入百度统计
router.beforeEach((to, from, next) => {
  if (to.path) {
    if (window._hmt) {
      window._hmt.push(['_trackPageview', '/#' + to.fullPath])
    }
  }
  next()
})

window.app = new Vue({
  el: '#app',
  router,
  store,
  data: {
    eventHub: new Vue()
  },
  render: h => h(App)
})
