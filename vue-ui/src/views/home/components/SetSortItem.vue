<template>
  <div class="data-statistics card">
    <flexbox class="card-title">
      <div class="card-title-left">
        <span
          :class="data.prop.icon"
          :style="{ color: data.prop.iconColor }"
          class="icon" />
        <span class="text">{{ data.name }}</span>
      </div>
      <slot name="header" />
    </flexbox>
    <div class="content">
      <img :src="data.prop.image" >
    </div>
  </div>
</template>

<script>
export default {
  name: 'SetSortItem',
  props: {
    data: {
      type: Object,
      default: {}
    }
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
