<template>
	<view class="container">
		<NavBar title="账号授权"/>
		<view class="wechatapp">
			<u-loading-icon size="190" mode="flower"></u-loading-icon>
		</view>
		<view class="auth-title">自动登录中，请稍后...</view>
		<view class="no-login-btn">
			<button class="button btn-normal" @click="handleRefresh">刷新登录</button>
		</view>
	</view>
</template>

<script>
import store from '@/store'
import * as LoginApi from '@/api/login'
export default {
	data() {
		return {
			code: '',
			needPhone: false
		}
	},
	mounted() {
		this.handleAuth()
	},
	methods: {
		handleAuth() {
			const app = this
			app.handleGetCode().then(code => {
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
				}).catch(res => {
					app.$error(res.msg)
				})
			})
		},
		handleGetCode() {
			return new Promise((resolve, reject) => {
				uni.login({
					provider: 'weixin',
					success: res => {
						resolve(res.code)
					},
					fail: reject
				})
			})
		},
		handleRefresh() {
			this.handleAuth();
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