<template>
	<view :style="{paddingBottom: '130rpx'}">
		<NavBar title="新增数据"/>
		<u--form
			ref="createForm"
			:model="fieldForm"
			:labelStyle="{color: '#666666', minWidth: labelWidth + 'rpx', marginRight: '20rpx'}"
			labelWidth="auto"
			labelAlign="right"
			errorType="border-bottom">
			<view class="lego-card">
				<FormItems
					v-for="(children, index) in fieldList"
					:key="index"
					:field-form="fieldForm"
					:field-list="children"
					@change="commonChange"/>
			</view>
		</u--form>
		<view class="formButton">
			<u-button
				type="primary"
				text="提交"
				customStyle="margin: 0rpx 20rpx"
				@click="handleSumbit"></u-button>
		</view>
	</view>
</template>

<script>
import * as TaskAPI from '@/api/notice/task'
import * as FieldAPI from '@/api/form/field'
import FormItems from '@/components/lego/form/formItems'
import RuleMixin from '@/components/lego/form/ruleMixin'
import ModelMixin from '@/components/lego/form/modelMixin'

export default {
	name: 'FormDetail',
	mixins: [ModelMixin, RuleMixin],
	components: {
		FormItems
	},
  computed: {
	},
	data() {
		return {
			detailData: {},
			fieldRules: {}
		}
	},
	onLoad(data) {
		this.initField(data)
	},
	methods: {
		initField(data) {
			const app = this
			FieldAPI.listCreate(data.formCode).then(res => {
				app.initRequest(res.data.form)
				app.fieldList = res.data.fields
				if (data.detailCode) {
					app.actionType = 'update'
					app.detailRequest(data.detailCode).then(res => {
						app.detailData = res.data
						app.initValue()
					})
				} else {
					app.initValue()
				}
			})
		},
		initValue() {
			const app = this
			app.fieldList.forEach(fields => {
				fields.forEach(field => {
					if (field.show !== false) {
						app.fieldRules[field.fieldCode] = app.getRules(field)
					}
					if (field.show !== false && !field.simpleType) {
						app.initSettingValue(field)
						app.$set(field, 'value', app.detailData[field.fieldCode])
						field.disabled = app.getDisable(field)
						app.setDefaultValue(field, app.fieldForm, app.actionType === 'add')
					}
				})
			})
			app.$refs.createForm.setRules(app.fieldRules)
		},
		handleSumbit() {
			const app = this
			app.$refs.createForm.validate().then(res => {
				if (app.detailData && app.detailData.code) {
					app.fieldForm['code'] = app.detailData.code
				}
				app.saveRequest(app.fieldForm).then(res => {
					app.actionType = 'update'
					app.$toast('操作成功')
					setTimeout(() => {
						uni.navigateBack({ success: () => {
							uni.$emit('common-add', { type: 'save-success' });
						}})
					}, 500)
				})
			}).catch(errors => {
				uni.$u.toast(errors[0].message)
			})
		}
	}
}
</script>

<style lang="scss" scoped>
.formButton {
	display: flex;
	position: fixed;
	padding: 10rpx 0rpx 30rpx;
	bottom: 0px;
	width: 100%;
	left: 0px;
	background-color: #fafafa;
}
</style>