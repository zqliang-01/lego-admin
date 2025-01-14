<template>
	<LegoPopup :visible="visible" :title="actionTitle" @close="handleClose" @success="handleConfirm">
		<scroll-view v-if="setting.length > 0" scroll-y style="max-height: 60vh;flex-grow: 1;">
			<u-cell-group>
				<u-radio-group v-model="selectedData.code" placement="column">
					<u-cell
						v-for="(item, index) in setting"
						:key="index"
						:title="item.name"
						@click="handleSelect(item)">
						<u-radio slot="icon" shape="circle" :name="item.code" @change="handleSelect(item)"></u-radio>
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
	data() {
		return {
			selectedData: {
				code: ''
			}
		}
	},
	methods: {
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