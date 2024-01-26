import request from '@/utils/request'
/**
 * 字典查询
 */
export function dictListAPI(model, type) {
  return request({
    url: '/back-end/' + model + '-dict/list?typeCode=' + type
  })
}

/**
 * 查询字典表类型
 */
export function dictTypeListAPI(model) {
  return request({
    url: '/back-end/' + model + '-dict/list-type'
  })
}

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
