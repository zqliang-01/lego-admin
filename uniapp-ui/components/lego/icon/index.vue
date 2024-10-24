<template>
	<view class="item-icon" :style="{width: width + 'rpx'}">
		<text
			:class="['legofont', 'lego lego-' + icon]"
			:style="[fontStyle]"></text>
	</view>
</template>

<script>
export default {
  props: {
		icon: String,
		color: {
      type: String,
      default: '#ffffff'
		},
    backgroundColor: {
      type: String,
      default: '#2362FB'
    },
		width: {
			type: [Number, String],
			default: 72
		},
		fontSize: {
			type: [Number, String],
			default: 40
		},
		size: {
			type: [Number, String],
			default: 64
		},
		shape: {
			type: String,
			default: 'circle'
		}
  },
	computed: {
		fontStyle() {
			return {
				color: this.color,
				backgroundColor: this.backgroundColor,
				fontSize: this.fontSize + 'rpx',
				width: this.size + 'rpx',
				height: this.size + 'rpx',
				lineHeight: this.size + 'rpx',
				borderRadius: this.shape === 'circle' ? '80rpx' : '10rpx'
			}
		}
	}
}
</script>

<style lang="scss" scoped>
.item-icon {
	display: inline-block;
	.legofont {
		margin: 0 auto;
		display: block;
		text-align: center;
	}
}
</style>