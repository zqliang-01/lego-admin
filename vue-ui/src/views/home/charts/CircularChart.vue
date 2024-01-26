<template>
  <div
    v-loading="loading"
    class="circular-chart-canvas card">
    <flexbox class="card-title">
      <span :class="[prop.icon, 'icon']" />
      <div class="card-title-center text-one-ellipsis">
        {{ prop.title }} (
        <span v-for="(item, index) in list" :key="index">
          {{ item.categoryName }}、
        </span>
        )
      </div>
    </flexbox>
    <div class="card-desc"/>
    <div id="circular-chart-canvas" />
  </div>
</template>

<script>
import echarts from 'echarts'
import ChartMixin from '../components/ChartMixin'

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
      list: [
        { categoryName: '分类一', money: 135300 },
        { categoryName: '分类二', money: 507005 },
        { categoryName: '分类三', money: 261000 }
      ],
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
      chartObj: null,
      loading: false
    }
  },
  computed: {},
  mounted() {
  },
  methods: {
    initChart() {
      this.chartObj = echarts.init(document.getElementById('circular-chart-canvas'))
    },

    getData() {
      const pieObj = {
        type: 'pie',
        data: [],
        radius: ['40%', '70%']
      }
      this.list.forEach(item => {
        pieObj.data.push({
          name: item.categoryName,
          value: item.money
        })
        this.chartOption.legend.data.push(item.categoryName)
      })
      this.chartOption.series.push(pieObj)
      this.chartOption.color = this.color
      this.chartObj.setOption(this.chartOption, true)
    }
  }
}
</script>

<style scoped lang="scss">
  @import "./style";
  #circular-chart-canvas {
    width: 100%;
    height: 300px;
  }
</style>
