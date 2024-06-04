import request from '@/utils/request'

export function dictListAPI(model, type) {
  return request({
    url: '/back-end/' + model + '-dict/list?typeCode=' + type
  })
}

export function dictAddAPI(model, data) {
  return request({
    url: '/back-end/' + model + '-dict/add',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function dictModifyAPI(model, data) {
  return request({
    url: '/back-end/' + model + '-dict/update',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function dictTypeListAPI(model) {
  return request({
    url: '/back-end/' + model + '-dict/list-type'
  })
}

export function dictTypeAddAPI(model, data) {
  return request({
    url: '/back-end/' + model + '-dict/add-type',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function dictTypeModifyAPI(model, data) {
  return request({
    url: '/back-end/' + model + '-dict/update-type',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
