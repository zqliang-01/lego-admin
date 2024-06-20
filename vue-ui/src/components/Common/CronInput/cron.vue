<template>
  <div>
    <el-row class="cron-row">
      <el-row :gutter="2">
        <el-col :span="3">
          <el-input ref="input1" v-model="tag.second" @focus="activeTabName='1'" />
        </el-col>
        <el-col :span="4">
          <el-input ref="input2" v-model="tag.minute" @focus="activeTabName='2'" />
        </el-col>
        <el-col :span="4">
          <el-input ref="input3" v-model="tag.hour" @focus="activeTabName='3'" />
        </el-col>
        <el-col :span="4">
          <el-input ref="input4" v-model="tag.dayOfMonth" @focus="activeTabName='4'" />
        </el-col>
        <el-col :span="3">
          <el-input ref="input5" v-model="tag.month" @focus="activeTabName='5'" />
        </el-col>
        <el-col :span="3">
          <el-input ref="input6" v-model="tag.dayOfWeek" @focus="activeTabName='6'" />
        </el-col>
        <el-col :span="3">
          <el-input ref="input7" v-model="tag.year" @focus="activeTabName='7'" />
        </el-col>
      </el-row>
    </el-row>
    <el-row class="cron-row">
      <el-tabs v-model="activeTabName" type="border-card">
        <el-tab-pane name="1">
          <span slot="label">秒</span>
          <second
            :tag="tag.second"
            @second-change="changeSecond"
          />
        </el-tab-pane>
        <el-tab-pane name="2">
          <span slot="label">分</span>
          <minute
            :tag="tag.minute"
            @minute-change="changeMinute"
          />
        </el-tab-pane>
        <el-tab-pane name="3">
          <span slot="label">时</span>
          <hour
            :tag="tag.hour"
            @hour-change="changeHour"
          />
        </el-tab-pane>
        <el-tab-pane name="4">
          <span slot="label">日</span>
          <day-of-month
            :tag="tag.dayOfMonth"
            @day-of-month-change="changeDayOfMonth"
          />
        </el-tab-pane>
        <el-tab-pane name="5">
          <span slot="label">月</span>
          <month
            :tag="tag.month"
            @month-change="changeMonth"
          />
        </el-tab-pane>
        <el-tab-pane name="6">
          <span slot="label">周</span>
          <day-of-week
            :tag="tag.dayOfWeek"
            @day-of-week-change="changeDayOfWeek"
          />
        </el-tab-pane>
        <el-tab-pane name="7">
          <span slot="label">年</span>
          <year
            :tag="tag.year"
            @year-change="changeYear"
          />
        </el-tab-pane>
        <el-tab-pane name="8">
          <span slot="label">快捷选择</span>
          <div class="cell-div">
            <span style="margin-right: 10px;">
              <el-button :disabled="!sample || sample.trim().length < 11" type="primary" @click="changeTime(sample)">使用</el-button>
            </span>
            <el-select
              v-model="sample"
              placeholder="请选择"
              :filter-method="filterCase"
              style="min-width: 320px;"
              filterable
            >
              <el-option
                v-for="item in cases"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              >
                <span style="float: left">{{ item.label }}</span>
                <span style="float: right; color: #8492a6; font-size: 13px">{{ item.value }}</span>
              </el-option>
            </el-select>
            <span style="margin-left: 5px;">
              {{ sample }}
            </span>
          </div>
          <div v-for="(item, index) in timeUnits" :key="index">
            {{ item }}:值为<strong>{{ vals[index] }}</strong>
            通配符支持<strong>{{ symbols[index] }}</strong>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-row>
  </div>
</template>

<script>
import Second from './time/second'
import Minute from './time/minute'
import Hour from './time/hour'
import DayOfMonth from './time/dayOfMonth'
import Month from './time/month'
import Year from './time/year'
import DayOfWeek from './time/dayOfWeek'

import {
  EMPTY,
  EVERY,
  UNFIXED,
  BASE_SYMBOL,
  DAY_OF_MONTH_SYMBOL,
  DAY_OF_WEEK_SYMBOL,
  DEFAULT_CRON_EXPRESSION,
  CUR_YEAR,
  UPPER_LIMIT_YEAR
} from './constant/filed'

