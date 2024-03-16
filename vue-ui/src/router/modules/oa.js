/** 系统管理路由 */
import Layout from '@/views/layout/OaLayout'

const layout = function(meta = {}, path = '/oa', requiresAuth = true) {
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
      permissions: ['oa', 'start']
    }),
    children: [{
      name: 'start',
      path: 'start',
      component: () => import('@/views/oa/start'),
      meta: {
        title: '发起审批',
        icon: 'top'
      }
    }]
  },
  {
    ...layout({
      permissions: ['oa', 'owner']
    }),
    children: [{
      name: 'owner',
      path: 'owner',
      component: () => import('@/views/oa/owner'),
      meta: {
        title: '我的流程',
        icon: 'my-task'
      }
    }]
  },
  {
    ...layout({
      permissions: ['oa', 'undo']
    }),
    children: [{
      name: 'undo',
      path: 'undo',
      component: () => import('@/views/oa/undo'),
      meta: {
        title: '待办任务',
        icon: 'contract'
      }
    }]
  },
  {
    ...layout({
      permissions: ['oa', 'unclaimed']
    }),
    children: [{
      name: 'claim',
      path: 'claim',
      component: () => import('@/views/oa/claim'),
      meta: {
        title: '待签任务',
        icon: 'icon-related-tasks'
      }
    }]
  },
  {
    ...layout({
      permissions: ['oa', 'finished']
    }),
    children: [{
      name: 'finished',
      path: 'finished',
      component: () => import('@/views/oa/finished'),
      meta: {
        title: '已办任务',
        icon: 'icon-task-state'
      }
    }]
  }
]
