import store from '../store'
import * as util from './util'
import * as MessageAPI from '@/api/message';

/**
 * 获取当前运行的终端(App H5 小程序)
 * https://uniapp.dcloud.io/platform
 */
export const getPlatform = () => {
	// #ifdef APP-PLUS
	const platform = 'App'
	// #endif
	// #ifdef APP-PLUS-NVUE
	const platform = 'App'
	// #endif
	// #ifdef H5
	const platform = 'H5'
	// #endif
	// #ifdef MP-WEIXIN
	const platform = 'MP-WEIXIN'
	// #endif
	// #ifdef MP-ALIPAY
	const platform = 'MP-ALIPAY'
	// #endif
	// #ifdef MP-BAIDU
	const platform = 'MP-BAIDU'
	// #endif
	// #ifdef MP-TOUTIAO
	const platform = 'MP-TOUTIAO'
	// #endif
	// #ifdef MP-QQ
	const platform = 'MP-QQ'
	// #endif
	// #ifdef MP-360
	const platform = 'MP-360'
	// #endif
	return platform;
}

/**
 * 显示成功提示框
 */
export const showSuccess = (msg, callback) => {
	uni.showToast({
		title: msg,
		icon: 'success',
		mask: true,
		duration: 1500,
		success() {
			callback && callback()
		}
	})
}

/**
 * 显示失败提示框
 */
export const showError = (msg, callback) => {
	uni.showModal({
		title: '友情提示',
		content: msg,
		showCancel: false,
		success(res) {
			callback && callback()
		}
	})
}

/**
 * 显示纯文字提示框
 */
export const showToast = msg => {
	uni.showToast({
		title: msg,
		icon: 'none'
	})
}

/**
 * tabBar页面路径列表 (用于链接跳转时判断)
 * tabBarLinks为常量, 无需修改
 */
export const getTabBarLinks = () => {
	const tabBarLinks = [
		'pages/index/index',
    'pages/notice/index',
    'pages/user/index'
	]
	return tabBarLinks
}

/**
 * 生成转发的url参数
 */
export const getShareUrlParams = (params) => {
	let path = util.urlEncode({
		spm: store.getters.userId, // 推荐人ID
		...params
	})
	console.log('转发url : ', path);
	return path;
}

/**
 * 跳转到指定页面url
 * 支持tabBar页面
 * @param {string}  url
 * @param {object}  query
 */
export const navTo = (url, query = {}) => {
	if (!url || url.length == 0) {
		return false
	}
	// tabBar页面, 使用switchTab
	if (util.inArray(url, getTabBarLinks())) {
		uni.switchTab({
			url: `/${url}`
		})
		return true
	}
	// 生成query参数
	const queryStr = !util.isEmpty(query) ? '?' + util.urlEncode(query) : ''
	// 普通页面, 使用navigateTo
	uni.navigateTo({
		url: `/${url}${queryStr}`
	})
	return true
}

/**
 * 该方法只能在tabbar页面中调用, 其他页面调用会报错
 */
export const setMessageTabBadge = (messageTotal, tabbarIndex = 2) => {
	if (messageTotal > 0) {
		uni.setTabBarBadge({
			index: tabbarIndex,
			text: `${messageTotal}`
		})
	} else {
		uni.removeTabBarBadge({
			index: tabbarIndex
		})
	}
}

/**
 * 验证是否已登录
 */
export const checkLogin = () => {
	const userId = store.getters.userId
	if (userId && !util.isEmpty(userId)) {
		return true;
	}
	return false;
}

/**
 * 发起支付请求
 * @param {Object} 参数
 */
export const wxPayment = (option) => {
	const options = {
		timeStamp: '',
		nonceStr: '',
		prepay_id: '',
		paySign: '',
		...option
	}
	// 微信内浏览器支付
	if (isWechat()) {
		return new Promise((resolve, reject) => {
			wxH5Payment(options,
				function(res) {
					resolve(res);
				},
				function(err) {
					reject(err);
				});
		})
	}

	// H5支付
	if (getPlatform() == 'H5' && option.mweb_url) {
		h5Pay(option.mweb_url, option.backUrl);
		return true;
	}

	// 微信小程序支付
	return new Promise((resolve, reject) => {
		uni.requestPayment({
			provider: 'wxpay',
			timeStamp: options.timeStamp,
			nonceStr: options.nonceStr,
			'package': options.package,
			signType: 'MD5',
			paySign: options.paySign,
			success: res => resolve(res),
			fail: res => reject(res)
		})
	})
}

export const isWechat = () => {
	try {
		const ua = window.navigator.userAgent.toLowerCase();
		if (ua.match(/micromessenger/i) == 'micromessenger') {
			return true;
		} else {
			return false;
		}
	} catch (e) {
		return false;
	}
}

export const wxH5Payment = (data, callback_succ_func, callback_error_func) => {
	if (!isWechat()) {
		return false;
	}
	if (typeof WeixinJSBridge == "undefined") {
		if (document.addEventListener) {
			document.addEventListener('WeixinJSBridgeReady', jsApiCall, false);
		} else if (document.attachEvent) {
			document.attachEvent('WeixinJSBridgeReady', jsApiCall);
			document.attachEvent('onWeixinJSBridgeReady', jsApiCall);
		}
	} else {
		jsApiCall(data, callback_succ_func, callback_error_func);
	}
}

export const jsApiCall = (data, callback_succ_func, callback_error_func) => {
	//使用原生的，避免初始化appid问题  
	WeixinJSBridge.invoke('getBrandWCPayRequest', {
			appId: data['appId'],
			timeStamp: data['timeStamp'],
			nonceStr: data['nonceStr'], // 支付签名随机串，不长于 32 位  
			package: data['package'], // 统一支付接口返回的prepay_id参数值，提交格式如：prepay_id=\*\*\*）  
			signType: data['signType'], // 签名方式，默认为'SHA1'，使用新版支付需传入'MD5'  
			paySign: data['paySign'], // 支付签名  
		},
		function(res) {
			var msg = res.err_msg ? res.err_msg : res.errMsg;
			//WeixinJSBridge.log(msg);  
			switch (msg) {
				case 'get_brand_wcpay_request:ok': //支付成功时  
					if (callback_succ_func) {
						callback_succ_func(res);
					}
					break;
				default: //支付失败时  
					WeixinJSBridge.log('支付失败!' + msg + ',请返回重试.');
					if (callback_error_func) {
						callback_error_func({
							msg: msg
						});
					}
					break;
			}
		})
}

export const h5Pay = (url, backUrl) => {
	// 设置回跳地址，支付完成之后回跳到哪
	let redirectUrl = '&redirect_url=' + encodeURIComponent(backUrl);
	// 拼接上回跳地址
	let linkUrl = url + redirectUrl
	const system = uni.getSystemInfoSync()
	if (system.platform == 'ios') {
		// 如果是iOS平台，使用location.href，iOS里面限制了window.open的使用。
		window.location.href = linkUrl;
	} else {
		window.open(linkUrl);
	}
}