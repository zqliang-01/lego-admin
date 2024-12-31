<template>
  <div class="setting-precisions">
    <el-select
      v-model="field.precisions"
      placeholder="请选择">
      <el-option
        v-for="item in options"
        :key="item.value"
        :label="item.label"
        :value="item.value" />
    </el-select>
  </div>
</template>

<script>
export default {
  name: 'SettingPrecisions',
  props: {
    field: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      options: []
    }
  },
  watch: {
    field: {
      handler() {
        if (!['select', 'checkbox'].includes(this.field.formType)) return
        this.options = [
          { label: '平铺', value: 1 },
          { label: '下拉', value: 2 }
        ]
        if (!this.field.precisions) {
          this.$set(this.field, 'precisions', this.field.formType === 'checkbox' ? 1 : 2)
        }
      },
      deep: true,
      immediate: true
    }
  }
}
</script>

<style scoped>
.el-select {
  width: 100%;
}
</style>
