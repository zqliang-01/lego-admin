import request from '@/utils/request'

export function definitionAddAPI(data) {
  return request({
    url: '/back-end/report-definition/add',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function definitionModifyAPI(data) {
  return request({
    url: '/back-end/report-definition/update',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function definitionSimpleListAPI(data) {
  return request({
    url: '/back-end/report-definition/list-simple',
    data: data
  })
}

export function definitionSimpleValidListAPI(data) {
  return request({
    url: '/back-end/report-definition/list-simple-valid',
    data: data
  })
}

export function definitionGetAPI(data) {
  return request({
    url: '/back-end/report-definition/get/' + data
  })
}

export function definitionOpenTestAPI(data) {
  return request({
    url: '/back-end/report-definition/open-test',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function definitionPermissionGetAPI(data) {
  return request({
    url: '/back-end/report-definition/permission-get',
    data: data
  })
}
