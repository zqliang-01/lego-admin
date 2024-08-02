<template>
  <component
    v-if="visible"
    :visible="visible"
    :is="componentName"
    :form-code="formCode"
    class="d-view"
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
    visible: {
      type: Boolean,
      default: false
    },
    formCode: {
      type: String,
      default: ''
    }
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
  methods: {
  }
}
</script>
<style lang="scss" scoped>
</style>
