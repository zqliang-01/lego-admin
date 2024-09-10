import request from '@/utils/request'

export function designAddAPI(data) {
  return request({
    url: '/back-end/report-design/add',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function designModifyAPI(data) {
  return request({
    url: '/back-end/report-design/update',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function designSortModifyAPI(data) {
  return request({
    url: '/back-end/report-design/update-sort',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function designDeleteAPI(data) {
  return request({
    url: '/back-end/report-design/delete',
    method: 'post',
    data: data
  })
}

export function designListAPI(data) {
  return request({
    url: '/back-end/report-design/list',
    data: data
  })
}

export function designValidListAPI(data) {
  return request({
    url: '/back-end/report-design/list-valid',
    data: data
  })
}

export function designGetAPI(data) {
  return request({
    url: '/back-end/report-design/get/' + data
  })
}
