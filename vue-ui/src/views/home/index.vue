<template>
  <div ref="workbench" class="lego-workbench">
    <div class="head">
      <flexbox class="head__body">
        <time-type-select
          :width="190"
          default-type="month"
          @change="timeTypeChange"/>
        <el-button
          :icon="'manage' | iconPre"
          @click="setSortShow = true" />
        <reminder class="setting-reminder" content="点击设置按钮支持编辑图表顺序！" />
      </flexbox>
    </div>
    <div v-empty="isEmpty" class="lego-workbench__body">
      <div
        v-for="(item, index) in sortTop"
        class="lego-workbench__top">
        <top-report
          :key="index"
          :data="item"
          :param="param"
          :rate-text="rateText"
          @onClick="handleReportClick"
        />
      </div>
      <flexbox class="section" align="stretch">
        <div class="left">
          <component
            v-for="(item, index) in sortLeft"
            :key="index"
            :is="item.chartType"
            :data="item"
            :param="param"
            class="left-content"
          />
        </div>
        <div class="right">
          <component
            v-for="(item, index) in sortRight"
            :key="index"
            :is="item.chartType"
            :data="item"
            :param="param"
            class="right-content"
          />
        </div>
      </flexbox>
    </div>
    <!-- 排序 -->
    <set-sort
      v-if="setSortShow"
      :visible.sync="setSortShow"
      :chart-list="chartList"
      @save-success="init" />
  </div>
</template>

<script>
import {
  designValidListAPI
} from '@/api/report/design'
import SetSort from './components/SetSort'
import TopReport from './components/TopReport'
import Reminder from '@/components/Reminder'
import TimeTypeSelect from '@/components/TimeTypeSelect'

const allCharts = require.context('./charts', true, /\.vue$/)
const res_charts = {}
allCharts.keys().forEach((item) => {
  const comp = allCharts(item)
  const name = comp.default.name
  res_charts[name] = comp.default
})

export default {
  name: 'HomeCenter',
  components: {
    SetSort,
    TopReport,
    Reminder,
    TimeTypeSelect,
    ...res_charts
  },
  computed: {
    isEmpty() {
      return this.sortTop.length === 0 && this.sortLeft.length === 0 && this.sortRight.length === 0
    }
  },
  data() {
    return {
      rateText: '本月',
      sortTop: [],
      sortLeft: [],
      sortRight: [],
      chartList: [],
      param: {},
      setSortShow: false
    }
  },
  mounted() {
    this.init()
  },
  methods: {
    init() {
      this.setSortShow = false
      designValidListAPI().then(res => {
        this.sortTop = []
        this.sortLeft = []
        this.sortRight = []
        this.chartList = []
        res.data.forEach(data => {
          if (data.position === 'top' && data.enable) {
            this.sortTop.push(data)
          } else if (data.position === 'left' && data.enable) {
            this.sortLeft.push(data)
          } else if (data.position === 'right' && data.enable) {
            this.sortRight.push(data)
          }
          const chart = res_charts[data.chartType]
          if (chart) {
            const prop = chart.data().prop
            data.prop = prop
            this.chartList.push(data)
          }
        })
      })
    },
    timeTypeChange(data) {
      this.param = data
      this.rateText = data.label
    },
    handleReportClick(item) {
      if (item.path) {
        this.$router.push(item.path)
      }
    }
  }
}
</script>

<style scoped lang="scss">
::v-deep .empty-mask{
  z-index: 0 !important;
}
.lego-workbench {
  width: 100%;
  min-width: 1000px;
  height: 100%;
  padding: 10px 10px 0px;
  position: relative;
  overflow: hidden;

  &__body {
    padding-top: 10px;
    height: 100%;
    overflow: auto;
  }

  &__top {
    padding-top: 20px;
  }

  .setting-reminder {
    width: auto;
    margin-left: 10px;
    display: inline-block;
  }

  .head {
    position: absolute;
    padding: 0px 10px 10px;
    top: 0;
    right: 0;
    left: 0;
    background: #f5f6f9;
    z-index: 1;

    &__body {
      position: relative;
      .user-box {
        width: unset;
        height: 36px;
        padding: 4px 7px;
        border: 1px solid #E1E1E1;
        border-radius: $xr-border-radius-base;
        background-color: white;
        margin-right: 20px;
        display: flex;
        cursor: pointer;
        .user-icon {
          background: $xr-color-primary;
          color: white;
          padding: 5px 6px;
          border-radius: 50%;
        }
        .username {
          font-size: 12px;
          margin: 0 8px;
        }
      }
      .el-radio-group {
        ::v-deep .el-radio-button__inner {
          font-size: 12px;
          padding: 11px 12px;
        }
      }
      ::v-deep .type-select {
        height: 36px;
      }
    }
  }

  .section {
    margin-top: 18px;
    .left {
      width: calc(60.5% - 20px);
      margin-right: 20px;
      &-content {
        height: 400px;
      }
    }
    .right {
      width: 39.5%;
      &-content {
        height: 400px;
      }
    }

    .left-content + .left-content,
    .right-content + .right-content {
      margin-top: 18px;
    }
  }
}
</style>
