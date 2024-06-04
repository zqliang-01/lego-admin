import request from '@/utils/request'

export function postRequest(url, data) {
  return request({
    url: url,
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function getRequest(url, data) {
  return request({
    url: url,
    data: data
  })
}

export function codeGetRequest(url, code) {
  return request({
    url: url + '/' + code
  })
}

export function fileRequest(url, data) {
  return request({
    url: url,
    method: 'post',
    data: data,
    responseType: 'blob',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
