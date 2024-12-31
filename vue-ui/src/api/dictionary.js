import request from '@/utils/request'

export function dictSimpleListAPI(type) {
  return request({
    url: '/back-end/sys-dict/list-simple?typeCode=' + type
  })
}

export function dictListAPI(type) {
  return request({
    url: '/back-end/sys-dict/list?typeCode=' + type
  })
}

export function dictAddAPI(data) {
  return request({
    url: '/back-end/sys-dict/add',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function dictModifyAPI(data) {
  return request({
    url: '/back-end/sys-dict/update',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function dictTypeListAPI() {
  return request({
    url: '/back-end/sys-dict/list-type'
  })
}

export function dictTypeAddAPI(data) {
  return request({
    url: '/back-end/sys-dict/add-type',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function dictTypeModifyAPI(data) {
  return request({
    url: '/back-end/sys-dict/update-type',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
