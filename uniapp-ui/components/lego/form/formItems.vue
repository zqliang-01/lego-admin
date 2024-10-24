<template>
  <view>
		<FormItem
			v-for="(item, index) in fieldList"
			:key="index"
			:item="item"
			:fieldForm="fieldForm"
			:disabled="disabled"
			@change="commonChange">
		</FormItem>
  </view>
</template>

<script>
import FormItem from './formItem'
export default {
	name: 'FormItems',
	components: {
		FormItem
	},
	props: {
		fieldForm: {
			type: Object,
			default: () => {
				return {}
			}
		},
		fieldList: {
			type: Array,
			default: () => {
				return []
			}
		},
		disabled: Boolean
	},
	watch: {
	},
	methods: {
    commonChange(item, value) {
      this.$emit('change', item, value)
    }
	}
}
</script>

<style>
</style>