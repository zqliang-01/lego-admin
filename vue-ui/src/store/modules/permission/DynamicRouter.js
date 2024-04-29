import Layout from '@/views/layout/TableFormLayout'
import LegoIndex from '@/components/lego/LegoAllIndex'

// 添加需要注册的动态路由（后期按模板适配）
export function addDynamicRouter(routers) {
  routers.push({
    path: '/:model',
    component: Layout,
    hidden: true,
    meta: {
      requiresAuth: true
    },
    children: [{
      path: ':menuCode',
      component: LegoIndex,
      hidden: true,
      meta: {}
    },
    {
      path: ':param1/:menuCode',
      component: LegoIndex,
      hidden: true,
      meta: {}
    },
    {
      path: ':param1/:param2/:menuCode',
      component: LegoIndex,
      hidden: true,
      meta: {}
    }]
  })
}

// 给菜单路由增加动态路由
const addDynamicMenu = function(auth, routers) {
  let router = routers.find(router => router.code === auth.code)
  if (!router) {
    router = {
      code: auth.code,
      ignore: true,
      children: []
    }
    routers.push(router)
  }
  router.path = auth.menu ? auth.code : '/' + auth.code
  router.meta = {
    sn: auth.sn,
    title: auth.name,
    icon: auth.icon,
    isMenu: auth.menu
  }
  auth.childrens.forEach(children => {
    if (children.menu && children.dynamicRoute) {
      addDynamicMenu(children, router.children)
      router.children = router.children.sort((a, b) => a.meta.sn - b.meta.sn)
    }
  })
}

// 合并动态路由进总路由
export function mergeDynamicRouter(authList, allRouters) {
  authList.forEach(auth => {
    if (auth.dynamicRoute) {
      let asyncRouter = allRouters.find(asyncRouter => asyncRouter.type === auth.code)
      if (!asyncRouter) {
        asyncRouter = { type: auth.code, router: [] }
        allRouters.push(asyncRouter)
      }
      addDynamicMenu(auth, asyncRouter.router)
    }
  })
  return allRouters
}
