<template>
	<view class="search-area">
		<view class="search-area__icon"><u-icon name="search" size="55rpx"></u-icon></view>
		<view v-if="conditionList.length === 0" class="search-area__content">
			<text >{{placeholder}}</text>
		</view>
		<view class="search-area__condition">
			<view v-for="(item, index) in conditionList" :key="index">
				<text class="search-area__condition__tag">{{conditionName(item)}}</text>
			</view>
		</view>
	</view>
</template>

<script>
export default {
  props: {
		placeholder: {
			type: String,
			default: '请输入查询信息'
		},
		conditionList: {
			type: Array,
			default: () => {
				return []
			}
		}
  },
	methods: {
		conditionName(item) {
			return item.name + ' 等于 ' + item.value
		}
	}
}
</script>

<style lang="scss" scoped>
.search-area {
	border-radius: 50rpx;
	padding: 10rpx 20rpx;
	border: 1px solid $u-border-color;
	background-color: #fff;
	line-height: 60rpx;
	font-size: 35rpx;
	display: flex;
	align-items: center;
	&__icon {
		margin-left: 15rpx;
	}
	&__content {
		flex: 1;
		font-size: 35rpx;
		margin-left: 10rpx;
		color: $u-tips-color;
		overflow: hidden;
		white-space: nowrap;
		text-overflow: ellipsis;
	}
	&__condition {
		display: flex;
		overflow-x: auto;
		white-space: nowrap;
		text-overflow: ellipsis;
		&__tag {
			font-size: 30rpx;
			padding: 5rpx 10rpx;
			border-radius: 15rpx;
			color: $u-tips-color;
			border: 1px solid $u-border-color;
			margin-left: 10rpx;
		}
	}
}
</style>