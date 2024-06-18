import request from '@/utils/request'

export function conditionListAPI(data) {
  return request({
    url: '/back-end/job-log/index',
    data: data
  })
}

export function taskListAPI(data) {
  return request({
    url: '/back-end/job-log/getJobsByGroup',
    data: data
  })
}

export function logListAPI(data) {
  return request({
    url: '/back-end/job-log/pageList',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function logDetailGetAPI(data) {
  return request({
    url: '/back-end/job-log/logDetailCat',
    data: data
  })
}

export function logClearAPI(data) {
  return request({
    url: '/back-end/job-log/clearLog',
    method: 'post',
    data: data
  })
}

export function logKillAPI(data) {
  return request({
    url: '/back-end/job-log/logKill/' + data,
    method: 'post'
  })
}
