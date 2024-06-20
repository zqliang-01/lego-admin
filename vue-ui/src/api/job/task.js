import request from '@/utils/request'

export function conditionListAPI(data) {
  return request({
    url: '/back-end/job-task/index',
    data: data
  })
}

export function taskListAPI(data) {
  return request({
    url: '/back-end/job-task/pageList',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function taskAddAPI(data) {
  return request({
    url: '/back-end/job-task/add',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function taskUpdateAPI(data) {
  return request({
    url: '/back-end/job-task/update',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function taskDeleteAPI(data) {
  return request({
    url: '/back-end/job-task/remove/' + data,
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function taskStopAPI(data) {
  return request({
    url: '/back-end/job-task/stop/' + data,
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function taskStartAPI(data) {
  return request({
    url: '/back-end/job-task/start/' + data,
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function taskTriggerAPI(data) {
  return request({
    url: '/back-end/job-task/trigger',
    method: 'post',
    data: data
  })
}

export function taskNextTriggerTimeAPI(data) {
  return request({
    url: '/back-end/job-task/nextTriggerTime',
    data: data
  })
}
