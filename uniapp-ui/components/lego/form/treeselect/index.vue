<template>
	<view>
		<u-form-item
			borderBottom
			:prop="item.fieldCode"
			:rules="item.rules"
			:label="item.name"
			:required="item.required"
			@click="handleShowAction">
			<u--input
				v-model="innerValue"
				border="none"
				disabled
				:disabledColor="isDisabled ? '#f1f1f1' : '#ffffff'"
				:placeholder="item.placeholder || actionTitle"
				:clearable="clearable"
				@click="handleShowAction"/>
			<view slot="right">
				<u-icon v-if="!isDisabled" name="arrow-right"></u-icon>
			</view>
		</u-form-item>
		<u-popup :show="showAction" @close="showAction = false">
			<view>
				<u-toolbar
					class="action-title"
					:title="actionTitle"
					@cancel="showAction = false"
					@confirm="handleConfirm"></u-toolbar>
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
			</view>
		</u-popup>
	</view>
</template>

<script>
import Mixin from '../mixin'
export default {
  mixins: [Mixin],
	data() {
		return {
			showAction: false,
			innerValue: '',
			parentCode: '',
			selectedData: {
				code: ''
			},
			tabList: [],
			dataList: this.item.setting
		}
	},
	watch: {
		showAction(val) {
			if (val) {
				this.tabList = []
				this.selectedData = { code: '' }
				this.dataList = this.item.setting
			}
		},
		item: {
			handler(newVal, oldVal) {
				if (newVal.value) {
					this.innerValue = newVal.value.name
				} else {
					this.innerValue = newVal.value
				}
			},
			immediate: true
		}
	},
	methods: {
		handleName(name) {
			if (name) {
				return name.slice(name.length - 2)
			}
			return name
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
		handleParent(data, index) {
			if (data === 'root') {
				this.tabList = []
				this.dataList = this.item.setting
				return;
			}
			this.tabList = this.tabList.slice(0, index + 1);
			this.dataList = data.childrens
			this.selectedData = { code: '' }
		},
		handleSelect(data) {
			this.selectedData = data
		},
		handleShowAction() {
			uni.hideKeyboard()
			if (this.disabled || this.item.disabled) {
				return;
			}
			this.showAction = true
		},
		handleConfirm() {
			this.innerValue = this.selectedData.name
			this.commonChange(this.item, this.selectedData.code)
			this.showAction = false
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