<template>
  <component
    v-if="visible"
    :visible="visible"
    :is="componentName"
    :form-code="formCode"
    v-bind="$attrs"
    v-on="$listeners"/>
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
    visible: {
      type: Boolean,
      default: false
    },
    formCode: {
      type: String,
      default: ''
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
  }
}
</script>
<style lang="scss" scoped>
</style>
