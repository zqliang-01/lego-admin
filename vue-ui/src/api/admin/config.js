import request from '@/utils/request'

/**
 * 企业首页
 */
export function systemInfoModifyAPI(data) {
  return request({
    url: '/back-end/sys-config/update-information',
    method: 'post',
    data: data
  })
}

/**
 * 企业首页
 */
export function systemInfoGetAPI() {
  return request({
    url: '/back-end/sys-config/get-information'
  })
}
