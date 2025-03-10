<template>
  <view class="container">
		<NavBar title="发起审批"/>
		<view v-if="dataList.length > 0">
			<view class="definition-item" v-for="(item,index) in dataList" :key="index">
				<view class="text" @click="handleDetail(item)">
					<view class="message">
						<text>{{item.name}}</text>
					</view>
					<view class="time">
						<text>{{item.deploymentTime}}</text>
					</view>
				</view>
				<view class="button" @click.stop="handleStart(item)" >
					<u-button color="#ff7300" icon="play-circle" iconColor="#fff" text="发起" size="mini" />
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
import * as DefinitionAPI from '@/api/app/oa/definition'
export default {
	data() {
		return {
			title: '',
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
			DefinitionAPI.list({
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
		handleStart(item) {
			const app = this
			DefinitionAPI.formKey(item.id).then(res => {
				if (res.data) {
					const task = {
						id: item.id,
						name: item.name,
						formKey: res.data
					}
					app.$navTo('pagesapp/oa/start/startForm', task)
					return
				}
				DefinitionAPI.start({
					definitionId: item.id
				}).then(() => {
					uni.showModal({
						title: '提示',
						confirmText: '去看看',
						content: '流程发起成功，可到“我的流程”中查看发起的审批！',
						success: function (res) {
							if (res.confirm) {
								app.$navTo('pagesapp/oa/owner/index')
							}
						}
					})
				})
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
			const flowImageUrl = DefinitionAPI.flowImageUrl + item.id + '?' + this.$tokenName + '=' + store.getters.token
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
	.definition-item {
		padding: 20rpx;
		margin: 20rpx;
		background-color: $uni-bg-color;
		border-radius: $uni-border-radius-infos;
		box-shadow: 0rpx 2rpx 8rpx 2rpx rgba(0, 122, 255, 0.08);
		display: flex;
		.text {
			flex: 1;
			.time {
				font-size: 28rpx;
				color: rgba($uni-ios-font, 0.7);
				margin-bottom: 12rpx;
			}
			.message {
				font-size: 35rpx;
				line-height: 45rpx;
				font-weight: 550;
				margin-bottom: 12rpx;
			}
		}
		.button {
			display: flex;
			align-items: center;
		}
	}
}
</style>