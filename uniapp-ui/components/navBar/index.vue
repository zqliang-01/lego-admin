<template>
	<u-navbar
		:autoBack="showBack"
		:title="title"
		:titleStyle ="titleStyle "
		:bgColor="backgroundColor"
		:border="false"
		:placeholder="true">
		<view slot="left">
			<slot name="left">
				<u-icon
					v-if="showBack"
					name="arrow-left"
					size="15"
					color="#fff"></u-icon>
			</slot>
		</view>
	</u-navbar>
</template>

<script>
  export default {
    name: "NavBar",
    props: {
			showBack: {
				type: Boolean,
				default: true
			},
      title: String,
			backgroundColor: {
				type: String,
				default: '#007aff'
			}
    },
		data() {
			return {
				titleStyle: {
					color: '#FFF'
				}
			}
		},
    methods: {
    }
  }
</script>

<style lang="scss" scoped>
</style>
