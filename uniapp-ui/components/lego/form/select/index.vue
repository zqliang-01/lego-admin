<template>
	<view>
		<u-form-item
			borderBottom
			:prop="item.fieldCode"
			:rules="item.rules"
			:label="item.name"
			:required="item.required"
			@click="handleShowAction">
			<u--input
				v-if="item.precisions !== 1"
				v-model="innerValue"
				border="none"
				disabled
				:disabledColor="isDisabled ? '#f1f1f1' : '#ffffff'"
				:placeholder="item.placeholder || actionTitle"
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
		<u-action-sheet
			:show="showAction"
			:title="actionTitle"
			@close="showAction = false">
			<scroll-view v-if="item.setting.length > 0" scroll-y style="max-height: 60vh;flex-grow: 1;">
				<u-cell-group>
						<u-cell
							v-for="(item, index) in item.setting"
							:key="index"
							:title="item.name"
							clickable
							@click="actionSelect(item)">
						</u-cell>
				</u-cell-group>
			</scroll-view>
		</u-action-sheet>
	</view>
</template>

<script>
import Mixin from '../mixin'
export default {
  mixins: [Mixin],
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
		actionSelect(data) {
			this.innerValue = data.name;
			this.commonChange(this.item, data.code)
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