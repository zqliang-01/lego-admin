<template>
	<view class="container">
		<NavBar v-if="platform !== 'H5'" title="账号登录" :show-message="false" />
		<view class="login-logo">
			<view class="logo">
				<image src="/static/logo.png" mode=""></image>
			</view>
			<view class="title">
				<text>LegoAdmin低代码开发平台</text>
			</view>
		</view>
		<view class="login-form">
			<!-- 手机号 -->
			<view class="form-item">
				<text class="iconfont icon-sy-yh"></text>
				<input class="form-item--input uni-input" type="text" v-model="account" maxlength="30" clearable="true"
					placeholder="请输入您的用户名" />
			</view>
			<!-- 密码 -->
			<view class="form-item">
				<text class="iconfont icon-suo"></text>
				<input class="form-item--input" type="password" autocomplete="off" v-model="password" maxlength="30"
					minlength="1" value="" placeholder="请输入您的密码" />
			</view>
			<!-- 图形验证码 -->
			<view class="form-item">
				<text class="iconfont icon-tuxingyanzhengma"></text>
				<input class="form-item--input" type="text" v-model="captchaCode" maxlength="5" placeholder="请输入图形验证码" />
				<view class="form-item--parts">
					<view class="captcha" @click="getCaptcha()">
						<image class="image" :src="captcha"></image>
					</view>
				</view>
			</view>
			<view class="login-button" @click="handleSubmit"><text>立即登录</text></view>
		</view>
	</view>
</template>

<script>
import store from '@/store'
import * as LoginApi from '@/api/login'
import {
	throttle,
	debounce
} from '@/utils/util'
import { isEmpty } from '@/utils/verify'
import { isWechat } from '@/utils/app'

export default {
	props: {
		openid: String
	},
	data() {
		return {
			// 正在加载
			isLoading: false,
			// 图形验证码信息
			captcha: "",
			// 图形验证码uuid
			captchaUuid: "",
			// 账号图形验证码信息
			captchaForAccount: "",
			// 账号
			account: 'test',
			// 密码
			password: 'test123',
			// 图形验证码
			captchaCode: ''
		}
	},
	created() {
		this.getCaptcha();
	},
	computed: {
		platform() {
			return store.getters.platform
		}
	},
	methods: {
		getCaptcha() {
			const app = this
			LoginApi.captcha().then(result => {
				app.captcha = 'data:image/gif;base64,' + result.data.image;
				app.captchaUuid = result.data.token;
				app.captchaForAccount = result.data.code;
			})
		},
		formValidation() {
			const app = this
			if (!app.validteAccount(app.account)
				|| !app.validtePassword(app.password)
				|| !app.validteCaptchaCode(app.captchaCode)) {
				return false;
			}
			const platform = store.getters.platform
			if (platform !== 'H5' && isEmpty(app.openid)) {
				this.$toast('用户未授权，请先授权后再登陆！')
				return false;
			}
			if (platform === 'H5' && isWechat() && isEmpty(app.openid)) {
				this.$toast('用户未授权，请先授权后再登陆！')
				return false;
			}
			return true;
		},
		validteAccount(str) {
			if (isEmpty(str)) {
				this.$toast('请先输入您的用户名')
				return false
			}
			if (str.length < 4) {
				this.$toast('用户名不能少于4位')
				return false
			}
			return true
		},
		validtePassword(str) {
			if (isEmpty(str)) {
				this.$toast('请先输入您的密码')
				return false
			}
			if (str.length < 6) {
				this.$toast('密码不能少于6位')
				return false
			}
			return true
		},
		validteCaptchaCode(str) {
			if (isEmpty(str)) {
				this.$toast('请先输入图形验证码')
				return false
			}
			if (this.captchaForAccount.toLowerCase() !== this.captchaCode.toLowerCase()) {
				this.$toast('验证码不一致，请重新输入')
				return false
			}
			return true
		},
		handleSubmit() {
			const app = this
			if (!app.isLoading && app.formValidation()) {
				app.submitLogin();
			}
			return true
		},
		submitLogin() {
			const app = this
			app.isLoading = true
			store.dispatch('Login', {
				username: app.account,
				password: app.password,
				code: app.captchaCode,
				token: app.captchaUuid
			}).then(result => {
				app.$emit('success', app.openid)
			}).finally(() => app.isLoading = false)
		}
	}
}
</script>

<style lang="scss" scoped>
.container {
	padding: 160rpx 60rpx 100rpx 60rpx;
	min-height: 100vh;
	background-color: #fff;

	.fast-icon {
		margin-bottom: 80rpx;
		font-size: 50rpx;
		cursor: pointer;
	}
}

.login-logo {
	margin: 50rpx 0rpx;
	text-align: center;

	.logo {
		display: inline-block;
		background-color: #fff;
		padding: 10rpx;
		margin: 0 auto;
		margin-top: -110rpx;
		border-radius: 40rpx;
		box-shadow: -2rpx 0rpx 6rpx 2rpx rgba(#bababd, 0.3);
		text-align: center;
		overflow: hiddem;

		image {
			display: block;
			margin: 0;
			padding: 0;
			width: 160rpx;
			height: 160rpx;
			border-radius: 30rpx;
		}
	}

	.title {
		margin-top: 30rpx;
		font-size: 38rpx;
	}
}

// 输入框元素
.form-item {
	display: flex;
	padding: 18rpx;
	border-bottom: 2rpx solid #cccccc;
	margin-bottom: 20rpx;
	height: 110rpx;
	align-items: center;
	justify-content: center;

	&--input {
		font-size: 32rpx;
		letter-spacing: 1rpx;
		flex: 1;
		height: 100%;
		padding-left: 5rpx;
	}

	&--parts {
		min-width: 100rpx;
		height: 100%;
	}

	// 图形验证码
	.captcha {
		height: 100%;

		.image {
			display: block;
			width: 192rpx;
			height: 80rpx;
		}
	}
}

// 登录按钮
.login-button {
	width: 96%;
	height: 86rpx;
	margin: 0 auto;
	margin-top: 60rpx;
	background: #268bd5;
	text-align: center;
	line-height: 86rpx;
	color: #fff;
	border-radius: 80rpx;
	box-shadow: 0px 10px 20px 0px rgba(0, 0, 0, 0.1);
	letter-spacing: 5rpx;
	cursor: pointer;
}

</style>