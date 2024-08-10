import {
  rebuildChildren,
  mergeMenuRouter,
  filterAsyncRouterMap } from './AsyncRouter'
import { mergeDynamicRouter, addDynamicRouter } from './DynamicRouter'
import { permissionCurrentListAPI } from '@/api/admin/permission'

/**
 * 忽略不注册的应用路由
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
 * 获取首个显示的菜单URL用于模块重定向地址
 */
const getRedirectPath = function(routers, basePath = '') {
  for (let i = 0; i < routers.length; i++) {
    const element = routers[i]
    if (element.hidden) {
      continue
    }
    let tmpBasePath = element.path
    if (basePath !== '') {
      tmpBasePath = basePath + '/' + element.path
    }
    if (element.children && element.children.length > 0) {
      const redirectPath = getRedirectPath(element.children, tmpBasePath)
      if (redirectPath) {
        return redirectPath
      }
    }
    return tmpBasePath
  }
  return ''
}

/**
 * 取第一个可视路由path作为模块首页重定向路由
 */
const initRouter = function(asyncRouterMap) {
  const menuRouters = {}
  let allRouters = []
  asyncRouterMap.forEach(mainRouter => {
    const accessedRouters = mainRouter.router
    const menuRouter = mergeMenuRouter(accessedRouters)
    const redirectPath = getRedirectPath(menuRouter)
    const filterRouters = filterIgnoreRouter(accessedRouters)
    filterRouters.forEach(router => {
      let childrens = []
      if (router.children) {
        router.children.forEach(children => {
          childrens = childrens.concat(rebuildChildren(children, undefined))
        })
        router.children = childrens
      }
    })

    // 添加模块首页路由以及重定向地址
    if (redirectPath) {
      const router = filterRouters.find(router => !router.ignore)
      if (router) {
        router.redirect = redirectPath
      }
      filterRouters.push({
        name: mainRouter.type,
        path: `/${mainRouter.type}`,
        redirect: redirectPath,
        hidden: true
      })
    }

    menuRouters[mainRouter.type] = menuRouter
    allRouters = allRouters.concat(filterRouters)
  })

  // 添加动态模板路由（由于动态路由会过滤注册，需添加模板路由用于跳转）
  addDynamicRouter(allRouters)

  // 设置首页重定向地址，暂时写死
  allRouters.push({
    path: '/',
    redirect: '/home/index',
    hidden: true
  })
  return { menuRouters, allRouters }
}

const permission = {
  state: {
    allRouters: [],
    menuRouters: [],
    manageRouters: []
  },
  mutations: {
    SET_ROUTERS: (state, data) => {
      state.allRouters = data.allRouters
      state.menuRouters = data.menuRouters || []
      state.manageRouters = data.menuRouters.manage || []
    }
  },
  actions: {
    GenerateRoutes({ commit, state }, data) {
      return new Promise(resolve => {
        // 路由完善 hidden：注册但不显示，ignore：不注册但显示
        permissionCurrentListAPI().then(res => {
          const asyncRouterMap = filterAsyncRouterMap(res.data)
          mergeDynamicRouter(res.data, asyncRouterMap)
          const routers = initRouter(asyncRouterMap)
          commit('SET_ROUTERS', routers)
          resolve(routers)
        })
      })
    }
  }
}

export default permission
