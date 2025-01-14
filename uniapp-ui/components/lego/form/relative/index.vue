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
				:clearable="clearable"/>
			<view slot="right">
				<u-icon v-if="!isDisabled" name="arrow-right"></u-icon>
			</view>
		</u-form-item>
		<u-popup :show="showAction" @close="showAction = false">
			<DataList :item="item" @onSelect="actionSelect"/>
		</u-popup>
	</view>
</template>

<script>
import DataList from './dataList'
import Mixin from '../mixin'
import { objDeepCopy } from '@/utils/util'
export default {
  mixins: [Mixin],
	components: {
		DataList
	},
	data() {
		return {
			showAction: false,
			innerValue: '',
			tabList: [],
			dataList: []
		}
	},
	watch: {
		item: {
			handler(newVal, oldVal) {
				if (newVal.value) {
					this.innerValue = newVal.value.name
				} else {
					this.innerValue = newVal.value
				}
			},
			immediate: true
		}
	},
	methods: {
		actionSelect(data) {
			this.innerValue = data.name
			const item = objDeepCopy(this.item)
			item.value = data
			this.commonChange(item, data.code)
			this.showAction = false
		},
		handleShowAction() {
			uni.hideKeyboard()
			if (this.disabled || this.item.disabled || this.item.precisions === 1) {
				return;
			}
			this.showAction = true
		}
	}
}
</script>
<style lang="scss" scoped>
.action-title {
	height: 90rpx;
}
</style>