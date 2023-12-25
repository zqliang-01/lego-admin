import request from '@/utils/request'

/**
 * 查询 打印模板列表
 * @param {*} data
 */
export function printTemplateListAPI(data) {
  return request({
    url: 'crmPrint/queryPrintTemplateList',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
   * 查询指定打印模板
   * templateId
   * @param {*} data
   */
export function printTemplateByIdAPI(data) {
  return request({
    url: 'crmPrint/queryPrintTemplateById',
    method: 'post',
    data: data
  })
}

/**
   * 添加模板 打印模板
   * templateName 模板名称
   * type 关联对象
   * content模板
   * @param {*} data
   */
export function printAddTemplateAPI(data) {
  return request({
    url: 'crmPrint/addPrintTemplate',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
   * 编辑模板 打印模板
   * templateName 模板名称
   * type 关联对象
   * content模板
   * @param {*} data
   */
export function printUpdateTemplateAPI(data) {
  return request({
    url: 'crmPrint/updatePrintTemplate',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
   * 删除指定打印模板
   * templateId
   * @param {*} data
   */
export function printDeleteTemplateAPI(data) {
  return request({
    url: 'crmPrint/deletePrintTemplate',
    method: 'post',
    data: data
  })
}

/**
   * 模块对应字段查询
   * type
   * @param {*} data
   */
export function printQueryFieldsAPI(data) {
  return request({
    url: 'crmPrint/queryFields',
    method: 'post',
    data: data
  })
}

/**
   * 模板校准为内容
   * type
   * @param {*} data
   */
export function printPrintAPI(data) {
  return request({
    url: 'crmPrint/print',
    method: 'post',
    data: data
  })
}

/**
   * 模板复制
   * type
   * @param {*} data
   */
export function printCopyTemplateAPI(data) {
  return request({
    url: 'crmPrint/copyTemplate',
    method: 'post',
    data: data
  })
}

/**
   * 保存打印记录
   * type
   * @param {*} data
   */
export function printSaveRecordAPI(data) {
  return request({
    url: 'crmPrint/savePrintRecord',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
   * 查询打印记录
   * type
   * @param {*} data
   */
export function printQueryPrintRecordAPI(data) {
  return request({
    url: 'crmPrint/queryPrintRecord',
    method: 'post',
    data: data
  })
}

/**
   * 打印记录详情
   * type
   * @param {*} data
   */
export function printRecordDetailAPI(data) {
  return request({
    url: 'crmPrint/queryPrintRecordById',
    method: 'post',
    data: data
  })
}

/**
   * 打印预览
   * type
   * @param {*} data
   */
export function printPreviewAPI(data) {
  return request({
    url: 'crmPrint/preview',
    method: 'post',
    data: data
  })
}
