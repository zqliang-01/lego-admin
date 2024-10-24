<template>
	<view>
		<view class="history-content">
			<u--text suffixIcon="photo" type="info" text="流程图"></u--text>
			<u--image width="100%" height="100%" :src="imageUrl" mode="widthFix" @click="handlePreview"></u--image>
		</view>
		<view class="history-content">
			<u--text suffixIcon="calendar" type="info" text="操作记录"></u--text>
			<u-steps v-if="commentList.length > 0" direction="column" :current="commentList.length">
				<view v-for="(item, index) in commentList" :key="index">
					<u-steps-item :title="item.content">
						<view slot="icon" class="history-item-icon" :style="{backgroundColor: handleIconColor(item.type)}">
							<u-icon size="30rpx" color="#ffffff" :name="handleIcon(item.type)"></u-icon>
						</view>
						<view class="history-item" slot="desc">
							<text class="type">{{item.type.name}}</text>
							<text class="time">{{item.createTime}}</text>
						</view>
					</u-steps-item>
				</view>
			</u-steps>
			<Empty v-else/>
		</view>
	</view>
</template>

<script>
import store from '@/store'
import { flowImageUrl } from '@/api/task'
export default {
	props: {
		taskId: String,
		commentList: {
			type: Array,
			default: () => {
				return []
			}
		}
	},
	computed: {
		imageUrl() {
			if (this.taskId) {
				return flowImageUrl + this.taskId + '?' + this.$tokenName + '=' + store.getters.token
			}
			return ''
		}
	},
  methods: {
    handleIconColor(type) {
      if (!type) {
        return '#487dff'
      }
      if (type.code === 'transfer') {
        return '#67C23A'
      }
      if (type.code === 'delegate') {
        return '#E6A23C'
      }
      if (type.code === 'reject') {
        return '#F56C6C'
      }
      return '#487dff'
    },
    handleIcon(type) {
      if (!type) {
        return 'checkbox-mark'
      }
      if (type.code === 'transfer') {
        return 'thumb-up'
      }
      if (type.code === 'delegate') {
        return 'chat'
      }
      if (type.code === 'reject') {
        return 'close'
      }
      return 'checkbox-mark'
    },
		handlePreview() {
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
		}
  }
}
</script>
<style lang="scss" scoped>
.history-content {
	padding: 20rpx 40rpx;
	position: relative;
	margin: 30rpx 20rpx;
	background-color: $uni-bg-color;
	border-radius: $uni-border-radius-infos;
	box-shadow: 0rpx 2rpx 10rpx 2rpx rgba(0,122,255,0.08);
}
.history-item {
	display: inline-block;
	font-size: 13px;
	.type {
		margin-right: 20rpx;
		color: $u-primary;
	}
	.time {
		color: $u-info;
	}
}
.history-item-icon {
	width: 30rpx;
	height: 30rpx;
	border-radius: 100px;
	line-height: 30rpx;
	text-align: center;
	padding: 10rpx;
}
</style>