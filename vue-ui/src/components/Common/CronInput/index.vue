<template>
  <div>
    <el-popover v-model="visible">
      <cron v-model="cron_" @change="change" />
      <el-input :disabled="disabled" slot="reference" v-model="cron_" placeholder="Cron表达式">
        <el-button slot="append" icon="el-icon-refresh" @click="reset" />
      </el-input>
    </el-popover>
  </div>
</template>

<script>
import Cron from './cron'
import { DEFAULT_CRON_EXPRESSION } from './constant/filed'
export default {
  name: 'CronInput',
  components: {
    Cron
  },
  props: {
    value: {
      type: String,
      default: DEFAULT_CRON_EXPRESSION
    },
    disabled: Boolean
  },
  data() {
    return {
      cron_: this.value,
      visible: false
    }
  },
  watch: {
    value(val) {
      this.setCron(val)
    }
  },
  methods: {
    setCron(newValue) {
      if (newValue && newValue.trim().length < 11) {
        this.$message.error('格式不正确，必须大于等于11位')
        return
      }
      this.cron_ = newValue
    },
    change(cron) {
      this.cron_ = cron
      this.$emit('input', this.cron_)
    },
    reset() {
      this.$emit('reset', this.value)
    }
  }
}
</script>
