import request from '@/utils/request'

const api = {
	current: '/back-end/sys-permission/list-current',
}

export const current = () => {
  return request.get(api.current)
}
