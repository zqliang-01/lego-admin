<template>
	<view class="container">
		<view class="history-content">
			<u-steps v-if="dataList.length > 0" direction="column" :current="dataList.length" dot>
				<view v-for="(item, index) in dataList" :key="index">
					<u-steps-item :title="item.description">
						<view class="history-item" slot="desc">
							<text class="type">{{item.operator.name}}</text>
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
import * as logAPI from '@/api/log'
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
				if (val === 'log') {
					this.init()
				}
			},
			immediate: true
		}
	},
  methods: {
		init() {
			const app = this
			logAPI.listEntity({
        entityCode: app.detailCode,
        permissionCode: app.menuCode
			}).then(res => {
				app.dataList = res.data
			})
		}
  }
}
</script>
<style lang="scss" scoped>
.container {
	padding-bottom: 10rpx;
	.history-content {
		padding: 20rpx 40rpx;
		position: relative;
		margin: $uni-ios-margin;
		margin-top: 0rpx;
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
}
</style>