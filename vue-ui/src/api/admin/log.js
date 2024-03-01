import request from '@/utils/request'

export function operationLogListAPI(data) {
  return request({
    url: '/back-end/sys-operation-log/list',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
