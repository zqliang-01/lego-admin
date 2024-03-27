/** 系统管理路由 */
import Layout from '@/views/layout/OaLayout'

const layout = function(menu, requiresAuth = true) {
  if (menu.meta) {
    menu.meta.requiresAuth = requiresAuth
  }
  return {
    code: 'oa',
    path: '/oa',
    component: Layout,
    meta: {
      requiresAuth: true
    },
    children: [
      menu
    ]
  }
}

export default [
  {
    ...layout({
      code: 'oa_start',
      name: 'start',
      path: 'start',
      component: () => import('@/views/oa/start'),
      meta: {
        title: '发起审批',
        icon: 'top'
      }
    })
  },
  {
    ...layout({
      code: 'oa_owner',
      name: 'owner',
      path: 'owner',
      component: () => import('@/views/oa/owner'),
      meta: {
        title: '我的流程',
        icon: 'my-task'
      }
    })
  },
  {
    ...layout({
      code: 'oa_undo',
      name: 'undo',
      path: 'undo',
      component: () => import('@/views/oa/undo'),
      meta: {
        title: '待办任务',
        icon: 'contract'
      }
    })
  },
  {
    ...layout({
      code: 'oa_unclaimed',
      name: 'claim',
      path: 'claim',
      component: () => import('@/views/oa/claim'),
      meta: {
        title: '待签任务',
        icon: 'icon-related-tasks'
      }
    })
  },
  {
    ...layout({
      code: 'oa_finished',
      name: 'finished',
      path: 'finished',
      component: () => import('@/views/oa/finished'),
      meta: {
        title: '已办任务',
        icon: 'icon-task-state'
      }
    })
  }
]
