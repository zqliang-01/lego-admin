import request from '@/utils/request'

/**
 * 场景列表
 */
export function sceneListAPI(data) {
  return request({
    url: '/back-end/sys-scene/list',
    data: data
  })
}

/**
 * 场景列表
 */
export function sceneVisibleListAPI(data) {
  return request({
    url: '/back-end/sys-scene/list-visible',
    data: data
  })
}

/**
 * 场景创建
 */
export function sceneAddAPI(data) {
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
export function sceneModifyAPI(data) {
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
 * 场景删除
 */
export function sceneDeleteAPI(data) {
  return request({
    url: '/back-end/sys-scene/delete/' + data,
    method: 'post'
  })
}

/**
 * 场景排序
 */
export function sceneVisibleModifyAPI(data) {
  return request({
    url: '/back-end/sys-scene/modify-visible',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
