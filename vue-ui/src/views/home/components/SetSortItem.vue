<template>
  <div class="data-statistics card">
    <flexbox class="card-title">
      <div class="card-title-left">
        <span
          :class="icon"
          :style="{ color: iconColor }"
          class="icon" />
        <span class="text">{{ title }}</span>
      </div>
      <slot name="header" />
    </flexbox>
    <div class="content">
      <img :src="img" >
    </div>
  </div>
</template>

<script>
export default {
  name: 'SetSortItem',
  props: {
    title: String,
    icon: String,
    iconColor: String,
    img: String
  },
  data() {
    return {}
  },
  methods: {}
}
</script>

<style scoped lang="scss">
.card {
  background-color: white;
  border-radius: $xr-border-radius-base;
  padding: 10px;
  border: 1px solid #e6e6e6;
  &:hover {
    box-shadow: 0 0 16px 0 rgba(0, 0, 0, 0.08);
    cursor: move;
  }
  &-title {
    position: relative;
    &-left {
      .icon {
        color: #4983ef;
        font-size: 13px;
      }
      .text {
        font-size: 14px;
        font-weight: bold;
        margin-left: 5px;
      }
    }
  }
}

.content {
  text-align: center;
  height: 60px;
  padding-top: 8px;
  overflow: hidden;

  img {
    max-width: 100%;
    height: 100%;
  }
}
</style>
