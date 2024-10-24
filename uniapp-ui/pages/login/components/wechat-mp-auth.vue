<template>
	<view class="container">
		<view class="wechatapp">
			<view class="header">
        <open-data class="avatar" type="userAvatarUrl"></open-data>
			</view>
		</view>
		<view class="auth-title">申请获取以下权限</view>
		<view v-if="needPhone">
			<view class="auth-subtitle">获得您微信绑定的手机号码</view>
			<view class="login-btn">
				<button class="button-mobile btn-primary" open-type="getPhoneNumber"
					@getphonenumber="getPhoneNumber">授权手机号</button>
			</view>
		</view>
		<view v-if="!needPhone">
			<view class="auth-subtitle">获得你的公开信息（昵称、头像等）</view>
			<view class="login-btn">
				<button class="button btn-normal" @click.stop="getUserProfile">授权登录</button>
			</view>
		</view>
		<view class="no-login-btn">
			<button class="button btn-normal" @click="cancelLogin">暂不授权</button>
		</view>
	</view>
</template>

<script>
import store from '@/store'
import * as UserApi from '@/api/user'
import * as LoginApi from '@/api/login'
export default {
	data() {
		return {
			code: '',
			needPhone: false
		}
	},
	created() {
	},
	methods: {
		// 获取微信用户信息
		getUserProfile() {
			const app = this;
			wx.canIUse('getUserProfile') && wx.getUserProfile({
				lang: 'zh_CN',
				desc: '获取用户相关信息',
				success() {
					app.onAuthSuccess();
				},
				fail(res) {
					console.log('登录授权失败，请设置小程序隐私保护协议，返回信息： ', res);
					app.$error("抱歉，登录授权失败.");
				}
			})
		},
		// 获取微信绑定的手机号
		getPhoneNumber(e) {
			if (e.detail.errMsg == "getPhoneNumber:ok") {
				this.onAuthSuccess({
					"type": "phone",
					"encryptedData": e.detail.encryptedData,
					"iv": e.detail.iv,
					"sessionKey": e.detail.iv
				})
			} else {
				this.$toast("抱歉，获取手机号码失败：" + e.detail.errMsg);
			}
		},
		onAuthSuccess() {
			const app = this
			app.$emit('success', {
				oauth: 'MP-WEIXIN',
				openId: result.data
			})
		},
		cancelLogin() {
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

		.header {
			width: 190rpx;
			height: 190rpx;
			border: 4rpx solid #fff;
			margin: 0 auto 0;
			border-radius: 50%;
			overflow: hidden;
			box-shadow: 2rpx 0 10rpx rgba(50, 50, 50, 0.3);
			image {
				width: 190rpx;
				height: 190rpx;
			}
		}
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

		.button-mobile {
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