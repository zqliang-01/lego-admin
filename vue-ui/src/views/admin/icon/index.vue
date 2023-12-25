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
      <el-button type="primary" @click="dialogTableVisible = !dialogTableVisible">
        <span>选择图标</span>
      </el-button>
      <i :class="currentIcon | iconPre">
        <span>{{ currentIcon }}</span>
      </i>
      <icon-list @select="iconSelect" />
      <el-dialog :visible.sync="dialogTableVisible" width="60%" title="图标选择">
        <div style="max-height: 500px; overflow-y: auto;">
          <icon-list @select="iconSelect" />
        </div>
      </el-dialog>
    </div>
  </flexbox>
</template>

<script>
import XrHeader from '@/components/XrHeader'
import IconList from '@/components/IconList'

export default {
  /** 客户管理 的 线索列表 */
  name: 'IconIndex',
  components: {
    XrHeader,
    IconList
  },
  data() {
    return {
      currentIcon: '',
      dialogTableVisible: false
    }
  },
  methods: {
    iconSelect(value) {
      this.currentIcon = value
      this.dialogTableVisible = false
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
