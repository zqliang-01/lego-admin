import store from '@/store'
import config from '@/config'
import storage from '@/utils/storage'
import request from './request'
import { isWechat } from '../app';

// 后端api地址
const baseURL = config.apiUrl;
const appCode = config.appCode;

// 可以new多个request来支持多个域名请求
const $http = new request({
  // 接口请求地址
  baseUrl: baseURL,
  // 服务器本地上传文件地址
  fileUrl: baseURL,
  // 服务器上传图片默认url
  defaultUploadUrl: 'back-end/sys-file/upload',
  // 设置请求头（如果使用报错跨域问题，可能是content-type请求类型和后台那边设置的不一致）
  header: {},
  // 请求超时时间, 单位ms（默认300000）
  timeout: 300000,
  // 默认配置（可不写）
  config: {
    // 是否自动提示错误
    isPrompt: true,
    // 是否显示加载动画
    load: true,
    // 是否使用数据工厂
    isFactory: true
  }
})

// 当前接口请求数
let requestNum = 0
// 请求开始拦截器
$http.requestStart = options => {
  if (options.load) {
    if (requestNum <= 0) {
      // 打开加载动画
      uni.showLoading({
        title: '加载中',
        mask: true
      })
    }
    requestNum += 1
  }
  // 图片上传大小限制
  if (options.method == "FILE" && options.maxSize) {
    // 文件最大字节: options.maxSize 可以在调用方法的时候加入参数
    const maxSize = options.maxSize
    for (let item of options.files) {
      if (item.size > maxSize) {
        setTimeout(() => {
          uni.showToast({
            title: "图片过大，请重新上传",
            icon: "none"
          })
        }, 10)
        return false
      }
    }
  }
  const flag = options.header['Content-Type'] && options.header['Content-Type'].indexOf('application/x-www-form-urlencoded;charset=UTF-8') !== -1
  if (!flag) {
		options.header['Content-Type'] = 'application/json'
  }
	options.header['appCode'] = appCode;
  options.header['isWechat'] = isWechat() ? 'Y' : 'N';
  options.header['platform'] = store.getters.platform ? String(store.getters.platform) : '';
	options.header[config.tokenName] = store.getters.token ? String(store.getters.token) : '';
  return options
}

// 请求结束
$http.requestEnd = options => {
  // 判断当前接口是否需要加载动画
  if (options.load) {
    requestNum = requestNum - 1
    if (requestNum <= 0) {
      uni.hideLoading()
    }
  }
}

// 登录弹窗次数
let loginPopupNum = 0
$http.dataFactory = async res => {
  // console.log("接口请求数据", {
  //   url: res.url,
  //   resolve: res.response,
  //   header: res.header,
  //   data: res.data,
  //   method: res.method,
  // })
  if (!res.response.statusCode || res.response.statusCode != 200) {
    return Promise.reject({
      code: res.response.statusCode,
      msg: '响应异常，异常编码[' + res.response.statusCode + ']异常原因[' + res.response.errMsg + ']'
    })
  }

  let httpData = res.response.data
  if (typeof httpData == "string") {
    try {
      httpData = JSON.parse(httpData)
    } catch {
      httpData = false
    }
  }
  if (httpData === false || typeof httpData !== 'object') {
    return Promise.reject({
      code: 9999,
      msg: "返回数据验证不通过"
    })
  }
	if (httpData.code !== 200) {
		if (httpData.code === 1000) {
		  if (loginPopupNum <= 0) {
		    loginPopupNum++
		    uni.showModal({
		      title: '温馨提示',
		      content: '此时此刻需要您登录喔~',
		      confirmText: "去登录",
		      cancelText: "再逛会",
		      success: res => {
		        loginPopupNum--
		        if (res.confirm) {
							store.dispatch('Logout')
		          uni.navigateTo({
		            url: "/pages/login/index"
		          })
		        }
		      }
		    })
		  }
		} else if (res.isPrompt) {
			setTimeout(() => {
				uni.showToast({
					title: httpData.msg,
					icon: "none",
					duration: 2500
				}, 10)
			})
		}
		return Promise.reject(httpData)
	}
	return Promise.resolve(httpData)
}
export default $http
