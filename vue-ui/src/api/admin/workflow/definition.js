import request from '@/utils/request'

/**
 * 修改
 */
export function definitionActiveAPI(data) {
  return request({
    url: '/back-end/flowable-definition/active/' + data,
    method: 'post'
  })
}

export function definitionSuspendAPI(data) {
  return request({
    url: '/back-end/flowable-definition/suspend/' + data,
    method: 'post'
  })
}

export function definitionStartAPI(data) {
  return request({
    url: '/back-end/flowable-definition/start/',
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
export function definitionListAPI(data) {
  return request({
    url: '/back-end/flowable-definition/list',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function definitionHistoryListAPI(data) {
  return request({
    url: '/back-end/flowable-definition/list-history',
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
export function definitionDeleteAPI(data) {
  return request({
    url: '/back-end/flowable-definition/delete/' + data,
    method: 'post'
  })
}

/**
 * 详情
 */
export function definitionBpmnXmlGetAPI(code) {
  return request({
    url: `/back-end/flowable-definition/getBpmnXml/${code}`
  })
}

