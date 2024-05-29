import 'babel-polyfill'
import Vue from 'vue'
import 'normalize.css/normalize.css'
import App from './App.vue'
import store from './store'
import router from './router'

import '@/permission'
import 'element-ui/lib/theme-chalk/index.css'
import '@/styles/index.scss'
import 'vue2-animate/dist/vue2-animate.min.css'
import '@/components/bpmn/theme/index.scss'

import config from '@/config'
window.SystemConfig = config
Vue.prototype.SystemConfig = config

import cache from '@/utils/cache'
cache.loadingCache()

// 加载基础组件
import ElementUI from 'element-ui'
Vue.use(ElementUI)
import VueBus from 'vue-bus'
Vue.use(VueBus)
import FileUpload from '@/components/FileUpload/index.js'
Vue.use(FileUpload)
import PreviewFile from '@/components/PreviewFile/main.js'
Vue.use(PreviewFile)
// 限制数据数值
import inputLimit from './directives/inputLimit'
Vue.use(inputLimit)
import vueNumeralFilterInstaller from './filters/vueNumeralFilter'
Vue.use(vueNumeralFilterInstaller, { locale: 'chs' })
// 处理时间的过滤器
Vue.use(require('vue-moment'))
import empty from './directives/empty'
Vue.use(empty)
import debounce from './directives/clickDebounce'
Vue.use(debounce)

import { Flexbox, FlexboxItem } from '@/components/Flexbox'
Vue.component('flexbox', Flexbox)
Vue.component('flexbox-item', FlexboxItem)
import Avatar from '@/components/Avatar'
Vue.component('xr-avatar', Avatar)
import LegoAllDetail from '@/components/lego/LegoAllDetail'
Vue.component('LegoAllDetail', LegoAllDetail)
import LegoAllCreate from '@/components/lego/LegoAllCreate'
Vue.component('LegoAllCreate', LegoAllCreate)

/** 懒加载图片 */
import VueSrc from './directives/src'
Vue.directive('src', VueSrc)
// 自定义全局点击空白处组件
import clickoutside from './directives/clickoutside'
Vue.directive('clickoutside', clickoutside)
import elClickoutside from './directives/elClickoutside'
Vue.directive('elclickoutside', elClickoutside)
import clipboard from './directives/clipboard'
Vue.directive('clipboard', clipboard)

// 注册全局过滤器
import * as filters from './filters'
Object.keys(filters).forEach(key => {
  Vue.filter(key, filters[key])
})
import moment from 'moment'
moment.locale('zh_cn')

import { Message } from 'element-ui'
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

new Vue({
  router,
  store,
  render: (h) => h(App)
}).$mount('#app')
