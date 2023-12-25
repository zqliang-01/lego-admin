<template>
  <div
    v-loading="loading"
    class="funnel-chart-canvas card">
    <flexbox class="card-title">
      <span :class="[prop.icon, 'icon']" />
      <div class="card-title-center text-one-ellipsis">{{ prop.title }}</div>
      <div class="card-title-right">
        <el-dropdown
          trigger="click"
          @command="handleCommand">
          <span class="box">
            {{ optionName }}<i class="el-icon-arrow-down el-icon--right" />
          </span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item
              v-for="(item, index) in options"
              :key="index"
              :command="index">
              {{ item.name }}
            </el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </flexbox>
    <div id="funnel-chart-canvas"/>
  </div>
</template>

<script>
import echarts from 'echarts'
import ChartMixin from '../components/ChartMixin'

export default {
  name: 'FunnelChart',
  components: {},
  mixins: [ChartMixin],
  props: {},
  data() {
    return {
      prop: {
        title: '漏斗图',
        position: 'right',
        icon: 'funnel',
        iconColor: '#4983EF',
        image: require('@/assets/img/skeleton/sort-funnel.png')
      },
      list: [
        { name: '阶段一', money: 38555, count: 3 },
        { name: '阶段二', money: 15220, count: 2 },
        { name: '阶段三', money: 5230, count: 1 }
      ],
      loading: false,
      optionName: '金额',
      optionValue: 1,
      options: [
        { name: '金额', value: 1 },
        { name: '数量', value: 2 }
      ],
      chartObj: null,
      funnelOption: null,
      /*
      图表暂无数据提示
       */
      notDataOption: {
        title: {
          text: '暂无数据',
          x: 'center',
          y: 'center',
          textStyle: {
            fontSize: 14,
            fontWeight: 'normal'
          }
        }
      }
    }
  },
  mounted() {
  },
  methods: {
    /**
     * 图表数据
     */
    getData() {
      if (this.list.length == 0) {
        this.chartObj.setOption(this.notDataOption, true)
        return
      }
      var data = []
      let sumCount = 0
      this.list.forEach(element => {
        if (this.optionValue == 1) {
          data.push({
            name: element.name,
            value: parseFloat(element.money)
          })
          sumCount += parseFloat(Number(element.money))
          this.funnelOption.tooltip.formatter = '{b} <br/> ' + this.optionName + ': {c}'
        }
        if (this.optionValue == 2) {
          data.push({
            name: element.name,
            value: parseFloat(element.count)
          })
          sumCount += parseFloat(Number(element.count))
          this.funnelOption.tooltip.formatter = '{b} <br/> ' + this.optionName + ': {c}个'
        }
      })
      this.funnelOption.series[0].data = data
      this.funnelOption.series[1].data = data
      this.funnelOption.legend.data = data.map(o => o.name)
      // 设置最大值
      this.funnelOption.series[0].max = sumCount < 1 ? 1 : sumCount
      this.funnelOption.series[1].max = sumCount < 1 ? 1 : sumCount
      this.chartObj.setOption(this.funnelOption, true)
    },
    initChart() {
      // 初始化漏斗图
      var chartObj = echarts.init(document.getElementById('funnel-chart-canvas'))
      // 漏斗图的属性
      var option = {
        toolbox: {
          showTitle: false,
          feature: {
            saveAsImage: {
              show: true,
              pixelRatio: 2
            }
          }
        },
        tooltip: {
          trigger: 'item',
          formatter: ''
        },
        legend: {
          data: [],
          // 图例文本格式化
          formatter: function(name) {
            return name.split('(')[0]
          }
        },
        grid: {
          left: 0,
          right: 0,
          bottom: 0,
          top: 0
        },
        color: this.color,
        series: [
          {
            name: '漏斗图',
            type: 'funnel',
            left: '20%',
            width: '56%',
            /** 数据排序 */
            sort: 'none',
            /** 数据图形间距。 */
            gap: 2,
            label: {
              normal: {
                formatter: '{d}%',
                show: true,
                position: 'inside'
              },
              emphasis: {
                textStyle: {
                  fontSize: 20
                }
              }
            },
            labelLine: {
              normal: {
                length: 50,
                lineStyle: {
                  width: 1,
                  type: 'solid'
                }
              }
            },
            data: []
          },
          {
            name: '漏斗图',
            type: 'funnel',
            left: '20%',
            width: '56%',
            /** 数据排序 */
            sort: 'none',
            /** 数据图形间距。 */
            gap: 2,
            labelLine: {
              normal: {
                length: 50,
                lineStyle: {
                  width: 1,
                  type: 'solid'
                }
              }
            },
            data: [],
            label: {
              normal: {
                formatter: '{b} {c}',
                show: true,
                position: 'right'
              },
              emphasis: {
                textStyle: {
                  fontSize: 20
                }
              }
            }
          }
        ]
      }
      chartObj.setOption(option, true)
      this.funnelOption = option
      this.chartObj = chartObj
    },
    /**
     * 下拉菜单选项选择
     * @param index 选项序号
     */
    handleCommand(index) {
      this.optionValue = this.options[index].value
      this.optionName = this.options[index].name
      this.getData()
    }
  }
}
</script>

<style scoped lang="scss">
  @import "./style";
  #funnel-chart-canvas {
    padding-top: 10px;
    width: 100%;
    height: 360px;
  }

  .funnel-chart-canvas {
    position: relative;
    .card-title-left .icon {
      color: #50CF9E;
    }

    .el-dropdown-selfdefine {
      display: inline-block;
      cursor: pointer;
    }

    .info-box {
      position: absolute;
      bottom: 24px;
      left: 0;
      width: 100%;
      .info-item {
        width: 15%;
        margin: 0 5px;
        .label {
          margin-bottom: 5px;
        }
        &:nth-child(1) {
          color: #6ca2ff;
        }
        &:nth-child(2) {
          color: #ff7474;
        }
      }
    }
  }
</style>
