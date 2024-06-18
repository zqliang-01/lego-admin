<template>
  <div ref="workbench" class="lego-workbench">
    <div class="head">
      <flexbox class="head__body">
        <time-type-select
          :width="190"
          default-type="week"
          @change="timeTypeChange"/>
        <el-tooltip content="刷新数据" placement="top-start">
          <el-button
            class="type-btn"
            icon="el-icon-refresh"
            @click.native="handleRefresh('refresh')" />
        </el-tooltip>
      </flexbox>
    </div>
    <div class="lego-workbench__body">
      <top-report
        :brief-list="briefList"
        @onClick="handleTopClick"
      />
      <flexbox
        class="section"
        align="stretch">
        <div class="left">
          <line-chart
            :category="lineCategory"
            :values="lineValues"
            class="left-content"
          />
        </div>
        <div class="right">
          <pie-chart
            :values="pieValues"
            class="right-content"
          />
        </div>
      </flexbox>
    </div>
  </div>
</template>

<script>
import TopReport from './components/TopReport'
import LineChart from './components/LineChart'
import PieChart from './components/PieChart'
import TimeTypeSelect from '@/components/TimeTypeSelect'
import {
  reportGetAPI,
  lineChartGetAPI
} from '@/api/job/index'

export default {
  name: 'JobIndex',
  components: {
    TopReport,
    LineChart,
    PieChart,
    TimeTypeSelect
  },
  data() {
    return {
      rateText: '本周',
      startTime: '',
      endTime: '',
      briefList: [],
      lineCategory: [],
      lineValues: [],
      pieValues: []
    }
  },
  mounted() {
    this.init()
    this.handleChartData()
  },
  methods: {
    timeTypeChange(data) {
      this.rateText = data.label
      this.startTime = data.startTime
      this.endDate = data.endTime
      this.handleChartData()
    },
    init() {
      reportGetAPI().then(res => {
        this.briefList = [
          { label: '任务数量(个)', type: 'jobTask', icon: 'tag', num: res.data.jobInfoCount, description: '调度中心运行的任务数量', color: '#2362FB' },
          { label: '调度次数(次)', type: 'jobLog', icon: 'calendar', num: res.data.jobLogCount, description: '调度中心触发的调度次数', color: '#FB9323' },
          { label: '执行器数量(个)', type: 'jobExecutor', icon: 'config', num: res.data.executorCount, description: '调度中心在线的执行器机器数量', color: '#27BA4A' }
        ]
      })
    },
    handleChartData() {
      lineChartGetAPI({
        startDate: this.startTime,
        endDate: this.endTime
      }).then(res => {
        this.pieValues = [
          { name: '成功', color: '#00A65A', data: res.data.triggerCountSucTotal },
          { name: '失败', color: '#c23632', data: res.data.triggerCountFailTotal },
          { name: '运行中', color: '#F39C12', data: res.data.triggerCountRunningTotal }
        ]
        this.lineCategory = res.data.triggerDayList
        this.lineValues = [
          { name: '成功', color: '#00A65A', data: res.data.triggerDayCountSucList },
          { name: '失败', color: '#c23632', data: res.data.triggerDayCountFailList },
          { name: '运行中', color: '#F39C12', data: res.data.triggerDayCountRunningList }
        ]
      })
    },
    handleRefresh(type) {
      if (type === 'refresh') {
        this.init()
        this.handleChartData()
      }
    },
    handleTopClick(item) {
      this.$router.push({ name: item.type })
    }
  }
}
</script>

<style scoped lang="scss">
.lego-workbench {
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
  .head {
    position: absolute;
    padding: 20px 20px 10px;
    top: 0;
    right: 0;
    left: 0;
    background: #f5f6f9;
    z-index: 1;

    &__body {
      position: relative;
    }
  }

  .section {
    margin-top: 18px;
    .left {
      width: calc(60.5% - 20px);
      margin-right: 20px;
    }
    .right {
      width: 39.5%;
    }

    .left-content + .left-content,
    .right-content + .right-content {
      margin-top: 18px;
    }
  }
}
</style>
