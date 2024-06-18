import request from '@/utils/request'

export function executorListAPI(data) {
  return request({
    url: '/back-end/job-group/list',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function executorGetAPI(data) {
  return request({
    url: '/back-end/job-group/loadById/' + data
  })
}

export function executorAddAPI(data) {
  return request({
    url: '/back-end/job-group/save',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function executorUpdateAPI(data) {
  return request({
    url: '/back-end/job-group/update',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function executorDeleteAPI(data) {
  return request({
    url: '/back-end/job-group/remove/' + data,
    method: 'post'
  })
}
