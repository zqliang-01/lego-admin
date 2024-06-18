/** 系统管理路由 */
import Layout from '@/views/layout/JobLayout'

const layout = function(menu, requiresAuth = true) {
  if (menu.meta) {
    menu.meta.requiresAuth = requiresAuth
  }
  return {
    code: 'job',
    path: '/job',
    component: Layout,
    meta: {
      requiresAuth: true
    },
    children: [
      menu
    ]
  }
}

export default { type: 'job', router: [
  {
    ...layout({
      code: 'job_index',
      path: 'index',
      component: () => import('@/views/job/index'),
      meta: {
        title: '运行报表',
        icon: 'perform'
      }
    })
  },
  {
    ...layout({
      code: 'job_task',
      name: 'jobTask',
      path: 'task',
      component: () => import('@/views/job/task'),
      meta: {
        title: '运行报表',
        icon: 'icon-full-setting'
      }
    })
  },
  {
    ...layout({
      name: 'jobTaskScript',
      path: 'code/:jobId',
      component: () => import('@/views/job/task/Script'),
      hidden: true,
      meta: {
        activeMenu: '/job/config'
      }
    }, false)
  },
  {
    ...layout({
      code: 'job_log',
      name: 'jobLog',
      path: 'log',
      component: () => import('@/views/job/log'),
      meta: {
        title: '调度日志',
        icon: 'plan'
      }
    })
  },
  {
    ...layout({
      name: 'jobLogList',
      path: 'log/:executorId/:jobId',
      component: () => import('@/views/job/log'),
      hidden: true,
      meta: {
        activeMenu: '/job/log'
      }
    }, false)
  },
  {
    ...layout({
      name: 'jobLogDetail',
      path: 'log-detail/:logId',
      component: () => import('@/views/job/log/Detail'),
      hidden: true,
      meta: {
        activeMenu: '/job/log'
      }
    }, false)
  },
  {
    ...layout({
      code: 'job_executor',
      name: 'jobExecutor',
      path: 'executor',
      component: () => import('@/views/job/executor'),
      meta: {
        title: '执行器管理',
        icon: 'my-task'
      }
    })
  }]
}
