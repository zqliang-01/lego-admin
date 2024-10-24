<template>
	<view v-if="type === 'BarChart'">
		<BarChart :data="data"/>
	</view>
	<view v-else-if="type === 'CircularChart'">
		<CircularChart :data="data"/>
	</view>
	<view v-else-if="type === 'FunnelChart'">
		<FunnelChart :data="data"/>
	</view>
	<view v-else-if="type === 'HorizontalBarChart'">
		<HorizontalBarChart :data="data"/>
	</view>
	<view v-else-if="type === 'LineChart'">
		<LineChart :data="data"/>
	</view>
	<view v-else-if="type === 'PieChart'">
		<PieChart :data="data"/>
	</view>
</template>

<script>
import BarChart from './barChart'
import CircularChart from './circularChart'
import FunnelChart from './funnelChart'
import HorizontalBarChart from './horizontalBarChart'
import LineChart from './lineChart'
import PieChart from './pieChart'

export default {
	components: {
		BarChart,
		CircularChart,
		FunnelChart,
		HorizontalBarChart,
		LineChart,
		PieChart
	},
	props: {
		type: String,
    data: {
      type: Object,
      default: function() {
        return {
					name: ''
				}
      }
    }
	},
	data() {
		return {
			
		}
	},
	methods: {
		
	}
}
</script>

<style>

</style>
