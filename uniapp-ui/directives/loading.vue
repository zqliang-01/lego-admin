<template>
	<view class="lego-loading-mask" v-if="visible">
		<view class="lego-loading-spinner">
			<u-loading-icon size="56" color="#5098ff"></u-loading-icon>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				visible: false
			}
		},
		methods: {}
	}
</script>
<style lang="scss" scoped>
.lego-loading-mask {
	position: absolute;
	background-color: hsla(0, 0%, 100%, .9);
	margin: 0;
	top: 0;
	right: 0;
	bottom: 0;
	left: 0;
	border-radius: $uni-border-radius-infos;
	transition: opacity .3s;

	.lego-loading-spinner {
		top: 50%;
		margin-top: -42rpx;
		width: 100%;
		text-align: center;
		position: absolute;
	}
}

.lego-loading-parent--relative {
	position: relative;
}
</style>