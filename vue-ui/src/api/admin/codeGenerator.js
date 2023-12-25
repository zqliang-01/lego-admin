import request from '@/utils/request'

/**
 * 获取编码
 */
export function codeGeneratorGenerateAPI(code) {
  return request({
    url: `/back-end/sys-code-generator/generate/${code}`
  })
}


/**
 * 新建
 */
export function codeGeneratorAddAPI(data) {
  return request({
    url: '/back-end/sys-code-generator/add',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 修改
 */
export function codeGeneratorUpdateAPI(data) {
  return request({
    url: '/back-end/sys-code-generator/update',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 删除
 */
export function codeGeneratorDeleteAPI(code) {
  return request({
    url: `/back-end/sys-code-generator/delete/${code}`,
    method: 'post'
  })
}

/**
 * 详情
 */
export function codeGeneratorGetAPI(code) {
  return request({
    url: `/back-end/sys-code-generator/get/${code}`
  })
}

