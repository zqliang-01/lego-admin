<template>
  <lego-detail
    v-if="visible"
    class="d-view"
    v-bind="$attrs"
    v-on="$listeners"
  />
</template>
<script type="text/javascript">
import LegoDetail from './LegoDetail'

export default {
  name: 'LegoAllDetail',
  components: {
    LegoDetail
  },
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    item: {
      type: Object,
      default: () => {}
    }
  },
  data() {
    return {}
  },
  computed: {},
  watch: {},
  mounted() {
    this.$nextTick(() => {
      document.body.appendChild(this.$el)
    })
  },
  destroyed() {
    if (this.$el && this.$el.parentNode) {
      this.$el.parentNode.removeChild(this.$el)
    }
  },
  methods: {
    handle(data) {
      this.$emit('handle', data)
    },
    hiddenView() {
      this.$emit('hide-view')
    }
  }
}
</script>
<style lang="scss" scoped>
</style>
