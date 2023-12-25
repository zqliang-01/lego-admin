import request from '@/utils/request'

export function permissionCurrentGetAPI(data) {
  return request({
    url: '/back-end/sys-permission/current',
    data: data
  })
}

export function permissionAllListAPI(data) {
  return request({
    url: '/back-end/sys-permission/list-all'
  })
}

export function permissionListAPI(data) {
  return request({
    url: '/back-end/sys-permission/list-role-auth',
    data: data
  })
}

/**
 * 查询菜单列表
 */
export function permissionMenuListAPI(data) {
  return request({
    url: '/back-end/sys-permission/list-menu'
  })
}
