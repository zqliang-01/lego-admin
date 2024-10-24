<template>
	<view class="item-box lego-box" v-loading="loading">
		<view class="item-box-title">
			<text>{{ data.name }}</text>
		</view>
		<view class="item-box-content">
			<qiun-data-charts type="bar" :canvas2d="open2d" :canvasId="canvaId" :opts="opts" :chartData="chartData" />
		</view>
	</view>
</template>

<script>
import * as HomeApi from '@/api/home'
import mixin from '../components/mixin'
export default {
	name: 'HorizontalBarChart',
	mixins: [mixin],
	data() {
		return {
			title: '横向柱状图',
			chartData: {
				categories: [],
				series: []
			},
			opts: {
        color: this.color,
        padding: [15, 30, 0, 5],
        enableScroll: false,
        legend: {},
        xAxis: {
          boundaryGap: "justify",
          disableGrid: false,
          axisLine: false,
        },
        yAxis: {},
        extra: {
          bar: {
            type: "group",
						width: 20,
            meterBorde: 1,
            meterFillColor: "#FFFFFF",
            activeBgColor: "#000000",
            activeBgOpacity: 0.08,
            linearType: "custom",
            barBorderCircle: true,
            seriesGap: 2,
            categoryGap: 2
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
						value.push(parseFloat(element[key.code]))
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