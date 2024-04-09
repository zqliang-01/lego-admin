import request from '@/utils/request'


/**
 * 系统消息列表
 */
export function systemMessageListAPI(data) {
  return request({
    url: '/back-end/sys-message/list',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function systemMessageReadAPI(data) {
  return request({
    url: '/back-end/sys-message/read/' + data,
    method: 'post'
  })
}

export function systemMessageReadAllAPI(data) {
  return request({
    url: '/back-end/sys-message/readAll',
    method: 'post',
    data: data
  })
}

export function systemMessageDeleteAPI(data) {
  return request({
    url: '/back-end/sys-message/delete/' + data,
    method: 'post'
  })
}

export function systemMessageDeleteAllAPI(data) {
  return request({
    url: '/back-end/sys-message/deleteAll',
    method: 'post',
    data: data
  })
}

export function systemMessageUnreadCountAPI() {
  return request({
    url: '/back-end/sys-message/unreadCount'
  })
}
