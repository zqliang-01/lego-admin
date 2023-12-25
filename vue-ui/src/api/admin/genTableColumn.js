import request from '@/utils/request'

export function genTableColumnListAPI(data) {
  return request({
    url: '/back-end/sys-gen-table-column/list',
    method: 'get',
    data: data
  })
}

export function genTableModifyAPI(data) {
  return request({
    url: '/back-end/sys-gen-table-column/modify',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
