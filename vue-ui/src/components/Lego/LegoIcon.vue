<template>
  <div class="item-icon">
    <i :style="[fontStyle]" :class="name | iconPre" class="lego-font"></i>
  </div>
</template>

<script>
export default {
  name: 'LegoIcon',
  props: {
    name: {
      type: String,
      required: true
    },
    size: Number,
    color: String,
    backgroundColor: String,
    shape: {
      type: String,
      default: 'circle'
    }
  },
  computed: {
    fontStyle() {
      return {
        color: this.color,
        backgroundColor: this.backgroundColor,
        fontSize: this.size + 'px',
        width: this.size + 'px',
        height: this.size + 'px',
        lineHeight: this.size + 'px',
        borderRadius: this.shape === 'circle' ? '80px' : '10px'
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.item-icon {
  display: inline-block;
  .lego-font {
    margin: 0 auto;
    display: block;
    text-align: center;
  }
}
</style>
