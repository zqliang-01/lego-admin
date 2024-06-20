<template>
  <el-row>
    <every
      ref="everys"
      :type="type_"
      :tag="tag_"
      :time-unit="timeUnit"
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
  </el-row>
</template>

<script>
import Every from '../config/common/every'
import Period from '../config/common/period'
import Range from '../config/common/range'
import Fixed from '../config/common/fixed'
import { EVERY, BASE_SYMBOL } from '../constant/filed'
import watchTime from '../mixins/watchTime'

// 60 seconds
const LENGTH = 60
const LOWER_LIMIT = 0
const STEP = 1

export default {
  components: {
    Every,
    Period,
    Range,
    Fixed
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
      timeUnit: '秒',
      symbol: BASE_SYMBOL,
      val: '0 1 2...59',
      nums: [],
      startConfig: {
        min: LOWER_LIMIT,
        step: STEP,
        max: LENGTH - 1
      },
      cycleConfig: {
        min: STEP,
        step: STEP,
        max: LENGTH - 1
      },
      lowerConfig: {
        min: LOWER_LIMIT,
        step: STEP,
        max: LENGTH - 1
      },
      upperConfig: {
        min: LOWER_LIMIT,
        step: STEP,
        max: LENGTH - 1
      }
    }
  },
  methods: {
    // 60 seconds like [ {label: '0', value: 0},{label: '1', value: 1}...{label: '59', value: 59} ]
    initNums() {
      for (let i = 0; i < LENGTH; i++) {
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
      this.$emit('second-change', this.tag_)
    },
    changeSiblingType(type) {
      this.$refs.everys.type_ =
        this.$refs.periods.type_ =
          this.$refs.ranges.type_ =
            this.$refs.fixeds.type_ = type
    }
  }
}
</script>
