/** 系统管理路由 */
import Layout from '@/views/layout/DocLayout'

const layout = function(menu, requiresAuth = true) {
  if (menu.meta) {
    menu.meta.requiresAuth = requiresAuth
  }
  return {
    code: 'doc',
    path: '/doc',
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
      code: 'doc_book',
      path: 'book',
      component: () => import('@/views/doc/book'),
      meta: {
        title: '我的知识库',
        icon: 'book'
      }
    })
  },
  {
    ...layout({
      name: 'DocBook',
      path: 'book-node/:bookCode',
      component: () => import('@/views/doc/node'),
      hidden: true,
      meta: {
        activeMenu: '/doc/book'
      }
    }, false)
  },
  {
    ...layout({
      name: 'DocBookNode',
      path: 'book-node/:bookCode/:nodeCode',
      component: () => import('@/views/doc/node'),
      hidden: true,
      meta: {
        activeMenu: '/doc/book'
      }
    }, false)
  },
  {
    ...layout({
      code: 'doc_collect',
      path: 'collect',
      component: () => import('@/views/doc/collect'),
      meta: {
        title: '我的收藏',
        icon: 'contract',
        isMenu: true,
        sn: 53
      }
    }, false)
  },
  {
    ...layout({
      path: 'public',
      component: () => import('@/views/doc/public'),
      meta: {
        title: '公共知识库',
        icon: 'customer',
        isMenu: true,
        sn: 53
      }
    }, false)
  },
  {
    ...layout({
      code: 'doc_recycle',
      path: 'recycle',
      component: () => import('@/views/doc/recycle'),
      meta: {
        title: '回收站',
        icon: 'recycle-bin'
      }
    })
  }
]
