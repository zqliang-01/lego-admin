import { createRouter, createWebHashHistory } from 'vue-router'
import syncRouter from './modules/index'

/**
* hidden: true                   if `hidden:true` will not show in the sidebar(default is false)
* alwaysShow: true               if set true, will always show the root menu, whatever its child routes length
*                                if not set alwaysShow, only more than one route under the children
*                                it will becomes nested mode, otherwise not show the root menu
* redirect: noredirect           if `redirect:noredirect` will no redirect in the breadcrumb
* name:'router-name'             the name is used by <keep-alive> (must set!!!)
* meta : {
    title: 'title'               the name show in submenu and breadcrumb (recommend set)
    icon: 'svg-name'             the icon show in the sidebar,
  }
**/
export const constantRouterMap = [
  {
    path: '/login',
    name: 'login',
    component: () => import('@/views/login/index'),
    hidden: true
  },
  {
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  },
  {
    path: '/person',
    component: () => import('@/views/layout/UserLayout'),
    redirect: '/person/index',
    name: 'person',
    hidden: true,
    meta: {
      title: '个人中心'
    },
    children: [{
      path: 'index',
      component: () => import('@/views/user/index')
    }]
  }
]

const router = createRouter({
  history: createWebHashHistory(), // 使用 hash 模式，如果需要 history 模式，替换为 createWebHistory()
  scrollBehavior: () => ({ top: 0 }), // y 改为 top
  routes: constantRouterMap
})

// 重置路由方法
export function resetRouter() {
  // 在 Vue Router 4 中，我们需要先删除所有路由
  router.getRoutes().forEach(route => {
    const { name } = route
    if (name) {
      router.hasRoute(name) && router.removeRoute(name)
    }
  })
  // 然后添加基础路由
  constantRouterMap.forEach(route => {
    router.addRoute(route)
  })
}

export default router
export const asyncRouterMap = syncRouter