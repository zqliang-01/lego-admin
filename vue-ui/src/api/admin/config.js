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
    url: '/back-end/sys-config/update',
    method: 'post'
  })
}

export function appPackageUploadAPI(data, config = {}) {
  var param = new FormData()
  Object.keys(data).forEach(key => {
    param.append(key, data[key])
  })
  return request({
    url: '/back-end/sys-config/upload-package',
    method: 'post',
    data: param,
    ...config,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}
