/** 客户管理路由 */
import Layout from '@/views/layout/HomeLayout'

const layout = function(meta = {}, path = '/home', requiresAuth = false) {
  return {
    path: path,
    component: Layout,
    meta: {
      requiresAuth: requiresAuth,
      ...meta
    }
  }
}

const homeRouter = [
  {
    ...layout(),
    children: [{
      path: 'index',
      component: () => import('@/views/home')
    }]
  }
]

export default homeRouter
