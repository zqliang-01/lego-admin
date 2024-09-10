<template>
  <div
    v-loading="loading"
    class="card">
    <flexbox class="card-title">
      <span :class="[prop.icon, 'icon']" />
      <div class="card-title-center text-one-ellipsis">
        {{ data.name }}
      </div>
    </flexbox>
    <div class="card-desc"/>
    <div class="circular-chart-canvas" :id="canvaId" />
  </div>
</template>

<script>
import echarts from 'echarts'
import ChartMixin from '../components/ChartMixin'
import { openDashBoardAPI } from '@/api/report/open'

export default {
  name: 'CircularChart',
  mixins: [ChartMixin],
  data() {
    return {
      prop: {
        title: '环形图',
        position: 'right',
        icon: 'target',
        iconColor: '#4983EF',
        image: require('@/assets/img/skeleton/sort-done.png')
      },
      pieObj: {
        type: 'pie',
        data: [],
        radius: ['40%', '70%']
      },
      chartOption: {
        tooltip: {
          trigger: 'item',
          formatter: '{b} : {c}'
        },
        legend: {
          orient: 'vertical',
          x: 'left',
          data: []
        },
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
    }
  }
}
</script>

<style scoped lang="scss">
  @import "./style";
  .circular-chart-canvas {
    width: 100%;
    height: 300px;
  }
</style>
