<template>
	<u-form-item
		borderBottom
		:prop="item.fieldCode"
		:rules="item.rules"
		:label="item.name"
		:required="item.required"
		labelPosition="top">
		<view style="margin-top: 15rpx; width: 100%;">
			<u-textarea
				v-model="fieldForm[item.fieldCode]"
				:disabled="isDisabled"
				:maxlength="item.maxlength || 800"
				:placeholder="item.placeholder"
				border="none"
				@input="commonChange(item, $event)">
			</u-textarea>
		</view>
	</u-form-item>
</template>

<script>
import Mixin from '../mixin'
export default {
  mixins: [Mixin],
	computed: {
	},
	data() {
		return {
		}
	},
	methods: {
	}
}
</script>

<style>
</style>