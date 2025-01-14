<template>
	<view class="message-item">
		<u-read-more :showHeight="50" closeText="展开" :shadowStyle="shadowStyle" color="#1d1d1f" toggle>
			<view @click="handleClick">
				<u--text :text="data.name" bold></u--text>
				<view v-for="(item, index) in fieldList" :key="index" class="message-text">
						<text v-if="item.fieldCode !== 'name'">
							{{item.name}} : {{handleViewValue(item)}}
						</text>
				</view>
			</view>
		</u-read-more>
	</view>
</template>

<script>
import { getShowValue } from '@/utils/form'
export default {
	props: {
		fieldList: Array,
    data: Object
	},
	data() {
		return {
			shadowStyle: {
				// #ifndef APP-NVUE
				backgroundImage: 'linear-gradient(-180deg, rgba(255, 255, 255, 0) 0%, #fff 80%)',
				// #endif
				// #ifdef APP-NVUE
				// nvue上不支持设置复杂的backgroundImage属性
				backgroundImage: 'linear-gradient(to top, #fff, rgba(255, 255, 255, 0.5))',
				// #endif
				paddingTop: '20px',
				marginTop: '-20px'
			}
		}
	},
	methods: {
		handleViewValue(item) {
			const formType = item.formType
			const value = this.data[item.fieldCode]
			return getShowValue(formType, value)
		},
		handleClick() {
			this.$emit("onSelect", this.data)
		}
	}
}
</script>

<style lang="scss" scoped>
.message-item {
	padding: 20rpx 0rpx;
	margin: 20rpx;
	background-color: $uni-bg-color;
	border-radius: $uni-border-radius-infos;
	box-shadow: 0rpx 2rpx 8rpx 2rpx rgba(0, 115, 239, 0.1);
	.message-text {
		margin-top: 10rpx;
		font-size: 30rpx;
		line-height: 40rpx;
	}
}
</style>