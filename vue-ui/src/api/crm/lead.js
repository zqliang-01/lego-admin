import request from '@/utils/request'

/**
 * 新建
 */
export function leadAddAPI(data) {
  return request({
    url: '/back-end/crm-lead/add',
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
export function leadUpdateAPI(data) {
  return request({
    url: '/back-end/crm-lead/update',
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
export function leadListAPI(data) {
  return request({
    url: '/back-end/crm-lead/list',
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
export function leadDeleteAPI(data) {
  return request({
    url: '/back-end/crm-lead/delete',
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
export function leadGetAPI(code) {
  return request({
    url: `/back-end/crm-lead/get/${code}`
  })
}

/**
 * 部分导出
 */
export function leadExcelExportAPI(data) {
  return request({
    url: '/back-end/crm-lead/export',
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
export function leadExcelAllExportAPI(data) {
  return request({
    url: '/back-end/crm-lead/export-all',
    method: 'post',
    data: data,
    responseType: 'blob',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
