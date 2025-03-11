<template>
  <flexbox
    class="main"
    direction="column"
    align="stretch">
    <xr-header
      :icon-class="'enterprise'"
      icon-color="#2362FB"
      label="系统配置" />
    <div class="body" v-loading="loading">
      <UpdateInfo :systemInfo="systemInfo"></UpdateInfo>
      <BaseInfo :systemInfo="systemInfo"></BaseInfo>
    </div>
  </flexbox>
</template>

<script>
import BaseInfo from './components/BaseInfo'
import UpdateInfo from './components/UpdateInfo'
import XrHeader from '@/components/XrHeader'

export default {
  name: 'SystemConfig',
  components: {
    BaseInfo,
    UpdateInfo,
    XrHeader
  },
  computed: {
  },
  data() {
    return {
      loading: false,
      systemInfo: {}
    }
  },
  created() {
    this.getDetail()
  },
  methods: {
    getDetail() {
      this.loading = true
      this.$store.dispatch('GetSystemInfo').then(res => {
        this.loading = false
        this.systemInfo = res.data
      }).catch(() => {
        this.loading = false
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.main {
  padding: 0 15px;
  height: 100%;
}

.body {
  flex: 1;
  overflow-y: auto;
  padding: 40px 30px 20px 30px;
  background-color: white;
  border: 1px solid #e6e6e6;
  border-radius: $xr-border-radius-base;
  position: relative;
}
</style>

