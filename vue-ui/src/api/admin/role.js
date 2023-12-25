import request from '@/utils/request'

export function roleListAPI(data) {
  return request({
    url: '/back-end/sys-role/list',
    method: 'get',
    data: data
  })
}

export function roleSimpleListAPI(data) {
  return request({
    url: '/back-end/sys-role/list-simple',
    method: 'get',
    data: data
  })
}

export function roleAddAPI(data) {
  return request({
    url: '/back-end/sys-role/add',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function roleDeleteAPI(data) {
  return request({
    url: '/back-end/sys-role/delete/' + data,
    method: 'post',
    data: data
  })
}

export function roleModifyAPI(data) {
  return request({
    url: '/back-end/sys-role/modify',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function roleAuthAPI(data) {
  return request({
    url: '/back-end/sys-role/auth',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

