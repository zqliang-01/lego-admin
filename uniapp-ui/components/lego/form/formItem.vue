<template>
	<view>
		<LegoInput
			v-if="isTrimInput"
			:item="item"
			:fieldForm="fieldForm"
			:disabled="disabled"
			:borderBottom="borderBottom"
			@change="commonChange" />
		<LegoSwitch
			v-else-if="item.formType == 'boolean'"
			:item="item"
			:fieldForm="fieldForm"
			:disabled="disabled"
			:borderBottom="borderBottom"
			@change="commonChange" />
		<LegoTextarea
			v-else-if="item.formType === 'textarea'"
			:item="item"
			:fieldForm="fieldForm"
			:disabled="disabled"
			:borderBottom="borderBottom"
			@change="commonChange" />
		<LegoSelect
			v-else-if="['select', 'user'].includes(item.formType)"
			:item="item"
			:fieldForm="fieldForm"
			:disabled="disabled"
			:borderBottom="borderBottom"
			@change="commonChange" />
		<LegoDate
			v-else-if="['date', 'datetime'].includes(item.formType)"
			:item="item"
			:fieldForm="fieldForm"
			:disabled="disabled"
			:borderBottom="borderBottom"
			@change="commonChange" />
		<LegoTreeSelect
			v-else-if="['structure'].includes(item.formType)"
			:item="item"
			:fieldForm="fieldForm"
			:disabled="disabled"
			:borderBottom="borderBottom"
			@change="commonChange" />
		<LegoRelative
			v-else-if="['entity'].includes(item.formType)"
			:item="item"
			:fieldForm="fieldForm"
			:disabled="disabled"
			:borderBottom="borderBottom"
			@change="commonChange" />
		<LegoUpload
			v-else-if="['picture'].includes(item.formType)"
			:item="item"
			:fieldForm="fieldForm"
			:disabled="disabled"
			:borderBottom="borderBottom"
			@change="commonChange" />
		<LegoRichtext
			v-else-if="['descText'].includes(item.formType)"
			:item="item"
			:fieldForm="fieldForm"
			:disabled="disabled"
			:borderBottom="borderBottom"
			@change="commonChange" />
		<LegoAddress
			v-else-if="['address'].includes(item.formType)"
			:item="item"
			:fieldForm="fieldForm"
			:disabled="disabled"
			:borderBottom="borderBottom"
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
import LegoAddress from './address'
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
		LegoRichtext,
		LegoAddress
  },
	computed: {
    isTrimInput() {
      return [
        'text',
        'mobile',
        'email',
        'website',
				'float',
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