<template>
	<view v-if="!loading">
		<NavBar :title="task.name"/>
		<view v-if="hasForm || !task.finished" class="tabs-fixed">
			<u-tabs
				bold
				:itemStyle="itemStyle"
				:lineWidth="60"
				:lineHeight="3"
				:scrollable="false"
				:list="tabList"
				@change="handleTypeChange"></u-tabs>
		</view>
		<view v-if="hasForm || !task.finished" class="task-content">
			<FormDetail v-show="currentType === 'form'" :task="task" @onComplete="init"/>
			<HistoryDetail v-if="currentType === 'history'" :commentList="task.comments" :taskId="task.id"/>
		</view>
		<HistoryDetail v-else :commentList="task.comments" :taskId="task.id"/>
	</view>
</template>

<script>
import FormDetail from './taskForm'
import HistoryDetail from './taskHistory'
import * as TaskAPI from '@/api/task'
import { isEmpty } from '@/utils/util'

export default {
	components: {
		FormDetail,
		HistoryDetail
	},
	data() {
		return {
			task: {
				name: '审批详情'
			},
			loading: true,
			currentType: 'form',
			tabList: [{
				name: '表单',
				code: 'form'
			},
			{
				name: '审批历史',
				code: 'history'
			}],
			itemStyle: {
				height: '90rpx'
			}
		}
	},
	computed: {
		hasForm() {
			return !isEmpty(this.task.formKey)
		}
	},
	onLoad(data) {
		const app = this
		app.taskCode = data.code
		app.init()
	},
	methods: {
		init() {
			const app = this
			TaskAPI.info(app.taskCode).then(res => {
				app.task = res.data
				app.loading = false
			}).catch(() => {
				app.loading = false
			})
		},
		handleTypeChange(data) {
			this.currentType = data.code
		}
	}
}
</script>

<style lang="scss" scoped>
.tabs-fixed {
	position: fixed;
	width: 100%;
	z-index: 10;
	background-color: #fff;
	box-shadow: 0rpx 2rpx 8rpx 2rpx rgba(0, 122, 255, 0.08);
}
.task-content {
	margin-top: 110rpx;
}
</style>