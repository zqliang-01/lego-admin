import request from '@/utils/request'

export function pageGetAPI(data) {
  return request({
    url: '/back-end/doc-page/get/' + data
  })
}

export function pageModifyAPI(data) {
  return request({
    url: '/back-end/doc-page/update',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
