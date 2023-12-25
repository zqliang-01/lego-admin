<template>
  <div ref="workbench" class="crm-workbench">
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
    <div class="crm-workbench__body">
      <top-report
        :brief-list="briefList"
        :rate-text="rateText"
      />
      <flexbox
        class="section"
        align="stretch">
        <div class="left">
          <component
            v-for="(item, index) in sortLeft"
            :key="index"
            :is="item"
            class="left-content"
          />
        </div>
        <div class="right">
          <component
            v-for="(item, index) in sortRight"
            :key="index"
            :is="item"
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
      @save="getModelSort" />
  </div>
</template>

<script>
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
  data() {
    return {
      rateText: '本月',
      briefList: [
        { label: '新增收入(元)', type: 'customer', icon: 'customer', num: '5852', rate: '93.06', status: 'bottom', color: '#2362FB' },
        { label: '新增模型(个)', type: 'contacts', icon: 'contacts', num: '58', rate: '88.68', status: 'bottom', color: '#27BA4A' },
        { label: '新增流程(个)', type: 'business', icon: 'business', num: '938', rate: '251', status: 'top', color: '#FB9323' },
        { label: '新增字典(个)', type: 'contract', icon: 'contract', num: '1568', rate: '28.54', status: 'bottom', color: '#4A5BFD' },
        { label: '累计金额(元)', type: 'contract', icon: 'receivables', num: '235514.00', rate: '8.45', status: 'bottom', color: '#19B5F6' },
        { label: '商机金额(元)', type: 'business', icon: 'icon-opportunities', num: '886652.25', rate: '20.85', status: 'top', color: '#AD5CFF' },
        { label: '材料金额(元)', type: 'receivables', icon: 'receivables', num: '13855.52', rate: '46.85', status: 'bottom', color: '#FFB940' },
        { label: '来访客户数(条)', type: 'record', icon: 'record', num: '85', rate: '150', status: 'top', color: '#4A5BFD' }
      ],
      sortLeft: [],
      sortRight: [],
      chartList: [],
      setSortShow: false
    }
  },
  mounted() {
    Object.keys(res_charts).forEach(key => {
      const chart = res_charts[key]
      const prop = chart.data().prop
      if (prop) {
        prop.show = true
        this.chartList.push(prop)
      }
      if (prop && prop.position == 'left') {
        this.sortLeft.push(chart.name)
      }
      if (prop && prop.position == 'right') {
        this.sortRight.push(chart.name)
      }
    })
  },
  methods: {
    getModelSort() {
    },
    timeTypeChange(data) {
      this.rateText = data.label
    }
  }
}
</script>

<style scoped lang="scss">
.crm-workbench {
  width: 100%;
  min-width: 1000px;
  height: 100%;
  padding: 15px 20px 20px;
  position: relative;

  &__body {
    height: 100%;
    overflow: auto;
    padding-top: 20px;
  }
  .setting-reminder {
    width: auto;
    margin-left: 10px;
    display: inline-block;
  }

  .head {
    position: absolute;
    padding: 0px 20px 10px;
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
      /deep/ .el-radio-button__inner {
          font-size: 12px;
          padding: 11px 12px;
        }
      }
      /deep/ .type-select {
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
