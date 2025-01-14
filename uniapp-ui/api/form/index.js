import request from '@/utils/request'

const api = {
  permission: '/back-end/sys-custom-form/get-permission/'
}

export const permission = (param) => {
  return request.get(api.permission + param)
}
