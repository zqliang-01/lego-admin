<template>
  <div class="cell-div">
    <el-radio v-model="type_" :label="label" @change="change">
      <span class="cell-symbol">{{ tag_ }}</span>
      在
      <el-input-number v-model="lower" :precision="0" :min="lowerConfig.min" :step="lowerConfig.step" :max="upper_" :size="size" :disabled="type_ !== label" />
      {{ timeUnit }}到
      <el-input-number v-model="upper_" :precision="0" :min="lower" :step="upperConfig.step" :max="upperConfig.max" :size="size" :disabled="type_ !== label" />
      之间的每{{ timeUnit }}
    </el-radio>
  </div>
</template>

<script>
import { RANGE } from '../../constant/filed'
import watchValue from '../../mixins/watchValue'
import { isNumber } from '../../util/tools'

export default {
  mixins: [watchValue],
  props: {
    upper: {
      type: Number,
      default: 1
    },
    lowerConfig: {
      type: Object,
      default: null
    },
    upperConfig: {
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
      default: RANGE
    },
    tag: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      label: RANGE,
      type_: this.type,
      lower: 0,
      upper_: this.upper
    }
  },
  computed: {
    tag_: {
      get() {
        return this.lower + RANGE + this.upper_
      },
      set(newValue) {
        if (this.type_ !== RANGE) {
          return
        }
        const arr = newValue.split(RANGE)
        if (arr.length !== 2) {
          this.$message.error('表达式不正确:' + newValue)
          return
        }
        if (!isNumber(arr[0]) || parseInt(arr[0]) < this.lowerConfig.min || parseInt(arr[0]) > this.lowerConfig.max) {
          this.$message.error('下限格式不符:' + arr[0])
          return
        }
        if (!isNumber(arr[1]) || parseInt(arr[1]) < this.upperConfig.min || parseInt(arr[1]) > this.upperConfig.max) {
          this.$message.error('上限格式不符:' + arr[1])
          return
        }
        if (parseInt(arr[0]) > parseInt(arr[1])) {
          this.$message.error('下限不能比上限大:' + arr[0] + '>' + arr[1])
          return
        }
        this.lower = parseInt(arr[0])
        this.upper_ = parseInt(arr[1])
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
