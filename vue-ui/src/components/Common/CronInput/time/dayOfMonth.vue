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
      :size="size"
      :time-unit="timeUnit"
      :lower-config="lowerConfig"
      :upper-config="upperConfig"
      @type-changed="changeType"
      @tag-changed="changeTag"
    />
    <fixed
      ref="fixeds"
      :type="type_"
      :tag="tag_"
      :size="size"
      :time-unit="timeUnit"
      :nums="nums"
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
      :size="size"
      :last-config="lastConfig"
      :time-unit="timeUnit"
      :target-time-unit="targetTimeUnit"
      @type-changed="changeType"
      @tag-changed="changeTag"
    />
    <work-day
      ref="workDays"
      :type="type_"
      :tag="tag_"
      :size="size"
      :start-date-config="startDateConfig"
      :time-unit="timeUnit"
      :target-time-unit="targetTimeUnit"
      @type-changed="changeType"
      @tag-changed="changeTag"
    />
    <last-work-day
      ref="lastWorkDays"
      :type="type_"
      :tag="tag_"
      :size="size"
      :target-time-unit="targetTimeUnit"
      @type-changed="changeType"
      @tag-changed="changeTag"
    />
  </el-row>
</template>

<script>
import Every from '../config/common/every'
import Period from '../config/common/period'
import Range from '../config/common/range'
import Fixed from '../config/common/fixed'
import Unfixed from '../config/custom/unfixed'
import WorkDay from '../config/custom/workDay'
import Last from '../config/custom/last'
import LastWorkDay from '../config/custom/lastWorkDay'
import { DAY_OF_MONTH_SYMBOL, EVERY } from '../constant/filed'
import watchTime from '../mixins/watchTime'

const LENGTH = 31
const LOWER_LIMIT = 1
const STEP = 1

export default {
  components: {
    LastWorkDay,
    Last,
    WorkDay,
    Every,
    Period,
    Range,
    Fixed,
    Unfixed
  },
  mixins: [watchTime],
  props: {
    tag: {
      type: String,
      default: EVERY
    },
    size: {
      type: String,
      default: 'mini'
    }
  },
  data() {
    return {
      type_: EVERY,
      // expression of second
      tag_: null,
      timeUnit: '日',
      targetTimeUnit: '月',
      symbol: DAY_OF_MONTH_SYMBOL,
      val: '1 2...31',
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
      },
      lastConfig: {
        min: LOWER_LIMIT,
        step: STEP,
        max: LENGTH
      }
    }
  },
  methods: {
    // 31 days like [ {label: '1', value: 1}...{label: '31', value: 31} ]
    initNums() {
      for (let i = 1; i <= LENGTH; i++) {
        const item = {
          label: i.toString(),
          value: i
        }
        this.nums.push(item)
      }
    },
    // change type
    changeType(type) {
      this.changeSiblingType(type)
      this.type_ = type
    },
    // change tag
    changeTag(tag) {
      this.tag_ = tag
      this.$emit('day-of-month-change', this.tag_)
    },
    changeSiblingType(type) {
      this.$refs.everys.type_ =
        this.$refs.periods.type_ =
          this.$refs.ranges.type_ =
            this.$refs.fixeds.type_ =
              this.$refs.unfixeds.type_ =
                this.$refs.lasts.type_ =
                  this.$refs.workDays.type_ =
                    this.$refs.lastWorkDays.type_ = type
    }
  }
}
</script>
