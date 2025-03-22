<template>
  <view class="container">
		<NavBar :title="title" :autoBack="false" @leftClick="handleBack"/>
		<view class="query-form">
			<u--form
				ref="createForm"
				:model="fieldForm"
				:labelStyle="{color: '#666666', marginLeft: '40rpx'}"
				:labelWidth="labelWidth"
				labelAlign="left">
				<view
					v-for="(item, index) in filterList"
					class="filter-item"
					:key="item.code">
					<FormItem
						:item="item.field"
						:field-form="item.fieldForm"
						:borderBottom="false"
						class="filter-item_input"
						@change="valueChange(index, arguments)"/>
					<view class="filter-item_del">
						<u-icon name="minus-circle-fill" color="#f43530" @click="handleDelete(index)"></u-icon>
					</view>
				</view>
			</u--form>
		</view>
		<view class="add-button">
			<u-button
				plain
				icon="plus"
				shape="circle"
				text="添加条件"
				@click="showAction = true"></u-button>
		</view>
		<view class="add-button">
			<u-button
				shape="circle"
				text="查询"
				type="primary"
				@click="handleQuery"></u-button>
		</view>
		<u-action-sheet
			:show="showAction"
			title="选择条件"
			@close="showAction = false">
			<scroll-view v-if="fieldList.length > 0" scroll-y style="max-height: 60vh;flex-grow: 1;">
				<u-cell-group>
						<u-cell
							v-for="(item, index) in fieldList"
							:key="index"
							:title="item.name"
							clickable
							@click="actionSelect(item)">
						</u-cell>
				</u-cell-group>
			</scroll-view>
			<Empty v-else/>
		</u-action-sheet>
	</view>
</template>

<script>
import * as fieldAPI from '@/api/form/field'
import FormItem from '@/components/lego/form/formItem'
import ModelMixin from '@/components/lego/form/modelMixin'
import RuleMixin from '@/components/lego/form/ruleMixin'
import { getDisplay } from '@/utils/data/address'
import { calculateStrLength, isArray, isObject, isEmpty, getTreeName, objDeepCopy } from '@/utils/util'
import { separator } from '@/utils/number'

export default {
	mixins: [ModelMixin, RuleMixin],
	components: {
		FormItem
	},
	props: {
		title: String,
		formData: Object,
		conditionList: {
			type: Array,
			default: () => {
				return []
			}
		}
	},
	data() {
		return {
			filterList: this.conditionList,
			showAction: false,
			dataList: []
		}
	},
	watch: {
		formData: {
			handler(newVal, oldVal) {
				if (newVal && newVal.fields) {
					this.init()
				}
			},
			immediate: true
		}
	},
  computed: {
		labelWidth() {
			let num = 8
			this.fieldList.forEach(field => {
				let tmpNum = calculateStrLength(field.name)
				if (tmpNum > num) {
					num = tmpNum;
				}
			})
			return num < 8 ? (num / 2 * 18 + 20) : (num / 2 * 18 + 20)
		}
	},
	methods: {
		init(){
			const app = this
			app.fieldList = []
			objDeepCopy(app.formData.fields).forEach(field => {
				app.initSettingValue(field)
				field.required = false
				app.fieldList.push(field)
				// if (app.filterList.length === 0 && field.fieldCode === 'name') {
				// 	app.filterList.push({
				// 		code: field.fieldCode,
				// 		field: field,
				// 		fieldForm: {}
				// 	})
				// }
			})
		},
		actionSelect(item) {
			const timestamp = new Date().getTime();
			this.filterList.push({
				code: item.fieldCode + timestamp,
				field: item,
				fieldForm: {}
			})
			this.showAction = false
		},
		valueChange(index, args) {
			if (args.length > 1) {
				this.filterList[index].field = args[0]
				this.filterList[index].value = args[1]
			}
		},
		handleValid() {
			for (let i = 0; i < this.filterList.length; ++i) {
				const formItem = this.filterList[i]
				if (formItem.field.formType === 'boolean' && !formItem.value) {
					formItem.value = false
				}
        if (!formItem.value && formItem.value !== false) {
					this.$toast(formItem.field.name + "不能为空！")
					return false
				}
			}
			return true
		},
		handleQuery() {
			const app = this
			if (!app.handleValid()) {
				return
			}
			const showItems = []
      const formItems = []
			app.filterList.forEach(formItem => {
				showItems.push({
					name: formItem.field.name,
					value: app.getConditionShowValue(formItem)
				})
        formItems.push({
          fieldCode: formItem.field.fieldCode,
          formType: formItem.field.formType,
          type: 'equals',
          values: [formItem.value]
        })
			})
			app.$emit("onQuery", {formItems: formItems, showItems: showItems, conditionList: app.filterList})
		},
		getConditionShowValue(formItem) {
			const item = formItem.field
			const value = formItem.value
		  const formType = item.formType
		  const placeholder = '--'
			if (formType === 'boolean') {
				return value ? '生效' : '失效'
			}
		  if (isEmpty(value)) {
		    return placeholder
		  }
		  if (formType === 'float') {
		    return separator(value)
		  } else if (formType === 'percent') {
		    return `${value}%`
		  } else if (['select', 'user', 'entity'].includes(formType)) {
		    return isEmpty(item.value) ? '' : item.value.name
		  } else if (formType === 'structure') {
		    return getTreeName(item.setting, value)
		  } else if (formType === 'checkbox') {
		    if (isArray(value)) {
		      const name = value.map(element => {
		        const settingValue = item.setting.find(item => item.code === element)
		        return isEmpty(settingValue) ? element : settingValue.name
		      }).join()
		      return isEmpty(name) ? placeholder : name
		    }
		    const name = item.setting.find(item => item.code === value).name
		    return isEmpty(name) ? placeholder : name
		  } else if (formType === 'address') {
				return getDisplay(value)
			}
		  return value
		},
		handleDelete(index) {
			this.filterList.splice(index, 1)
		},
		handleBack() {
			this.$emit("back")
		}
	}
}
</script>

<style lang="scss" scoped>
.query-form {
	background-color: #fff;
	margin-top: 30rpx;
}
.add-button {
	padding: 30rpx 40rpx 0rpx;
}
.filter-item {
	display: flex;
	border-bottom-width: 1px;
	border-bottom-style: solid;
	border-bottom-color: rgb(214, 215, 217);
	&_input {
		flex: 1;
	}
	&_del {
		display: flex;
		padding: 0rpx 35rpx;
	}
}
</style>