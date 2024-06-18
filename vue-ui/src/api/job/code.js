import request from '@/utils/request'

export function codeGetAPI(data) {
  return request({
    url: '/back-end/job-code/index/' + data
  })
}

export function codeSaveAPI(data) {
  return request({
    url: '/back-end/job-code/save',
    method: 'post',
    data: data
  })
}
