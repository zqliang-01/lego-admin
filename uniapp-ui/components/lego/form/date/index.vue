<template>
	<view>
		<u-form-item
			borderBottom
			:prop="item.fieldCode"
			:rules="item.rules"
			:label="item.name"
			:required="item.required"
			@click="handleShowAction">
			<u--input
				v-model="innerValue"
				border="none"
				disabled
				:disabledColor="isDisabled ? '#f1f1f1' : '#ffffff'"
				:placeholder="item.placeholder || actionTitle"
				:clearable="clearable"></u--input>
			<view slot="right">
				<u-icon v-if="!isDisabled" name="arrow-right"></u-icon>
			</view>
		</u-form-item>
		<u-datetime-picker
			:show="showAction"
			v-model="innerDateValue"
			:title="actionTitle"
			:mode="item.formType"
			:minDate="200"
			@cancel="showAction = false"
			@confirm="handleConfirm"></u-datetime-picker>
	</view>
</template>

<script>
import Mixin from '../mixin'
import moment from '@/components/moment'
export default {
  mixins: [Mixin],
	data() {
		return {
			innerValue: '',
			innerDateValue: Number(new Date()),
			showAction: false
		}
	},
	watch: {
		item: {
			handler(newVal, oldVal) {
				this.innerValue = moment(newVal.value).format(this.formatterValue)
			},
			immediate: true
		}
	},
	computed: {
		formatterValue() {
			if (this.item.formType === 'datetime') {
				return 'YYYY-MM-DD HH:mm:ss'
			}
			return 'YYYY-MM-DD'
		}
	},
	methods: {
		handleShowAction() {
			uni.hideKeyboard()
			if (this.isDisabled) {
				return;
			}
			this.showAction = true
		},
		handleConfirm(data) {
			this.innerValue = moment(data.value).format(this.formatterValue)
			this.commonChange(this.item, this.innerValue)
			this.showAction = false
		}
	}
}
</script>
<style>
</style>