<template>
  <el-popover
    v-model="popverVisible"
    :width="width"
    placement="bottom"
    popper-class="no-padding-popover"
    trigger="click">
    <div class="xr-radio-menu">
      <div class="xr-radio-menu__content">
        <div
          v-for="(item, index) in options"
          :key="index"
          :class="{ 'selected' : value == item.command}"
          class="xr-radio-menu-item"
          @click="selectClick(item)">
          <div class="mark"/>{{ item.label }}
        </div>
      </div>
    </div>
    <slot
      slot="reference"
      name="reference" />
  </el-popover>
</template>

<script type="text/javascript">
export default {
  name: 'XrRadioMenu',
  props: {
    width: {
      type: [String, Number],
      default: 200
    },
    options: Array, // {command label}
    value: [String, Number]
  },
  data() {
    return {
      popverVisible: false
    }
  },
  computed: {
  }, // 时间类型选择
  watch: {
  },
  mounted() {
  },
  methods: {
    // 类型选择
    selectClick(item) {
      this.popverVisible = false
      this.$emit('input', item.command)
      this.$emit('select', item.command)
    }
  }
}
</script>
<style lang="scss" scoped>
.xr-radio-menu {
  .xr-radio-menu__content {
    max-height: 250px;
    overflow-y: auto;
    .xr-radio-menu-item {
      height: 34px;
      line-height: 34px;
      padding: 0 20px;
      color: #606266;
      position: relative;
      cursor: pointer;
      .mark {
        display: inline-block;
        width: 6px;
        height: 6px;
        border-radius: 3px;
        margin-right: 8px;
        background-color: transparent;
      }
    }

    .selected {
      color: $xr-color-primary;
      font-weight: bold;
      .mark {
        background-color: $xr-color-primary;
      }
    }
    .xr-radio-menu-item:hover {
      background-color: #f5f7fa;
    }
  }
}
</style>
