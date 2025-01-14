import request from '@/utils/request'

const api = {
  list: '/back-end/sys-file/list'
}

export const list = (param) => {
  return request.get(api.list, param)
}
