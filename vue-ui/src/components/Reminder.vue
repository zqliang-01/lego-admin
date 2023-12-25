<template>
  <flexbox class="reminder-wrapper">
    <flexbox
      align="stretch"
      class="reminder-body">
      <i :class="'warning reminder-icon' | iconPre"/>
      <div
        :style="{'font-size': fontSize + 'px'}"
        class="reminder-content"
        v-html="content"/>
      <slot />
      <i
        v-if="closeShow"
        class="el-icon-close close"
        @click="close" />
    </flexbox>
  </flexbox>
</template>

<script type="text/javascript">
// 警示信息

export default {
  name: 'Reminder',
  components: {},
  props: {
    closeShow: {
      type: Boolean,
      default: false
    },
    content: {
      type: String,
      default: '内容'
    },
    fontSize: {
      type: String,
      default: '13'
    }
  },
  data() {
    return {}
  },
  computed: {},
  mounted() {},
  destroyed() {},
  methods: {
    close() {
      this.$emit('close')
    }
  }
}
</script>
<style lang="scss" scoped>
.reminder-wrapper {
  .reminder-body {
    width: auto;
    padding: 0 8px;
    line-height: 28px;
    border-radius: $xr-border-radius-base;
    background-color: #FFF6E7;
    .reminder-icon {
      color: #F9A74E;
      margin-right: 5px;
      font-size: 14px;
    }

    .reminder-content {
      color: #666666;
      word-wrap:break-word;
      white-space: pre-wrap;
      word-break: break-all;
    }

    .close {
      display: block;
      font-size: 16px;
      color: #909399;
      margin-left: 8px;
      cursor: pointer;
      line-height: 31px;
    }
    .close:hover {
      color: $xr-color-primary;
    }
  }
}
</style>
