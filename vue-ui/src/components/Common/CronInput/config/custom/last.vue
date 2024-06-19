<template>
  <div class="cell-div">
    <el-radio v-model="type_" :label="label" @change="change">
      <span class="cell-symbol">{{ tag_ }}</span>
      本{{ targetTimeUnit }}倒数第
      <el-input-number v-model="lastNum" :precision="0" :min="lastConfig.min" :step="lastConfig.step" :max="lastConfig.max" :size="size" :disabled="type_ !== label" />
      {{ timeUnit }}
    </el-radio>
  </div>
</template>

<script>
import { LAST } from '../../constant/filed'
import watchValue from '../../mixins/watchValue'
import { isNumber } from '../../util/tools'

export default {
  mixins: [watchValue],
  props: {
    lastConfig: {
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
    targetTimeUnit: {
      type: String,
      default: null
    },
    type: {
      type: String,
      default: LAST
    },
    tag: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      label: LAST,
      type_: this.type,
      lastNum: 1
    }
  },
  computed: {
    tag_: {
      get() {
        return this.lastNum === 1 ? LAST : LAST + '-' + (this.lastNum - 1)
      },
      set(newValue) {
        if (this.type_ !== LAST) {
          return
        }
        if (newValue === LAST) {
          this.lastNum = 1
          return
        }
        const numStr = newValue.substring(2)
        if (!isNumber(numStr) || parseInt(numStr) < this.lastConfig.min - 1 || parseInt(numStr) > this.lastConfig.max - 1) {
          this.$message.error('含有非法数字:' + numStr)
          return
        }
        this.lastNum = parseInt(numStr) + 1
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
