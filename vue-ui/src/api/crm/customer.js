import request from '@/utils/request'

/**
 * 新建
 */
export function customerAddAPI(data) {
  return request({
    url: '/back-end/crm-customer/add',
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
export function customerUpdateAPI(data) {
  return request({
    url: '/back-end/crm-customer/update',
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
export function customerListAPI(data) {
  return request({
    url: '/back-end/crm-customer/list',
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
export function customerDeleteAPI(data) {
  return request({
    url: '/back-end/crm-customer/delete',
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
export function customerGetAPI(code) {
  return request({
    url: `/back-end/crm-customer/get/${code}`
  })
}

/**
 * 部分导出
 */
export function customerExcelExportAPI(data) {
  return request({
    url: '/back-end/crm-customer/export',
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
export function customerExcelAllExportAPI(data) {
  return request({
    url: '/back-end/crm-customer/export-all',
    method: 'post',
    data: data,
    responseType: 'blob',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
