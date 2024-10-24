<template>
	<u-form-item
		:prop="item.fieldCode"
		:rules="item.rules">
		<u-parse :content="item.defaultValue"></u-parse>
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
	methods: {
	}
}
</script>
<style>
</style>