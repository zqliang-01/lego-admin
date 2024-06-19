<template>
  <div class="cell-div">
    值为<strong>{{ val }}</strong>
  </div>
</template>

<script>
export default {
  props: {
    val: {
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
