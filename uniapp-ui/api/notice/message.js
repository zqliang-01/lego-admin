import request from '@/utils/request'

const api = {
  list: '/back-end/sys-message/list',
	unreadCount: '/back-end/sys-message/unreadCount',
  read: '/back-end/sys-message/read/',
  delete: '/back-end/sys-message/delete/',
	detail: '/back-end/sys-message/get/'
}

export const list = (param) => {
  return request.post(api.list, param)
}

export const read = (param) => {
  return request.post(api.read + param)
}

export const unreadCount = () => {
  return request.get(api.unreadCount)
}

export const deleted = (param) => {
  return request.post(api.delete + param)
}

export const detail = (param) => {
  return request.get(api.detail + param)
}