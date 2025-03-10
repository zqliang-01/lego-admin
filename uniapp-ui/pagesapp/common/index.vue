<template>
  <view class="container">
		<NavBar :title="title"/>
		<u-overlay :show="showCondition">
			<Condition
				class="condition"
				:title="title"
				:formData="formData"
				:conditionList="conditionList"
				@onQuery="handleCondition"
				@back="showCondition = false"/>
		</u-overlay>
		<template v-if="!showCondition">
			<view class="search" @click="showCondition = !showCondition">
				<LegoSearch placeholder="条件搜索" :conditionList="showItems"></LegoSearch>
			</view>
			<DataList :dataList="dataList" :fieldList="formData.fields" @onSelect="handleDetail"/>
			<view class="com-loadmore">
				<u-loadmore :status="loadStatus" @loadmore="handleLoadMore" />
			</view>
			<view class="fixed-add">
				<u-button :hairline="false" @click="handleAdd" :customStyle="addButtonStyle">
					<u-icon name="plus" size="13" color="#767676"></u-icon>
					<text class="fixed-add__text">新增</text>
				</u-button>
			</view>
		</template>
	</view>
</template>

<script>
import Condition from './condition'
import DataList from './dataList'
import * as fieldAPI from '@/api/form/field'
import { postRequest } from '@/api/form/common'

export default {
	components: {
		Condition,
		DataList
	},
	data() {
		return {
			title: '',
			formData: {},
			showCondition: false,
			loadStatus: 'loadmore',
			conditionList: [],
			pageIndex: 1,
			pageSize: 10,
			dataList: [],
			formItems: [],
			showItems: [],
			listRequest: {},
			addButtonStyle: {
				paddingBottom: '30rpx',
				borderColor: '#fff',
				height: '150rpx'
			}
		}
	},
	onLoad(data) {
		const app = this
		app.title = data.name
		if (data.formCode) {
			fieldAPI.listHeader(data.formCode).then(res => {
				app.formData = res.data
				app.listRequest = function doRequest(data) {
					return postRequest(app.formData.form.queryApiUrl, data)
				}
				app.init()
			})
			uni.$on('common-add',function(data){
				if (data.type === 'save-success') {
					app.init()
				}
			})
		}
	},
	onUnload() {
		uni.$off('common-add');
	},
	onReachBottom() {
		this.handleLoadMore()
	},
	methods: {
		onPullDownRefresh() {
			this.init()
		},
		init() {
			this.pageIndex = 1
			this.dataList = []
			this.loadStatus = 'loadmore'
			this.handleQuery()
		},
		handleCondition(condition) {
			this.formItems = condition.formItems
			this.showItems = condition.showItems
			this.conditionList = condition.conditionList
			this.showCondition = false
			this.init()
		},
		handleQuery() {
			const app = this
			app.listRequest({
				pageIndex: app.pageIndex,
				pageSize: app.pageSize,
				searchList: app.formItems
			}).then(res => {
				uni.stopPullDownRefresh()
				if (res.data.result.length === 0) {
					app.loadStatus = 'nomore'
					return;
				}
        app.dataList = app.dataList.concat(res.data.result)
				app.loadStatus = res.data.totalCount == app.dataList.length ? 'nomore' : 'loadmore'
			}).catch(() => {
				uni.stopPullDownRefresh()
			})
		},
		handleLoadMore() {
			if (this.loadStatus === 'nomore') {
				return;
			}
			this.pageIndex++;
			this.handleQuery()
		},
		handleAdd() {
			this.$navTo('pagesapp/common/create', {formCode: this.formData.form.code})
		},
		handleDetail(data) {
			this.$navTo('pagesapp/common/detail', {formCode: this.formData.form.code, detailCode: data.code})
		}
	}
}
</script>

<style lang="scss" scoped>
.query-form {
	background-color: #fff;
	margin-top: 30rpx;
	padding-right: 30rpx;
}
.search {
	padding: 30rpx 20rpx 10rpx;
}
.condition {
	background-color: #fafafa;
	height: 100%;
}
.add-button {
	padding: 30rpx 40rpx 0rpx;
}
.com-loadmore {
	padding-bottom: 150rpx;
}
.fixed-add {
	width: 100%;
	height: 150rpx;
	position: fixed;
	bottom: 0rpx;
	background-color: #fff;
	text-align: center;
	&__text {
		color: #767676;
		padding-left: 10rpx;
	}
}
</style>