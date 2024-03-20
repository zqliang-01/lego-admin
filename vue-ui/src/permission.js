import router from './router'
import store from './store'
import NProgress from 'nprogress' // Progress 进度条
import 'nprogress/nprogress.css' // Progress 进度条样式
import { Message } from 'element-ui'
import { checkAuth } from '@/utils/auth' // 验权

let loadAsyncRouter = false
const whiteList = ['/login', '/welcome'] // 不重定向白名单
router.beforeEach((to, from, next) => {
  if (to.path) {
    if (window._hmt) {
      window._hmt.push(['_trackPageview', '/#' + to.fullPath])
    }
  }
  if (to.meta.disabled) {
    next(false)
    return
  }
  NProgress.start()
  /** 请求头包含授权信息 并且 页面必须授权 直接进入 */
  if (checkAuth()) {
    if (whiteList.includes(to.path)) {
      next({
        path: '/'
      })
      NProgress.done()
      return
    }
    if (!loadAsyncRouter) { // 判断当前用户是否获取权限
      loadAsyncRouter = true
      if (store.getters.allAuth) {
        store.dispatch('GenerateRoutes', store.getters.allAuth).then(() => { // 根据auths权限生成可访问的路由表
          for (const r of store.getters.allRouters) { // 动态添加可访问路由表
            router.addRoute(r)
          }
          if (to.path === '/404') {
            next({
              path: to.redirectedFrom || '/',
              replace: true
            })
          } else {
            next({
              ...to,
              replace: true
            })
          }
        })
        return
      }
      store.dispatch('getAuth').then(auths => { // 拉取user_info
        store.dispatch('GenerateRoutes', auths).then(() => { // 根据auths权限生成可访问的路由表
          for (const r of store.getters.allRouters) { // 动态添加可访问路由表
            router.addRoute(r)
          }
          if (to.path === '/404') {
            next({
              path: to.redirectedFrom || '/',
              replace: true
            })
          } else {
            next({
              ...to,
              replace: true
            })
          }
        })
      }).catch((err) => {
        loadAsyncRouter = false
        store.dispatch('LogOut').then(() => {
          Message.error(err.msg || '获取用户信息失败')
          next({
            path: '/'
          })
        })
      })
      return
    }
    next()
    return
  }
  if (whiteList.indexOf(to.path) !== -1) {
    next()
    return
  }
  next(`/login`) // 否则全部重定向到登录页
  NProgress.done()
})

router.afterEach(() => {
  NProgress.done() // 结束Progress
})

router.onError((error) => {
  const pattern = /Loading chunk (\d)+ failed/g
  const isChunkLoadFailed = error.message.match(pattern)
  const targetPath = router.history.pending.fullPath
  if (isChunkLoadFailed) {
    router.replace(targetPath)
  }
})
