<template>
	<u-form-item
		:prop="item.fieldCode"
		:rules="item.rules"
		:borderBottom="false">
		<u-parse :content="content" disabledColor='#ffffff' :disabled="isDisabled"></u-parse>
	</u-form-item>
</template>

<script>
import Mixin from '../mixin'
export default {
  mixins: [Mixin],
	data() {
		return {
		}
	},
	computed: {
		content() {
			if (this.item.defaultValue) {
				return this.item.defaultValue
			}
			return ''
		}
	},
	methods: {
	}
}
</script>
<style>
</style>