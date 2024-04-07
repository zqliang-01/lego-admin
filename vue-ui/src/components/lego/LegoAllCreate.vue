<template>
  <component
    v-if="visible"
    :is="componentName"
    :form-code="formCode"
    v-bind="$attrs"
    v-on="$listeners"
    @hiden-view="hiddenView"
    @close="hiddenView"/>
</template>

<script type="text/javascript">
import LegoCreate from './LegoCreate'
import crmCreate from '@/views/crm/components/create'
import { getFormAuth } from '@/utils/auth'

export default {
  name: 'LegoAllCreate',
  components: {
    LegoCreate,
    ...crmCreate
  },
  inheritAttrs: false,
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
      if (!auth.dynamicRoute && auth.className && crmCreate[auth.className + 'Create']) {
        return auth.className + 'Create'
      }
      return 'LegoCreate'
    }
  },
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
