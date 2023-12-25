import request from '@/utils/request'

/**
 * 新建
 */
export function contractAddAPI(data) {
  return request({
    url: '/back-end/crm-contract/add',
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
export function contractUpdateAPI(data) {
  return request({
    url: '/back-end/crm-contract/update',
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
export function contractListAPI(data) {
  return request({
    url: '/back-end/crm-contract/list',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 删除
 */
export function contractDeleteAPI(data) {
  return request({
    url: '/back-end/crm-contract/delete',
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
export function contractGetAPI(code) {
  return request({
    url: `/back-end/crm-contract/get/${code}`
  })
}

/**
 * 部分导出
 */
export function contractExcelExportAPI(data) {
  return request({
    url: '/back-end/crm-contract/export',
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
export function contractExcelAllExportAPI(data) {
  return request({
    url: '/back-end/crm-contract/export-all',
    method: 'post',
    data: data,
    responseType: 'blob',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
