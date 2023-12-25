<template>
  <div class="xr-table-header">
    <flexbox
      v-show="!handleShow"
      class="xr-table-header__body"
      align="center">
      <slot />
    </flexbox>
    <flexbox
      v-show="handleShow"
      class="xr-table-header__handle">
      <div class="selected—title">已选中 <span class="selected—count">{{ selects.length }}</span> 项</div>
      <flexbox class="selection-items-box">
        <slot name="prefix" />
        <el-button
          v-for="(item, index) in handles"
          :icon="item.icon | iconPre"
          :key="index"
          type="primary"
          @click.native="selectionBarClick(item.command)">{{ item.label }}</el-button>
        <slot name="suffix" />
      </flexbox>
    </flexbox>
    <slot name="append" />
  </div>
</template>

<script>

export default {
  // 表头
  name: 'XrTableHeader',
  components: {
  },
  props: {
    selects: {
      type: Array,
      default: () => {
        return []
      }
    },
    handles: {
      type: Array,
      default: () => {
        return []
      }
    }
  },
  data() {
    return {}
  },
  computed: {
    // 操作展示
    handleShow() {
      return this.selects && this.selects.length > 0
    }
  },
  watch: {},
  mounted() {},

  beforeDestroy() {},
  methods: {
    selectionBarClick(command) {
      this.$emit('command', command)
    }
  }
}
</script>

<style lang="scss" scoped>
.xr-table-header {
  background: white;

  &__handle {
    font-size: 12px;
    height: 50px;
    padding: 0 20px;
    color: #777;

    .selected—title {
      flex-shrink: 0;
      padding-right: 20px;
      color: #333;
      .selected—count {
        color: $xr-color-primary;
      }
    }
  }

  .selection-items-box {
    overflow-x: auto;
    overflow-y: hidden;
    padding: 0 15px;

    .el-button {
      color: #666;
      background-color: #f9f9f9;
      border-color: #e5e5e5;
      font-size: 12px;
      height: 28px;
      border-radius: 4px;
      /deep/ i {
        font-size: 12px;
        margin-right: 5px;
      }
    }

    .el-button--primary:hover {
      background: $xr-color-primary;
      border-color: $xr-color-primary;
      color: #ffffff;
    }

    .el-button + .el-button {
      margin-left: 15px;
    }
  }

  &__body {
    font-size: 13px;
    height: 50px;
    padding: 0 20px;
  }
}
</style>
