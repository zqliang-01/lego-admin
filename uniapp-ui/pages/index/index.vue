<template>
	<view>
		<NavBar :showBack="false" title="首页"/>
		<view class="index-body">
      <view v-for="(item, index) in topList" :key="index">
        <TopReport :data="item" />
      </view>
		</view>
		<view class="index-body">
			<view v-for="(item, num) in chartList" :key="num">
				<ChartView :type="item.chartType" :data="item" />
			</view>
		</view>
		<Empty v-if="isEmpty"/>
	</view>
</template>

<script>
import store from '@/store'
import * as HomeApi from '@/api/home'
import * as MessageAPI from '@/api/message';
import { setMessageTabBadge } from '@/utils/app'
import ChartView from './charts'
import TopReport from './components/topReport'
import Empty from '@/components/empty'

const App = getApp()
export default {
	components: {
		TopReport,
		ChartView,
		Empty
	},
	data() {
		return {
			currentUserId: '',
      topList: [],
      chartList: []
		}
	},
	computed: {
		isEmpty() {
			return this.topList.length === 0 && this.chartList.length === 0
		}
	},
	onLoad() {
		this.init();
		this.handleUpdateMessageCount();
	},
	methods: {
		handleUpdateMessageCount() {
			const app = this;
			MessageAPI.unreadCount().then(res => {
				setMessageTabBadge(res.data.all)
			})
		},
		init() {
			const app = this;
			app.topList = [];
			app.chartList = [];
			HomeApi.list().then(res => {
				res.data.forEach(data => {
					if (data.position === 'top' && data.enable) {
						app.topList.push(data);
					} else {
						app.chartList.push(data);
					}
				})
				uni.stopPullDownRefresh()
			})
		},
		onPullDownRefresh() {
			this.init()
		}
	},
	onShareAppMessage() {
		const app = this
		return {
			title: config.name,
			path: "/pages/index/index?" + app.$getShareUrlParams()
		}
	},
	/**
	 * 分享到朋友圈
	 * 本接口为 Beta 版本，暂只在 Android 平台支持，详见分享到朋友圈 (Beta)
	 * https://developers.weixin.qq.com/miniprogram/dev/framework/open-ability/share-timeline.html
	 */
	onShareTimeline() {
		const app = this
		const { page } = app
		return {
			title: config.name,
			path: "/pages/index/index?" + app.$getShareUrlParams()
		}
	}
}
</script>

<style lang="scss" scoped>
</style>