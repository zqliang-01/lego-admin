<template>
	<view>
		<LegoInput
			v-if="isTrimInput"
			:item="item"
			:fieldForm="fieldForm"
			:disabled="disabled"
			@change="commonChange" />
		<LegoSwitch
			v-else-if="item.formType == 'boolean_value'"
			:item="item"
			:fieldForm="fieldForm"
			:disabled="disabled"
			@change="commonChange" />
		<LegoTextarea
			v-else-if="item.formType === 'textarea'"
			:item="item"
			:fieldForm="fieldForm"
			:disabled="disabled"
			@change="commonChange" />
		<LegoSelect
			v-else-if="['select', 'user'].includes(item.formType)"
			:item="item"
			:fieldForm="fieldForm"
			:disabled="disabled"
			@change="commonChange" />
		<LegoDate
			v-else-if="['date', 'datetime'].includes(item.formType)"
			:item="item"
			:fieldForm="fieldForm"
			:disabled="disabled"
			@change="commonChange" />
		<LegoTreeSelect
			v-else-if="['structure'].includes(item.formType)"
			:item="item"
			:fieldForm="fieldForm"
			:disabled="disabled"
			@change="commonChange" />
		<LegoRelative
			v-else-if="['entity'].includes(item.formType)"
			:item="item"
			:fieldForm="fieldForm"
			:disabled="disabled"
			@change="commonChange" />
		<LegoUpload
			v-else-if="['picture'].includes(item.formType)"
			:item="item"
			:fieldForm="fieldForm"
			:disabled="disabled"
			@change="commonChange" />
		<LegoRichtext
			v-else-if="['desc_text'].includes(item.formType)"
			:item="item"
			:fieldForm="fieldForm"
			:disabled="disabled"
			@change="commonChange" />
	</view>
</template>

<script>
import LegoDate from './date'
import LegoInput from './input'
import LegoSwitch from './switch'
import LegoSelect from './select'
import LegoRelative from './relative'
import LegoTextarea from './textarea'
import LegoTreeSelect from './treeselect'
import LegoUpload from './upload'
import LegoRichtext from './richtext'
import { isEmpty } from '@/utils/util'
import Mixin from './mixin'

export default {
  name: 'FormItem',
  mixins: [Mixin],
  components: {
		LegoDate,
		LegoInput,
		LegoSwitch,
		LegoSelect,
		LegoRelative,
		LegoTextarea,
		LegoTreeSelect,
		LegoUpload,
		LegoRichtext
  },
	computed: {
    isTrimInput() {
      return [
        'text',
        'mobile',
        'email',
        'website',
				'floatnumber',
				'number',
				'percent'
      ].includes(this.item.formType)
    }
	},
  methods: {
  }
}
</script>

<style>
</style>