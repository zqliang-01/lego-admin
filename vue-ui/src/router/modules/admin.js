/** 系统管理路由 */
import Layout from '@/views/layout/AdminLayout'

const layout = function(menu, requiresAuth = true) {
  menu.meta.isMenu = true
  menu.meta.requiresAuth = requiresAuth
  return {
    path: '/manage',
    component: Layout,
    meta: {
      requiresAuth: true,
      permissions: ['manage']
    },
    children: [
      menu
    ]
  }
}

export default [
  {
    ...layout({
      name: 'systemconfig',
      path: 'systemconfig', // 企业首页
      component: () => import('@/views/admin/config'),
      meta: {
        title: '企业首页',
        icon: 'enterprise',
        permissions: ['manage_system']
      }
    })
  },
  {
    ...layout({
      name: 'application',
      path: 'application', // 应用管理
      component: () => import('@/views/admin/application'),
      meta: {
        title: '应用管理',
        icon: 'all',
        permissions: ['manage_configSet']
      }
    })
  },
  {
    ...layout({
      path: 'icon',
      component: () => import('@/views/admin/icon'),
      meta: {
        title: '图标',
        icon: 'icon-edition'
      }
    }, false)
  },
  {
    ...layout({
      name: 'EmployeeDept',
      path: 'employee-dept',
      component: () => import('@/views/admin/employee'),
      meta: {
        title: '员工与部门管理',
        icon: 's-seas',
        permissions: ['manage_users']
      }
    })
  },
  {
    ...layout({
      name: 'RoleAuth',
      path: 'role-auth',
      component: () => import('@/views/admin/roleAuth'),
      meta: {
        title: '角色权限管理',
        icon: 'user',
        permissions: ['manage_role']
      }
    })
  },
  {
    ...layout({
      name: 'genTableList',
      path: 'gen-table-list',
      component: () => import('@/views/admin/genTable'),
      meta: {
        title: '代码生成',
        icon: 'double-gear',
        permissions: ['manage_genTable']
      }
    })
  },
  {
    ...layout({
      name: 'genTableColumn',
      path: 'gen-table-column/:tableCode',
      component: () => import('@/views/admin/genTableColumn'),
      hidden: true,
      meta: {
        activeMenu: '/manage/gen-table-list'
      }
    }, false)
  },
  {
    ...layout({
      name: 'customForm',
      path: 'custom-form',
      component: () => import('@/views/admin/customForm'),
      meta: {
        title: '自定义表单',
        icon: 'icon-full-setting',
        permissions: ['manage_customForm']
      }
    })
  },
  {
    ...layout({
      name: 'customField',
      path: 'custom-field/:formCode',
      component: () => import('@/views/admin/customForm/fields'),
      hidden: true,
      meta: {
        activeMenu: '/manage/custom-form'
      }
    }, false)
  },
  {
    ...layout({
      name: 'Menu',
      path: 'menu',
      component: () => import('@/views/admin/menu'),
      meta: {
        title: '菜单管理',
        icon: 'icon-des',
        permissions: ['manage_permission']
      }
    })
  },
  {
    ...layout({
      path: 'workflow',
      meta: {
        title: '审批流程管理',
        icon: 'icon-workflow',
        permissions: ['manage_workflow']
      },
      children: [
        {
          path: 'model',
          component: () => import('@/views/admin/workflow/model'),
          meta: {
            title: '模型管理',
            requiresAuth: true,
            permissions: ['manage_workflow_model']
          }
        },
        {
          name: 'modelDesign',
          path: 'model-design/:modelId',
          component: () => import('@/views/admin/workflow/model/design'),
          hidden: true,
          meta: {
            activeMenu: '/manage/workflow/model'
          }
        },
        {
          path: 'definition',
          component: () => import('@/views/admin/workflow/definition'),
          meta: {
            title: '部署管理',
            requiresAuth: true,
            permissions: ['manage_workflow_definition']
          }
        }
      ]
    })
  },
  {
    ...layout({
      path: 'sharding',
      meta: {
        title: '分表管理',
        icon: 'icon-category-note',
        permissions: ['manage_sharding']
      },
      children: [
        {
          path: 'config',
          component: () => import('@/views/admin/sharding/config'),
          meta: {
            title: '分片规则配置',
            requiresAuth: true,
            permissions: ['manage_sharding_config']
          }
        },
        {
          path: 'dataSource',
          component: () => import('@/views/admin/sharding/dataSource'),
          meta: {
            title: '分片数据源配置',
            requiresAuth: true,
            permissions: ['manage_sharding_dataSource']
          }
        },
        {
          path: 'table',
          component: () => import('@/views/admin/sharding/table'),
          meta: {
            title: '分片表配置',
            requiresAuth: true,
            permissions: ['manage_sharding_table']
          }
        },
        {
          path: 'algorithm',
          component: () => import('@/views/admin/sharding/algorithm'),
          meta: {
            title: '分片算法配置',
            requiresAuth: true,
            permissions: ['manage_sharding_algorithm']
          }
        },
        {
          path: 'template',
          component: () => import('@/views/admin/sharding/template/index'),
          meta: {
            title: '分片模板配置',
            requiresAuth: true,
            permissions: ['manage_sharding_template']
          }
        }
      ]
    })
  },
  {
    ...layout({
      name: 'Log',
      path: 'log',
      component: () => import('@/views/admin/log'),
      meta: {
        title: '日志管理',
        icon: 'task',
        permissions: ['manage_log']
      }
    })
  }
]
