<template>
	<view class="container">
		<view v-if="dataList.length > 0" class="file-list">
			<view v-for="(item,index) in dataList" :key="index" class="file-list__item">
				<view class="left" @click="handleChoose(item)">
					<u-icon class="icon" size="15" name="file-text-fill"></u-icon>
					<text class="text">{{item.name}}</text>
				</view>
				<view class="right">
					<text>{{item.size}}</text>
				</view>
			</view>
		</view>
		<Empty v-else/>
	</view>
</template>

<script>
import store from '@/store'
import * as FileAPI from '@/api/file'
import { filePreviewUrl } from '@/api/upload'
import { fileSize } from '@/utils/util'
export default {
	props: {
		type: String,
		detailCode: String,
		menuCode: String
	},
	data() {
		return {
			dataList: []
		}
	},
	watch: {
		type: {
			handler(val) {
				if (val === 'file') {
					this.init()
				}
			},
			immediate: true
		}
	},
	methods: {
		init() {
			const app = this
			FileAPI.list({
        entityCode: app.detailCode,
        permissionCode: app.menuCode
			}).then(res => {
				app.dataList = res.data
        app.dataList = res.data.map(item => {
          item.size = fileSize(item.size)
          return item
        })
			})
		},
		handleChoose(data) {
			const app = this;
			uni.showModal({
				title: '提示：',
				content: data.name,
				confirmText: data.type.code === 'image' ? '预览' : '下载',
				success: function(res) {
					if (res.confirm) {
						app.handleFile(data)
					}
				}
			});
		},
		handleFile(data) {
			const app = this
			const fileUrl = filePreviewUrl + data.code
			if (data.type.code === 'image') {
				uni.previewImage({
					urls: [fileUrl + '?' + app.$tokenName + '=' + store.getters.token],
					current: fileUrl + '?' + app.$tokenName + '=' + store.getters.token,
					fail() {
						uni.$u.toast('预览图片失败')
					},
				});
			} else {
				uni.downloadFile({
					url: fileUrl,
					success: (result) => {
						// #ifdef H5
						const a = document.createElement('a');
						a.href = result.tempFilePath;
						a.download = data.name;
						a.click();
						// #endif
						//  #ifdef MP-WEIXIN || APP-PLUS
						uni.getFileSystemManager().saveFile({
							tempFilePath: result.tempFilePath,
							filePath: uni.env.USER_DATA_PATH + '/downloads/',
							success: (res) => {
								app.$toast('保存成功:' + res.savedFilePath)
							},
							fail: (err) => {
								app.$toast('保存失败')
							}
						})
						// #endif	
					}
				})
			}
		}
	}
}
</script>

<style lang="scss" scoped>
.container {
	padding: 20rpx 40rpx;
	position: relative;
	margin: $uni-ios-margin;
	margin-top: 0rpx;
	background-color: $uni-bg-color;
	border-radius: $uni-border-radius-infos;
	box-shadow: 0rpx 2rpx 10rpx 2rpx rgba(0,122,255,0.08);
	.file-list {
		&__item {
			display: flex;
			padding: $uni-spacing-row-base 0rpx;
			border-bottom: 1px solid $uni-ios-gray;
			.left {
				flex: 1;
				display: flex;
				overflow: hidden;
				font-size: 35rpx;
				line-height: 38rpx;
				margin-right: $uni-spacing-row-sm;
				.icon {
					margin-right: 10rpx;
				}
				.text {
					overflow: hidden;
					white-space: nowrap;
					text-overflow: ellipsis;
					color: $uni-color-primary;
					text-decoration: underline;
				}
			}
			.right {
				width: 130rpx;
				text-align: right;
				font-size: 30rpx;
				line-height: 38rpx;
			}
		}
	}
}
</style>
