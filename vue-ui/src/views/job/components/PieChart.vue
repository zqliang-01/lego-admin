<template>
  <div
    v-loading="loading"
    class="pie-chart-canvas card">
    <flexbox class="card-title">
      <span :class="[prop.icon, 'icon']" />
      <div class="card-title-center text-one-ellipsis">{{ prop.title }}</div>
    </flexbox>
    <div class="card-desc"/>
    <div id="pie-chart-canvas" />
  </div>
</template>

<script>
import echarts from 'echarts'

export default {
  name: 'PieChart',
  props: {
    values: {
      type: Array,
      default: function() {
        return []
      }
    }
  },
  watch: {
    values: {
      handler(val) {
        if (this.chartObj != null) {
          this.getData()
        }
      },
      deep: true,
      immediate: true
    }
  },
  data() {
    return {
      prop: {
        title: '成功比例',
        icon: 'target'
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
          formatter: '{b} : {c} ({d}%)'
        },
        series: []
      },
      chartObj: null,
      loading: false,
      optionName: '合同额',
      optionValue: 6
    }
  },
  mounted() {
    this.initChart()
    this.getData()
  },
  methods: {
    initChart() {
      this.chartObj = echarts.init(document.getElementById('pie-chart-canvas'))
    },
    getData() {
      this.chartOption.series = []
      const pieObj = {
        radius: '55%',
        center: ['50%', '60%'],
        type: 'pie',
        data: [],
        itemStyle: {
          emphasis: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
      this.chartOption.legend.data = []
      this.chartOption.color = []
      this.values.forEach(item => {
        const obj = {
          name: item.name,
          value: item.data
        }
        pieObj.data.push(obj)
        this.chartOption.color.push(item.color)
        this.chartOption.legend.data.push(item.name)
      })
      this.chartOption.series.push(pieObj)
      this.chartObj.setOption(this.chartOption, true)
    }
  }
}
</script>

<style scoped lang="scss">
  @import "style";
  #pie-chart-canvas {
    width: 100%;
    height: 300px;
  }
</style>
