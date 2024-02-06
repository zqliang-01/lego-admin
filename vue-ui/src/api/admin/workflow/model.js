import request from '@/utils/request'

/**
 * 新建
 */
export function modelAddAPI(data) {
  return request({
    url: '/back-end/flowable-model/add',
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
export function modelUpdateAPI(data) {
  return request({
    url: '/back-end/flowable-model/update',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function modelDesignAPI(data) {
  return request({
    url: '/back-end/flowable-model/design',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function modelDeployAPI(id) {
  return request({
    url: '/back-end/flowable-model/deploy/' + id,
    method: 'post'
  })
}


/**
 * 列表
 */
export function modelListAPI(data) {
  return request({
    url: '/back-end/flowable-model/list',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function modelHistoryListAPI(data) {
  return request({
    url: '/back-end/flowable-model/list-history',
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
export function modelDeleteAPI(data) {
  return request({
    url: '/back-end/flowable-model/delete/' + data,
    method: 'post'
  })
}

/**
 * 详情
 */
export function modelBpmnXmlGetAPI(code) {
  return request({
    url: `/back-end/flowable-model/getBpmnXml/${code}`
  })
}

