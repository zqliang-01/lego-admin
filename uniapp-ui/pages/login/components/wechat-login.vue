<template>
	<view class="container">
		<NavBar title="账号授权"/>
		<view class="wechatapp">
			<u-loading-icon size="190" mode="flower"></u-loading-icon>
		</view>
		<view class="auth-title">自动登录中，请稍后...</view>
		<view class="no-login-btn">
			<button class="button btn-normal" @click="handleCancel">取消登录</button>
		</view>
	</view>
</template>

<script>
import store from '@/store'
import * as LoginApi from '@/api/login'
export default {
	data() {
		return {
			code: ''
		}
	},

	/**
	 * 生命周期函数--监听页面显示
	 */
	onLoad(options) {
		if (options.code) {
			this.code = options.code;
		} else {
			this.code = this.getQueryVariable('code');
		}
		this.doLogin();
	},

	methods: {
		doLogin() {
			const app = this;
			if (!app.code) {
				app.$toast("抱歉，授权失败，未获取到认证code信息！");
				return false;
			}
			store.dispatch('WechatMpLogin', code).then(response => {
				if (response.needLogin) {
					setTimeout(() => {
						app.$emit('passwordLogin', {
							oauth: 'MP-WEIXIN',
							openid: response.openid
						})
					}, 500)
					return
				}
				app.$toast("授权登陆成功")
				setTimeout(() => {
					app.$emit('success', {
						oauth: 'MP-WEIXIN',
						openid: response.openid
					})
				}, 500)
			});
		},
		getQueryVariable(variable) {
			const query = window.location.search.substring(1);
			const vars = query.split("&");
			for (let i = 0; i < vars.length; i++) {
				let pair = vars[i].split("=");
				if (pair[0] == variable) {
					return pair[1];
				}
			}
			return (false);
		},
		handleCancel() {
			uni.navigateBack({
				delta: Number(1)
			})
		}
	}
}
</script>

<style lang="scss" scoped>
.container {
	padding: 0 60rpx;
	font-size: 32rpx;
	background: #fff;
	min-height: 100vh;
}

.wechatapp {
	padding: 80rpx 0 48rpx;
	border-bottom: 1rpx solid #e3e3e3;
	margin-bottom: 72rpx;
	text-align: center;
}

.auth-title {
	color: #585858;
	font-size: 34rpx;
	margin-bottom: 40rpx;
}

.auth-subtitle {
	color: #888;
	margin-bottom: 88rpx;
	font-size: 28rpx;
}

.login-btn {
	padding: 0 20rpx;

	.button {
		height: 88rpx;
		line-height: 88rpx;
		background: #268bd5;
		color: #fff;
		font-size: 30rpx;
		border-radius: 12rpx;
		text-align: center;
	}
}

.no-login-btn {
	margin-top: 24rpx;
	padding: 0 20rpx;

	.button {
		height: 88rpx;
		line-height: 88rpx;
		background: #dfdfdf;
		color: #fff;
		font-size: 30rpx;
		border-radius: 12rpx;
		text-align: center;
	}
}
</style>