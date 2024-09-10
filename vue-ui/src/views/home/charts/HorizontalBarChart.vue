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
    <div class="horizontal-bar-chart-canvas" :id="canvaId" />
  </div>
</template>

<script>
import echarts from 'echarts'
import ChartMixin from '../components/ChartMixin'
import { openDashBoardAPI } from '@/api/report/open'

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
          data: [],
          top: '0px',
          itemWidth: 14
        },
        xAxis: [],
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
      chartObj: null
    }
  },
  mounted() {
  },
  methods: {
    initChart() {
      this.chartObj = echarts.init(document.getElementById(this.canvaId))
    },
    getData() {
      this.loading = true
      openDashBoardAPI(this.getBaseParams()).then(res => {
        const titles = res.data.titles
        const dataMap = new Map()
        this.chartOption.xAxis = []
        titles.forEach(title => {
          if (this.data.dataCategories.includes(title.sqlKey)) {
            dataMap.set({ code: title.sqlKey, name: title.name }, [])
            this.chartOption.xAxis.push({
              type: 'value',
              name: title.name,
              show: false
            })
          }
        })
        this.chartOption.yAxis[0].data = []
        res.data.results.forEach(element => {
          dataMap.forEach((value, key) => {
            value.push(element[key.code])
          })
          this.chartOption.yAxis[0].data.push(element[this.data.dataDimension])
        })
        this.chartOption.series = this.getChartSeries(dataMap)
        this.chartObj.setOption(this.chartOption, true)
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    getChartSeries(dataMap) {
      const series = []
      this.chartOption.color = []
      this.chartOption.legend.data = []
      let index = 0
      dataMap.forEach((value, key) => {
        series.push({
          name: key.name,
          type: 'bar',
          xAxisIndex: index,
          stack: '' + index,
          barWidth: '40%',
          barGap: '10%',
          barCategoryGap: '10%',
          label: {
            position: 'right',
            show: true
          },
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
  @import "./style";
  .horizontal-bar-chart-canvas {
    width: 100%;
    height: 350px;
  }
</style>
