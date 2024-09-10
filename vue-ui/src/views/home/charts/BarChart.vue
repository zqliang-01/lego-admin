<template>
  <div
    v-loading="loading"
    class="card">
    <flexbox class="card-title">
      <span :class="[prop.icon, 'icon']" />
      <div class="card-title-center text-one-ellipsis">{{ data.name }}</div>
      <div class="card-title-right"/>
    </flexbox>
    <div class="card-desc"/>
    <div class="bar-chart-canvas" :id="canvaId" />
  </div>
</template>

<script>
import echarts from 'echarts'
import ChartMixin from '../components/ChartMixin'
import { openDashBoardAPI } from '@/api/report/open'

export default {
  name: 'BarChart',
  mixins: [ChartMixin],
  data() {
    return {
      prop: {
        title: '柱状图',
        position: 'right',
        icon: 'hollow-results',
        iconColor: '#4983EF',
        image: require('@/assets/img/skeleton/sort-chart.png')
      },
      chartOption: {
        color: [],
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            // 坐标轴指示器，坐标轴触发有效
            type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
          }
        },
        grid: {
          top: '46px',
          left: '20px',
          right: '20px',
          bottom: '10px',
          containLabel: true,
          borderColor: '#fff'
        },
        legend: {
          right: '20px',
          data: []
        },
        xAxis: [
          {
            type: 'category',
            data: [],
            axisTick: {
              alignWithLabel: true,
              lineStyle: { width: 0 }
            },
            axisLabel: {
              color: '#333'
            },
            /** 坐标轴轴线相关设置 */
            axisLine: {
              lineStyle: { color: '#BDBDBD' }
            },
            splitLine: {
              show: false
            }
          }
        ],
        yAxis: [
          {
            type: 'value',
            axisTick: {
              alignWithLabel: true,
              lineStyle: { width: 0 }
            },
            /** 坐标轴轴线相关设置 */
            axisLine: {
              lineStyle: { width: 0 }
            },
            splitLine: {
              lineStyle: {
                color: '#eee',
                type: 'dotted'
              }
            }
          }
        ],
        series: []
      },
      chartObj: null
    }
  },
  computed: {},
  mounted() {
  },
  methods: {
    initChart() {
      this.chartObj = echarts.init(document.getElementById(this.canvaId))
      this.chartObj.setOption(this.chartOption, true)
    },
    getData() {
      this.loading = true
      openDashBoardAPI(this.getBaseParams()).then(res => {
        const titles = res.data.titles
        const dataMap = new Map()
        titles.forEach(title => {
          if (this.data.dataCategories.includes(title.sqlKey)) {
            dataMap.set({ code: title.sqlKey, name: title.name }, [])
          }
        })
        this.chartOption.xAxis[0].data = []
        res.data.results.forEach(element => {
          dataMap.forEach((value, key) => {
            value.push(element[key.code])
          })
          this.chartOption.xAxis[0].data.push(element[this.data.dataDimension])
        })
        this.chartOption.series = this.getChartSeries(dataMap)
        this.chartObj.setOption(this.chartOption, true)
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },

    /**
     * 获取展示系列
     */
    getChartSeries(dataMap) {
      const series = []
      this.chartOption.color = []
      this.chartOption.legend.data = []
      let index = 0
      dataMap.forEach((value, key) => {
        series.push({
          name: key.name,
          type: 'bar',
          stack: 'one',
          barGap: '0',
          barWidth: '50%',
          barCategoryGap: '20%',
          data: value
        })
        this.chartOption.color.push(this.color[index % 17])
        this.chartOption.legend.data.push(key.name)
        ++index
      })
      return series
    }
  }
}
</script>

<style scoped lang="scss">
  @import "style";
  .bar-chart-canvas {
    width: 100%;
    height: 300px;
  }
</style>
