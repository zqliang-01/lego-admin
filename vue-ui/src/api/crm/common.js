import request from '@/utils/request'
/**
 * 字典查询
 */
export function dictListAPI(model, type) {
  return request({
    url: '/back-end/' + model + '-dict/list?typeCode=' + type
  })
}

/**
 * 查询字典表类型
 */
export function dictTypeListAPI(model) {
  return request({
    url: '/back-end/' + model + '-dict/list-type'
  })
}
