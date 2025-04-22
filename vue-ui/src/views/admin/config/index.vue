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
      <UpdateInfo :system-info="systemInfo" />
      <BaseInfo :system-info="systemInfo" />
    </div>
  </flexbox>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useStore } from 'vuex'
import BaseInfo from './components/BaseInfo'
import UpdateInfo from './components/UpdateInfo'
import XrHeader from '@/components/XrHeader'

const store = useStore()
const loading = ref(false)
const systemInfo = ref({})

const getDetail = async () => {
  loading.value = true
  try {
    const res = await store.dispatch('GetSystemInfo')
    systemInfo.value = res.data
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  getDetail()
})
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
