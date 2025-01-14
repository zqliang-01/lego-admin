<template>
	<u-form-item
		:prop="item.fieldCode"
		:rules="item.rules"
		:label="item.name"
		:required="required"
		:borderBottom="borderBottom">
		<u-textarea
			v-model="fieldForm[item.fieldCode]"
			:disabled="isDisabled"
			:maxlength="item.maxlength || 800"
			:placeholder="placeholder"
			border="none"
			@input="commonChange(item, $event)">
		</u-textarea>
	</u-form-item>
</template>

<script>
import Mixin from '../mixin'
export default {
  mixins: [Mixin],
	computed: {
		placeholder() {
			if (!this.isDisabled) {
				return this.item.placeholder || '请填写' + this.item.name
			}
			return ''
		}
	},
	data() {
		return {
		}
	},
	methods: {
	}
}
</script>

<style lang="scss" scoped>
</style>