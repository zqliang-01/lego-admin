<template>
  <div
    :class="data.enable ? 'edit-view' : 'edit-view-disable'"
    @click="handleNodeClick">
    <slot v-if="!empty"/>
    <span v-else :style="{height: height + 'px'}">{{ tips }}</span>
    <div v-if="data.enable" class="edit-view__cover">
      <span v-if="empty">
        <i class="el-icon-circle-plus-outline"/>添加数据源
      </span>
      <span v-else>
        <i class="el-icon-edit"/>编辑数据源
      </span>
    </div>
    <div v-else class="edit-view-disable__cover">
      <span>
        图表已被禁用，点击启用
      </span>
    </div>
  </div>
</template>

<script>
export default {
  name: 'EditView',
  props: {
    empty: {
      type: Boolean,
      default: false
    },
    height: {
      type: Number,
      default: 50
    },
    tips: {
      type: String,
      default: '暂无内容，请编辑数据源！'
    },
    data: {
      type: Object,
      default: {}
    }
  },
  data() {
    return {
    }
  },
  methods: {
    handleNodeClick() {
      this.$emit('onClick', this.data)
    }
  }
}
</script>

<style scoped lang="scss">
.edit-view {
  position: relative;
  margin-top: 15px;
  cursor: pointer;
  box-sizing: border-box;
  border: 1px dashed #d9d9d9;
  background-color: white;
  border-radius: $xr-border-radius-base;
  span {
    display: flex;
    justify-content: center;
    align-items: center;
    color: $xr-color-text-placeholder;
  }
  &__cover{
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    display: none;
    position: absolute;
    background: rgba(0, 0, 0, 0.3);
    span {
      position: absolute;
      top: 50%;
      left: 50%;
      transform: translate(-50%, -50%);
      font-size: 15px;
      i {
        color: #fff;
        padding-right: 5px;
      }
    }
  }
}
.edit-view-disable {
  position: relative;
  margin-top: 15px;
  cursor: pointer;
  box-sizing: border-box;
  background: rgba(0, 0, 0, 0.3);
  filter: blur(1px);
  border: 1px dashed #d9d9d9;
  background-color: white;
  border-radius: $xr-border-radius-base;
  &__cover{
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    display: block;
    position: absolute;
    background: rgba(0, 0, 0, 0.25);
    span {
      position: absolute;
      top: 50%;
      left: 50%;
      transform: translate(-50%, -50%);
      font-size: 15px;
      color: #fff;
    }
  }
}
.edit-view:hover,.edit-view-disable:hover {
  border-color: #2362FB;
  .edit-view__cover {
    display: block;
    cursor: pointer;
    span {
      filter: blur(0px);
      color: #fff;
    }
  }
}
</style>
