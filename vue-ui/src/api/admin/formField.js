import request from '@/utils/request'

export function customFormListAPI(data) {
  return request({
    url: '/back-end/sys-custom-form/list',
    method: 'post',
    data: data
  })
}

export function customFormSimpleListAPI() {
  return request({
    url: '/back-end/sys-custom-form/list-simple'
  })
}

export function customFormGetAPI(code) {
  return request({
    url: '/back-end/sys-custom-form/get/' + code
  })
}

export function customFormInitGetAPI(tableCode) {
  return request({
    url: '/back-end/sys-custom-form/get-init',
    data: { tableCode: tableCode }
  })
}

export function customFormModifyAPI(data) {
  return request({
    url: '/back-end/sys-custom-form/modify',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function customFormAddAPI(data) {
  return request({
    url: '/back-end/sys-custom-form/add',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function customFormDeleteAPI(data) {
  return request({
    url: '/back-end/sys-custom-form/delete/' + data,
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function customFieldSimpleListAPI(data) {
  return request({
    url: '/back-end/sys-custom-field/list-simple',
    data: data
  })
}

export function customFieldTypeListAPI(data) {
  return request({
    url: `/back-end/sys-custom-field/list-type`,
    data: data
  })
}

export function customFieldListAPI(data) {
  return request({
    url: `/back-end/sys-custom-field/list`,
    data: data
  })
}
export function customFieldInitListAPI(data) {
  return request({
    url: `/back-end/sys-custom-field/list-init`,
    data: data
  })
}

export function customFieldModifyAPI(data) {
  return request({
    url: '/back-end/sys-custom-field/modify',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 表头字段查询
 */
export function tableHeaderFieldListAPI(data) {
  return request({
    url: '/back-end/sys-custom-field/list-table-header/' + data
  })
}

/**
 * 新增表单字段查询
 */
export function createFieldListAPI(data) {
  return request({
    url: '/back-end/sys-custom-field/list-create/' + data
  })
}

/**
 * 详情表单字段查询
 */
export function detailFieldListAPI(data) {
  return request({
    url: '/back-end/sys-custom-field/list-detail/' + data
  })
}

/**
 * 列表字段排序数据查询
 */
export function columnSortListAPI(data) {
  return request({
    url: '/back-end/sys-column-sort/list',
    data: data
  })
}

/**
 * 列表字段排序编辑
 */
export function columnSortModifyAPI(data) {
  return request({
    url: '/back-end/sys-column-sort/modify-all',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 列表宽度设置
 */
export function columnWidthModifyAPI(data) {
  return request({
    url: '/back-end/sys-column-sort/modify',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
