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
		<Detail
			:visible.sync="showAction"
			:actionTitle="actionTitle"
			:setting="item.setting"
			@close="showAction = false"
			@success="handleConfirm"/>
	</view>
</template>

<script>
import Detail from './detail'
import Mixin from '../mixin'
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
		handleShowAction() {
			uni.hideKeyboard()
			if (this.disabled || this.item.disabled) {
				return;
			}
			this.showAction = true
		},
		handleConfirm(selectedData) {
			this.innerValue = selectedData.name
			const item = objDeepCopy(this.item)
			item.value = selectedData
			this.commonChange(item, selectedData.code)
			this.showAction = false
		}
	}
}
</script>
<style lang="scss" scoped>
</style>