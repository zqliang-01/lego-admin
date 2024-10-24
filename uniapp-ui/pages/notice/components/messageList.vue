<template>
  <view class="container">
		<view class="message-item" v-for="(item,index) in dataList" :key="index">
			<view @click="handleDetail(item, index)">
				<view class="time">
					<view class="text">
						<LegoIcon :icon="handleIcon(item)" :backgroundColor="handleIconColor(item)" fontSize="30" size="50"/>
						<text>{{formatTime(item.createTime)}}</text>
					</view>
					<u-badge v-if="!item.readed" absolute :offset="[0,0]" type="error" :isDot="true" />
				</view>
				<view class="message">
					<view class="text">
						<text v-if="item.type.code === 'flowable'">{{item.content}}</text>
						<text v-else>{{item.name}}</text>
					</view>
				</view>
			</view>
			<view class="btns">
				<text v-if="!item.readed" class="link" @click="handleReaded(item, index)">标记已读</text>
				<text class="link" @click="handleDelete(item, index)">删除</text>
			</view>
		</view>
	</view>
</template>

<script>
import moment from '@/components/moment'

export default {
	props: {
		dataList: Array
	},
	data() {
		return {
			options: [{
				text: '删除'
			}]
		}
	},
	methods: {
		handleIcon(data) {
			if (data.type.code === 'flowable') {
				return 'office'
			}
			return 'icon-full-clock'
		},
		handleIconColor(data) {
			if (data.type.code === 'flowable') {
				return '#2362FB'
			}
			return '#FF6666'
		},
		handleReaded(item, index) {
			this.$emit('onReaded', item.code, index)
		},
		handleDelete(item, index) {
			const app = this;
			uni.showModal({
				title: '提示：',
				content: '请确认是否删除该消息？',
				success: function(res) {
					if (res.confirm) {
						app.$emit('onDelete', item.code, index)
					}
				}
			});
		},
		handleDetail(item, index) {
			const app = this;
			if (!item.readed) {
				app.$emit('onReaded', item.code, index)
			}
			if (item.type.code === 'flowable') {
				app.$navTo('pages/notice/detail/task', {code: item.entityCode})
			} else {
				app.$navTo('pages/notice/detail/message', {code: item.code})
			}
		},
		formatTime(time) {
		  const timeMoment = moment(time)
		  const nowMoment = moment()
		  const diff = nowMoment.diff(timeMoment, 'seconds')
		
		  if (diff < 30) {
		    return '刚刚'
		  } else if (diff < 3600) {
		    return Math.ceil(diff / 60) + '分钟前'
		  } else if (diff < 3600 * 24) {
		    return Math.ceil(diff / 3600) + '小时前'
		  } else if (diff < 3600 * 24 * 2) {
		    return '1天前'
		  }
		  const timeYear = timeMoment.format('YYYY')
		  const nowYear = nowMoment.format('YYYY')
		  if (timeYear == nowYear) {
		    return timeMoment.format('MM月DD日')
		  } else {
		    return timeMoment.format('YY年MM月DD日')
		  }
		}
	}
}
</script>

<style lang="scss" scoped>
.container {
	.message-item {
		padding: 20rpx;
		margin: 20rpx;
		background-color: $uni-bg-color;
		border-radius: $uni-border-radius-infos;
		box-shadow: 0rpx 2rpx 8rpx 2rpx rgba(0, 122, 255, 0.08);
		.time {
			position: relative;
			font-size: 28rpx;
			color: rgba($uni-ios-font, 0.7);
			margin-bottom: 12rpx;
		}

		.message {
			font-size: 30rpx;
			line-height: 40rpx;
			.name {
				color: #007aff;
			}
		}

		.btns {
			text-align: right;
			.link {
				margin-left: 30rpx;
				color: rgba($uni-ios-font, 0.6);
				font-size: 26rpx;
			}
		}
	}
}
</style>