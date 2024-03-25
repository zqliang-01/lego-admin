import { asyncRouterMap } from '@/router'
import { permissionCurrentListAPI } from '@/api/admin/permission'
import Layout from '@/views/layout/TableFormLayout'
import LegoIndex from '@/components/LegoIndex'

function checkAuth(router, authList) {
  // 判断当前路由在权限数组中是否存在
  if (router.meta) {
    const metaInfo = router.meta
    if (!metaInfo.requiresAuth) {
      return true
    }
    if (metaInfo.permissions) {
      return authList.some(auth => forCheckPermission(router, metaInfo.permissions, auth))
    }
  }
  return true
}

/**
 * 循环关键字检查权限
 */
const forCheckPermission = function(router, permissions, auth) {
  if (permissions.some(permission => permission === auth.code)) {
    router.meta.isMenu = auth.menu
    router.meta.sn = auth.sn
    router.meta.title = auth.name
    if (auth.icon) {
      router.meta.icon = auth.icon
    }
    return true
  }
  if (auth.childrens.some(children => forCheckPermission(router, permissions, children))) {
    return true
  }
  return false
}

const filterAsyncRouter = function(routers, authList) {
  const res = []
  routers.forEach(router => {
    const tmp = {
      ...router
    }
    if (checkAuth(tmp, authList)) {
      if (tmp.children) {
        tmp.children = filterAsyncRouter(tmp.children, authList)
      }
      res.push(tmp)
    }
  })
  return res.sort((a, b) => {
    return a.meta.sn - b.meta.sn
  })
}


/**
 * 忽略用于菜单展示的传参路由
 */
const filterIgnoreRouter = function(routers) {
  const res = []
  routers.forEach(router => {
    const tmp = {
      ...router
    }
    if (!tmp.ignore) {
      if (tmp.children) {
        tmp.children = filterIgnoreRouter(tmp.children)
      }
      res.push(tmp)
    }
  })
  return res
}

/**
 * 当模块第一个路由为动态路由时，会配置为hidden并跳过作为首页重定向，此方法获取回动态路由作为首页重定向目标
 */
const setRouterRedirect = function(redirect, accessedRouters) {
  for (let index = 0; index < accessedRouters.length; index++) {
    const element = accessedRouters[index]
    if (element.children && element.children.length > 0) {
      const firstChild = element.children[0]
      if (firstChild.ignore) {
        continue
      }
      element.redirect = redirect
      break
    }
  }
}

/**
 * 合并静态路由 + 动态路由，取第一个可视路由作为模块首页重定向路由，设置默认打开应用重定向路由
 */
const initRouter = function(authList, newAsyncRouterMap) {
  const routerObj = {}
  let addRouter = []
  let redirect = ''
  let topRedirect = '' // 置顶的第一个路由
  for (let index = 0; index < newAsyncRouterMap.length; index++) {
    const mainRouter = newAsyncRouterMap[index]
    const accessedRouters = mainRouter.router
    for (let childIndex = 0; childIndex < accessedRouters.length; childIndex++) {
      const element = accessedRouters[childIndex]
      if (element.children && element.children.length > 0) {
        const firstChild = element.children[0]
        if (firstChild.hidden) {
          continue
        }
        const childPath = firstChild.meta ? firstChild.meta.redirect || firstChild.path : firstChild.path
        element.redirect = element.path + '/' + childPath
        if (element.ignore) {
          setRouterRedirect(element.path + '/' + childPath, accessedRouters)
        }
      }

      // 获取跳转
      if (element.redirect) {
        if (!redirect) {
          redirect = element.redirect
        }

        if (authList.firstModel && !topRedirect) {
          if (authList.firstModel == mainRouter.type) {
            topRedirect = element.redirect
          }
        }

        // 为导航头 获取每个模块的 重定向 url
        accessedRouters.push({
          path: `/${mainRouter.type}`,
          name: mainRouter.type,
          redirect: element.redirect,
          hidden: true
        })

        break
      }
    }
    routerObj[mainRouter.type] = accessedRouters
    addRouter = addRouter.concat(filterIgnoreRouter(accessedRouters))
  }

  addRouter.forEach(router => {
    let childrens = []
    if (router.children && router.children.length > 0) {
      router.children.forEach(children => {
        childrens = childrens.concat(rebuildChildren(children, undefined))
      })
      router.children = childrens
    }
  })

  if (redirect || topRedirect) {
    addRouter.push({
      path: '/',
      redirect: topRedirect || redirect,
      hidden: true
    })
  }
  return { router: routerObj, addRouter }
}

/**
 * 重建子节点，所有多级路由合并为二级路由
 */
const rebuildChildren = function(router, basePath = '') {
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

const filterAsyncRouterMap = function(authList) {
  const newAsyncRouterMap = []
  asyncRouterMap.forEach(asyncRouter => {
    newAsyncRouterMap.push({
      type: asyncRouter.type,
      router: filterAsyncRouter(asyncRouter.router, authList)
    })
  })
  return newAsyncRouterMap
}

/**
 * 路由重定向和角色路由完善
 */
const perfectRouter = function(authInfo, result) {
  const dynamicRouters = []
  permissionCurrentListAPI({ routeType: 'Dynamic' }).then(res => {
    res.data.forEach(router => {
      if (router.childrens.length > 0) {
        const modelRouters = [{
          path: '/:model',
          component: Layout,
          meta: {},
          children: [{
            path: ':menuCode',
            component: LegoIndex,
            hidden: true,
            meta: {}
          }]
        }]
        router.childrens.forEach(item => {
          modelRouters.push({
            path: '/' + router.code,
            component: Layout,
            ignore: true,
            meta: {},
            children: [{
              path: `${encodeURI(item.code)}`,
              meta: {
                title: item.name,
                icon: item.icon,
                isMenu: item.menu
              }
            }]
          })
        })
        dynamicRouters.push({ type: router.code, router: modelRouters })
      }
    })
    if (result) {
      permissionCurrentListAPI().then(res => {
        const newAsyncRouterMap = filterAsyncRouterMap(res.data)
        result(initRouter(res.data, newAsyncRouterMap.concat(dynamicRouters)))
      })
    }
  })
}

const permission = {
  state: {
    allRouters: [],
    menuRouters: [],
    manageRouters: []
  },
  mutations: {
    SET_ROUTERS: (state, data) => {
      state.allRouters = data.addRouter
      state.menuRouters = data.router || []
      state.manageRouters = data.router.manage || []
    }
  },
  actions: {
    GenerateRoutes({
      commit,
      state
    }, data, authList) {
      return new Promise(resolve => {
        // 路由完善
        perfectRouter(data, (routers) => {
          commit('SET_ROUTERS', routers)
          resolve()
        })
      })
    }
  }
}

export default permission
