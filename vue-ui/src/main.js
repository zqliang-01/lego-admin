import 'babel-polyfill'
import mitt from 'mitt'
import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'

import '@/components/Bpmn/theme/index.scss'
import '@/permission'
import '@/styles/index.scss'
import 'element-plus/dist/index.css'
import 'normalize.css/normalize.css'

import config from '@/config'
window.SystemConfig = config

import cache from '@/utils/cache'
cache.loadingCache()

// 加载 Element Plus
import ElementPlus from 'element-plus'
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'

// 导入组件
import Avatar from '@/components/Avatar'
import FileUpload from '@/components/FileUpload/index.js'
import { Flexbox, FlexboxItem } from '@/components/Layout/Flexbox'
import LegoAllCreate from '@/components/Lego/LegoAllCreate'
import LegoAllDetail from '@/components/Lego/LegoAllDetail'
import LegoIcon from '@/components/Lego/LegoIcon'
import PreviewFile from '@/components/PreviewFile/main.js'
import PreviewImage from '@/components/PreviewImage/main.js'

// 导入指令
import debounce from './directives/clickDebounce'
import clickoutside from './directives/clickoutside'
import clipboard from './directives/clipboard'
import elClickoutside from './directives/elClickoutside'
import empty from './directives/empty'
import inputLimit from './directives/inputLimit'
import VueSrc from './directives/src'

// 创建应用实例
const app = createApp(App)

// 配置 Element Plus
app.use(ElementPlus, {
  locale: zhCn
})

// 注册组件
app.component('flexbox', Flexbox)
app.component('flexbox-item', FlexboxItem)
app.component('xr-avatar', Avatar)
app.component('LegoAllDetail', LegoAllDetail)
app.component('LegoAllCreate', LegoAllCreate)
app.component('LegoIcon', LegoIcon)

// 注册指令
app.directive('src', VueSrc)
app.directive('clickoutside', clickoutside)
app.directive('elclickoutside', elClickoutside)
app.directive('clipboard', clipboard)
app.use(inputLimit)
app.use(empty)
app.use(debounce)

// 注册插件
app.use(FileUpload)
app.use(PreviewFile)
app.use(PreviewImage)

// 配置全局属性
app.config.globalProperties.SystemConfig = config
app.config.globalProperties.$filters = filters
app.config.globalProperties.$moment = moment

// 配置事件总线
const emitter = mitt()
app.config.globalProperties.$bus = emitter

// 配置全局消息提示
import { ElMessage } from 'element-plus'
app.config.globalProperties.$message = {
  success: (msg) => {
    ElMessage({
      message: msg,
      duration: 1500,
      type: 'success'
    })
  },
  error: (msg) => {
    ElMessage({
      message: msg,
      duration: 1500,
      type: 'error'
    })
  }
}

// 注册 store 和 router
app.use(store)
app.use(router)

// 挂载应用
app.mount('#app')
