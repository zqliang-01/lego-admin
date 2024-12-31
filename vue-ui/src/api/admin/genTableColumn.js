import request from '@/utils/request'

export function genTableColumnListAPI(data) {
  return request({
    url: '/back-end/sys-gen-table-column/list',
    method: 'get',
    data: data
  })
}

export function genTableMetaColumnListAPI(data) {
  return request({
    url: '/back-end/sys-gen-table-column/list-meta/' + data,
    method: 'get'
  })
}

export function genTableColumnAddAPI(data) {
  return request({
    url: '/back-end/sys-gen-table-column/add',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function genTableColumnModifyAPI(data) {
  return request({
    url: '/back-end/sys-gen-table-column/modify',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function genTableColumnDeleteAPI(data) {
  return request({
    url: '/back-end/sys-gen-table-column/delete/' + data,
    method: 'post'
  })
}

