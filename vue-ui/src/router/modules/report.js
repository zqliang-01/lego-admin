import Layout from '@/views/layout/ReportLayout'

const layout = function(menu, requiresAuth = true) {
  if (menu.meta) {
    menu.meta.requiresAuth = requiresAuth
  }
  return {
    code: 'report',
    path: '/report',
    component: Layout,
    meta: {
      requiresAuth: true
    },
    children: [
      menu
    ]
  }
}

const reportRouter = [
  {
    ...layout({
      code: 'report_definition',
      path: 'definition',
      component: () => import('@/views/report/definition'),
      meta: {
        title: '报表管理',
        icon: 'icon-title'
      }
    })
  }
]

export default reportRouter
