import request from '@/utils/request'

/**
 * 修改头像
 */
export function adminUsersUpdateImgAPI(data) {
  return request({
    url: 'adminUser/updateImg',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

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
export function employeeModifyAPI(data) {
  return request({
    url: '/back-end/sys-employee/modify',
    method: 'post',
    data: data,
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
