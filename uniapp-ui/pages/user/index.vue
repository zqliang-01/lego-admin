<template>
	<view>
		<NavBar :showBack="false" title="我的信息"/>
		<view class="info-list">
			<view class="info-item">
				<view class="contacts avatar-warp">
					<text class="name">头像</text>
					<image v-if="form.imageCode" class="avatar" @click="handleChooseImage" :src="avatarUrl" />
					<view v-else class="text-name" @click="handleChooseImage">{{ userName }}</view>
				</view>
			</view>
			<view class="info-item">
				<view class="contacts">
					<text class="name">账号</text>
					<text class="text">{{ userInfo.code }}</text>
				</view>
			</view>
			<view class="info-item">
				<view class="contacts">
					<text class="name">称呼</text>
					<input class="value" type="nickname" @blur="getnickname" v-model="form.name" placeholder="请输入称呼" />
				</view>
			</view>
			<view class="info-item">
				<view class="contacts">
					<text class="name">密码</text>
					<view class="value" @click="handleChangePassword">
						<text class="password">********</text>
						<text class="text">修改</text>
					</view>
				</view>
			</view>
			<view class="info-item">
				<view class="contacts">
					<text class="name">部门</text>
					<text class="value">{{ deptName }}</text>
				</view>
			</view>
			<view class="info-item">
				<view class="contacts">
					<text class="name">角色</text>
					<text class="value">{{ roleName }}</text>
				</view>
			</view>
		</view>
		<!-- 底部操作按钮 -->
		<view class="footer-fixed">
			<view class="btn-wrapper">
				<view class="btn-item btn-item-main" @click="handleSave">保存信息</view>
			</view>
			<view class="btn-wrapper">
				<view class="btn-item btn-item-out" @click="handleLogout">退出登录</view>
			</view>
		</view>
	</view>
</template>

<script>
import store from '@/store'
import * as UserApi from '@/api/user'
import * as UploadApi from '@/api/upload'
import { filePreviewUrl } from '@/api/upload'
export default {
	components: {
	},
	data() {
		return {
			userInfo: {},
			form: {
				name: '',
				imageCode: ''
			}
		}
	},
	computed: {
		avatarUrl() {
			if (this.form.imageCode) {
				return filePreviewUrl + this.form.imageCode + '?' + this.$tokenName + '=' + store.getters.token
			}
			return ''
		},
		roleName() {
			if (this.userInfo && this.userInfo.roles) {
				return this.userInfo.roles.map(r => r.name).join(',')
			}
			return ''
		},
		deptName() {
			if (this.userInfo && this.userInfo.dept) {
				return this.userInfo.dept.name
			}
			return ''
		},
		userName() {
			const name = this.userInfo.name
			if (name) {
				return name.slice(name.length - 2)
			}
			return name
		}
	},
	onShow(options) {
		this.getPageData()
	},
	methods: {
		getPageData(callback) {
			const app = this
			UserApi.info().then(result => {
				app.userInfo = result.data
				app.form.name = result.data.name
				app.form.imageCode = result.data.imageCode
				uni.stopPullDownRefresh()
			})
		},
		onPullDownRefresh() {
			this.getPageData();
		},
		getnickname(e) {
			this.form.name = e.detail.value;  
		}, 
		handleChooseImage() {
			const app = this
			uni.chooseImage({
				count: 1,
				sizeType: ['original', 'compressed'], // 可以指定是原图还是压缩图，默认二者都有
				sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
				success({ tempFiles }) {
					const imageList = tempFiles;
					return new Promise((resolve, reject) => {
						if (imageList.length > 0) {
							UploadApi.image(imageList).then(files => {
								if (files && files.length > 0) {
									app.form.imageCode = files[0].data
								}
								resolve(files)
							}).catch(err => reject(err))
						} else {
							resolve()
						}
					})
				}
			});
		},
		handleChangePassword() {
			this.$navTo('pages/user/resetPassword');
		},
		handleSave() {
			const app = this
			UserApi.modify(this.form).then(() => {
				app.$toast("修改成功")
			})
		},
		handleLogout() {
			const app = this
			store.dispatch('Logout').then(() => {
				app.$toast("注销成功")
				app.$navTo('pages/login/index')
			})
		}
	}
}
</script>

<style lang="scss" scoped>
.info-list {
	padding: 0rpx 20rpx 20rpx 20rpx;
	margin-top: 25rpx;
}
.info-item {
	margin: 20rpx auto 20rpx auto;
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
	.name {
		margin-left: 0px;
	}
	.value {
		float: right;
		color: #999999;
		text-align: right;
		font-size: 30rpx;
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
		max-width: calc(100% - 80rpx);
		.second {
			margin-left: .6rem;
		}
	}
	.text {
		color: #00aaff;
		margin-left: 2px;
		float: right;
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
		max-width: calc(100% - 80rpx);
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
	.text-name {
		display: block;
		float: right;
		width: 120rpx;
		height: 120rpx;
		background-color: $uni-color-primary;
		border-radius: 50%;
		text-align: center;
		line-height: 120rpx;
		font-size: 32rpx;
		color: $uni-ios-white;
	}
}
.item-option {
	display: flex;
	justify-content: space-between;
	height: 48rpx;
}
.footer-fixed {
	padding-bottom: 20rpx;
	.btn-wrapper {
		height: 100%;
		align-items: center;
		padding: 0 20rpx;
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
	.btn-item-out {
		margin-top: 20rpx;
		background: #FFFFFF;
		border: 1px solid #268bd5;
		color: #666666;
	}
}
</style>