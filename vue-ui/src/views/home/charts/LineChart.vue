<template>
  <div
    v-loading="loading"
    class="line-chart-canvas card">
    <flexbox class="card-title">
      <span :class="[prop.icon, 'icon']" />
      <div class="card-title-center text-one-ellipsis">{{ prop.title }}</div>
      <div class="card-title-right"/>
    </flexbox>
    <div class="card-desc"/>
    <div id="line-chart-canvas" />
  </div>
</template>

<script>

import echarts from 'echarts'
import ChartMixin from '../components/ChartMixin'

export default {
  name: 'LineChart',
  mixins: [ChartMixin],
  data() {
    return {
      prop: {
        title: '折线图',
        position: 'left',
        icon: 'perform',
        iconColor: '#4983EF',
        image: require('@/assets/img/skeleton/sort-ranking.png')
      },
      list: [
        { target: 20, complete: 10, type: '一月' },
        { target: 30, complete: 20, type: '二月' },
        { target: 20, complete: 10, type: '三月' },
        { target: 40, complete: 30, type: '四月' },
        { target: 20, complete: 20, type: '五月' },
        { target: 30, complete: 20, type: '六月' },
        { target: 10, complete: 10, type: '七月' },
        { target: 30, complete: 20, type: '八月' },
        { target: 20, complete: 10, type: '九月' },
        { target: 40, complete: 30, type: '十月' },
        { target: 50, complete: 30, type: '十一月' },
        { target: 40, complete: 30, type: '十二月' }
      ],
      chartOption: {
        color: [],
        tooltip: {
          trigger: 'axis',
          formatter: '{b} </br> {a0} {c0}% </br> {a1} {c1}%',
          axisPointer: {
            // 坐标轴指示器，坐标轴触发有效
            type: 'line' // 默认为直线，可选为：'line' | 'shadow'
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
          data: ['目标数据', '事实数据']
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
            axisLabel: {
              formatter: '{value} %'
            },
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
      this.chartObj = echarts.init(document.getElementById('line-chart-canvas'))
      this.chartObj.setOption(this.chartOption, true)
    },

    getData() {
      const targetList = []
      const completeList = []
      this.chartOption.xAxis[0].data = []
      this.list.forEach(element => {
        targetList.push(element.target)
        completeList.push(element.complete)
        this.chartOption.xAxis[0].data.push(element.type)
      })

      // 大于6当做天展示
      this.chartOption.series = this.getChartSeries(targetList, completeList)
      this.chartOption.color = ['#ff7474', '#6ca2ff']
      this.chartObj.setOption(this.chartOption, true)
    },

    /**
     * 获取展示系列
     */
    getChartSeries(targetList, completeList) {
      const series = []
      series.push({
        name: '目标数据',
        type: 'line',
        stack: 'one',
        barGap: '0',
        data: targetList
      })
      series.push({
        name: '事实数据',
        type: 'line',
        stack: 'two',
        barMaxWidth: 25,
        barGap: '0%',
        data: completeList
      })
      return series
    }
  }
}
</script>

<style scoped lang="scss">
  @import "./style";
  #line-chart-canvas {
    width: 100%;
    height: 300px;
  }
</style>
