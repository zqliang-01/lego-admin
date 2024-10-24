import request from '@/utils/request'
import config from '@/config'

const api = {
  info: '/back-end/flowable-task/get-form-detail/',
	complete: '/back-end/flowable-task/complete',
	reject: '/back-end/flowable-task/reject'
}
export const flowImageUrl = config.apiUrl + '/back-end/flowable-task/download-image/';

export const info = (param) => {
  return request.get(api.info + param)
}

export const complete = (param) => {
	return request.post(api.complete, param)
}

export const reject = (param) => {
	return request.post(api.reject, param)
}
