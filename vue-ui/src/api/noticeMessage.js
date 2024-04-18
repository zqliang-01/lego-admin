import request from '@/utils/request'

export function noticeMessageListAPI(data) {
  return request({
    url: '/back-end/sys-notice/list',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function noticeMessageReadAPI(data) {
  return request({
    url: '/back-end/sys-notice/read/' + data,
    method: 'post'
  })
}

export function noticeMessageReadAllAPI(data) {
  return request({
    url: '/back-end/sys-notice/readAll',
    method: 'post',
    data: data
  })
}

export function noticeMessageDeleteAPI(data) {
  return request({
    url: '/back-end/sys-notice/delete/' + data,
    method: 'post'
  })
}

export function noticeMessageDeleteAllAPI(data) {
  return request({
    url: '/back-end/sys-notice/deleteAll',
    method: 'post',
    data: data
  })
}

export function noticeMessageUnreadCountAPI() {
  return request({
    url: '/back-end/sys-notice/unreadCount'
  })
}
