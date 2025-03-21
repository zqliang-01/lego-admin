import Vue from 'vue'
import App from './App'
import store from './store'
import config from '@/config'
import uView from '@/uni_modules/uview-ui'
import bootstrap from './core/bootstrap'
import {
  getPlatform,
  navTo,
  redTo,
  showToast,
  showSuccess,
  showError,
  getShareUrlParams
} from './utils/app'
import './core/ican-H5Api'
import comDirectives from 'directives/index'

Vue.config.productionTip = false

App.mpType = 'app'


// 载入uView库
Vue.use(uView)
Vue.use(comDirectives, app)

import LegoIcon from '@/components/lego/icon'
import LegoSearch from '@/components/lego/search'
import NavBar from '@/components/navBar'
import Empty from '@/components/empty'
Vue.component('LegoIcon', LegoIcon)
Vue.component('LegoSearch', LegoSearch)
Vue.component('NavBar', NavBar)
Vue.component('Empty', Empty)

// 挂载全局函数
Vue.prototype.$toast = showToast
Vue.prototype.$success = showSuccess
Vue.prototype.$error = showError
Vue.prototype.$navTo = navTo
Vue.prototype.$redTo = redTo
Vue.prototype.$getShareUrlParams = getShareUrlParams
Vue.prototype.$tokenName = config.tokenName
Vue.prototype.$platform = getPlatform()

// 实例化应用
const app = new Vue({
  ...App,
  store,
  created: bootstrap
})
app.$mount()
