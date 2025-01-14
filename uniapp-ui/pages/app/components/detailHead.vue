<template>
	<view class="detail-head">
		<view class="detail-head__bg" />
		<view class="detail-head__body">
			<view class="detail-head__body__name">
				<view class="detail-head__body__name__left">
					<LegoIcon :icon="icon" shape="square" size="80" fontSize="55" backgroundColor="#f0f0f1" color="#2362FB"/>
				</view>
				<view class="detail-head__body__name__center">
					<text class="detail-head__body__name__center__title">{{title}}</text>
				</view>
				<view class="detail-head__body__name__right">
					<slot name="right" />
				</view>
			</view>
			<slot name="body">
				<slot />
			</slot>
		</view>
	</view>
</template>

<script>
	export default {
		name: 'detailHead',
		props: {
			title: {
				type: [String, Number],
				default: ''
			},
			icon: {
				type: String,
				default: 's-seas'
			}
		},
		data() {
			return {}
		},
		created() {
		},
		methods: {
		}
	}
</script>
<style lang="scss" scoped>
.detail-head {
	position: relative;
	overflow: hidden;
	padding: 15rpx 40rpx 30rpx;
	z-index: 0;

	&__bg {
		position: absolute;
		top: 0rpx;
		left: 0rpx;
		right: 0rpx;
		background-color: #007aff;
		height: 140rpx;
		z-index: -1;
	}

	&__body {
		background-color: $uni-bg-color;
		border-radius: $uni-border-radius-infos;
		padding: 24rpx;
		box-shadow: $uni-ios-shdow;
		

		&__name {
			display: flex;
			min-height: 70rpx;
			padding-bottom: 10rpx;

			&__left {
				width: 80rpx;
				height: 80rpx;
				overflow: hidden;
				background-color: #f0f0f1;
				border-radius: $uni-border-radius-lg;
				padding: 5rpx;
				margin-right: $uni-spacing-col-base;

				image {
					display: block;
					width: 60rpx;
					height: 60rpx;
				}
			}
			&__center {
				flex: 1;
				overflow: hidden;
				text-overflow: ellipsis;
				display: flex;
				align-items: center;
				margin-right: 16rpx;
				
				&__title {
					font-size: 40rpx;
					line-height: 45rpx;
					font-weight: 550;
					color: $uni-ios-font;
				}
			}
			&__right {
				display: flex;
				align-items: center;
			}
		}
	}
}
</style>
