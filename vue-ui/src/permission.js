import { checkAuth } from '@/utils/auth' // 验权
import { ElMessage } from 'element-plus'
import NProgress from 'nprogress' // Progress 进度条
import 'nprogress/nprogress.css' // Progress 进度条样式
import router from './router'
import store from './store'

const whiteList = ['/login', '/welcome'] // 不重定向白名单

router.beforeEach(async(to, from, next) => {
  // 百度统计
  if (to.path && window._hmt) {
    window._hmt.push(['_trackPageview', '/#' + to.fullPath])
  }

  // 检查路由是否禁用
  if (to.meta.disabled) {
    next(false)
    return
  }

  NProgress.start()

  // 检查认证状态
  if (!checkAuth()) {
    if (whiteList.indexOf(to.path) !== -1) {
      next()
    } else {
      next(`/login?redirect=${to.path}`)
      NProgress.done()
    }
    return
  }

  // 如果已认证且要访问登录页，重定向到首页
  if (to.path === '/login') {
    next({ path: '/' })
    return
  }

  // 检查是否已加载路由
  if (store.getters.allRouters && store.getters.allRouters.length > 0) {
    next()
    return
  }

  // 获取权限并生成路由
  try {
    const auths = await store.dispatch('getAuth')
    const { allRouters } = await store.dispatch('GenerateRoutes', auths)
    await authNext(to, next, allRouters)
  } catch (error) {
    await store.dispatch('LogOut')
    ElMessage.error(error?.message || '获取用户信息失败')
    next(`/login?redirect=${to.path}`)
    NProgress.done()
  }
})

// 处理动态路由添加
const authNext = async(to, next, allRouters) => {
  // 清除所有现有路由
  router.getRoutes().forEach(route => {
    if (route.name) {
      router.hasRoute(route.name) && router.removeRoute(route.name)
    }
  })

  // 添加新的动态路由
  for (const route of allRouters) {
    router.addRoute(route)
  }

  // 处理重定向
  if (to.path === '/404') {
    next({ path: to.redirectedFrom || '/', replace: true })
  } else {
    // 确保添加完路由后再跳转
    next({ ...to, replace: true })
  }
}

// 路由跳转完成
router.afterEach(() => {
  NProgress.done()
})

// 路由错误处理
router.onError((error) => {
  const pattern = /Loading chunk (\d)+ failed/g
  const isChunkLoadFailed = error.message.match(pattern)
  if (isChunkLoadFailed && router.currentRoute.value) {
    const targetPath = router.currentRoute.value.fullPath
    router.replace(targetPath)
  }
})
