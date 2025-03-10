<template>
  <view class="container">
		<NavBar :showBack="false" title="应用中心"/>
		<view v-for="(item) in appList" :key="item.code">
			<template v-if="item.list.length > 0">
				<view class="app-group-name">
					<u-line
						margin="7rpx 5rpx"
						direction="col"
						length="9"
						color="#2362FB"
						:customStyle="{borderLeftWidth: '15rpx', borderRadius: '5rpx'}"></u-line>
					<text>{{ item.name }}</text>
				</view>
				<view class="app-group">
					<u-grid :col="4">
						<u-grid-item v-for="(app, index) in item.list" :key="index">
							<view class="grid-item" @click="handleClick(app)">
								<LegoIcon :icon="app.icon" :backgroundColor="color[index % 19]" :fontSize="45" :size="80" :width="80"/>
								<u--text :text="app.name" align="center" wordWrap="anywhere" lines="4" size="12" margin="10rpx 0rpx"></u--text>
							</view>
						</u-grid-item>
					</u-grid>
				</view>
			</template>
		</view>
		<Empty v-if="appList.length === 0"/>
  </view>
</template>

<script>
import loader from '@/pagesapp/components/loader'
import * as PermissionApi from '@/api/app/permission'
import { isObject } from '@/utils/util'

export default {
	data() {
		return {
			appList: [],
			color: [
			  '#FF7474',
			  '#F59561',
			  '#6CA2FF',
			  '#6AC9D7',
			  '#72DCA2',
			  '#48E78D',
			  '#FECD51',
			  '#DBB375',
			  '#FF7474',
			  '#F59561',
			  '#A3AEBC',
			  '#4C84FF',
			  '#0DBEB4',
			  '#00DEDE',
			  '#FFAA00',
			  '#C7C116',
			  '#F7A57C',
			  '#F661AC',
			  '#8652EE'
			],
			systemAppList: loader
		}
	},
	onLoad() {
		this.getPageData()
	},
	methods: {
		getPageData() {
			const app = this
			PermissionApi.current().then(res => {
				app.appList = []
				res.data.forEach(data => {
					if (data.type.code === 'app') {
						let systemApp = app.systemAppList.find(systemApp => systemApp.code === data.code)
						if (systemApp) {
							app.appList.push({
								code: data.code,
								name: data.name,
								list: systemApp.list.filter(children => {
									return children.auth === false || app.systemAppAuth(children, data.childrens)
								})
							})
						} else {
							app.appList.push({
								code: data.code,
								name: data.name,
								list: app.buildDynamicAppList(data.childrens)
							})
						}
					}
				})
				uni.stopPullDownRefresh()
			})
		},
		buildDynamicAppList(childrens) {
			const app = this
			let appList = []
			childrens.forEach(data => {
				const hasForm = data.form && data.form.code
				if (data.menu && data.dynamicRoute && hasForm && data.childrens.length === 0) {
					appList.push({
						code: data.code,
						icon: data.icon ? data.icon : 'loading',
						name: data.name,
						formCode: data.form.code,
						type: 'dynamic'
					})
				} else if (data.childrens > 0) {
					appList = appList.concat(app.buildAppList(data.childrens))
				}
			})
			return appList
		},
		systemAppAuth(systemApp, childrens) {
			if (systemApp.auth) {
				return true
			}
			const app = this
			let auth = false
			childrens.forEach(children => {
				if (!auth && children.childrens.length > 0) {
					auth = app.systemAppAuth(systemApp, children.childrens)
				} else if (!auth && children.code == systemApp.code) {
					auth = true
				}
			})
			return auth
		},
		handleClick(item) {
			if (item.type === 'dynamic') {
				const data = { formCode: item.formCode, name: item.name, menuCode: item.code }
				this.$navTo('pagesapp/common/index', data)
			} else if (item.path) {
				this.$navTo(item.path)
			} else {
				this.$toast('应用[' + item.name + ']未授权！')
			}
		},
		onPullDownRefresh() {
			this.getPageData()
		}
	}
}
</script>

<style lang="scss" scoped>
.container {
	.app-group {
		padding-top: 5px;
		background: #fff;
		border-top: 1px solid #eee;
		border-bottom: 1px solid #eee;
		&-name {
			color: #555;
			padding: 20rpx;
			font-size: 30rpx;
			line-height: 14px;
			font-weight: 700;
			display: flex;
			align-items: center;
		}
	}
}
.app-item {
	background-color: $uni-bg-color;
	margin: 20rpx;
	overflow: hidden;
	border-radius: $uni-border-radius-infos;
	box-shadow: 0rpx 2rpx 10rpx 2rpx rgba(0,122,255,0.08);
}
.grid-item {
	padding: 25rpx 0rpx;
	text-align: center;
}
</style>
