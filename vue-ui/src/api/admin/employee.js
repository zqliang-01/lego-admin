import request from '@/utils/request'

export function employeeListAPI(data) {
  return request({
    url: '/back-end/sys-employee/list',
    method: 'get',
    data: data
  })
}

export function employeeSimpleListAPI(data) {
  return request({
    url: '/back-end/sys-employee/list-simple',
    method: 'get',
    data: data
  })
}

export function employeeGetAPI(data) {
  return request({
    url: '/back-end/sys-employee/get/' + data
  })
}

export function employeeModifyAPI(params) {
  return request({
    url: '/back-end/sys-employee/modify',
    method: 'post',
    data: params,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function employeeAddAPI(params) {
  return request({
    url: '/back-end/sys-employee/add',
    method: 'post',
    data: params,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function employeeRoleModifyAPI(params) {
  return request({
    url: '/back-end/sys-employee/modify-role',
    method: 'post',
    data: params,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function employeeResetPasswordAPI(data) {
  return request({
    url: '/back-end/sys-employee/reset-password',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
