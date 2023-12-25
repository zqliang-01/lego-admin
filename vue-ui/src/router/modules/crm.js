/** 客户管理路由 */
import Layout from '@/views/layout/CrmLayout'

const layout = function(meta = {}, path = '/crm', requiresAuth = true) {
  return {
    path: path,
    component: Layout,
    meta: {
      requiresAuth: requiresAuth,
      ...meta
    }
  }
}

export default [
  {
    ...layout({
      permissions: ['crm', 'customer']
    }),
    children: [{
      path: 'customer',
      component: () => import('@/views/crm/customer'),
      meta: {
        title: '客户',
        icon: 's-seas'
      }
    }]
  },
  {
    ...layout({
      permissions: ['crm', 'lead']
    }),
    children: [{
      path: 'lead', // 线索列表
      component: () => import('@/views/crm/lead'),
      meta: {
        title: '线索',
        icon: 'leads'
      }
    }]
  },
  {
    ...layout({
      permissions: ['crm', 'contract']
    }),
    children: [{
      path: 'contract',
      component: () => import('@/views/crm/contract'),
      meta: {
        title: '合同',
        icon: 'icon-contract'
      }
    }]
  }
]
