<template>
  <div>
    <el-input
      v-model="currentIcon"
      :clearable="clearable"
      :disabled="disabled"
      placeholder="请选择图标"
      class="input-with-select"
      @change="valueChange">
      <i slot="prepend" :class="currentIcon | iconPre" />
      <el-button slot="append" icon="el-icon-search" @click="dialogTableVisible = !dialogTableVisible"></el-button>
    </el-input>
    <el-dialog
      :append-to-body="true"
      :visible.sync="dialogTableVisible"
      width="60%"
      title="图标选择">
      <div style="max-height: 500px; overflow-y: auto;">
        <icon-list @select="iconSelect" />
      </div>
    </el-dialog>
  </div>
</template>

<script>
import IconList from '@/components/IconList'
import Emitter from 'element-ui/lib/mixins/emitter'

export default {
  name: 'SelectIcon',
  mixins: [Emitter],
  props: {
    value: [String, Number],
    placeholder: { type: String, default: '请选择' },
    clearable: { type: Boolean, default: () => { return true } },
    disabled: { type: Boolean, default: () => { return false } }
  },
  components: {
    IconList
  },
  watch: {
    value: {
      handler(newVal, oldVal) {
        this.currentIcon = this.value
      },
      immediate: true
    }
  },
  data() {
    return {
      currentIcon: '',
      dialogTableVisible: false
    }
  },
  mounted() {
    this.currentIcon = this.value
  },
  methods: {
    iconSelect(value) {
      this.currentIcon = value
      this.dialogTableVisible = false
      this.$emit('input', this.currentIcon)
      this.$emit('change', this.currentIcon)
      this.dispatch('ElFormItem', 'el.form.change', this.currentIcon)
    },
    valueChange() {
      this.$emit('input', this.currentIcon)
      this.$emit('change', this.currentIcon)
      this.dispatch('ElFormItem', 'el.form.change', this.currentIcon)
    }
  }
}
</script>

<style lang="scss" scoped>
</style>
