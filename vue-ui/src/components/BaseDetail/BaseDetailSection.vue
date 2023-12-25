<template>
  <flexbox
    :gutter="gutter"
    align="stretch"
    class="base-detail-section"
    wrap="wrap">
    <flexbox-item
      v-for="(item, index) in list"
      :span="item.span ? item.span : 0.5"
      :key="index">
      <flexbox
        align="stretch"
        class="base-detail-section__item">
        <div class="item-name">{{ item.label }}</div>
        <div class="item-value">{{ item.value }}</div>
      </flexbox>
    </flexbox-item>
    <el-dropdown
      v-if="dropdownItems && dropdownItems.length > 0"
      trigger="hover"
      class="base-detail-section__more"
      @command="handleTypeClick">
      <i
        style="cursor: pointer;"
        class="el-icon-more" />
      <el-dropdown-menu slot="dropdown">
        <el-dropdown-item
          v-for="(item, index) in dropdownItems"
          :key="index"
          :icon="item.icon"
          :command="item.command">{{ item.label }}</el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
    <slot />
  </flexbox>
</template>

<script>

export default {
  name: 'BaseDetailSection',
  components: {},
  props: {
    list: Array,
    gutter: {
      type: Number,
      default: 0
    },
    dropdownItems: Array
  },
  data() {
    return {}
  },
  computed: {},
  watch: {},
  mounted() {},

  beforeDestroy() {},
  methods: {
    handleTypeClick(type) {
      this.$emit('command-select', type)
    }
  }
}
</script>

<style lang="scss" scoped>
.base-detail-section {
  position: relative;
  &__item {
    width: auto;
    padding: 8px;

    .item-name {
      width: 100px;
      margin-right: 10px;
      font-size: 13px;
      flex-shrink: 0;
      color: #777;
      line-height: 40px;
    }
    .item-value {
      font-size: 13px;
      color: #333;
      line-height: 40px;
      white-space: pre-wrap;
      word-wrap: break-word;
      word-break: break-all;
    }
  }

  &__more {
    position: absolute;
    right: 0;
    top: 0;
  }
}
</style>
