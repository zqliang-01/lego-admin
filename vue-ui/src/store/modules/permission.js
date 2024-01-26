import { asyncRouterMap } from '@/router'
import { permissionListAPI } from '@/api/admin/permission'
import Layout from '@/views/layout/TableFormLayout'
import LegoIndex from '@/components/LegoIndex'

function checkAuth(router, authInfo) {
  // 判断当前路由在权限数组中是否存在
  if (router.meta) {
    const metaInfo = router.meta
    if (!metaInfo.requiresAuth) {
      return true
    } else {
      if (metaInfo.permissions) {
        authInfo = { ...authInfo }
        return forCheckPermission(metaInfo.permissions, authInfo)
      } else if (metaInfo.permissionList) { // 一个路由受多个权限判断
        for (let index = 0; index < metaInfo.permissionList.length; index++) {
          authInfo = { ...authInfo }
          const permission = forCheckPermission(metaInfo.permissionList[index], authInfo)
          if (permission) {
            return true
          }
        }
        return false
      }
    }
  }
  return true
}

/**
 * 循环关键字检查权限
 */
const forCheckPermission = function(permissions, authInfo) {
  for (let index = 0; index < permissions.length; index++) {
    const key = permissions[index]
    authInfo = authInfo[key]
    if (!authInfo) {
      return false
    } else if (permissions.length - 1 === index) {
      return true
    }
  }
}

const filterAsyncRouter = function(routers, authInfo) {
  const res = []
  routers.forEach(router => {
    const tmp = {
      ...router
    }
    if (checkAuth(tmp, authInfo)) {
      if (tmp.children) {
        tmp.children = filterAsyncRouter(tmp.children, authInfo)
      }
      res.push(tmp)
    }
  })
  return res
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
 * 合并静态路由 + 动态路由，取第一个可视路由作为模块首页跳转
 */
const initRouter = function(authInfo, dynamicRouters) {
  const routerObj = {}
  let addRouter = []
  let redirect = ''
  let topRedirect = '' // 置顶的第一个路由
  const allRouters = asyncRouterMap.concat(dynamicRouters)
  for (let index = 0; index < allRouters.length; index++) {
    const mainRouter = allRouters[index]
    const accessedRouters = filterAsyncRouter(mainRouter.router, authInfo)
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

        if (authInfo.firstModel && !topRedirect) {
          if (authInfo.firstModel == mainRouter.type) {
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
 * 路由重定向和角色路由完善
 */
const perfectRouter = function(authInfo, result) {
  const dynamicRouters = []
  permissionListAPI({ routeType: 'Dynamic' }).then(res => {
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
                icon: item.icon
              }
            }]
          })
        })
        dynamicRouters.push({ type: router.code, router: modelRouters })
      }
    })
    if (result) {
      result(initRouter(authInfo, dynamicRouters))
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
    }, data) {
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
