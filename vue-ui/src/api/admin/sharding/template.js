import request from '@/utils/request'

/**
 * 新建
 */
export function templateAddAPI(data) {
  return request({
    url: '/back-end/sharding-template/add',
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
export function templateUpdateAPI(data) {
  return request({
    url: '/back-end/sharding-template/update',
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
export function templateListAPI(data) {
  return request({
    url: '/back-end/sharding-template/list',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function templateSimpleListAPI(data) {
  return request({
    url: '/back-end/sharding-template/list-simple',
    method: 'get',
    data: data
  })
}

/**
 * 删除
 */
export function templateDeleteAPI(data) {
  return request({
    url: '/back-end/sharding-template/delete',
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
export function templateGetAPI(code) {
  return request({
    url: `/back-end/sharding-template/get/${code}`
  })
}

/**
 * 详情
 */
export function templateJsonGetAPI(code) {
  return request({
    url: `/back-end/sharding-template/getJson/${code}`
  })
}

/**
 * 部分导出
 */
export function templateExcelExportAPI(data) {
  return request({
    url: '/back-end/sharding-template/export',
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
export function templateExcelAllExportAPI(data) {
  return request({
    url: '/back-end/sharding-template/export-all',
    method: 'post',
    data: data,
    responseType: 'blob',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 模板类型列表
 */
export function templateTypeListAPI(data) {
  return request({
    url: '/back-end/sharding-template-type/list'
  })
}
