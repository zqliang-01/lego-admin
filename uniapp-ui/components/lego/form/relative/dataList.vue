<template>
	<view class="container">
		<u-search
			margin="20rpx"
			actionText="搜索"
			:showAction="true"
			:animation="true"
			@search="handleSearch"
			@custom="handleSearch"></u-search>
		<scroll-view v-if="dataList.length > 0" scroll-y style="max-height: 60vh;flex-grow: 1;">
			<DataItem
				v-for="(item, index) in dataList"
				:key="index"
				:data="item"
				:fieldList="fieldList"
				@onSelect="actionSelect">
			</DataItem>
			<view class="com-loadmore">
				<u-loadmore :status="loadStatus" loadmore-text="点击加载更多" @loadmore="handleLoadMore" />
			</view>
		</scroll-view>
		<Empty v-else/>
	</view>
</template>

<script>
import DataItem from './dataItem'
import { listHeader } from '@/api/form/field'
import { postRequest } from '@/api/form/common'
export default {
	components: {
		DataItem
	},
	props: {
    item: Object
	},
	data() {
		return {
			searchValue: '',
			pageIndex: 1,
			pageSize: 10,
			loadStatus: 'loadmore',
			queryApiUrl: '',
			fieldList: [],
			dataList: []
		}
	},
	onReachBottom() {
		this.handleLoadMore()
	},
	created() {
		const app = this;
		if (app.item.relativeForm) {
			listHeader(app.item.relativeForm.code).then(res => {
				app.queryApiUrl = res.data.form.queryApiUrl
				app.fieldList = res.data.fields
				app.handleQuery()
			})
		}
	},
	methods: {
		handleLoadMore() {
			if (this.loadStatus === 'nomore') {
				return;
			}
			this.pageIndex++;
			this.handleQuery()
		},
		handleQuery() {
			const app = this;
			app.loadStatus = 'loading'
			postRequest(app.queryApiUrl, {
				pageSize: app.pageSize,
				pageIndex: app.pageIndex,
				search: app.searchValue
			}).then(res => {
				if (res.data.result.length === 0) {
					app.loadStatus = 'nomore'
					return;
				}
        app.dataList = app.dataList.concat(res.data.result)
				app.loadStatus = res.data.totalCount == app.dataList.length ? 'nomore' : 'loadmore'
			})
		},
		handleSearch(value) {
			this.pageIndex = 1
			this.searchValue = value
			this.dataList = []
			this.handleQuery()
		},
		actionSelect(data) {
			this.$emit("onSelect", data)
		}
	}
}
</script>
<style lang="scss" scoped>
.container {
	background-color: #f7f7f7;
	height: 100%;
	display: flex;
	flex-direction: column;
}
.action-title {
	height: 90rpx;
}
.com-loadmore {
	padding: 15rpx;
	.u-more-text {
		font-weight: 500;
		font-size: 26rpx !important;
		color: rgb(185, 185, 185) !important;
	}
}
</style>