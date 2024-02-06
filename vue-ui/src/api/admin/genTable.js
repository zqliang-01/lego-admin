import request from '@/utils/request'

export function genTableListAPI(data) {
  return request({
    url: '/back-end/sys-gen-table/list',
    method: 'post',
    data: data
  })
}

export function genTableNotExistsListAPI(data) {
  return request({
    url: '/back-end/sys-gen-table/list-not-exists'
  })
}

export function genTableAllListAPI(data) {
  return request({
    url: '/back-end/sys-gen-table/list-all'
  })
}

export function genTableInitGetAPI(code) {
  return request({
    url: `/back-end/sys-gen-table/get-init/${code}`
  })
}

export function genTableNameListAPI(data) {
  return request({
    url: '/back-end/sys-gen-table/list-name'
  })
}

export function genTableAddAPI(data) {
  return request({
    url: '/back-end/sys-gen-table/add',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function genTableModifyAPI(data) {
  return request({
    url: '/back-end/sys-gen-table/modify',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function genTableSyncAPI(code) {
  return request({
    url: `/back-end/sys-gen-table/sync/${code}`,
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function genTablePreviewAPI(code) {
  return request({
    url: `/back-end/sys-gen-table/preview/${code}`
  })
}

export function genTableDownloadAPI(code) {
  return request({
    url: `/back-end/sys-gen-table/download/${code}`,
    method: 'get',
    responseType: 'blob'
  })
}
