<template>
	<view>
		<u-form-item
			:prop="item.fieldCode"
			:rules="item.rules"
			:label="item.name"
			:required="required"
			:borderBottom="borderBottom"
			@click="handleShowAction">
			<u--input
				v-model="innerValue"
				border="none"
				disabled
				disabledColor='#ffffff'
				:placeholder="placeholder"
				:clearable="clearable"
				@click="handleShowAction"/>
			<view slot="right">
				<u-icon v-if="!isDisabled" name="arrow-right"></u-icon>
			</view>
		</u-form-item>
		<u-overlay :show="showAction">
			<Detail
				class="warp"
				:value="item.value"
				:placeholder="placeholder"
				@success="handleSelect"
				@back="showAction = false"/>
		</u-overlay>
	</view>
</template>

<script>
import Mixin from '../mixin'
import Detail from './detail'
import { getDisplay } from '@/utils/data/address'
import { objDeepCopy } from '@/utils/util'
export default {
  mixins: [Mixin],
	components: {
		Detail
	},
	data() {
		return {
			showAction: false,
			innerValue: ''
		}
	},
	watch: {
		item: {
			handler(newVal, oldVal) {
				this.innerValue = getDisplay(newVal.value)
			},
			immediate: true,
			deep: true
		}
	},
	methods: {
		handleShowAction() {
			uni.hideKeyboard()
			if (this.disabled || this.item.disabled) {
				return;
			}
			this.showAction = true
		},
		handleSelect(data) {
			const item = objDeepCopy(this.item)
			item.value = data
			this.commonChange(item, data)
			this.innerValue = getDisplay(data)
			this.showAction = false
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
.action-scroll {
	padding-bottom: 0rpx;
	background: rgba(0, 0, 0, 0.02);
}
.action-tabs {
	@include flex(column);
	flex-direction: row;
	padding: 20rpx 10rpx;
	&-text {
		white-space: nowrap;
		font-size: 15px;
		padding: 0rpx 25rpx;
		&-active {
			color: #0095F2;
			font-weight: bold;
		}
	}
}
.warp {
	height: 100%;
	padding: 20rpx;
	background-color: #fff;
}
</style>