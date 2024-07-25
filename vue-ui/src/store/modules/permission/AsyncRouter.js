import { asyncRouterMap } from '@/router'

/**
 * 循环关键字检查权限
 */
const forCheckPermission = function(router, auth) {
  if (router.code === auth.code) {
    router.meta.isMenu = auth.menu
    router.meta.sn = auth.sn
    router.meta.title = auth.name
    if (auth.icon) {
      router.meta.icon = auth.icon
    }
    return !auth.dynamicRoute || auth.childrens.length > 0
  }
  return auth.childrens.some(children => forCheckPermission(router, children))
}

const checkAuth = function(router, authList) {
  if (router.meta && !router.meta.requiresAuth) {
    return true
  }
  return authList.some(auth => forCheckPermission(router, auth))
}

const filterAsyncRouter = function(routers, authList) {
  const newRouters = []
  routers.forEach(router => {
    const newRouter = {
      ...router
    }
    if (checkAuth(newRouter, authList)) {
      if (router.children) {
        newRouter.children = filterAsyncRouter(router.children, authList)
      }
      newRouters.push(newRouter)
    }
  })
  return newRouters.sort((a, b) => a.meta.sn - b.meta.sn)
}

/**
 * 菜单路由合并为一个树（用于显示菜单）
 */
export function mergeMenuRouter(menuRouters) {
  const routers = []
  menuRouters.forEach(menuRouter => {
    const newRouter = { ...menuRouter }
    const router = routers.find(router => router.code === newRouter.code)
    if (router) {
      const childrens = [...newRouter.children || []]
      router.children = mergeMenuRouter(childrens.concat([...router.children || []]))
    } else if (!newRouter.hidden) {
      routers.push(newRouter)
    }
  })
  return routers.sort((a, b) => a.meta.sn - b.meta.sn)
}

/**
 * 重建子节点，所有多级路由合并为二级路由（用于注册）
 */
export function rebuildChildren(router, basePath = '') {
  let result = []
  if (router.children && router.children.length > 0) {
    router.children.forEach(children => {
      var tmpBasePath = router.path
      if (basePath) {
        tmpBasePath = basePath + '/' + router.path
      }
      result = result.concat(rebuildChildren(children, tmpBasePath))
    })
    return result
  }
  if (basePath) {
    router.path = basePath + '/' + router.path
  }
  result.push(router)
  return result
}

export function filterAsyncRouterMap(authList) {
  const newAsyncRouterMap = []
  asyncRouterMap.forEach(asyncRouter => {
    newAsyncRouterMap.push({
      type: asyncRouter.type,
      router: filterAsyncRouter(asyncRouter.router, authList)
    })
  })
  return newAsyncRouterMap
}
