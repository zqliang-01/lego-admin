<template>
  <div
    v-loading="loading"
    class="horizontal-bar-chart-canvas card">
    <flexbox class="card-title">
      <span :class="[prop.icon, 'icon']" />
      <div class="card-title-center text-one-ellipsis">{{ prop.title }}</div>
      <div class="card-title-right"/>
    </flexbox>
    <div class="card-desc"/>
    <div id="horizontal-bar-chart-canvas" />
  </div>
</template>

<script>
import echarts from 'echarts'
import ChartMixin from '../components/ChartMixin'

export default {
  name: 'HorizontalBarChart',
  mixins: [ChartMixin],
  data() {
    return {
      prop: {
        title: '横向柱状图',
        position: 'left',
        icon: 'hollow-results',
        iconColor: '#4983EF',
        image: require('@/assets/img/skeleton/sort-data.png')
      },
      list: [
        { money: 5550, count: 0, name: '数据一' },
        { money: 68551, count: 1, name: '数据二' },
        { money: 2288452, count: 3, name: '数据三' },
        { money: 345008, count: 4, name: '数据四' }
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
          left: '40px',
          right: '120px',
          top: '10px',
          bottom: '0px',
          containLabel: true,
          borderColor: '#fff'
        },
        legend: {
          data: ['金额', '数量'],
          top: '0px',
          itemWidth: 14
        },
        xAxis: [
          {
            type: 'value',
            name: '金额',
            show: false
          },
          {
            type: 'value',
            name: '数量',
            show: false
          }
        ],
        yAxis: [
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
              lineStyle: { color: '#BDBDBD' },
              // 不显示轴线
              show: false
            },
            splitLine: {
              show: false
            }
          }
        ],
        series: []
      },
      axisList: [],
      chartObj: null,
      loading: false
    }
  },
  mounted() {
  },
  methods: {
    /** 柱状图 */
    initChart() {
      this.chartObj = echarts.init(document.getElementById('horizontal-bar-chart-canvas'))
      this.chartObj.setOption(this.chartOption, true)
    },

    getData() {
      const moneyList = []
      const countList = []
      const yAxisData = []
      this.list.forEach(element => {
        moneyList.push(element.money)
        countList.push(element.count)
        yAxisData.push(element.name)
      })
      this.chartOption.yAxis[0].data = yAxisData
      // 大于6当做天展示
      this.chartOption.series = this.getChartSeries(moneyList, countList)
      this.chartOption.color = ['#ff7474', '#6ca2ff']
      this.chartObj.setOption(this.chartOption, true)
    },

    /**
     * 获取展示系列
     */
    getChartSeries(moneyList, countList) {
      const series = []
      series.push({
        name: '金额',
        type: 'bar',
        xAxisIndex: 0,
        stack: '0',
        barWidth: '40%',
        label: {
          position: 'right',
          show: true
        },
        data: moneyList
      })

      series.push({
        name: '数量',
        type: 'bar',
        xAxisIndex: 1,
        stack: '1',
        barWidth: '40%',
        barGap: '10%',
        barCategoryGap: '10%',
        label: {
          position: 'right',
          show: true
        },
        data: countList
      })

      return series
    },

    getMaxValueList(maxValue, list) {
      const maxMoneyList = []
      for (let index = 0; index < list.length; index++) {
        const element = list[index]
        maxMoneyList.push(maxValue - Number(element))
      }
      return maxMoneyList
    }

  }
}
</script>

<style scoped lang="scss">
  @import "./style";
  #horizontal-bar-chart-canvas {
    width: 100%;
    height: 350px;
  }
</style>
