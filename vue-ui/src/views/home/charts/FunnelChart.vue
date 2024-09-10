<template>
  <div
    v-loading="loading"
    class="funnel-chart-canvas-body card">
    <flexbox class="card-title">
      <span :class="[prop.icon, 'icon']" />
      <div class="card-title-center text-one-ellipsis">{{ data.name }}</div>
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
    <div class="funnel-chart-canvas" :id="canvaId"/>
  </div>
</template>

<script>
import echarts from 'echarts'
import ChartMixin from '../components/ChartMixin'
import { openDashBoardAPI } from '@/api/report/open'

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
      optionName: '',
      options: [],
      dataMap: new Map(),
      chartObj: null,
      chartOption: {
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
      },
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
    initChart() {
      this.chartObj = echarts.init(document.getElementById(this.canvaId))
    },
    /**
     * 图表数据
     */
    getData() {
      this.loading = true
      openDashBoardAPI(this.getBaseParams()).then(res => {
        const titles = res.data.titles
        this.options = []
        this.dataMap = new Map()
        titles.forEach(title => {
          if (this.data.dataCategories.includes(title.sqlKey)) {
            this.dataMap.set(title.sqlKey, [])
            this.options.push({ code: title.sqlKey, name: title.name })
          }
        })
        this.chartOption.legend.data = []
        res.data.results.forEach(element => {
          this.options.forEach(option => {
            this.dataMap.get(option.code).push({
              name: element[this.data.dataDimension],
              value: element[option.code]
            })
          })
          this.chartOption.legend.data.push(element[this.data.dataDimension])
        })
        this.handleCommand(0)
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    handleCommand(index) {
      const option = this.options[index]
      this.optionName = option.name
      this.chartOption.series[0].data = this.dataMap.get(option.code)
      this.chartOption.series[1].data = this.dataMap.get(option.code)
      this.chartOption.tooltip.formatter = '{b} <br/> ' + option.name + ': {c}'
      this.chartObj.setOption(this.chartOption, true)
    }
  }
}
</script>

<style scoped lang="scss">
  @import "./style";
  .funnel-chart-canvas {
    padding-top: 10px;
    width: 100%;
    height: 360px;
  }

  .funnel-chart-canvas-body {
    position: relative;
    .card-title-left .icon {
      color: #50CF9E;
    }

    .el-dropdown-selfdefine {
      display: inline-block;
      cursor: pointer;
    }
  }
</style>
