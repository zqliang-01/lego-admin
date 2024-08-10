import request from '@/utils/request'

/**
 * 修改系统信息
 */
export function systemInfoModifyAPI(data) {
  return request({
    url: '/back-end/sys-config/update-information',
    method: 'post',
    data: data
  })
}

/**
 * 查询系统信息
 */
export function systemInfoGetAPI() {
  return request({
    url: '/back-end/sys-config/get-information'
  })
}

/**
 * 检查更新
 */
export function systemCheckUpdateAPI() {
  return request({
    url: '/back-end/sys-config/check-update'
  })
}

export function systemUpdateAPI() {
  return request({
    url: '/back-end/sys-config/update'
  })
}
