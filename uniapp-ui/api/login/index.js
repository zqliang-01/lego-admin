import request from '@/utils/request'

const api = {
  login: '/back-end/sys-index/login',
  logout: '/back-end/mobile-login/logout',
  captcha: '/back-end/sys-index/captchaImage',
  wechatMpLogin: '/back-end/mobile-login/wechat-mp',
  wechatLogin: '/back-end/mobile-login/wechat'
}

// 用户登录
export function login(data) {
  return request.post(api.login, data, {
	  header: {
		  'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
	  }
  })
}

export function logout(data) {
  return request.get(api.logout)
}

// 微信小程序快捷登录
export function wechatMpLogin(code) {
  return request.get(api.wechatMpLogin + '/' + code)
}

// 微信公众号授权
export function wechatLogin(code) {
  return request.get(api.wechatLogin + '/' + code)
}

// 图形验证码
export function captcha() {
  return request.get(api.captcha)
}
