import request from '@/utils/request'

export function design(data) {
  return request({
    url: '/back-end/sys-flowable-model/design',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function taskStatusGetAPI(id) {
  return request({
    url: `/back-end/sys-flowable-task/get-bpmn-xml/${id}`
  })
}
