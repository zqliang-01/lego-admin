import request from '@/utils/request'

const api = {
  list: '/back-end/report-design/list-valid',
	open: '/back-end/report-open/dash-board'
}

export const list = () => {
  return request.get(api.list)
}

export const open = (param) => {
  return request.post(api.open, param)
}
