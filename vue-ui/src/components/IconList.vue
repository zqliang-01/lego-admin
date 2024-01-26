<template>
  <div class="icon-container">
    <a
      v-for="(icon, index) in iconData"
      :key="index"
      @click="selectItem(icon.value)">
      <i :class="icon.value | iconPre" />
      <span>{{ icon.name }}</span>
    </a>
  </div>
</template>
<script>
import data from '@/utils/icon-data.js'
export default {
  name: 'IconList',
  data() {
    return {
      iconData: data,
      activeIndex: 0
    }
  },
  methods: {
    selectItem(icon) {
      this.$emit('select', icon)
    }
  }
}
</script>

<style lang='scss' scoped>
@import '@/styles/emoji-sprite.scss';
.icon-container {
    overflow-y: auto;
    overflow-x: hidden;
    position: relative;
    margin: 10px;
    a {
      float: left;
      overflow: hidden;
      height: 60px;
      width: 60px;
      text-align: center;
      transition: all ease-out 0.2s;
      border-radius: 4px;
      margin: 5px;
      &:hover {
        background-color: #d8d8d8;
        border-color: #d8d8d8;
      }
      i {
        font-size: 25px;
      }
    }
    span {
      font-size: 10px;
      width: 60px;
      float: left;
      text-align: center;
      line-height: 10px;
      display: block;
    }
  }
</style>
