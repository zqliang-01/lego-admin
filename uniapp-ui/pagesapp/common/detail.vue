<template>
  <view class="container">
		<NavBar :title="title"/>
		<DetailHead :title="detailData.name" :icon="icon">
			<view class="detail-head">
				<text class="detail-head__label">编码：</text>
				<text class="detail-head__text">{{detailData.code}}</text>
			</view>
			<view class="detail-head">
				<text class="detail-head__label">更新时间：</text>
				<text class="detail-head__text">{{detailData.updateTime}}</text>
			</view>
			<view slot="right">
				<u-button color="#ff7300" icon="edit-pen" iconColor="#fff" size="mini" text="编辑" @click="handleUpdate" />
			</view>
		</DetailHead>
		<u-sticky bgColor="#f5f5f5" :offsetTop="offsetHeight">
			<u-tabs
				:lineWidth="0"
				:list="tabList"
				:activeStyle="{color: '#00aaff', fontWeight: 550 }"
				:scrollable="false"
				@change="handleTypeChange"></u-tabs>
		</u-sticky>
		<view class="detail-tab">
			<DetailInfo v-show="currentType === 'info'" :fieldList="fieldList" :detailData="detailData"></DetailInfo>
			<DetailLog v-show="currentType === 'log'" :type="currentType" :menuCode="menuCode" :detailCode="detailData.code"></DetailLog>
			<DetailFile v-show="currentType === 'file'" :type="currentType" :menuCode="menuCode" :detailCode="detailData.code"></DetailFile>
		</view>
	</view>
</template>

<script>
import DetailInfo from './components/detailInfo'
import DetailLog from './components/detailLog'
import DetailFile from './components/detailFile'
import DetailHead from './components/detailHead'

import * as FieldAPI from '@/api/form/field'
import * as FormAPI from '@/api/form/index'
import ModelMixin from '@/components/lego/form/modelMixin'
export default {
	mixins: [ModelMixin],
	components: {
		DetailLog,
		DetailInfo,
		DetailFile,
		DetailHead
	},
	data() {
		return {
			icon: '',
			title: '',
			formCode: '',
			menuCode: '',
			detailCode: '',
			detailData: {},
			offsetHeight: 0,
			currentType: 'info',
			tabList: [{
        name: '详细资料',
				code: 'info'
      },
			{
			  name: '操作日志',
				code: 'log'
			},
			{
			  name: '附件',
				code: 'file'
			}]
		}
	},
	onLoad(data) {
		const app = this
		app.initField(data)
		uni.$on('common-add',function(data){
			if (data.type === 'save-success') {
				app.initData()
			}
		})
		uni.getSystemInfo({
			success: (e) => {
				// #ifdef MP-WEIXIN
				let custom = uni.getMenuButtonBoundingClientRect();
				this.offsetHeight = custom.bottom + custom.top - e.statusBarHeight;
				// #endif
			}
		});
	},
	onUnload() {
		uni.$off('common-add');
	},
	methods: {
		initField(data) {
			const app = this
			app.formCode = data.formCode
			app.detailCode = data.detailCode
			FieldAPI.listDetail(data.formCode).then(res => {
				app.initRequest(res.data.form)
				app.fieldList = res.data.fields
				app.initData()
			})
		},
		initData() {
			const app = this
			FormAPI.permission(app.formCode).then(res => {
				app.icon = res.data.icon
				app.title = res.data.permissionName
				app.menuCode = res.data.permissionCode
				app.detailRequest(app.detailCode).then(res => {
					uni.stopPullDownRefresh()
					app.detailData = res.data
				}).catch(() => {
					uni.stopPullDownRefresh()
				})
			}).catch(() => {
				uni.stopPullDownRefresh()
			})
		},
		handleTypeChange(data) {
			this.currentType = data.code
		},
		handleUpdate() {
			this.$navTo('pagesapp/common/create', {formCode: this.formCode, detailCode: this.detailData.code})
		},
		onPullDownRefresh() {
			this.initData()
		}
	}
}
</script>

<style lang="scss" scoped>
.detail-head {
	padding-top: 15rpx;
	font-size: 13px;
	line-height: 17px;
	font-weight: 550;
	color: rgba(29, 29, 31, .8);
}
.detail-tab {
	padding-top: 20rpx;
}
</style>