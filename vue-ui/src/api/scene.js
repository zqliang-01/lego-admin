import request from '@/utils/request'

/**
 * 场景列表
 */
export function crmSceneListAPI(data) {
  return request({
    url: '/back-end/sys-scene/list',
    data: data
  })
}

/**
 * 场景列表
 */
export function crmSceneVisibleListAPI(data) {
  return request({
    url: '/back-end/sys-scene/list-visible',
    data: data
  })
}

/**
 * 场景创建
 */
export function crmSceneAddAPI(data) {
  return request({
    url: '/back-end/sys-scene/add',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 场景编辑
 */
export function crmSceneModifyAPI(data) {
  return request({
    url: '/back-end/sys-scene/modify',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 场景排序
 */
export function crmSceneVisibleModifyAPI(data) {
  return request({
    url: '/back-end/sys-scene/modify-visible',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
