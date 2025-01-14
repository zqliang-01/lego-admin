<template>
	<view :style="{paddingBottom: task.finished ? '10rpx' : '130rpx'}">
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
			<view v-if="!isView" class="lego-card">
				<FormItems
					v-for="(children, index) in otherFieldList"
					:key="children.key"
					:field-form="fieldForm"
					:field-list="children.data"
					:disabled="isView"
					@change="commonChange"/>
			</view>
		</u--form>
		<view class="formButton" v-if="!isView">
			<u-button
				type="error"
				text="拒绝"
				customStyle="margin: 0rpx 10rpx 0rpx 30rpx"
				@click="handleReject"></u-button>
			<u-button
				type="primary"
				text="提交"
				customStyle="margin: 0rpx 30rpx 0rpx 10rpx"
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
	props: {
		task: {
			type: Object,
			default: () => {
				return {
					id: '',
					code: '',
					finished: false,
					formKey: ''
				}
			}
		}
	},
  computed: {
    isView() {
      return this.actionType === 'view'
    }
	},
	data() {
		return {
			detailCode: this.task.code,
			detailData: {},
			fieldRules: {
				comment: [{ required: true, message: '审批意见不能为空', trigger: 'blur' }]
			},
      otherFieldList: [
        {
					data: [
						{ fieldCode: 'comment', name: '审批意见', formType: 'text', required: true }
					],
					key: 'otherComment'
				}
      ]
		}
	},
	mounted() {
		const app = this
		app.actionType = app.task.finished ? 'view' : app.actionType
		if (app.task.formKey) {
			app.initField(app.task.formKey)
		} else {
			app.$refs.createForm.setRules(app.fieldRules)
		}
	},
	methods: {
		initField(code) {
			const app = this
			FieldAPI.listCreate(code).then(res => {
				app.initRequest(res.data.form)
				app.fieldList = res.data.fields
				if (app.detailCode) {
					app.actionType = app.actionType !== 'view' ? 'update' : app.actionType
					app.detailRequest(app.detailCode).then(res => {
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
				TaskAPI.complete({
					id: app.task.id,
					variables: app.fieldForm,
					comment: app.fieldForm.comment
				}).then(() => {
					app.actionType = 'view'
					app.$emit("onComplete")
					app.$toast("提交成功")
				})
			}).catch(errors => {
				uni.$u.toast(errors[0].message)
			})
		},
		handleReject() {
			const app = this
			if (!app.fieldForm.comment) {
				app.$toast("请填写审批意见！")
				return;
			}
			uni.showModal({
				title: '提示',
				content: '此操作将拒绝任务【' + app.task.name + '】并回退到上一审批节点，是否继续?',
				success: function (res) {
					if (res.confirm) {
						TaskAPI.reject({
							id: app.task.id,
							comment: app.fieldForm.comment
						}).then(() => {
							app.$emit("onComplete")
							app.$toast("已拒绝")
						})
					}
				}
			});
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