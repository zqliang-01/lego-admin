<template>
  <div
    v-loading="loading"
    class="card">
    <flexbox class="card-title">
      <span :class="[prop.icon, 'icon']" />
      <div class="card-title-center text-one-ellipsis">{{ data.name }}</div>
    </flexbox>
    <div class="card-desc"/>
    <div class="pie-chart-canvas" :id="canvaId" />
  </div>
</template>

<script>
import echarts from 'echarts'
import ChartMixin from '../components/ChartMixin'
import { openDashBoardAPI } from '@/api/report/open'

export default {
  name: 'PieChart',
  mixins: [ChartMixin],
  data() {
    return {
      prop: {
        title: '饼状图',
        position: 'left',
        icon: 'target',
        iconColor: '#4983EF',
        image: require('@/assets/img/skeleton/sort-done.png')
      },
      list: [
        { name: '分类1', industryMoney: 665821 },
        { name: '分类2', industryMoney: 98555.1 },
        { name: '分类3', industryMoney: 55481 },
        { name: '分类4', industryMoney: 33685 },
        { name: '分类5', industryMoney: 99555 },
        { name: '分类6', industryMoney: 33695 },
        { name: '分类7', industryMoney: 88455 },
        { name: '分类8', industryMoney: 96514 },
        { name: '分类9', industryMoney: 68455 },
        { name: '分类10', industryMoney: 77458 },
        { name: '分类11', industryMoney: 33521 },
        { name: '分类12', industryMoney: 85523 },
        { name: '分类13', industryMoney: 44811 }
      ],
      pieObj: {
        radius: '70%',
        type: 'pie',
        data: [],
        minAngle: 5, // 最小角度
        startAngle: 270 // 起始角度
      },
      chartOption: {
        legend: {
          orient: 'vertical',
          x: 'left',
          data: [],
          type: 'scroll'
        },
        tooltip: {
          trigger: 'item',
          formatter: '{b} : {c}'
        },
        series: []
      },
      chartObj: null,
      optionName: '合同额',
      optionValue: 6,
      options: [
        { name: '合同额', value: 6 },
        { name: '现金到账', value: 7 }
      ]
    }
  },
  methods: {
    initChart() {
      this.chartObj = echarts.init(document.getElementById(this.canvaId))
    },
    getData() {
      this.loading = true
      this.pieObj.data = []
      this.chartOption.series = []
      openDashBoardAPI(this.getBaseParams()).then(res => {
        res.data.results.forEach(element => {
          this.chartOption.legend.data.push(element[this.data.dataDimension])
          this.data.dataCategories.forEach(category => {
            this.pieObj.data.push({
              name: element[this.data.dataDimension],
              value: element[category]
            })
          })
        })
        this.chartOption.series.push(this.pieObj)
        this.chartOption.color = this.color
        this.chartObj.setOption(this.chartOption, true)
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    handleCommand(index) {
      this.optionValue = this.options[index].value
      this.optionName = this.options[index].name
      this.getData()
    }
  }
}
</script>

<style scoped lang="scss">
  @import "style";
  .pie-chart-canvas {
    width: 100%;
    height: 300px;
  }
</style>
