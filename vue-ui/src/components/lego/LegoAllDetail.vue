<template>
  <component
    v-if="visible"
    class="d-view"
    :is="componentName"
    :form-code="formCode"
    v-bind="$attrs"
    v-on="$listeners"
  />
</template>
<script type="text/javascript">
import LegoDetail from './LegoDetail'
import crmDetail from '@/views/crm/components/detail'
import { getFormAuth } from '@/utils/auth'

export default {
  name: 'LegoAllDetail',
  components: {
    LegoDetail,
    ...crmDetail
  },
  props: {
    formCode: {
      type: String,
      default: ''
    },
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
  computed: {
    componentName() {
      const auth = getFormAuth(this.formCode)
      if (!auth.dynamicRoute && auth.className && crmDetail[auth.className + 'Detail']) {
        return auth.className + 'Detail'
      }
      return 'LegoDetail'
    }
  },
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
