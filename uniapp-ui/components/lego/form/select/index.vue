<template>
	<view>
		<u-form-item
			:prop="item.fieldCode"
			:rules="item.rules"
			:label="item.name"
			:required="required"
			:borderBottom="borderBottom"
			@click="handleShowAction">
			<u--input
				v-if="item.precisions !== 1"
				v-model="innerValue"
				border="none"
				disabled
				disabledColor='#ffffff'
				:placeholder="placeholder"
				:clearable="clearable"
				@click="handleShowAction"/>
			<u-radio-group
				v-else
				v-model="fieldForm[item.fieldCode]"
				:disabled="isDisabled"
				class="select-radio"
				@change="commonChange(item, $event)">
				<u-radio
					:customStyle="{marginRight: '20px'}"
					v-for="(item, index) in item.setting"
					:key="index"
					:label="item.name"
					:name="item.code">
				</u-radio>
			</u-radio-group>
			<view slot="right">
				<u-icon v-if="item.precisions !== 1 && !isDisabled" name="arrow-right"></u-icon>
			</view>
		</u-form-item>
		<Detail
			:visible.sync="showAction"
			:actionTitle="actionTitle"
			:setting="item.setting"
			@close="showAction = false"
			@success="handleConfirm"/>
	</view>
</template>

<script>
import Detail from './detail'
import Mixin from '../mixin'
import { objDeepCopy } from '@/utils/util'
export default {
  mixins: [Mixin],
	components: {
		Detail
	},
	data() {
		return {
			showAction: false,
			innerValue: ''
		}
	},
	watch: {
		item: {
			handler(newVal, oldVal) {
				if (newVal.value) {
					this.innerValue = newVal.value.name
				} else {
					this.innerValue = newVal.value
				}
			},
			immediate: true
		}
	},
	methods: {
		handleConfirm(data) {
			this.innerValue = data.name;
			const item = objDeepCopy(this.item)
			item.value = data
			this.commonChange(item, data.code)
			this.showAction = false
		},
		handleShowAction() {
			uni.hideKeyboard()
			if (this.disabled || this.item.disabled || this.item.precisions === 1) {
				return;
			}
			this.showAction = true
		}
	}
}
</script>
<style lang="scss" scoped>
.select-radio {
	flex-wrap: wrap;
}
</style>