<template>
  <div class="icon-container">
    <div class="icon-item"
      v-for="(icon, index) in iconData"
      :key="index"
      @click="selectItem(icon.font_class)">
      <i :class="icon.font_class | iconPre" />
      <span>{{ icon.name }}</span>
    </div>
  </div>
</template>
<script>
import data from '@/utils/data/icon-data'

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
    margin: 10px;
    flex-wrap: wrap;
    flex-direction: row;
    align-items: center;
    justify-content: center;
    .icon-item {
      float: left;
      display: flex;
      flex-wrap: wrap;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      width: 60px;
      height: 60px;
      cursor: pointer;
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
