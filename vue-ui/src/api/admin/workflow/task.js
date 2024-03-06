import request from '@/utils/request'

export function taskStartAPI(data) {
  return request({
    url: '/back-end/flowable-task/start',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function taskUndoListAPI(data) {
  return request({
    url: '/back-end/flowable-task/list-undo',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function taskCompletedListAPI(data) {
  return request({
    url: '/back-end/flowable-task/list-completed',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function taskCompleteAPI(data) {
  return request({
    url: '/back-end/flowable-task/complete',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function taskRejectAPI(data) {
  return request({
    url: '/back-end/flowable-task/reject',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function taskFormDetailGetAPI(id) {
  return request({
    url: '/back-end/flowable-task/get-form-detail/' + id
  })
}

export function taskHistoryListAPI(data) {
  return request({
    url: '/back-end/flowable-task/list-his',
    data: data
  })
}