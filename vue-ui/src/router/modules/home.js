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

export default { type: 'home', router: [
  {
    ...layout(),
    children: [{
      path: 'index',
      component: () => import('@/views/home'),
      meta: {
        requiresAuth: false
      }
    }]
  },
  {
    name: 'print',
    path: '/print/:formCode/:detailCode',
    hidden: true,
    component: () => import('@/components/Print'),
    meta: {
      title: '打印',
      icon: 'print',
      requiresAuth: false
    }
  },
  {
    name: 'reprint',
    path: '/reprint/:code',
    hidden: true,
    component: () => import('@/components/Print/reprint'),
    meta: {
      title: '重打印',
      icon: 'print',
      requiresAuth: false
    }
  }]
}
