<template>
  <view>
		<view class="message-item" v-for="(data, index) in dataList" :key="index">
			<u-read-more :showHeight="80" closeText="展开" :shadowStyle="shadowStyle" color="#9f9f9f" toggle>
				<view @click="handleClick(data)">
					<view class="message-item__name">
						<view class="message-item__name__avatar">{{ index + 1 }}</view>
						<text class="message-item__name__title">{{data.name}}</text>
					</view>
					<view v-for="(item, index) in fieldList" :key="index">
						<view v-if="item.fieldCode !== 'name'" class="message-item__text">
							<text class="message-item__text__label">{{item.name}}：</text>
							<text class="message-item__text__content">{{handleViewValue(item, data)}}</text>
						</view>
					</view>
				</view>
			</u-read-more>
		</view>
	</view>
</template>

<script>
import { getShowValue } from '@/utils/form'
export default {
	props: {
		dataList: Array,
		fieldList: Array
	},
	data() {
		return {
			shadowStyle: {
				// #ifndef APP-NVUE
				backgroundImage: 'linear-gradient(-180deg, rgba(255, 255, 255, 0) 0%, #fff 80%)',
				// #endif
				// #ifdef APP-NVUE
				backgroundImage: 'linear-gradient(to top, #fff, rgba(255, 255, 255, 0.5))',
				// #endif
				paddingTop: '20px',
				marginTop: '-20px'
			}
		}
	},
	methods: {
		handleViewValue(item, data) {
			const formType = item.formType
			const value = data[item.fieldCode]
			return getShowValue(formType, value)
		},
		handleClick(data) {
			this.$emit("onSelect", data)
		}
	}
}
</script>

<style lang="scss" scoped>
.message-item {
	padding-top: 30rpx;
	padding-bottom: 20rpx;
	margin: 20rpx;
	background-color: $uni-bg-color;
	border-radius: $uni-border-radius-infos;
	box-shadow: 0rpx 2rpx 8rpx 2rpx rgba(0, 122, 255, 0.08);
	&__name {
		display: flex;
		text-indent: 0rem;
		padding-bottom: 10rpx;
		padding-left: 25rpx;
		margin: 0rpx 20rpx 10rpx;
		border-bottom-width: 1px;
		border-bottom-style: solid;
		border-bottom-color: #eee;
		&__avatar {
			color: #fff;
			width: 50rpx;
			height: 50rpx;
			font-size: 30rpx;
			line-height: 50rpx;
			border-radius: 50rpx;
			text-align: center;
			background: #82afea;
			margin-right: 20rpx;
		}
		&__title {
			flex: 1;
			font-size: 35rpx;
			font-weight: 700;
			color: #4f4f4f;
			overflow: hidden;
			line-height: 50rpx;
			white-space: nowrap;
			text-overflow: ellipsis;
		}
	}
	&__text {
		display: flex;
		margin-top: 10rpx;
		font-size: 30rpx;
		line-height: 50rpx;
		text-indent: 0rem;
		padding-left: 50rpx;
		&__label {
			color: #666;
			width: 170rpx;
		}
		&__content {
			flex: 1;
			overflow: hidden;
		}
	}
}
</style>