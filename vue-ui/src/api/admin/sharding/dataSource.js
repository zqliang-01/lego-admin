import request from '@/utils/request'

/**
 * 新建
 */
export function dataSourceAddAPI(data) {
  return request({
    url: '/back-end/sharding-data-source/add',
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
export function dataSourceUpdateAPI(data) {
  return request({
    url: '/back-end/sharding-data-source/update',
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
export function dataSourceListAPI(data) {
  return request({
    url: '/back-end/sharding-data-source/list',
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
export function dataSourceSimpleListAPI() {
  return request({
    url: '/back-end/sharding-data-source/list-simple'
  })
}

/**
 * 删除
 */
export function dataSourceDeleteAPI(data) {
  return request({
    url: '/back-end/sharding-data-source/delete',
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
export function dataSourceGetAPI(code) {
  return request({
    url: `/back-end/sharding-data-source/get/${code}`
  })
}

/**
 * 部分导出
 */
export function dataSourceExcelExportAPI(data) {
  return request({
    url: '/back-end/sharding-data-source/export',
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
export function dataSourceExcelAllExportAPI(data) {
  return request({
    url: '/back-end/sharding-data-source/export-all',
    method: 'post',
    data: data,
    responseType: 'blob',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
