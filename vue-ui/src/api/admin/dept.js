import request from '@/utils/request'

export function depListAPI(data) {
  return request({
    url: '/back-end/sys-dept/list'
  })
}

export function depSimpleListAPI(data) {
  return request({
    url: '/back-end/sys-dept/list-simple'
  })
}

export function depModifyAPI(data) {
  return request({
    url: '/back-end/sys-dept/modify',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function depAddAPI(data) {
  return request({
    url: '/back-end/sys-dept/add',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
