import request from '@/utils/request'

export function noticeListAPI(data) {
  return request({
    url: '/back-end/sys-notice-template/list',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function noticeAddAPI(data) {
  return request({
    url: '/back-end/sys-notice-template/add',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function noticeModifyAPI(data) {
  return request({
    url: '/back-end/sys-notice-template/modify',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function noticePublishAPI(data) {
  return request({
    url: '/back-end/sys-notice-template/publish/' + data,
    method: 'post'
  })
}
