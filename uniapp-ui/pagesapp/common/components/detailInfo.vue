<template>
	<view class="information-box">
		<view v-for="(list, id) in allFieldList" :key="id" class="information-box-body">
			<view class="title">
				<text class="point"></text>
				<text class="text">{{list.name}}</text>
			</view>
			<view class="information-list">
				<view v-for="(item, index) in list.data" :key="index">
					<view v-if="!item.simpleType" class="information-list-item">
						<text class="label" :style="{ minWidth: labelWidth + 'rpx' }">{{item.name}}：</text>
						<text
							v-if="item.formType === 'entity'"
							class="link"
							@click="handleClick(item)">{{handleViewValue(item)}}</text>
						<text v-else class="value">{{handleViewValue(item)}}</text>
					</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
import { getShowValue } from '@/utils/form'
import { calculateStrLength } from '@/utils/util'
export default {
	name: 'information',
	props: {
		fieldList: Array,
		detailData: {
			type: Object,
			default: () => {
				return {}
			}
		}
	},
	watch: {
		fieldList: {
			handler(newVal, oldVal) {
				this.allFieldList = [
					{
						name: '基本信息',
						data: newVal
					},
					{
						name: '系统信息',
						data: [
							{ fieldCode: 'createTime', name: '创建时间', formType: 'text' },
							{ fieldCode: 'updateTime', name: '更新时间', formType: 'text' },
							{ fieldCode: 'creator', name: '创建人', formType: 'user' }
						]
					}
				]
			},
			immediate: true
		}
	},
  computed: {
		labelWidth() {
			let num = 8
			this.fieldList.forEach(field => {
				let tmpNum = calculateStrLength(field.name)
				if (tmpNum > num && !field.simpleType) {
					num = tmpNum;
				}
			})
			return num / 2 * 40
		}
  },
	data() {
		return {
			loading: false,
			allFieldList: []
		}
	},
	methods: {
		handleViewValue(item) {
			const formType = item.formType
			const value = this.detailData[item.fieldCode]
			return getShowValue(formType, value)
		},
		handleClick(item) {
			if (item.formType === 'entity') {
				const entityCode = this.detailData[item.fieldCode].code
				const data = {formCode: item.relativeForm.code, detailCode: entityCode}
				this.$navTo('pagesapp/common/detail', data)
			}
		}
	}
}
</script>

<style lang="scss" scoped>
.information-box {
	position: relative;
	padding-bottom: 20rpx;
	&-body {
		position: relative;
		margin: $uni-ios-margin;
		margin-top: 0rpx;
		background-color: $uni-bg-color;
		padding: $uni-spacing-col-base;
		border-radius: $uni-spacing-row-base;
		box-shadow: $uni-ios-shdow;
		overflow: hidden;
		.title {
			margin-top: -10rpx;
			border-bottom: 1px solid $uni-ios-gray;
			line-height: 76rpx;
			.point {
				vertical-align: middle;
				display: inline-block;
				width: 14rpx;
				height: 14rpx;
				margin: 0 10rpx;
				background-color: $uni-ios-font;
				border-radius: 16rpx;
			}
			.text {
				vertical-align: middle;
				font-size: 35rpx;
				font-weight: 550;
				color: $uni-ios-font;
			}
		}
	}
}
.information-list {
	&-item {
		border-bottom: 1px solid $uni-ios-gray;
		padding: $uni-spacing-col-base;
		display: flex;
		flex-direction: row;
		align-items: center;
		.label {
			display: flex;
			flex-direction: row;
			align-items: center;
			justify-content: flex-end;
			min-height: 30rpx;
			font-size: 30rpx;
			line-height: 40rpx;
			min-width: 160rpx;
			color: $uni-ios-label;
		}
		.value {
			min-height: 30rpx;
			font-size: 30rpx;
			line-height: 40rpx;
			flex: 1;
			color: $uni-ios-font;
		}
		.link {
			font-weight: 550;
			min-height: 30rpx;
			font-size: 30rpx;
			line-height: 35rpx;
			color: $uni-color-primary;
		}
	}
}
</style>
