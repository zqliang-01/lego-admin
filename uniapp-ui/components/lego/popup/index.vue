<template>
	<u-popup :show="visible" @close="handleClose">
		<view>
			<u-toolbar
				class="action-title"
				:title="title"
				@cancel="handleClose"
				@confirm="handleConfirm" />
		</view>
		<slot></slot>
	</u-popup>
</template>

<script>
export default {
	name: 'LegoPopup',
	props: {
    visible: {
      type: Boolean,
      default: false
    },
		title: String
	},
	methods: {
		handleClose() {
      this.$emit('close')
		},
		handleConfirm() {
      this.$emit('success', this.selectedData)
		}
	}
}
</script>

<style lang="scss" scoped>
.action-title {
	height: 90rpx;
	background: rgba(0, 0, 0, 0.05);
	border-bottom: 2rpx solid rgba(0, 0, 0, 0.08);
}
</style>