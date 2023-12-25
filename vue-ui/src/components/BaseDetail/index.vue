<template>
  <div class="base-detail">
    <create-sections
      v-for="(mainItem, mainIndex) in list"
      :key="mainIndex"
      :title="mainItem.name"
      :dropdown-items="dropdownItems"
      class="base-detail__section"
      content-height="auto"
      @command-select="sectionCommandSelect">
      <base-detail-section
        :list="mainItem.list"
      />
      <div slot="header" class="base-detail__more">
        <slot name="more"/>
      </div>
      <slot />
    </create-sections>
  </div>
</template>

<script>
import CreateSections from '@/components/CreateSections'
import BaseDetailSection from './BaseDetailSection'

export default {
  // 基本详情
  name: 'BaseDetail',
  components: {
    CreateSections,
    BaseDetailSection
  },
  props: {
    list: Array,
    dropdownItems: Array
  },
  data() {
    return {

    }
  },
  computed: {},
  watch: {},
  mounted() {},

  beforeDestroy() {},
  methods: {
    sectionCommandSelect(type) {
      this.$emit('top-command-select', type)
    }
  }
}
</script>

<style lang="scss" scoped>
.base-detail {
  &__section {
    margin-top: 25px;
  }
  /deep/ .create-sections-content {
    padding: 0 20px;
  }

  &__more {
    position: absolute;
    right: 15px;
  }
}
</style>
