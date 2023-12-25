<template>
  <div class="setting-number">
    <flexbox
      align="center"
      justify="flex-start"
      class="setting-number-item">
      <el-checkbox v-model="checked" @change="checkedChange" />
      <span style="font-size: 13px;">支持小数</span>
      <el-tooltip
        content="不选择只能输入整数，勾选后可规定小数位数"
        effect="dark"
        popper-class="setting-number-tooltip"
        placement="top">
        <i :class="'help wk-help-tips' | iconPre" />
      </el-tooltip>
      <template v-if="checked">
        <span>限制&nbsp;</span>
        <el-select
          v-model="field.precisions"
          size="small"
          placeholder=""
          @change="handleSelectChange">
          <el-option
            v-for="item in precisionList"
            :key="item.value"
            :label="item.label"
            :value="item.value" />
        </el-select>
        <span>&nbsp;位</span>
      </template>
    </flexbox>
  </div>
</template>

<script>
import { isEmpty } from '@/utils/types'

export default {
  name: 'SettingNumber',
  props: {
    field: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      checked: false,
      precisionList: []
    }
  },
  watch: {
    field: {
      handler() {
        if ([
          'floatnumber',
          'percent'
        ].includes(this.field.formType)) {
          this.checked = !isEmpty(this.field.precisions)
        }
      },
      deep: true,
      immediate: true
    }
  },
  methods: {
    checkedChange() {
      if (!this.checked) {
        this.field.precisions = null
      } else {
        this.field.precisions = 2
      }
    },
    handleSelectChange() {
      this.$set(this.field, 'precisions', this.field.precisions)
      this.$forceUpdate()
    }
  }
}
</script>

  <style>
  .setting-number-tooltip {
    max-width: 250px;
  }
  </style>
  <style scoped lang="scss">
  .setting-number-item {
    height: 32px;
    &:nth-child(2) {
      height: auto;
      margin-top: 10px;
    }
    .el-checkbox {
      margin-right: 8px;
    }
    .el-tooltip {
      margin: 0 10px 0 5px;
    }
    .el-select {
      width: 70px;
    }
    .el-input-number {
      width: 100%;
    }
    .number-range-body {
      margin-top: 10px;
    }
    .number-range-text {
      padding: 0 10px;
    }
  }
  </style>
