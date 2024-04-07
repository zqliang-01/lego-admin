import Layout from '@/views/layout/TableFormLayout'

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

const crmRouter = [
  {
    ...layout({
      code: 'crm_lead',
      name: 'lead',
      path: 'lead',
      component: () => import('@/views/crm/lead'),
      meta: {
        title: '线索',
        icon: 'icon-title'
      }
    })
  }
]

export default crmRouter
