<template>
	<view class="item-box lego-box" v-loading="loading">
		<view class="item-box-title">
			<text>{{ data.name }}</text>
		</view>
		<view class="item-box-content">
			<qiun-data-charts type="column" :canvas2d="open2d" :canvasId="canvaId" :opts="opts" :chartData="chartData" />
		</view>
	</view>
</template>

<script>
import * as HomeApi from '@/api/home'
import mixin from '../components/mixin'
export default {
	name: 'BarChart',
	mixins: [mixin],
	data() {
		return {
			title: '柱状图',
			chartData: {
				categories: [],
				series: []
			},
			//您可以通过修改 config-ucharts.js 文件中下标为 ['column'] 的节点来配置全局默认参数，如都是默认参数，此处可以不传 opts 。实际应用过程中 opts 只需传入与全局默认参数中不一致的【某一个属性】即可实现同类型的图表显示不同的样式，达到页面简洁的需求。
			opts: {
				color: this.color,
				padding: [15, 15, 0, 5],
				enableScroll: false,
				legend: {},
				xAxis: {
					disableGrid: true
				},
				yAxis: {
					data: [{
						min: 0
					}]
				},
				extra: {
					column: {
						type: "group",
						width: 20,
						activeBgColor: "#000000",
						activeBgOpacity: 0.08,
						linearType: "custom",
						seriesGap: 5,
						linearOpacity: 0.5,
						barBorderCircle: true,
						customColor: [
							"#FA7D8D",
							"#EB88E2"
						]
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
				const titles = res.data.titles
				const dataMap = new Map()
				titles.forEach(title => {
					if (app.data.dataCategories.includes(title.sqlKey)) {
						dataMap.set({ code: title.sqlKey, name: title.name }, [])
					}
				})
				app.chartData.categories = []
				res.data.results.forEach(element => {
					dataMap.forEach((value, key) => {
						value.push(element[key.code])
					})
					app.chartData.categories.push(element[app.data.dataDimension])
				})
				app.chartData.series = app.getChartSeries(dataMap)
				app.loading = false
			}).catch(() => {
				app.loading = false
			})
		},
    getChartSeries(dataMap) {
      const series = []
      dataMap.forEach((value, key) => {
        series.push({
          name: key.name,
          data: value
        })
      })
      return series
    }
	}
};
</script>

<style lang="scss" scoped>
  @import "../components/chartStyle";
</style>