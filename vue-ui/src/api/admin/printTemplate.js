import request from '@/utils/request'

/**
 * 查询打印模板列表
 */
export function printTemplateListAPI(data) {
  return request({
    url: '/back-end/sys-print-template/list',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function printTemplateSimpleListAPI(data) {
  return request({
    url: '/back-end/sys-print-template/list-simple',
    data: data
  })
}

/**
   * 添加打印模板
   */
export function printTemplateAddAPI(data) {
  return request({
    url: '/back-end/sys-print-template/add',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
   * 编辑打印模板
   */
export function printTemplateModifyAPI(data) {
  return request({
    url: '/back-end/sys-print-template/update',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
   * 设计打印模板
   */
export function printTemplateDesignAPI(data) {
  return request({
    url: '/back-end/sys-print-template/design',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
   * 删除指定打印模板
   */
export function printTemplateDeleteAPI(data) {
  return request({
    url: '/back-end/sys-print-template/delete/' + data,
    method: 'post',
    data: data
  })
}

/**
   * 模板校准为内容
   */
export function printTemplatePrintAPI(data) {
  return request({
    url: '/back-end/sys-print-template/print',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
   * 打印预览
   */
export function printTemplatePreviewAPI(data) {
  return request({
    url: '/back-end/sys-print-template/preview',
    method: 'post',
    data: data,
    responseType: 'blob'
  })
}

export function printTemplateGetAPI(data) {
  return request({
    url: '/back-end/sys-print-template/get/' + data
  })
}
