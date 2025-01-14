<template>
	<u-form-item
		borderBottom
		:prop="item.fieldCode"
		:rules="item.rules"
		:label="item.name"
		:required="required"
		:borderBottom="borderBottom">
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
			innerValue: false
		}
	},
	watch: {
		item: {
			handler(newVal, oldVal) {
				if (newVal.value) {
					this.innerValue = true
				} else {
					this.innerValue = false
				}
				this.commonChange(this.item, this.innerValue)
			},
			immediate: true
		}
	},
	methods: {
	}
}
</script>
<style>
</style>