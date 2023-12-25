<template>
  <div
    :class="{'is-select':select}"
    class="xr-menu-item">
    <i
      :style="{backgroundColor: select ? iconColor || '#2362FB' : '#edf2f6'}"
      :class="['xr-menu-item__icon', iconClass]" />
    <span class="xr-menu-item__label">{{ label }}</span>
    <el-badge
      v-if="num > 0"
      :max="99"
      :value="num"/>
  </div>
</template>

<script>
export default {
  // 菜单
  name: 'XrMenuItem',
  components: {},
  props: {
    iconClass: String,
    iconColor: String,
    label: String,
    // 关键字
    name: String,
    num: [String, Number],
    select: Boolean
  },
  data() {
    return {}
  },
  computed: {},
  watch: {},
  mounted() {},

  beforeDestroy() {},
  methods: {}
}
</script>

<style lang="scss" scoped>
.xr-menu-item {
  padding: 12px 20px;
  position: relative;
  cursor: pointer;

  &__icon {
    display: inline-block;
    font-size: 12px;
    padding: 6px;
    width: 25px;
    height: 25px;
    border-radius: $xr-border-radius-base;
    background-color: #edf2f6;
    color: #8a94a6;;
  }

  &__label {
    color: #333;
    font-size: 13px;
    margin-left: 10px;
  }

  &.is-select {
    .xr-menu-item__icon {
      background-color: $xr-color-primary;
      color: white;
    }
  }
}

.xr-menu-item.is-select,
.xr-menu-item:hover {
  background-color: $xr--background-color-base;
}

.xr-menu-item::before {
  content: ' ';
  position: absolute;
  left: 0;
  top: 0;
  bottom: 0;
  width: 2px;
  background-color: $xr-color-primary;
  opacity: 0;
}

.xr-menu-item.is-select::before {
  opacity: 1;
}

.el-badge /deep/ .el-badge__content {
  border: none;
  top: 0;
}

.el-badge {
  position: absolute;
  right: 10px;
  top: 15px;
}
</style>
