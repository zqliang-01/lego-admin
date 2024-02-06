<template>
  <div class="app-main">
    <router-view />
    <slot />
  </div>
</template>

<script>
export default {
  name: 'AppMain',
  computed: {}
}
</script>

<style scoped>
.app-main {
  position: relative;
  height: 100%;
  min-width: 900px;
  min-height: 605px;
}
</style>
