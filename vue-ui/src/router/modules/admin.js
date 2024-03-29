/** 系统管理路由 */
import Layout from '@/views/layout/AdminLayout'

const layout = function(menu, requiresAuth = true) {
  menu.meta.isMenu = true
  menu.meta.requiresAuth = requiresAuth
  return {
    code: 'manage',
    path: '/manage',
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
      code: 'manage_system',
      name: 'systemconfig',
      path: 'systemconfig', // 企业首页
      component: () => import('@/views/admin/config'),
      meta: {
        title: '企业首页',
        icon: 'enterprise'
      }
    })
  },
  {
    ...layout({
      code: 'manage_configSet',
      name: 'application',
      path: 'application', // 应用管理
      component: () => import('@/views/admin/application'),
      meta: {
        title: '应用管理',
        icon: 'all'
      }
    })
  },
  {
    ...layout({
      code: 'manage_users',
      name: 'EmployeeDept',
      path: 'employee-dept',
      component: () => import('@/views/admin/employee'),
      meta: {
        title: '员工与部门管理',
        icon: 's-seas'
      }
    })
  },
  {
    ...layout({
      code: 'manage_role',
      name: 'RoleAuth',
      path: 'role-auth',
      component: () => import('@/views/admin/roleAuth'),
      meta: {
        title: '角色权限管理',
        icon: 'user'
      }
    })
  },
  {
    ...layout({
      code: 'manage_genTable',
      name: 'genTableList',
      path: 'gen-table-list',
      component: () => import('@/views/admin/genTable'),
      meta: {
        title: '代码生成',
        icon: 'double-gear'
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
      code: 'manage_customForm',
      name: 'customForm',
      path: 'custom-form',
      component: () => import('@/views/admin/customForm'),
      meta: {
        title: '自定义表单',
        icon: 'icon-full-setting'
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
      code: 'manage_permission',
      name: 'Menu',
      path: 'menu',
      component: () => import('@/views/admin/menu'),
      meta: {
        title: '菜单管理',
        icon: 'icon-des'
      }
    })
  },
  {
    ...layout({
      code: 'manage_workflow',
      path: 'workflow',
      meta: {
        title: '审批流程管理',
        icon: 'icon-workflow'
      },
      children: [
        {
          code: 'manage_workflow_model',
          path: 'model',
          component: () => import('@/views/admin/workflow/model'),
          meta: {
            title: '模型管理',
            requiresAuth: true
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
          code: 'manage_workflow_definition',
          path: 'definition',
          component: () => import('@/views/admin/workflow/definition'),
          meta: {
            title: '部署管理',
            requiresAuth: true
          }
        }
      ]
    })
  },
  {
    ...layout({
      code: 'manage_sharding',
      path: 'sharding',
      meta: {
        title: '分表管理',
        icon: 'icon-category-note'
      },
      children: [
        {
          code: 'manage_sharding_config',
          path: 'config',
          component: () => import('@/views/admin/sharding/config'),
          meta: {
            title: '分片规则配置',
            requiresAuth: true
          }
        },
        {
          code: 'manage_sharding_dataSource',
          path: 'dataSource',
          component: () => import('@/views/admin/sharding/dataSource'),
          meta: {
            title: '分片数据源配置',
            requiresAuth: true
          }
        },
        {
          code: 'manage_sharding_table',
          path: 'table',
          component: () => import('@/views/admin/sharding/table'),
          meta: {
            title: '分片表配置',
            requiresAuth: true
          }
        },
        {
          code: 'manage_sharding_algorithm',
          path: 'algorithm',
          component: () => import('@/views/admin/sharding/algorithm'),
          meta: {
            title: '分片算法配置',
            requiresAuth: true
          }
        },
        {
          code: 'manage_sharding_template',
          path: 'template',
          component: () => import('@/views/admin/sharding/template/index'),
          meta: {
            title: '分片模板配置',
            requiresAuth: true
          }
        }
      ]
    })
  },
  {
    ...layout({
      code: 'manage_log',
      name: 'Log',
      path: 'log',
      component: () => import('@/views/admin/log'),
      meta: {
        title: '日志管理',
        icon: 'task'
      }
    })
  },
  {
    ...layout({
      path: 'icon',
      component: () => import('@/views/admin/icon'),
      meta: {
        title: '图标',
        icon: 'icon-edition',
        sn: 9999
      }
    }, false)
  }
]
