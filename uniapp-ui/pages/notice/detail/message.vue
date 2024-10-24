<template>
	<view class="container">
		<NavBar showBack :title="message.name"/>
		<view class="item">
				<view class="text"><view class="title">创建人:</view>{{ message.creator.name }}</view>
		</view>
		<view class="item">
				<view class="text"><view class="title">推送时间:</view>{{ message.createTime }}</view>
		</view>
		<view class="message-content">
			<u-parse :content="message.content" :tag-style="{ table: 'width: 100%!importent'}"></u-parse>
		</view>
	</view>
</template>

<script>
import * as MessageAPI from '@/api/message';
export default {
	data() {
		return {
			message: {
				creator: {}
			}
		}
	},
	onLoad(data) {
		const app = this;
		if (data.code) {
			MessageAPI.detail(data.code).then(res => {
				app.message = res.data;
			})
		}
	}
}
</script>

<style lang="scss" scoped>
.container {
	padding: 10rpx;
}
.item {
	display: flex;
	height: 70rpx;
	cursor:pointer;
	.title {
		font-weight: bold;
		display: flex;
		float: left;
		margin-right: 10rpx;
	}
	.text {
		padding: 10rpx;
		border-bottom: solid 1px #cccccc;
		width: 100%;
		text-align: left;
		font-size: 13px;
	}
}
.message-content {
	padding: 20rpx 0rpx;
	width: 100%;
	table {
		width: 100%!important;
	}
}
</style>