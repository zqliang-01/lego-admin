import request from '@/utils/request'

export function permissionAddAPI(data) {
  return request({
    url: '/back-end/sys-permission/add',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function permissionModifyAPI(data) {
  return request({
    url: '/back-end/sys-permission/modify',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function permissionDeleteAPI(data) {
  return request({
    url: '/back-end/sys-permission/delete/' + data,
    method: 'post'
  })
}

export function permissionGetAPI(data) {
  return request({
    url: '/back-end/sys-permission/get/' + data
  })
}

export function permissionCurrentGetAPI(data) {
  return request({
    url: '/back-end/sys-permission/current',
    data: data
  })
}

export function permissionListAPI(data) {
  return request({
    url: '/back-end/sys-permission/list',
    data: data
  })
}

export function permissionDynamicListAPI(data) {
  return request({
    url: '/back-end/sys-permission/list-dynamic-current'
  })
}

export function permissionAuthListAPI(data) {
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

/**
 * 查询菜单类型列表
 */
export function permissionTypeListAPI() {
  return request({
    url: '/back-end/sys-permission/list-type'
  })
}

/**
 * 查询菜单路由类型列表
 */
export function permissionRouteTypeListAPI() {
  return request({
    url: '/back-end/sys-permission/list-route-type'
  })
}
