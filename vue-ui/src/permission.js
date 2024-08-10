import router from './router'
import store from './store'
import NProgress from 'nprogress' // Progress 进度条
import 'nprogress/nprogress.css' // Progress 进度条样式
import { Message } from 'element-ui'
import { checkAuth } from '@/utils/auth' // 验权

const whiteList = ['/login', '/welcome'] // 不重定向白名单
router.beforeEach(async(to, from, next) => {
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
  if (!checkAuth()) {
    if (whiteList.indexOf(to.path) !== -1) {
      next()
      return
    }
    next(`/login?redirect=${to.path}`)
    NProgress.done()
    return
  }
  if (to.path === '/login') {
    next({ path: '/' })
    return
  }
  if (store.getters.allRouters && store.getters.allRouters.length > 0) {
    next()
    return
  }
  try {
    const auths = await store.dispatch('getAuth')
    const { allRouters } = await store.dispatch('GenerateRoutes', auths)
    authNext(to, next, allRouters)
  } catch (error) {
    await store.dispatch('LogOut')
    Message.error(error || '获取用户信息失败')
    next(`/login?redirect=${to.path}`)
    NProgress.done()
  }
})

const authNext = function(to, next, allRouters) {
  // 动态添加可访问路由表
  for (const r of allRouters) {
    router.addRoute(r)
  }
  if (to.path === '/404') {
    next({ path: to.redirectedFrom || '/', replace: true })
  } else {
    next({ ...to, replace: true })
  }
}

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
