<template>
  <view class="container">
		<NavBar title="待办任务"/>
		<view v-if="dataList.length > 0">
			<view class="data-item" v-for="(item,index) in dataList" :key="index">
				<view class="message" @click="handleDetail(item)">
					<view class="text">
						<view class="title">
							<text>{{item.name}}</text>
						</view>
						<view class="time">
							<text>发起时间：{{item.createTime||''}}</text>
						</view>
						<view class="time">
							<text>流程名称：{{item.definitionName||''}}</text>
						</view>
						<view class="time">
							<text>受理人：{{item.assignee.name}}</text>
						</view>
						<view class="name">
							<view class="icon">{{ item.startUser.name }}</view>
							<text>由{{item.startUser.name}}发起</text>
						</view>
					</view>
				</view>
			</view>
			<view class="com-loadmore">
				<u-loadmore :status="loadStatus" @loadmore="handleLoadMore" />
			</view>
		</view>
		<Empty v-else/>
	</view>
</template>

<script>
import * as TaskAPI from '@/api/app/oa/task'

export default {
	data() {
		return {
			pageIndex: 1,
			pageSize: 10,
			loadStatus: 'loadmore',
			dataList: []
		}
	},
	onLoad() {
		this.init()
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
		handleQuery() {
			const app = this
			TaskAPI.undoList({
				pageIndex: app.pageIndex,
				pageSize: app.pageSize
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
		handleDetail(item) {
			this.$navTo('pages/components/task/taskDetail', {code: item.id})
		}
	}
}
</script>

<style lang="scss" scoped>
.container {
	.data-item {
		padding: 20rpx;
		margin: 20rpx;
		background-color: $uni-bg-color;
		border-radius: $uni-border-radius-infos;
		box-shadow: 0rpx 2rpx 8rpx 2rpx rgba(0, 122, 255, 0.08);
		.message {
			display: flex;
			.text {
				flex: 1;
				.time {
					font-size: 28rpx;
					color: rgba($uni-ios-font, 0.7);
					margin-bottom: 12rpx;
				}
				.title {
					font-size: 35rpx;
					line-height: 45rpx;
					font-weight: 550;
					margin-bottom: 12rpx;
				}
				.name {
					flex: 1;
					line-height: 50rpx;
					font-size: 30rpx;
					padding-top: 15rpx;
					.icon {
						display: block;
						float: left;
						margin-right: 10rpx;
						width: 50rpx;
						height: 50rpx;
						background-color: $uni-color-primary;
						border-radius: 50%;
						text-align: center;
						font-size: 20rpx;
						color: $uni-ios-white;
					}
				}
			}
		}
	}
}
</style>