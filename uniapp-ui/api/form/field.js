import request from '@/utils/request'

const api = {
  listCreate: '/back-end/sys-custom-field/list-create/',
	listHeader: '/back-end/sys-custom-field/list-table-header/'
}

export const listCreate = (param) => {
  return request.get(api.listCreate + param)
}

export const listHeader = (param) => {
  return request.get(api.listHeader + param)
}
