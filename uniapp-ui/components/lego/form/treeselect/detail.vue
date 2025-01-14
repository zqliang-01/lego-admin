<template>
	<LegoPopup :visible="visible" :title="actionTitle" @close="handleClose" @success="handleConfirm">
		<u-scroll-list class="action-scroll" :indicator="false">
			<view class="action-tabs">
				<text class="action-tabs-text" @click="handleParent('root')">根节点</text>
				<template v-for="(item, index) in tabList">
					/<text class="action-tabs-text" @click="handleParent(item, index)">{{item.name}}</text>
				</template>
			</view>
		</u-scroll-list>
		<scroll-view v-if="dataList.length > 0" scroll-y style="max-height: 60vh;flex-grow: 1;">
			<u-cell-group>
				<u-radio-group v-model="selectedData.code" placement="column">
					<u-cell
						v-for="(item, index) in dataList"
						:key="index"
						:title="item.name"
						@click="handleSelect(item)">
						<u-radio slot="icon" shape="circle" :name="item.code" @change="handleSelect(item)"></u-radio>
						<view slot="right-icon" class="action-item-right">
							<template v-if="item.childrens.length > 0">
								<text @click.stop="handleChildren(item)">下级</text>
								<u-icon name="arrow-right" color="info"></u-icon>
							</template>
						</view>
					</u-cell>
				</u-radio-group>
			</u-cell-group>
		</scroll-view>
		<Empty v-else/>
	</LegoPopup>
</template>

<script>
import LegoPopup from '@/components/lego/popup'
export default {
	components: {
		LegoPopup
	},
	props: {
    visible: {
      type: Boolean,
      default: false
    },
		actionTitle: String,
		setting: Array
	},
	watch: {
		visible: {
			handler(newVal, oldVal) {
				if (newVal) {
					this.tabList = []
					this.dataList = this.setting
				}
			},
			immediate: true
		}
	},
	data() {
		return {
			selectedData: {
				code: ''
			},
			tabList: [],
			dataList: []
		}
	},
	methods: {
		handleParent(data, index) {
			if (data === 'root') {
				this.tabList = []
				this.dataList = this.setting
				return;
			}
			this.tabList = this.tabList.slice(0, index + 1);
			this.dataList = data.childrens
			this.selectedData = { code: '' }
		},
 		handleChildren(data) {
 			if (data.childrens.length === 0) {
 				this.$toast('部门已无下级部门！')
 				return;
 			}
 			this.tabList.push(data)
 			this.dataList = data.childrens
 			this.selectedData = { code: '' }
 		},
		handleSelect(data) {
			this.selectedData = data
		},
		handleClose() {
      this.$emit('close')
		},
		handleConfirm() {
      this.$emit('success', this.selectedData)
		}
	}
}
</script>

<style lang="scss" scoped>
.action-title {
	height: 90rpx;
	background: rgba(0, 0, 0, 0.05);
	border-bottom: 2rpx solid rgba(0, 0, 0, 0.08);
}
.action-scroll {
	padding-bottom: 0rpx;
	background: rgba(0, 0, 0, 0.02);
}
.action-tabs {
	@include flex(column);
	flex-direction: row;
	padding: 5px 10px;
	&-text {
		white-space: nowrap;
		font-size: 15px;
		padding: 0rpx 10rpx;
		color: #0095F2;
	}
}
.action-item-right {
	display: flex;
	color: $u-info;
}
</style>