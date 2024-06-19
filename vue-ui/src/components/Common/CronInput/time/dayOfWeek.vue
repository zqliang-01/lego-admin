<template>
  <el-row>
    <every
      ref="everys"
      :type="type_"
      :tag="tag_"
      :time-unit="timeUnit"
      :symbol="symbol"
      @type-changed="changeType"
      @tag-changed="changeTag"
    />
    <period
      ref="periods"
      :type="type_"
      :tag="tag_"
      :nums="nums"
      :size="size"
      :time-unit="timeUnit"
      :start-config="startConfig"
      :cycle-config="cycleConfig"
      @type-changed="changeType"
      @tag-changed="changeTag"
    />
    <range
      ref="ranges"
      :type="type_"
      :tag="tag_"
      :nums="nums"
      :size="size"
      :time-unit="timeUnit"
      @type-changed="changeType"
      @tag-changed="changeTag"
    />
    <fixed
      ref="fixeds"
      :type="type_"
      :tag="tag_"
      :nums="nums"
      :size="size"
      @type-changed="changeType"
      @tag-changed="changeTag"
    />
    <unfixed
      ref="unfixeds"
      :type="type_"
      :tag="tag_"
      @type-changed="changeType"
      @tag-changed="changeTag"
    />
    <last
      ref="lasts"
      :type="type_"
      :tag="tag_"
      :nums="nums"
      :size="size"
      :time-unit="timeUnit"
      :target-time-unit="targetTimeUnit"
      @type-changed="changeType"
      @tag-changed="changeTag"
    />
    <week-day
      ref="weekDays"
      :type="type_"
      :tag="tag_"
      :nums="nums"
      :size="size"
      :time-unit="timeUnit"
      :target-time-unit="targetTimeUnit"
      @type-changed="changeType"
      @tag-changed="changeTag"
    />
  </el-row>
</template>

<script>
import Every from '../config/common/every'
import Fixed from '../config/common/fixed'
import Unfixed from '../config/custom/unfixed'
import { DAY_OF_WEEK_SYMBOL, DAYS_OF_WEEK, UNFIXED } from '../constant/filed'
import Period from '../config/custom/dayOfWeek/period'
import Range from '../config/custom/dayOfWeek/range'
import Last from '../config/custom/dayOfWeek/last'
import WeekDay from '../config/custom/dayOfWeek/weekDay'
import watchTime from '../mixins/watchTime'

// 31 days
const LENGTH = 7
const LOWER_LIMIT = 1
const STEP = 1

export default {
  components: {
    WeekDay,
    Every,
    Fixed,
    Unfixed,
    Period,
    Range,
    Last
  },
  mixins: [watchTime],
  props: {
    size: {
      type: String,
      default: 'mini'
    },
    tag: {
      type: String,
      default: UNFIXED
    }
  },
  data() {
    return {
      type_: UNFIXED,
      // expression of second
      tag_: null,
      timeUnit: '日',
      targetTimeUnit: '月',
      symbol: DAY_OF_WEEK_SYMBOL,
      val: '1 2...7或星期的缩写(SUN ... SAT)',
      nums: [],
      startConfig: {
        min: LOWER_LIMIT,
        step: STEP,
        max: LENGTH
      },
      startDateConfig: {
        min: LOWER_LIMIT,
        step: STEP,
        max: LENGTH
      },
      cycleConfig: {
        min: STEP,
        step: STEP,
        max: LENGTH
      },
      lowerConfig: {
        min: LOWER_LIMIT,
        step: STEP,
        max: LENGTH
      },
      upperConfig: {
        min: LOWER_LIMIT,
        step: STEP,
        max: LENGTH
      }
    }
  },
  methods: {
    // 7 days like [ {label: 'Sunday', value: 1}...{label: 'Saturday', value: 7} ]
    initNums() {
      import('../translate/dict.js').then(array => {
        this.nums = array['daysOfWeek']
      })
    },
    // change type
    changeType(type) {
      this.changeSiblingType(type)
      this.type_ = type
    },
    // change tag
    changeTag(tag) {
      this.tag_ = tag
      this.$emit('day-of-week-change', this.tag_)
    },
    changeSiblingType(type) {
      this.$refs.everys.type_ =
        this.$refs.periods.type_ =
          this.$refs.ranges.type_ =
            this.$refs.fixeds.type_ =
              this.$refs.unfixeds.type_ =
                this.$refs.lasts.type_ =
                  this.$refs.weekDays.type_ = type
    },
    resolveCustom(val) {
      for (let i = 0; i < DAYS_OF_WEEK.length; i++) {
        const item = DAYS_OF_WEEK[i]
        if (val.indexOf(item) !== -1) {
          val = val.replace(item, i + 1)
        }
      }
      return val
    }
  }
}
</script>
