<template>
  <view class="container">
		<NavBar title="我的流程"/>
		<view v-if="dataList.length > 0">
			<view class="data-item" v-for="(item,index) in dataList" :key="index">
				<view class="message" @click="handleDetail(item)">
					<view class="text">
						<view class="title">
							<text>{{item.name}}</text>
						</view>
						<view class="time">
							<text>开始时间：{{item.startTime||''}}</text>
						</view>
						<view class="time">
							<text>结束时间：{{item.endTime||''}}</text>
						</view>
						<view class="time">
							<text>时长：{{item.duration}}</text>
						</view>
					</view>
					<view v-if="item.status.code === 'running'" class="button" @click.stop="handleStop(item)">
						<u-button icon="pause" color="#ff3200" iconColor="#fff" size="mini" text="终止"/>
					</view>
				</view>
				<view class="author">
					<view class="name">
						<view class="icon">{{ item.startUser.name }}</view>
						<text>由{{item.startUser.name}}发起</text>
					</view>
					<view class="status">
						<text :style="{color: statusColor(item.status.code)}">{{item.status.name}}</text>
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
import store from '@/store'
import * as InstanceAPI from '@/api/app/oa/instance'
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
			InstanceAPI.list({
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
		handleStop(item) {
			const app = this
			uni.showModal({
				title: '提示',
				content: '此操作将取消流程【' + item.name + '】，是否继续?',
				success: function (res) {
					if (res.confirm) {
						InstanceAPI.stop(item.id).then(res => {
							app.$toast('流程终止成功！')
							item.status.code = 'terminated'
							item.status.name = '已终止'
						})
					}
				}
			})
		},
		handleLoadMore() {
			if (this.loadStatus === 'nomore') {
				return;
			}
			this.pageIndex++;
			this.handleQuery()
		},
		statusColor(status) {
			if (status === 'running') {
				return '#5ac725'
			}
			if (status === 'terminated') {
				return '#f56c6c'
			}
			if (status === 'completed') {
				return '#909399'
			}
			return '#f9ae3d'
		},
		handleDetail(item) {
			const flowImageUrl = InstanceAPI.flowImageUrl + item.id + '?' + this.$tokenName + '=' + store.getters.token
			uni.previewImage({
				urls: [flowImageUrl],
				current: flowImageUrl,
				fail() {
					uni.$u.toast('预览图片失败')
				},
			});
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
			.status {
				padding-right: 10rpx;
			}
		}
	}
}
</style>