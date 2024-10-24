<template>
	<view class="container">
		<NavBar showBack title="重置密码"/>
		<view class="info-item">
			<view class="contacts">
				<text class="name">旧密码：</text>
				<input class="weui-input value" type="password" v-model="passwordOld"
					placeholder="请输入旧密码" />
			</view>
		</view>
		<view class="info-item">
			<view class="contacts">
				<text class="name">新密码：</text>
				<input class="weui-input value" type="password" v-model="password"
					placeholder="请输入新密码" />
			</view>
		</view>
		<view class="info-item">
			<view class="contacts">
				<text class="name">新密码确认：</text>
				<input class="weui-input value" type="password" v-model="passwordCopy"
					placeholder="请输入新密码确认" />
			</view>
		</view>
		<!-- 底部操作按钮 -->
		<view class="footer-fixed">
			<view class="btn-wrapper">
				<view class="btn-item btn-item-main" @click="handleSubmit()">保存</view>
			</view>
		</view>
	</view>
</template>

<script>
import * as UserApi from '@/api/user'
export default {
	components: {},
	data() {
		return {
			title: "",
			popType: 1,
			password: "",
			passwordCopy: "",
			passwordOld: ""
		}
	},
	methods: {
		// 输入密码
		handleValidfy(type) {
			const app = this;
			if (!app.password) {
				app.$error('新密码不能为空！');
				return false;
			}
			if (!app.passwordOld) {
				app.$error('旧密码不能为空！');
				return false;
			}
			if (!app.passwordCopy) {
				app.$error('确认密码不能为空！');
				return false;
			}
			if (app.password !== app.passwordCopy) {
				app.$error('新密码与确认密码输入不一致！');
				return false;
			}
			return true;
		},
		// 提交保存
		handleSubmit() {
			const app = this;
			if (app.handleValidfy()) {
				UserApi.resetPassword({
					password: app.password,
					originalPassword: app.passwordOld,
				}).then(result => {
					app.$success('保存成功！');
					app.password = '';
					app.passwordOld = '';
					app.passwordCopy = '';
				})
			}
		}
	}
}
</script>

<style lang="scss" scoped>
.info-list {
	padding-bottom: 30rpx;
	margin-top: 30rpx;
}

.info-item {
	margin: 20rpx;
	padding: 30rpx 40rpx;
	box-shadow: 0 1rpx 5rpx 0px rgba(0, 0, 0, 0.05);
	border-radius: 16rpx;
	background: #fff;

	.avatar-warp {
		line-height: 120rpx;
	}
}

.contacts {
	font-size: 30rpx;
	height: 40rpx;

	.name {
		margin-left: 0px;
		float: left;
		margin-right: 10rpx;
		line-height: 40rpx;
	}

	.value {
		float: right;
		color: #999999;
		text-align: right;
		font-size: 30rpx;

		.second {
			margin-left: .6rem;
		}
	}

	.vcode {
		float: left;
		line-height: 40rpx;
	}

	.password {
		text-align: right;
		float: left;
		padding-right: 5rpx;
	}

	.avatar {
		width: 120rpx;
		height: 120rpx;
		border-radius: 120rpx;
		border: solid 1px #cccccc;
		float: right;
	}
}

// 底部操作栏
.footer-fixed {
	z-index: 11;
	box-shadow: 0 -4rpx 40rpx 0 rgba(144, 52, 52, 0.1);
	margin-top: 80rpx;

	.btn-wrapper {
		height: 100%;
		display: flex;
		text-align: center;
		align-items: center;
		padding: 0 30rpx;
		margin-bottom: 10rpx;
	}

	.btn-item {
		flex: 1;
		font-size: 28rpx;
		height: 80rpx;
		line-height: 80rpx;
		text-align: center;
		color: #fff;
		border-radius: 40rpx;
	}

	.btn-item-main {
		background: linear-gradient(to right, #f9211c, #ff6335);
	}

	.btn-item-back {
		margin-top: 20rpx;
		background: #FFFFFF;
		border: 1px solid #268bd5;
		color: #666666;
	}

}
</style>