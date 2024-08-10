import Layout from '@/views/layout/CommonLayout'

const layout = function(menu, requiresAuth = true) {
  if (menu.meta) {
    menu.meta.requiresAuth = requiresAuth
  }
  return {
    code: 'crm',
    path: '/crm',
    component: Layout,
    meta: {
      requiresAuth: true
    },
    children: [
      menu
    ]
  }
}

export default { type: 'crm', router: [
  {
    ...layout({
      code: 'crm_lead',
      path: 'lead',
      component: () => import('@/views/crm/lead'),
      meta: {
        title: '线索',
        icon: 'icon-title'
      }
    })
  }]
}
