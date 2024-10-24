<template>
	<view>
		<u-form-item
			borderBottom
			:prop="item.fieldCode"
			:rules="item.rules"
			:label="item.name"
			:required="item.required">
			<image v-if="imageUrl" class="image-name" @click="handleChooseImage" :src="imageUrl"/>
			<view v-else class="image-name" @click="handleChooseImage"></view>
			<u--text
				v-if="imageUrl"
				prefixIcon="eye"
				:iconStyle="{color: '#3c9cff', margin: '10rpx'}"
				type="primary"
				text="查看"
				@click="handleView"></u--text>
		</u-form-item>
	</view>
</template>

<script>
import store from '@/store'
import Mixin from '../mixin'
import * as UploadApi from '@/api/upload'
import { filePreviewUrl } from '@/api/upload'
export default {
  mixins: [Mixin],
	data() {
		return {
		}
	},
	computed: {
		imageUrl() {
			const imageCode = this.fieldForm[this.item.fieldCode]
			if (imageCode) {
				return filePreviewUrl + imageCode + '?' + this.$tokenName + '=' + store.getters.token
			}
			return ''
		}
	},
	methods: {
		handleView() {
			const app = this
			if (app.imageUrl) {
				uni.previewImage({
					urls: [app.imageUrl],
					current: app.imageUrl,
					fail() {
						uni.$u.toast('预览图片失败')
					},
				});
			}
		},
		handleChooseImage() {
			const app = this
			if (this.disabled || this.item.disabled) {
				return;
			}
			uni.chooseImage({
				count: 1,
				sizeType: ['original', 'compressed'],
				sourceType: ['album', 'camera'],
				success({ tempFiles }) {
					const imageList = tempFiles;
					return new Promise((resolve, reject) => {
						if (imageList.length > 0) {
							UploadApi.image(imageList).then(files => {
								if (files && files.length > 0) {
									app.commonChange(app.item, files[0].data)
								}
								resolve(files)
							}).catch(err => reject(err))
						} else {
							resolve()
						}
					})
				}
			});
		}
	}
}
</script>
<style lang="scss" scoped>
.avatar {
	width: 120rpx;
	height: 120rpx;
	border: solid 1px #cccccc;
}
.image-name {
	display: block;
	float: right;
	width: 120rpx;
	height: 120rpx;
	text-align: center;
	line-height: 120rpx;
	font-size: 32rpx;
	border: solid 1px #cccccc;
}
</style>