<template>
  <div :class="{ 'is-close': collapse }" class="menu-item-content">
    <i
      v-if="icon"
      :class="icon | iconPre"/>
    <span class="side-bar-label">{{ title }}</span>
    <span v-if="count">({{ count }})</span>
    <el-badge
      v-if="num && num > 0"
      :max="99"
      :value="num"/>
  </div>
</template>

<script>

export default {
  name: 'Item',
  components: {},
  props: {
    icon: String,
    title: String,
    num: [String, Number],
    collapse: Boolean,
    count: [String, Number]
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
.menu-item-content {
  width: 100%;
  border-radius: $xr-border-radius-base;
  line-height: 36px;
  margin: 5px 0;
  padding: 0 10px;
  position: relative;
  overflow: hidden;
  text-overflow: ellipsis;
  cursor: pointer;
}

.menu-item-content.is-close {
  .el-badge {
    position: absolute;
    top: 6px;
    left: 50%;
    right: auto;
    transform: translateX(-50%);
  }

  .side-bar-label {
    opacity: 0;
  }
}

.wk {
  margin-right: 8px;
}

.side-bar-label {
  opacity: 1;
  transition: transform .3s;
}

// 消息数
.el-badge {
  position: absolute;
  right: 4px;
  top: 6px;
  ::v-deep .el-badge__content {
    border-width: 0;
  }
}
</style>
