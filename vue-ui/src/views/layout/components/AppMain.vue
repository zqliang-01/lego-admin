<template>
  <div class="app-main">
    <router-view />
    <slot />
  </div>
</template>

<script setup>
defineOptions({
  name: 'AppMain'
})
</script>

<style scoped>
.app-main {
  position: relative;
  height: 100%;
  min-width: 900px;
  min-height: 500px;
}
</style>
