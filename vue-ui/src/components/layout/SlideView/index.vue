<template>
  <slide-transaction
    v-if="visible"
    class="d-view"
    v-bind="$attrs"
    v-on="$listeners"
  >
    <slot/>
  </slide-transaction>
</template>
<script type="text/javascript">
import SlideTransaction from './SlideTransaction'

export default {
  name: 'SlideView',
  components: {
    SlideTransaction
  },
  props: {
    visible: {
      type: Boolean,
      default: false
    }
  },
  mounted() {
    this.$nextTick(() => {
      document.body.appendChild(this.$el)
    })
  },
  destroyed() {
    if (this.$el && this.$el.parentNode) {
      setTimeout(() => {
        this.$el.parentNode.removeChild(this.$el)
      }, 350)
    }
  }
}
</script>
<style lang="scss" scoped>
</style>
