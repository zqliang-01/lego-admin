/** 系统管理路由 */
import Layout from '@/views/layout/AdminLayout'

const layout = function(meta = {}, path = '/manage', requiresAuth = true) {
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
      permissions: ['manage', 'system']
    }),
    children: [{
      name: 'systemconfig',
      path: 'systemconfig', // 企业首页
      component: () => import('@/views/admin/config'),
      meta: {
        title: '企业首页',
        icon: 'enterprise'
      }
    }]
  },
  {
    ...layout({
      permissions: ['manage', 'configSet']
    }),
    children: [{
      name: 'application',
      path: 'application', // 应用管理
      component: () => import('@/views/admin/application'),
      meta: {
        title: '应用管理',
        icon: 'all'
      }
    }]
  },
  {
    ...layout({
      permissions: ['manage']
    }),
    children: [{
      path: 'icon',
      component: () => import('@/views/admin/icon'),
      meta: {
        title: '图标',
        icon: 'icon-edition'
      }
    }]
  },
  {
    ...layout({
      permissions: ['manage', 'users']
    }),
    children: [{
      name: 'EmployeeDept',
      path: 'employee-dept',
      component: () => import('@/views/admin/employee'),
      meta: {
        title: '员工与部门管理',
        icon: 's-seas'
      }
    }]
  },
  {
    ...layout({
      permissions: ['manage', 'role']
    }),
    children: [{
      name: 'RoleAuth',
      path: 'role-auth',
      component: () => import('@/views/admin/roleAuth'),
      meta: {
        title: '角色权限管理',
        icon: 'user'
      }
    }]
  },
  {
    ...layout({
      permissions: ['manage', 'genTable']
    }),
    children: [{
      name: 'genTableList',
      path: 'gen-table-list',
      component: () => import('@/views/admin/genTable'),
      meta: {
        title: '代码生成',
        icon: 'double-gear'
      }
    },
    {
      name: 'genTableColumn',
      path: 'gen-table-column/:tableCode',
      component: () => import('@/views/admin/genTableColumn'),
      hidden: true,
      meta: {
        activeMenu: '/manage/gen-table-list'
      }
    }]
  },
  {
    ...layout({
      permissions: ['manage', 'customForm']
    }),
    children: [{
      name: 'customFieldList',
      path: 'custom-field-list',
      component: () => import('@/views/admin/customField'),
      meta: {
        title: '自定义表单',
        icon: 'icon-list'
      }
    },
    {
      name: 'customField',
      path: 'custom-field/:formCode',
      component: () => import('@/views/admin/fields'),
      hidden: true,
      meta: {
        activeMenu: '/manage/custom-field-list'
      }
    }]
  },
  {
    ...layout({
      permissions: ['manage', 'sharding'],
      title: '分表管理',
      icon: 'icon-category-note'
    }, '/manage/sharding'),
    children: [
      {
        path: 'dataSource',
        component: () => import('@/views/admin/sharding/dataSource'),
        meta: {
          title: '分片数据源配置',
          requiresAuth: true,
          permissions: ['manage', 'sharding', 'dataSource']
        }
      },
      {
        path: 'template',
        component: () => import('@/views/admin/sharding/template/index'),
        meta: {
          title: '分片模板配置',
          requiresAuth: true,
          permissions: ['manage', 'sharding', 'template']
        }
      },
      {
        path: 'config',
        component: () => import('@/views/admin/sharding/config'),
        meta: {
          title: '分片规则配置',
          requiresAuth: true,
          permissions: ['manage', 'sharding', 'config']
        }
      },
      {
        path: 'algorithm',
        component: () => import('@/views/admin/sharding/algorithm'),
        meta: {
          title: '分片算法配置',
          requiresAuth: true,
          permissions: ['manage', 'sharding', 'algorithm']
        }
      },
      {
        path: 'table',
        component: () => import('@/views/admin/sharding/table'),
        meta: {
          title: '分片表配置',
          requiresAuth: true,
          permissions: ['manage', 'sharding', 'table']
        }
      }
    ]
  }
]
