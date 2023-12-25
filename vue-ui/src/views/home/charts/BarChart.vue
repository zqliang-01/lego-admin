<template>
  <div
    v-loading="loading"
    class="bar-chart-canvas card">
    <flexbox class="card-title">
      <span :class="[prop.icon, 'icon']" />
      <div class="card-title-center text-one-ellipsis">{{ prop.title }}</div>
      <div class="card-title-right"/>
    </flexbox>
    <div class="card-desc"/>
    <div id="bar-chart-canvas" />
  </div>
</template>

<script>
import echarts from 'echarts'
import ChartMixin from '../components/ChartMixin'

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
      list: [
        { type: '分类一', amount: 1 },
        { type: '分类二', amount: 2 },
        { type: '分类三', amount: 10 },
        { type: '分类四', amount: 3 },
        { type: '分类五', amount: 10 }
      ],
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
          data: ['分类']
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
      chartObj: null,
      loading: false
    }
  },
  computed: {},
  mounted() {
  },
  methods: {
    initChart() {
      this.chartObj = echarts.init(document.getElementById('bar-chart-canvas'))
      this.chartObj.setOption(this.chartOption, true)
    },
    getData() {
      const dataList = []
      const xAxisData = []
      this.list.forEach(element => {
        dataList.push(element.amount)
        xAxisData.push(element.type)
      })
      this.chartOption.xAxis[0].data = xAxisData
      // 大于6当做天展示
      this.chartOption.series = this.getChartSeries(dataList)
      this.chartOption.color = ['#ff7474']
      this.chartObj.setOption(this.chartOption, true)
    },

    /**
     * 获取展示系列
     */
    getChartSeries(dataList) {
      const series = []
      series.push({
        name: '分类',
        type: 'bar',
        stack: 'one',
        barGap: '0',
        barWidth: '50%',
        // barCategoryGap: '20%',
        data: dataList
      })
      return series
    }
  }
}
</script>

<style scoped lang="scss">
  @import "style";
  #bar-chart-canvas {
    width: 100%;
    height: 300px;
  }
</style>
