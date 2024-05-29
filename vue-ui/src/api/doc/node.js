import request from '@/utils/request'

export function nodeListAPI(data) {
  return request({
    url: '/back-end/doc-node/list',
    data: data
  })
}

export function nodeChildrenListAPI(data) {
  return request({
    url: '/back-end/doc-node/list-children',
    data: data
  })
}

export function nodeAddAPI(data) {
  return request({
    url: '/back-end/doc-node/add',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function nodeModifyAPI(data) {
  return request({
    url: '/back-end/doc-node/update',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function nodeDisableAPI(data) {
  return request({
    url: '/back-end/doc-node/disable/' + data,
    method: 'post'
  })
}

export function nodeGetAPI(data) {
  return request({
    url: '/back-end/doc-node/get/' + data
  })
}
