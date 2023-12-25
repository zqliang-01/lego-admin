import request from '@/utils/request'

/**
 * 新建
 */
export function algorithmAddAPI(data) {
  return request({
    url: '/back-end/sharding-algorithm/add',
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
export function algorithmUpdateAPI(data) {
  return request({
    url: '/back-end/sharding-algorithm/update',
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
export function algorithmListAPI(data) {
  return request({
    url: '/back-end/sharding-algorithm/list',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function algorithmSimpleListAPI() {
  return request({
    url: '/back-end/sharding-algorithm/list-simple'
  })
}

/**
 * 删除
 */
export function algorithmDeleteAPI(data) {
  return request({
    url: '/back-end/sharding-algorithm/delete',
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
export function algorithmGetAPI(code) {
  return request({
    url: `/back-end/sharding-algorithm/get/${code}`
  })
}

/**
 * 部分导出
 */
export function algorithmExcelExportAPI(data) {
  return request({
    url: '/back-end/sharding-algorithm/export',
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
export function algorithmExcelAllExportAPI(data) {
  return request({
    url: '/back-end/sharding-algorithm/export-all',
    method: 'post',
    data: data,
    responseType: 'blob',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
