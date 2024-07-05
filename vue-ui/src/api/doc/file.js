import request from '@/utils/request'

export const filePreviewUrl = process.env.VUE_APP_BASE_API + 'back-end/doc-file/download/'

export function fileUploadAPI(data, config = {}) {
  var param = new FormData()
  Object.keys(data).forEach(key => {
    param.append(key, data[key])
  })
  return request({
    url: '/back-end/doc-file/upload',
    method: 'post',
    data: param,
    ...config,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

export function fileDownloadAPI(code) {
  return request({
    url: `/back-end/doc-file/download/${code}`,
    method: 'get',
    responseType: 'blob'
  })
}
