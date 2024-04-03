import request from '@/utils/request'

export function printLogListAPI(data) {
  return request({
    url: '/back-end/sys-print-log/list',
    data: data
  })
}

export function printLogGetAPI(data) {
  return request({
    url: '/back-end/sys-print-log/get/' + data
  })
}

export function printLogAddAPI(data) {
  return request({
    url: '/back-end/sys-print-log/add',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
