<template>
  <div
    v-loading="loading"
    class="line-chart-canvas card">
    <flexbox class="card-title">
      <span :class="[icon, 'icon']" />
      <div class="card-title-center text-one-ellipsis">{{ title }}</div>
      <div class="card-title-right"/>
    </flexbox>
    <div class="card-desc"/>
    <div id="line-chart-canvas" />
  </div>
</template>

<script>
import echarts from 'echarts'
export default {
  name: 'LineChart',
  props: {
    category: {
      type: Array,
      default: function() {
        return []
      }
    },
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
      title: '日期分布图',
      icon: 'perform',
      chartOption: {
        color: [],
        tooltip: {
          trigger: 'axis',
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
          right: '20px'
        },
        xAxis: [
          {
            type: 'category',
            data: [],
            boundaryGap: false,
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
              formatter: '{value}'
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
    this.initChart()
    this.getData()
  },
  methods: {
    initChart() {
      this.chartObj = echarts.init(document.getElementById('line-chart-canvas'))
    },
    getData() {
      this.chartOption.color = []
      this.chartOption.series = []
      this.chartOption.legend.data = []
      this.chartOption.xAxis[0].data = this.category
      this.values.forEach(item => {
        this.chartOption.series.push({
          name: item.name,
          type: 'line',
          areaStyle: {
            normal: {}
          },
          data: item.data
        })
        this.chartOption.color.push(item.color)
        this.chartOption.legend.data.push(item.name)
      })
      this.chartObj.setOption(this.chartOption, true)
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
