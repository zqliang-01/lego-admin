<template>
	<view :style="{paddingBottom: task.finished ? '10rpx' : '140rpx'}">
		<NavBar :title="task.name"/>
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
					:disabled="isView"
					@change="commonChange"/>
			</view>
		</u--form>
		<view class="formButton" v-if="!isView">
			<u-button
				type="primary"
				text="提交"
				customStyle="margin: 0rpx 20rpx"
				@click="handleSumbit"></u-button>
		</view>
	</view>
</template>

<script>
import * as FieldAPI from '@/api/form/field'
import * as DefinitionAPI from '@/api/app/oa/definition'
import FormItems from '@/components/lego/form/formItems'
import RuleMixin from '@/components/lego/form/ruleMixin'
import ModelMixin from '@/components/lego/form/modelMixin'

export default {
	name: 'startForm',
	mixins: [ModelMixin, RuleMixin],
	components: {
		FormItems
	},
  computed: {
    isView() {
      return this.actionType === 'view'
    }
	},
	data() {
		return {
			task: {
				id: '',
				code: '',
				finished: false,
				formKey: ''
			},
			detailData: {},
			fieldRules: {}
		}
	},
	onLoad(data) {
		const app = this
		app.task = data
		app.actionType = app.task.finished ? 'view' : app.actionType
		app.initField(app.task.formKey)
	},
	mounted() {
	},
	methods: {
		initField(code) {
			const app = this
			FieldAPI.listCreate(code).then(res => {
				app.initRequest(res.data.form)
				app.fieldList = res.data.fields
				if (app.task.code) {
					app.actionType = app.actionType !== 'view' ? 'update' : app.actionType
					app.detailRequest(app.task.code).then(res => {
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
						field.disabled = app.getDisable(field, app.actionType === 'add')
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
				DefinitionAPI.start({
					formKey: app.task.formKey,
					definitionId: app.task.id,
					variables: app.fieldForm
				}).then(() => {
					app.actionType = 'view'
					app.task.finished = true
					uni.showModal({
						title: '提示',
						confirmText: '去看看',
						content: '流程发起成功，可到“我的流程”中查看发起的审批！',
						success: (res) => {
							if (res.confirm) {
								app.$navTo('pages/app/oa/owner/index')
							}
						},
						fail: () => {
							uni.navigateBack()
						}
					})
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