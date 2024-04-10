<template>
  <component
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
    name: {
      type: String,
      default: ''
    },
    formCode: {
      type: String,
      default: ''
    }
  },
  computed: {
    componentName() {
      if (this.name) {
        return this.name
      }
      const auth = getFormAuth(this.formCode)
      if (!auth.dynamicRoute && auth.className && crmDetail[auth.className + 'Detail']) {
        return auth.className + 'Detail'
      }
      return 'LegoDetail'
    }
  }
}
</script>
<style lang="scss" scoped>
</style>
