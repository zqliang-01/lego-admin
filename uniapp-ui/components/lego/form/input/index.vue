<template>
	<u-form-item
		:prop="item.fieldCode"
		:rules="item.rules"
		:label="item.name"
		:required="required"
		:borderBottom="borderBottom">
		<u--input
			v-model="fieldForm[item.fieldCode]"
			border="none"
			disabledColor='#ffffff'
			:type="inputType"
			:disabled="isDisabled"
			:maxlength="inputMaxlength"
			:placeholder="placeholder"
			:clearable="clearable"
			@input="commonChange(item, $event)">
		</u--input>
		<template slot="right" >
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
			return ['number', 'percent', 'float'].includes(this.item.formType) ? 'number' : 'text'
		},
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
	mounted() {
	},
	methods: {
	}
}
</script>
<style>
</style>