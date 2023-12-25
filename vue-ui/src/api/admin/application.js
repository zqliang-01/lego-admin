import request from '@/utils/request'

/**
 * 应用列表接口
 */
export function appListAPI(data) {
  return request({
    url: '/back-end/sys-config/list-app'
  })
}

/**
 * 应用状态改变
 */
export function appModifyAPI(data) {
  return request({
    url: '/back-end/sys-config/set-app',
    method: 'post',
    data: data
  })
}
