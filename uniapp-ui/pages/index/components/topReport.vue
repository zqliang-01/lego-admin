<template>
	<view class="item-box lego-box" v-loading="loading">
		<view class="item-box-title">
			<text>{{ data.name }}</text>
		</view>
		<view class="item-box-content">
			<u-grid :border="true" :col="2">
				<u-grid-item v-for="(item, index) in briefList" :key="index" :custom-style="{padding: '25rpx 15rpx'}">
					<view class="brief-report-item">
						<LegoIcon :icon="item.icon" :backgroundColor="item.color" :fontSize="30"/>
						<view class="item-content">
							<view class="title">
								<text>{{item.label}}</text>
							</view>
							<view class="value">
								<text>{{item.num}}</text>
							</view>
						</view>
					</view>
				</u-grid-item>
			</u-grid>
		</view>
	</view>
</template>

<script>
import * as HomeApi from '@/api/home'
import mixin from './mixin'

export default {
	mixins: [mixin],
	data() {
		return {
			briefList: []
		}
	},
	methods: {
		init() {
			const app = this;
      if (!app.data.definition || !app.data.definition.code) {
        return
      }
      app.loading = true
      HomeApi.open(app.getBaseParams()).then(res => {
        app.briefList = res.data.results
        app.loading = false
      }).catch(() => {
        app.loading = false
      })
		}
	}
}
</script>

<style lang="scss" scoped>
@import "./chartStyle";
.brief-report-item {
	width: 100%;
	padding: 0 4%;
	display: flex;
	font-size: 28rpx;
	.item-content {
		margin: 0 10rpx;
		flex: 1;
		overflow: hidden;
		.title {
			white-space: nowrap;
			font-size: 26rpx;
			font-weight: 500;
			line-height: 32rpx;
			overflow: hidden;
		}
		.value {
			white-space: nowrap;
			font-size: 26rpx;
			font-weight: 550;
			line-height: 40rpx;
		}
	}
	.item-rate {
		width: 20%;
		min-width: 80rpx;
		font-size: 26rpx;
		text-align: right;
		white-space: nowrap;
		.label {
			color: rgba($uni-ios-font, 0.8);
			line-height: 32rpx;
		}
		.rate {
			color: $uni-color-success;
			font-size: 26rpx;
			line-height: 40rpx;
		}
	}
}
</style>