export default {
  name: 'Cron',
  components: {
    DayOfWeek,
    Year,
    Month,
    DayOfMonth,
    Hour,
    Minute,
    Second
  },
  props: {
    value: {
      type: String,
      default: DEFAULT_CRON_EXPRESSION
    }
  },
  data() {
    return {
      tag: {
        second: EVERY,
        minute: EVERY,
        hour: EVERY,
        dayOfMonth: EVERY,
        month: EVERY,
        dayOfWeek: UNFIXED,
        year: EMPTY
      },
      activeTabName: '1',
      timeUnits: ['秒', '分', '时', '日', '月', '周', '年'],
      vals: [
        '0 1 2...59', '0 1 2...59', '0 1 2...23', '1 2...31',
        '1 2...12，或12个月的缩写(JAN ... DEC)', '1 2...7或星期的缩写(SUN ... SAT)', CUR_YEAR + ' ... ' + UPPER_LIMIT_YEAR
      ],
      symbols: [
        BASE_SYMBOL, BASE_SYMBOL, BASE_SYMBOL, DAY_OF_MONTH_SYMBOL, BASE_SYMBOL, DAY_OF_WEEK_SYMBOL, BASE_SYMBOL
      ],
      sample: '',
      cases: [],
      bakCases: []
    }
  },
  watch: {
    value(val) {
      this.changeTime(val)
    },
    activeTabName(val) {
      const input_ = this.$refs['input' + val]
      if (input_) {
        input_.focus()
      }
    },
    tag: {
      handler(curVal, oldVal) {
        this.changeCron()
      },
      deep: true
    }
  },
  created() {
    this.loadConst()
    this.changeTime(this.value)
  },
  methods: {
    changeSecond(tag) {
      this.tag.second = tag
    },
    changeMinute(tag) {
      this.tag.minute = tag
    },
    changeHour(tag) {
      this.tag.hour = tag
    },
    changeDayOfMonth(tag) {
      this.tag.dayOfMonth = tag
    },
    changeMonth(tag) {
      this.tag.month = tag
    },
    changeDayOfWeek(tag) {
      this.tag.dayOfWeek = tag
    },
    changeYear(tag) {
      this.tag.year = tag
    },
    changeCron() {
      const cron = (this.tag.second + ' ' + this.tag.minute + ' ' + this.tag.hour + ' ' + this.tag.dayOfMonth + ' ' +
          this.tag.month + ' ' + this.tag.dayOfWeek + ' ' + this.tag.year).trim()
      this.$emit('change', cron)
    },
    changeTime(newValue) {
      if (!newValue || newValue.trim().length < 11) {
        this.$message.error('格式不正确，必须有6或7位')
        return
      }
      const arr = newValue.trim().split(' ')
      if (arr.length !== 6 && arr.length !== 7) {
        this.$message.error('格式不正确，必须有6或7位')
        return
      }
      this.tag.second = arr[0]
      this.tag.minute = arr[1]
      this.tag.hour = arr[2]
      this.tag.dayOfMonth = arr[3]
      this.tag.month = arr[4]
      this.tag.dayOfWeek = arr[5]
      this.tag.year = arr.length === 7 ? arr[6] : ''
    },
    filterCase(query) {
      if (query !== '') {
        this.loading = true
        setTimeout(() => {
          this.loading = false
          this.cases = this.bakCases.filter(item => {
            return item.label.toLowerCase()
              .indexOf(query.toLowerCase()) !== -1 ||
            item.value.toLowerCase()
              .indexOf(query.toLowerCase()) !== -1
          })
        }, 100)
      } else {
        this.cases = this.bakCases
      }
    },
    loadConst() {
      import('./translate/dict.js').then(array => {
        this.bakCases = this.cases = array['cases']
      })
    }
  }
}
</script>
<style lang="scss" scoped>
.cron-row {
  margin-top: 8px;
  max-width: 550px;
}
.cell-div {
  margin-bottom: 8px;
}
</style>
