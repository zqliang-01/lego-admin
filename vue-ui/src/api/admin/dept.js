import request from '@/utils/request'

export function deptListAPI(data) {
  return request({
    url: '/back-end/sys-dept/list'
  })
}

export function deptChildrenListAPI(data) {
  return request({
    url: '/back-end/sys-dept/list-children',
    data: data
  })
}

export function deptSimpleListAPI(data) {
  return request({
    url: '/back-end/sys-dept/list-simple'
  })
}

export function deptModifyAPI(data) {
  return request({
    url: '/back-end/sys-dept/modify',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function deptAddAPI(data) {
  return request({
    url: '/back-end/sys-dept/add',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
