<template>
	<view class="item-box lego-box" v-loading="loading">
		<view class="item-box-title">
			<text>{{ data.name }}</text>
		</view>
		<view class="item-box-content">
			<qiun-data-charts type="funnel" :canvas2d="open2d" :canvasId="canvaId" :opts="opts" :chartData="chartData" />
		</view>
	</view>
</template>

<script>
import * as HomeApi from '@/api/home'
import mixin from '../components/mixin'
export default {
	name: 'FunnelChart',
	mixins: [mixin],
	data() {
		return {
			title: '漏斗图',
			chartData: {
				series: []
			},
			opts: {
        color: this.color,
        padding: [5, 5, 5, 5],
        dataLabel: true,
        enableScroll: false,
        legend: {
          show: true,
          position: "top"
        },
        extra: {
          funnel: {
            border: true,
            borderWidth: 2,
            borderColor: "#FFFFFF",
            fillOpacity: 1,
            labelWidth: 15,
            labelAlign: "top"
          }
        }
      }
		};
	},
	methods: {
		init() {
			const app = this;
			app.loading = true
			HomeApi.open(app.getBaseParams()).then(res => {
				const data = []
				res.data.results.forEach(element => {
					app.data.dataCategories.forEach(category => {
						data.push({
							name: element[app.data.dataDimension],
							centerText: element[category],
							value: parseFloat(element[category])
						})
					})
				})
				app.chartData.series = [{ data: data }]
				app.loading = false
			}).catch(() => {
				app.loading = false
			})
		}
	}
};
</script>

<style lang="scss" scoped>
  @import "../components/chartStyle";
</style>