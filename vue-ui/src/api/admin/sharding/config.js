import request from '@/utils/request'

/**
 * 新建
 */
export function configAddAPI(data) {
  return request({
    url: '/back-end/sharding-config/add',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 修改
 */
export function configUpdateAPI(data) {
  return request({
    url: '/back-end/sharding-config/update',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 列表
 */
export function configListAPI(data) {
  return request({
    url: '/back-end/sharding-config/list',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function configSimpleListAPI() {
  return request({
    url: '/back-end/sharding-config/list-simple'
  })
}

/**
 * 删除
 */
export function configDeleteAPI(data) {
  return request({
    url: '/back-end/sharding-config/delete',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 详情
 */
export function configGetAPI(code) {
  return request({
    url: `/back-end/sharding-config/get/${code}`
  })
}

/**
 * 部分导出
 */
export function configExcelExportAPI(data) {
  return request({
    url: '/back-end/sharding-config/export',
    method: 'post',
    data: data,
    responseType: 'blob',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 全部导出
 */
export function configExcelAllExportAPI(data) {
  return request({
    url: '/back-end/sharding-config/export-all',
    method: 'post',
    data: data,
    responseType: 'blob',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
