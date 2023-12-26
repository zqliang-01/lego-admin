import request from '@/utils/request'

/**
 * 个人详情
 */
export function employeeCurrentGetAPI(data) {
  return request({
    url: '/back-end/sys-employee/current',
    data: data
  })
}

/**
 * 修改个人信息
 */
export function employeeCurrentModifyAPI(params) {
  return request({
    url: '/back-end/sys-employee/modify-current',
    method: 'post',
    data: params,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 修改密码
 */
export function employeePasswordModifyAPI(data) {
  return request({
    url: '/back-end/sys-employee/modify-password',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
