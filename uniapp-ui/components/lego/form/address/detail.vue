<template>
	<view>
		<NavBar title="地址填写" :autoBack="false" @leftClick="handleBack"/>
		<u-form-item label="地址区域" borderBottom @click="handleShowAction">
			<u--input
				v-model="innerArea"
				border="none"
				disabled
				disabledColor='#ffffff'
				:placeholder="placeholder"
				@click="handleShowAction"/>
			<view slot="right">
				<u-icon name="arrow-right"></u-icon>
			</view>
		</u-form-item>
		<u-form-item label="详细地址" borderBottom>
			<u--input
				v-model="selectedData.detail"
				border="none"
				disabledColor='#ffffff'
				placeholder="请输入详细地址"/>
		</u-form-item>
		<u-button
			type="primary"
			text="确认"
			customStyle="margin-top: 30rpx"
			@click="handleSumbit"></u-button>
		<u-popup :show="showAction" @close="showAction = false">
			<view>
				<u-toolbar
					class="action-title"
					@cancel="showAction = false"
					@confirm="handleConfirm"></u-toolbar>
				<u-scroll-list class="action-scroll" :indicator="false">
					<view class="action-tabs">
						<template v-for="(item, index) in tabList">
							<text :class="['action-tabs-text', { 'action-tabs-text-active': item.type === currentType }]" @click="handleTabClick(item, index)">{{item.name}}</text>
						</template>
					</view>
				</u-scroll-list>
				<scroll-view v-if="dataList.length > 0" scroll-y style="max-height: 60vh;flex-grow: 1;">
					<u-cell-group>
						<u-radio-group placement="column" v-model="currentValue">
							<u-cell
								v-for="(item, index) in dataList"
								:key="index"
								:title="item.name"
								@click="handleSelect(item)">
								<u-radio slot="icon" shape="circle" :name="item.code"></u-radio>
								<u-icon slot="right-icon" name="arrow-right" color="info"></u-icon>
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
import { getChildren, getNode, getName } from '@/utils/data/address'
import { objDeepCopy } from '@/utils/util'
export default {
	props: {
		value: [String, Number, Boolean, Object],
		placeholder: String
	},
	data() {
		return {
			showAction: false,
			innerArea: '',
			currentType: 'province',
			currentValue: '',
			selectedData: {
				province: '',
				city: '',
				area: '',
				detail: ''
			},
			tabList: [],
			dataList: []
		}
	},
	watch: {
		value: {
			handler(newVal, oldVal) {
				if (newVal) {
					this.selectedData = newVal
					this.innerArea = getName(newVal.province) + getName(newVal.city) + getName(newVal.area)
				}
			},
			immediate: true,
			deep: true
		},
		showAction(val) {
			if (val) {
				if (this.selectedData.province) {
					const province = getNode(this.selectedData.province) || {}
					const city = getNode(this.selectedData.city, province.children) || {}
					const area = getNode(this.selectedData.area, city.children) || {}
					this.tabList = [{
							name: province.name,
							value: province.code,
							dataList: getChildren(),
							type: 'province'
						},
						{
							name: city.name,
							value: city.code,
							dataList: province.children,
							type: 'city'
						},
						{
							name: area.name,
							value: area.code,
							dataList: city.children,
							type: 'area'
						}
					]
					this.currentType = 'province'
					this.currentValue = province.code
				} else {
					this.tabList = [{
						name: '请选择',
						value: '',
						dataList: getChildren(),
						type: 'province'
					}]
					this.currentType = 'province'
					this.currentValue = ''
				}
				this.dataList = getChildren()
			}
		}
	},
	methods: {
		handleTabClick(data, index) {
			this.dataList = data.dataList
			this.currentType = data.type
			this.$nextTick(() => {
				this.currentValue = data.value
			})
		},
		handleSelect(data) {
			if (this.currentValue) {
				this.handleCleanTab(this.currentType)
			}
			this.currentValue = data.code
			let currentTab = this.handleGetTab(this.currentType)
			currentTab.name = data.name
			currentTab.value = data.code
			if (data.children && data.children.length > 0) {
				const nextType = this.handleNextType(this.currentType)
				let children = this.handleGetTab(nextType)
				if (!children.dataList) {
					this.tabList.push(children)
				}
				children.name = '请选择'
				children.value = ''
				children.dataList = data.children

				this.$nextTick(() => {
					this.currentValue = ''
				})
				this.dataList = data.children
				this.currentType = children.type
			}
		},
		handleGetTab(type) {
			return this.tabList.find(tab => tab.type === type) || { type: type }
		},
		handleNextType(type) {
			if (type === 'province') {
				return 'city'
			} else if (type === 'city') {
				return 'area'
			}
			return type
		},
		handleCleanTab(type) {
			if (type && type !== 'area') {
				type = this.handleNextType(type)
				this.tabList = this.tabList.filter(tab => tab.type !== type)
				this.handleCleanTab(type)
			}
		},
		handleShowAction() {
			uni.hideKeyboard()
			this.showAction = true
		},
		handleConfirm() {
			this.showAction = false
			const province = this.handleGetTab('province')
			const city = this.handleGetTab('city')
			this.selectedData.province = province.value
			this.innerArea = province.name
			if (city && city.value) {
				this.selectedData.city = city.value
				this.innerArea = this.innerArea + city.name
			}
			const area = this.handleGetTab('area')
			if (area && area.value) {
				this.selectedData.area = area.value
				this.innerArea = this.innerArea + area.name
			}
		},
		handleSumbit() {
			this.$emit('success', this.selectedData)
		},
		handleBack() {
			this.$emit('back')
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
	padding: 20rpx 10rpx;
	&-text {
		white-space: nowrap;
		font-size: 15px;
		padding: 0rpx 25rpx;
		&-active {
			color: #0095F2;
			font-weight: bold;
		}
	}
}
</style>