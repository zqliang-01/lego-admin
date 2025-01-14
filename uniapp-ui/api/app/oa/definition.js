import request from '@/utils/request'
import config from '@/config'

const api = {
	list: '/back-end/flowable-definition/list',
	formKey: '/back-end/flowable-definition/get-form-key/',
	start: '/back-end/flowable-definition/start'
}
export const flowImageUrl = config.apiUrl + '/back-end/flowable-definition/download-image/';

export const list = (param) => {
  return request.post(api.list, param)
}

export const formKey = (param) => {
  return request.get(api.formKey + param)
}

export const start = (param) => {
  return request.post(api.start, param)
}
