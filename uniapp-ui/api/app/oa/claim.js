import request from '@/utils/request'

const api = {
	list: '/back-end/flowable-task/list-claim',
	claim: '/back-end/flowable-task/claim'
}

export const list = (param) => {
  return request.post(api.list, param)
}

export const claim = (param) => {
  return request.post(api.claim, param)
}
