<template>
  <el-alert
    :title="systemAlertProps.title"
    :type="systemAlertProps.type"
    :description="systemAlertProps.description"
    :closable="systemAlertProps.closable"
    :center="systemAlertProps.center"
    :close-text="systemAlertProps.closeText"
    :show-icon="systemAlertProps.showIcon"
    :effect="systemAlertProps.effect"
    class="system-alert"
    @close="systemAlertClose"/>
</template>

<script>
import { adminConfigIgnoreCompanyStatusAPI } from '@/api/admin/config'

import { mapGetters } from 'vuex'

export default {
  // 顶部系统消息
  name: 'SystemAlert',

  components: {},

  props: {},

  data() {
    return {
    }
  },

  computed: {
    ...mapGetters([
      'systemAlertProps'
    ])
  },

  watch: {},

  created() {},

  mounted() {},

  beforeDestroy() {},

  methods: {
    /**
     * 系统消息关闭
     */
    systemAlertClose() {
      adminConfigIgnoreCompanyStatusAPI().then(res => {
        this.$store.dispatch('SystemLogoAndName')
      }).catch(res => {})
    }
  }
}
</script>

<style lang="scss" scoped>
.system-alert {
  border-bottom: 1px solid #e6e6e6;
}
</style>
