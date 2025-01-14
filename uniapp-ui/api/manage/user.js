import request from '@/utils/request'

// api地址
const api = {
  userInfo: '/back-end/sys-employee/current',
	userBind: '/back-end/mobile-user-bind/update',
	userModify: '/back-end/sys-employee/modify-current',
	userResetPassword: '/back-end/sys-employee/modify-password',
	userSimpleList: '/back-end/sys-employee/list-simple'
}

export const info = (param, option) => {
  return request.get(api.userInfo)
}

export const bind = (param) => {
  return request.post(api.userBind + '/' + param)
}

export const modify = (param) => {
  return request.post(api.userModify, param)
}

export const resetPassword = (param) => {
  return request.post(api.userResetPassword, param)
}

export const simpleList = (param) => {
  return request.get(api.userSimpleList, param)
}
