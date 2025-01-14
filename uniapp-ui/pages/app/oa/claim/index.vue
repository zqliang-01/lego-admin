<template>
  <view class="container">
		<NavBar title="待签任务"/>
		<view v-if="dataList.length > 0">
			<view class="data-item" v-for="(item,index) in dataList" :key="index">
				<view class="message">
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
					</view>
					<view class="button">
						<u-button icon="checkmark" color="#5ac725" iconColor="#fff" size="mini" text="签收" @click="handleClaim(item, index)" />
					</view>
				</view>
				<view class="author">
					<view class="name">
						<view class="icon">{{ item.startUser.name }}</view>
						<text>由{{item.startUser.name}}发起</text>
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
import * as ClaimAPI from '@/api/app/oa/claim'
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
			ClaimAPI.list({
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
		handleClaim(item, index) {
			const app = this
			ClaimAPI.claim({ id: item.id }).then(() => {
				app.$toast('签收成功！')
				app.dataList.splice(index, 1)
			})
		},
		handleLoadMore() {
			if (this.loadStatus === 'nomore') {
				return;
			}
			this.pageIndex++;
			this.handleQuery()
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
			margin-bottom: 20rpx;
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
			}
		}
		.author {
			display: flex;
			font-size: 30rpx;
			line-height: 40rpx;
			.name {
				flex: 1;
				line-height: 50rpx;
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
</style>