<template>
  <lego-create
    v-if="visible"
    v-bind="$attrs"
    v-on="$listeners"
    @hiden-view="hiddenView"
    @close="hiddenView"/>
</template>

<script type="text/javascript">
import LegoCreate from './LegoCreate'
export default {
  name: 'LegoAllCreate',
  components: {
    LegoCreate
  },
  inheritAttrs: false,
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
  methods: {
    hiddenView() {
      this.$emit('close')
    }
  }
}
</script>
<style lang="scss" scoped>
</style>
