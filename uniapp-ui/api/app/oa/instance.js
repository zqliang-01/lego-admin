import request from '@/utils/request'
import config from '@/config'

const api = {
	list: '/back-end/flowable-instance/list',
	stop: '/back-end/flowable-instance/stop/'
}
export const flowImageUrl = config.apiUrl + '/back-end/flowable-instance/download-image/';

export const list = (param) => {
  return request.post(api.list, param)
}

export const stop = (param) => {
  return request.post(api.stop + param)
}
