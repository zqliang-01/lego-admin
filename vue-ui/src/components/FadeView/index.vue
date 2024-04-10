<template>
  <fade-transaction
    v-if="visible"
    :body-style="{ height: '100%'}"
    @afterEnter="afterEnter">
    <flexbox
      v-loading="loading"
      direction="column"
      align="stretch"
      class="fade-view">
      <flexbox class="fade-view__header">
        <div v-if="!$slots.title" class="title">{{ title }}</div>
        <slot name="title" />
        <i class="el-icon-close close" @click="close" />
      </flexbox>
      <div class="fade-view__body">
        <slot />
      </div>
      <div class="fade-view__footer">
        <el-button
          v-if="showCancel"
          @click.native="close">取消</el-button>
        <slot name="footer" />
        <el-button
          v-if="showConfirm"
          type="primary"
          @click.native="debouncedSaveField(false)">{{ confirmButtonText }}</el-button>
      </div>
    </flexbox>
  </fade-transaction>
</template>

<script type="text/javascript">
import FadeTransaction from './FadeTransaction'
import { debounce } from 'throttle-debounce'

export default {
  name: 'FadeLayout',
  components: {
    FadeTransaction
  },
  inheritAttrs: false,
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    title: String,
    loading: Boolean,
    appendToBody: {
      type: Boolean,
      default: true
    },
    showCancel: {
      type: Boolean,
      default: true
    },
    showConfirm: {
      type: Boolean,
      default: true
    },
    confirmButtonText: {
      type: String,
      default: '保存'
    }
  },
  data() {
    return {}
  },
  computed: {},
  watch: {},
  created() {
    this.debouncedSaveField = debounce(300, this.save)
  },
  mounted() {
    if (this.appendToBody) {
      document.body.appendChild(this.$el)
    }
  },

  destroyed() {
    if (this.appendToBody) {
      if (this.$el && this.$el.parentNode) {
        this.$el.parentNode.removeChild(this.$el)
      }
    }
  },
  methods: {
    afterEnter() {
      this.$emit('afterEnter')
    },

    save() {
      this.$emit('save')
    },

    close() {
      this.$emit('close')
    }
  }
}
</script>
<style lang="scss" scoped>
.fade-view {
  position: relative;
  height: 100%;

  &__header {
    height: 40px;
    margin-bottom: 20px;
    padding: 0 10px;
    flex-shrink: 0;
    .title {
      flex:1;
      font-size:17px;
      color:#333;
      font-weight: bold;
    }
    .close {
      display: block;
      font-size: 24px;
      color: #909399;
      margin-right: -10px;
      padding: 10px;
      cursor: pointer;
    }
    .close:hover {
      color: $xr-color-primary;
    }
  }

  &__body {
    position: relative;
    overflow-x: hidden;
    overflow-y: auto;
    flex: 1;
  }

  &__footer {
    position: relative;
    text-align: right;
    padding: 5px 20px 0;
  }
}
</style>
