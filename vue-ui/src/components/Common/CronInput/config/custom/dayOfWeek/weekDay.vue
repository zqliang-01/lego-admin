<template>
  <div class="cell-div">
    <el-radio v-model="type_" :label="label" @change="change">
      <span class="cell-symbol">{{ tag_ }}</span>
      本{{ targetTimeUnit }}第
      <el-input-number v-model="nth" :precision="0" :size="size" :min="1" :step="1" :max="5" :disabled="type_ !== label" />
      个
      <el-select
        v-model="weekDayNum"
        :size="size"
        placeholder="请选择"
        :disabled="type_ !== label"
        style="width: 100px;"
        filterable
      >
        <el-option
          v-for="item in nums"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
    </el-radio>
  </div>
</template>

<script>
import watchValue from '../../../mixins/watchValue'
import { WEEK_DAY } from '../../../constant/filed'
import { isNumber } from '../../../util/tools'

export default {
  mixins: [watchValue],
  props: {
    nums: {
      type: Array,
      default: null
    },
    size: {
      type: String,
      default: 'mini'
    },
    timeUnit: {
      type: String,
      default: null
    },
    targetTimeUnit: {
      type: String,
      default: null
    },
    type: {
      type: String,
      default: WEEK_DAY
    },
    tag: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      label: WEEK_DAY,
      type_: this.type,
      nth: null,
      weekDayNum: 1
    }
  },
  computed: {
    tag_: {
      get() {
        return this.weekDayNum + WEEK_DAY + this.nth
      },
      set(newValue) {
        if (this.type_ !== WEEK_DAY) {
          return
        }
        const arr = newValue.split(WEEK_DAY)
        if (arr.length !== 2) {
          this.$message.error('表达式不正确:' + newValue)
          return
        }
        if (!isNumber(arr[0]) || parseInt(arr[0]) < this.nums[0].value || parseInt(arr[0]) > this.nums[this.nums.length - 1].value) {
          this.$message.error('周数格式不符:' + arr[0])
          return
        }
        if (!isNumber(arr[1]) || parseInt(arr[1]) < 1 || parseInt(arr[1]) > 5) {
          this.$message.error('天数格式不符:' + arr[1])
          return
        }
        this.weekDayNum = parseInt(arr[0])
        this.nth = parseInt(arr[1])
      }
    }
  },
  methods: {
    change() {
      this.$emit('type-changed', this.type_)
      this.$emit('tag-changed', this.tag_)
    }
  }
}
</script>
<style lang="scss" scoped>
.cell-symbol {
  color: #67C23A;
}
.cell-div {
    margin-bottom: 8px;
}
</style>
