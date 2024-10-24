<template>
	<u-form-item
		borderBottom
		:prop="item.fieldCode"
		:rules="item.rules"
		:label="item.name"
		:required="item.required">
		<u--input
			v-model="fieldForm[item.fieldCode]"
			border="none"
			:disabledColor="isDisabled ? '#f1f1f1' : '#ffffff'"
			:type="inputType"
			:disabled="isDisabled"
			:maxlength="inputMaxlength"
			:placeholder="item.placeholder"
			:clearable="clearable"
			@input="commonChange(item, $event)">
		</u--input>
		<template v-slot:right >
			<LegoIcon v-if="inputIcon" :width="65" :icon="inputIcon" color="#c0c4cc" backgroundColor="transparent"/>
		</template>
	</u-form-item>
</template>

<script>
import Mixin from '../mixin'
export default {
  mixins: [Mixin],
	computed: {
		inputIcon() {
		  return {
		    mobile: 'icon-mobile',
		    email: 'icon-email-outline',
		    website: 'icon-link',
				percent: 'icon-percent'
		  }[this.item.formType]
		},
		inputMaxlength() {
		  if (this.item.formType === 'website') {
		    return 800
		  }
		  return 100
		},
		inputType() {
			return ['number', 'percent'].includes(this.item.formType) ? 'number' : 'text'
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
<style>
</style>