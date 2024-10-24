<template>
  <view class="container">
		<NavBar :showBack="false" title="消息中心">
			<u-checkbox-group slot="left">
				<u-checkbox
					size="14"
					label="未读"
					shape="circle"
					labelSize="14"
					activeColor="red"
					labelColor="#fff"
					@change="handleUnread"></u-checkbox>
			</u-checkbox-group>
		</NavBar>
		<view class="tabs-fixed">
			<u-tabs
				bold
				:itemStyle="itemStyle"
				:lineWidth="60"
				:lineHeight="3"
				:scrollable="false"
				:list="tabList"
				@change="handleTypeChange"></u-tabs>
		</view>
		<view v-for="(item,index) in tabList" :key="index">
			<view v-if="currentType === item.code" class="message-list">
				<MessageList :dataList="dataList" @onReaded="handleReaded" @onDelete="handleDelete"/>
				<view class="com-loadmore">
					<u-loadmore :status="loadStatus" @loadmore="handleLoadMore" />
				</view>
			</view>
		</view>
  </view>
</template>

<script>
import store from '@/store'
import * as MessageAPI from '@/api/message';
import MessageList from './components/messageList'
import { setMessageTabBadge } from '@/utils/app'

export default {
	components: {
		MessageList
	},
	data() {
		return {
			currentUserId: '',
			unread: false,
			pageIndex: 1,
			loadStatus: 'loadmore',
			dataList: [],
			currentType: 'all',
			tabList: [{
        name: '全部',
				code: 'all',
				badge: {
					value: 0
				}
      },
      {
        name: '审批',
        code: 'flowable',
				badge: {
					value: 0
				}
      },
      {
        name: '任务',
        code: 'form',
				badge: {
					value: 0
				}
      }],
			itemStyle: {
				height: '90rpx'
			}
		}
	},
	onReachBottom() {
		this.handleLoadMore()
	},
	onLoad() {
		this.init()
	},
	onShow() {
		this.handleUpdateCount()
	},
	methods: {
		onPullDownRefresh() {
			this.init()
		},
		init() {
			this.pageIndex = 1
			this.dataList = []
			this.handleUpdateCount()
			this.handleQuery()
		},
		handleUnread(data) {
			console.log(data)
			this.unread = data;
			this.init()
		},
		handleTypeChange(data) {
			this.currentType = data.code
			this.init()
		},
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
			MessageAPI.list({
				pageIndex: app.pageIndex,
				pageSize: 10,
				readed: app.unread ? false : null,
				type: app.currentType === 'all' ? '' : app.currentType
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
		handleReaded(code, index) {
			const app = this;
			MessageAPI.read(code).then(() => {
        app.dataList[index].readed = true
        app.handleUpdateCount()
			})
		},
		handleDelete(code, index) {
			const app = this;
			MessageAPI.deleted(code).then(() => {
				app.dataList.splice(index, 1)
				app.$toast('删除成功！')
        app.handleUpdateCount()
			})
		},
		handleUpdateCount() {
			const app = this;
			MessageAPI.unreadCount().then(res => {
				app.tabList.forEach(tab => {
					tab.badge.value = res.data[tab.code]
				})
				setMessageTabBadge(res.data.all, 1)
			})
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
.message-list {
	padding-top: 90rpx;
}
.com-loadmore{
	padding: 15rpx;
	.u-more-text{
		font-weight: 500;
		font-size: 26rpx !important;
		color: rgb(185, 185, 185) !important;
	}
}
</style>
