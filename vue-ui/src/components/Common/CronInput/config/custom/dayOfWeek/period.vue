<template>
  <div class="cell-div">
    <el-radio v-model="type_" :label="label" @change="change">
      <span class="cell-symbol">{{ tag_ }}</span>
      从
      <el-select
        v-model="start"
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
      开始每
      <el-input-number v-model="cycle" :precision="0" :min="cycleConfig.min" :step="cycleConfig.step" :max="cycleConfig.max" :size="size" :disabled="type_ !== label" />
      {{ timeUnit }}
    </el-radio>
  </div>
</template>

<script>
import { PERIOD } from '../../../constant/filed'
import watchValue from '../../../mixins/watchValue'
import { isNumber } from '../../../util/tools'

export default {
  mixins: [watchValue],
  props: {
    nums: {
      type: Array,
      default: null
    },
    startConfig: {
      type: Object,
      default: null
    },
    cycleConfig: {
      type: Object,
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
    type: {
      type: String,
      default: PERIOD
    },
    tag: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      label: PERIOD,
      type_: this.type,
      start: 1,
      cycle: 1
    }
  },
  computed: {
    tag_: {
      get() {
        return this.start + PERIOD + this.cycle
      },
      set(newValue) {
        if (this.type_ !== PERIOD) {
          return
        }
        const arr = newValue.split(PERIOD)
        if (arr.length !== 2) {
          this.$message.error('表达式不正确:' + newValue)
          return
        }
        if (!isNumber(arr[0]) || parseInt(arr[0]) < this.startConfig.min || parseInt(arr[0]) > this.startConfig.max) {
          this.$message.error('开始格式不符:' + arr[0])
          return
        }
        if (!isNumber(arr[1]) || parseInt(arr[1]) < this.cycleConfig.min || parseInt(arr[1]) > this.cycleConfig.max) {
          this.$message.error('循环格式不符:' + arr[1])
          return
        }
        this.start = parseInt(arr[0])
        this.cycle = parseInt(arr[1])
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
