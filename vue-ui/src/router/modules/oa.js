/** 系统管理路由 */
import Layout from '@/views/layout/OaLayout'

const layout = function(menu, requiresAuth = true) {
  if (menu.meta) {
    menu.meta.requiresAuth = requiresAuth
  }
  return {
    path: '/oa',
    component: Layout,
    meta: {
      requiresAuth: true,
      permissions: ['oa']
    },
    children: [
      menu
    ]
  }
}

export default [
  {
    ...layout({
      name: 'start',
      path: 'start',
      component: () => import('@/views/oa/start'),
      meta: {
        title: '发起审批',
        icon: 'top',
        permissions: ['oa_start']
      }
    })
  },
  {
    ...layout({
      name: 'owner',
      path: 'owner',
      component: () => import('@/views/oa/owner'),
      meta: {
        title: '我的流程',
        icon: 'my-task',
        permissions: ['oa_owner']
      }
    })
  },
  {
    ...layout({
      name: 'undo',
      path: 'undo',
      component: () => import('@/views/oa/undo'),
      meta: {
        title: '待办任务',
        icon: 'contract',
        permissions: ['oa_undo']
      }
    })
  },
  {
    ...layout({
      name: 'claim',
      path: 'claim',
      component: () => import('@/views/oa/claim'),
      meta: {
        title: '待签任务',
        icon: 'icon-related-tasks',
        permissions: ['oa_unclaimed']
      }
    })
  },
  {
    ...layout({
      name: 'finished',
      path: 'finished',
      component: () => import('@/views/oa/finished'),
      meta: {
        title: '已办任务',
        icon: 'icon-task-state',
        permissions: ['oa_finished']
      }
    })
  }
]
