<template>
	<view class="cateBox">
		<!-- 左侧列表 -->
		<scroll-view scroll-y class="cate-left">
			<view class="cate-left-list" v-for="(item,index) in data" :key="index" @click="tapselect(index)">
				<view class="cate-left-item " :class="{activeItem:currentTab==index}">
					<label></label><u--text :text="item.name" :lines="1">{{item.name}}</u--text>
				</view>
			</view>
		</scroll-view>

		<!-- 右侧列表 -->
		<scroll-view scroll-y class="cate-right">
			<slot></slot>
		</scroll-view>
	</view>
</template>

<script>
	export default {
		props: {
			data: {
				type: Array,
				default () {
					return []
				}
			}
		},
		data() {
			return {
				currentTab: 0,
				list: [{}],

			}
		},
		methods: {
			//点击左侧按钮
			tapselect(index) {
				// 左边侧边栏点击了
				this.currentTab = index;
				this.$emit('itemClick', this.data[index])
			}
		}
	}
</script>

<style lang="scss" scoped>
	.cateBox {
		display: flex;
		overflow: hidden;
		height: 100%;
		background-color: $uni-bg-color-hover;
	}

	.cate-left {
		width: 260rpx;
		overflow-y: auto;
	}

	.cate-left-item {
		height: 50px;
		display: flex;
		padding-left: 10rpx;
		align-items: center;
	}

	.cate-left-item.activeItem {
		background: #fff;
	}
	/* 
 */

	.cate-left-item label {
		width: 6rpx;
		height: 15px;
		float: left;
		margin-right: 10rpx;
	}

	.cate-left-item.activeItem label {
		background: $lego-theme;
	}

	/* 右侧*/
	.cate-right {
		flex: 1;
		overflow-y: auto;
		background-color: $uni-bg-color;
	}

</style>