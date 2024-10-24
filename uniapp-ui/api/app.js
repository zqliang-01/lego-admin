import request from '@/utils/request'

const api = {
  all: '/back-end/sys-app-sort/list-all',
	current: '/back-end/sys-permission/list-current'
}

export const all = (param, option) => {
  return request.get(api.all)
}

export const current = () => {
  return request.get(api.current)
}
