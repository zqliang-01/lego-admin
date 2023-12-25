import request from '@/utils/request'

/**
 * 新建
 */
export function tableAddAPI(data) {
  return request({
    url: '/back-end/sharding-table/add',
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
export function tableUpdateAPI(data) {
  return request({
    url: '/back-end/sharding-table/update',
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
export function tableListAPI(data) {
  return request({
    url: '/back-end/sharding-table/list',
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
export function tableDeleteAPI(data) {
  return request({
    url: '/back-end/sharding-table/delete',
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
export function tableGetAPI(code) {
  return request({
    url: `/back-end/sharding-table/get/${code}`
  })
}

/**
 * 部分导出
 */
export function tableExcelExportAPI(data) {
  return request({
    url: '/back-end/sharding-table/export',
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
export function tableExcelAllExportAPI(data) {
  return request({
    url: '/back-end/sharding-table/export-all',
    method: 'post',
    data: data,
    responseType: 'blob',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
