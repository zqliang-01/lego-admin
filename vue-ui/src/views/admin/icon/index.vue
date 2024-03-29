<template>
  <flexbox
    class="main"
    direction="column"
    align="stretch">
    <xr-header
      :icon-class="'icon-edition'"
      icon-color="#2362fb"
      label="应用管理" />
    <div class="body">
      <select-icon v-model="currentIcon"/>
      <icon-list @select="iconSelect"/>
    </div>
  </flexbox>
</template>

<script>
import XrHeader from '@/components/XrHeader'
import IconList from '@/components/IconList'
import SelectIcon from '@/components/NewCom/SelectIcon'

export default {
  name: 'IconIndex',
  components: {
    XrHeader,
    IconList,
    SelectIcon
  },
  data() {
    return {
      currentIcon: ''
    }
  },
  methods: {
    iconSelect(value) {
      this.currentIcon = value
    }
  }
}
</script>

<style lang="scss" scoped>
.main {
  padding: 0 15px;
  height: 100%;
}

.body {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  background-color: white;
  border: 1px solid $xr-border-line-color;
  border-radius: $xr-border-radius-base;
}
</style>
