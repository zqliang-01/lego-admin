<template>
  <div class="cell-div">
    通配符支持<strong>{{ symbol }}</strong>
  </div>
</template>

<script>
export default {
  props: {
    symbol: {
      type: String,
      default: null
    }
  }
}
</script>
<style lang="scss" scoped>
.cell-div {
    margin-bottom: 8px;
}
</style>
