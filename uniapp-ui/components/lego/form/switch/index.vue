<template>
	<u-form-item
		borderBottom
		:prop="item.fieldCode"
		:rules="item.rules"
		:label="item.name"
		:required="item.required">
		<u-switch
			v-model="innerValue"
			size="20"
			:disabled="isDisabled"
			@change="commonChange(item, $event)">
		</u-switch>
	</u-form-item>
</template>

<script>
import Mixin from '../mixin'
export default {
  mixins: [Mixin],
	data() {
		return {
			innerValue: this.fieldForm[this.item.fieldCode]
		}
	},
	methods: {
	}
}
</script>
<style>
</style>