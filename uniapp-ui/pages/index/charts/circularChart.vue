<template>
	<view class="item-box lego-box" v-loading="loading">
		<view class="item-box-title">
			<text>{{ data.name }}</text>
		</view>
		<view class="item-box-content">
			<qiun-data-charts type="ring" :canvas2d="open2d" :canvasId="canvaId" :opts="opts" :chartData="chartData" />
		</view>
	</view>
</template>

<script>
import * as HomeApi from '@/api/home'
import mixin from '../components/mixin'
export default {
	name: 'CircularChart',
	mixins: [mixin],
	data() {
		return {
			title: '环形图',
			chartData: {
				series: []
			},
			opts: {
        rotate: false,
        rotateLock: false,
        color: this.color,
        padding: [5, 5, 5, 5],
        dataLabel: true,
        enableScroll: false,
        legend: {
          show: true,
          position: "bottom",
          lineHeight: 25
        },
        title: {
          name: "",
          fontSize: 15,
          color: "#666666"
        },
        subtitle: {
          name: "",
          fontSize: 20,
          color: "#7cb5ec"
        },
        extra: {
          ring: {
            ringWidth: 30,
            activeOpacity: 0.5,
            activeRadius: 10,
            offsetAngle: 0,
            labelWidth: 15,
            border: false,
            borderWidth: 3,
            borderColor: "#FFFFFF"
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