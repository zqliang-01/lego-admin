<template>
  <flexbox class="main-top" v-loading="loading">
    <flexbox-item class="title">
      <span>{{ nodeDetail.name }}</span><span>{{ nodeDetail.creator.name }}于{{ nodeDetail.createTime }}创建</span>
    </flexbox-item>
    <div class="control">
      <el-button
        v-if="nodeDetail.editable"
        icon="lego lego-edit"
        type="text"
        class="control-btn"
        @click="handleEdit">
        编辑
      </el-button>
      <el-button
        :icon="!collectCode ? 'lego lego-focus-on' : 'lego lego-focus-on collected'"
        type="text"
        class="control-btn"
        @click="handleCollection">
        {{ !collectCode ? '收藏' :'取消收藏' }}
      </el-button>
      <el-button
        v-if="nodeDetail.type === 'page'"
        icon="lego lego-export"
        type="text"
        class="control-btn"
        @click="handleCommandClick('export')">
        导出
      </el-button>
      <el-button
        v-if="nodeDetail.type === 'file'"
        icon="lego lego-download"
        type="text"
        class="control-btn"
        @click="handleCommandClick('download')">
        下载
      </el-button>
      <el-dropdown
        v-if="nodeDetail.editable"
        trigger="click"
        @command="handleCommandClick">
        <el-button
          class="dropdown-btn"
          icon="el-icon-more" />
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item command="delete">删除</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
  </flexbox>
</template>
<script>
import {
  collectAddAPI,
  collectDeleteAPI,
  collectCodeGetAPI
} from '@/api/doc/collect'
import { nodeDisableAPI } from '@/api/doc/node'

export default {
  name: 'DocNodeHead',
  props: {
    nodeDetail: {
      type: Object,
      required: true,
      default: function() {
        return {
          name: '',
          createTime: '',
          creator: {}
        }
      }
    }
  },
  data() {
    return {
      loading: false,
      collectCode: ''
    }
  },
  watch: {
    nodeDetail: {
      handler(val) {
        this.init()
      },
      deep: true,
      immediate: true
    }
  },
  methods: {
    init() {
      collectCodeGetAPI({
        nodeCode: this.nodeDetail.code
      }).then(res => {
        this.collectCode = res.data
      })
    },
    handleEdit() {
      this.$emit('onClickEdit')
    },
    handleCollection() {
      this.loading = true
      if (this.collectCode) {
        collectDeleteAPI(this.collectCode).then(() => {
          this.loading = false
          this.collectCode = ''
          this.$message.success('取消收藏成功！')
        }).catch(() => {
          this.loading = false
        })
        return
      }
      collectAddAPI({
        nodeCode: this.nodeDetail.code
      }).then(res => {
        this.loading = false
        this.collectCode = res.data
        this.$message.success('收藏成功！')
      }).catch(() => {
        this.loading = false
      })
    },
    handleCommandClick(command) {
      if (command === 'delete') {
        this.$confirm(`请确认删除文章【${this.nodeDetail.name}】，是否继续?`, '提示', {
          type: 'warning'
        }).then(() => {
          nodeDisableAPI(this.nodeDetail.code).then(() => {
            this.$message.success('删除成功！')
            this.$emit('onDeleteSuccess')
          })
        })
      }
      if (command === 'export') {
        this.$emit('onExport')
      }
      if (command === 'download') {
        this.$emit('onDownload')
      }
    }
  }
}
</script>
<style scoped lang="scss">
.main-top {
  .title {
    color: $xr-color-text-secondary;
    span:first-child {
      margin-right: 4px;
    }
  }
  .control {
    .dropdown-btn {
      padding: 8px;
    }
    .el-dropdown {
      margin-left: 8px;
    }
    .control-btn {
      color: $xr-color-text-regular;
    }
    .control-btn.active {
      color: $xr-color-primary;
      ::v-deep i {
        color: $xr-color-primary;
      }
    }
    ::v-deep .lego-focus-on.collected {
      color: #fac23d;
    }
  }
}
</style>
