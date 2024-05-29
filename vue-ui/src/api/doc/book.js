import request from '@/utils/request'

/**
 * 新建
 */
export function bookAddAPI(data) {
  return request({
    url: '/back-end/doc-book/add',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 修改
 */
export function bookModifyAPI(data) {
  return request({
    url: '/back-end/doc-book/update',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 列表
 */
export function bookListAPI(data) {
  return request({
    url: '/back-end/doc-book/list',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function bookDisableListAPI(data) {
  return request({
    url: '/back-end/doc-book/list-disable',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function bookPublicListAPI(data) {
  return request({
    url: '/back-end/doc-book/list-public',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function bookDisableAPI(data) {
  return request({
    url: '/back-end/doc-book/disable/' + data,
    method: 'post'
  })
}

export function bookEnableAPI(data) {
  return request({
    url: '/back-end/doc-book/enable/' + data,
    method: 'post'
  })
}

export function bookDeleteAPI(data) {
  return request({
    url: '/back-end/doc-book/delete/' + data,
    method: 'post'
  })
}

export function bookGetAPI(data) {
  return request({
    url: '/back-end/doc-book/get/' + data
  })
}
