<template>
  <component
    v-loading="loading"
    v-if="componentName"
    :is="componentName"
    v-bind="$attrs"
    v-on="$listeners"/>
</template>

<script type="text/javascript">
import LegoIndex from './LegoIndex'
import LegoReportIndex from './LegoReport'
import { permissionGetAPI } from '@/api/admin/permission'

export default {
  name: 'LegoAllIndex',
  components: {
    LegoIndex,
    LegoReportIndex
  },
  watch: {
    $route: function(val) {
      this.initComponent()
    }
  },
  data() {
    return {
      loading: false,
      componentName: ''
    }
  },
  computed: {
  },
  mounted() {
    this.initComponent()
  },
  methods: {
    initComponent() {
      this.loading = true
      this.componentName = ''
      permissionGetAPI(this.$route.params.menuCode).then(res => {
        if (res.data.type.code === 'menu') {
          this.componentName = 'LegoIndex'
        }
        if (res.data.type.code === 'report') {
          this.componentName = 'LegoReportIndex'
        }
        this.loading = false
      }).catch(res => {
        this.loading = false
      })
    }
  }
}
</script>
<style lang="scss" scoped>
</style>
