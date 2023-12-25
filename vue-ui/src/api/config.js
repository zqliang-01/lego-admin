import request from '@/utils/request'

/**
 * 查询顶部导航栏所有已授权的模块
 */
export function headerModelAllListAPI() {
  return request({
    url: '/back-end/sys-app-sort/list-all'
  })
}

/**
 * 查询顶部导航栏显示的模块
 */
export function headerModelListAPI() {
  return request({
    url: '/back-end/sys-app-sort/list-header'
  })
}

/**
 * 修改首页顶部导航栏设置接口
 */
export function headerModelModifyAPI(data) {
  return request({
    url: '/back-end/sys-app-sort/update',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

