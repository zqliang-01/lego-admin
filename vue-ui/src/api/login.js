import request from '@/utils/request'

export function loginAPI(params) {
  return request({
    url: '/back-end/sys-index/login',
    method: 'post',
    data: params
  })
}

export function systemInitAPI() {
  return request({
    url: '/back-end/sys-index/init',
    method: 'post'
  })
}

export function logoutAPI() {
  return request({
    url: '/back-end/sys-index/logout'
  })
}

export function getCodeImg() {
  return request({
    url: '/back-end/sys-index/captchaImage'
  })
}
