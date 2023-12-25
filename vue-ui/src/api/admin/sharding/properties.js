import request from '@/utils/request'

/**
 * 列表
 */
export function propertiesListAPI(data) {
  return request({
    url: '/back-end/sharding-properties/list',
    method: 'get',
    data: data
  })
}

export function propertiesAddAPI(data) {
  return request({
    url: '/back-end/sharding-properties/add',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
