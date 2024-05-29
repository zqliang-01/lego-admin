import request from '@/utils/request'

export function collectListAPI(data) {
  return request({
    url: '/back-end/doc-collect/list',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function collectAddAPI(data) {
  return request({
    url: '/back-end/doc-collect/add',
    method: 'post',
    data: data
  })
}

export function collectDeleteAPI(data) {
  return request({
    url: '/back-end/doc-collect/delete/' + data,
    method: 'post'
  })
}

export function collectCodeGetAPI(data) {
  return request({
    url: '/back-end/doc-collect/get-code',
    data: data
  })
}
