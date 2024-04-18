<template>
  <flexbox class="file-cell">
    <img
      :src="fileIcon"
      class="file-cell__header"
      alt>
    <div
      :class="{'cursor-pointer' :cursorPointer}"
      class="file-cell__body text-one-line">
      {{ fileName.length > 20 ? fileName.substring(0, 20) + '...' : fileName }}
    </div>
    <div class="file-cell__size">
      ({{ data.size | getFileSize }})
    </div>
    <div class="file-cell__footer">
      <template>
        <span
          v-if="previewShow"
          class="xr-text-btn primary"
          @click="previewClick">预览</span>
        <span
          class="xr-text-btn primary"
          @click="downloadClick">下载</span>
        <span
          v-if="deleteShow"
          class="xr-text-btn delete"
          @click="deleteClick">删除</span>
      </template>
    </div>
  </flexbox>
</template>

<script type="text/javascript">
import { fileUrlDownloadAPI } from '@/api/common'

import { downloadFileWithBuffer, getFileIconWithSuffix, fileSize, canPreviewFile, previewFile } from '@/utils'

export default {
  name: 'FileCell',
  filters: {
    getFileSize(size) {
      return fileSize(size)
    }
  },
  props: {
    index: Number,
    data: Object,
    // 完整数据
    list: Array,
    showFoot: {
      type: Boolean,
      default: true
    },
    cursorPointer: {
      type: Boolean,
      default: false
    },
    previewShow: {
      type: Boolean,
      default: true
    },
    deleteShow: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {}
  }, // 附件展示效果
  computed: {
    fileName() {
      if (this.data) {
        return this.data.name || this.data.fileName
      }
      return ''
    },

    fileIcon() {
      const temps = this.fileName ? this.fileName.split('.') : []
      var ext = ''
      if (temps.length > 0) {
        ext = temps[temps.length - 1]
      } else {
        ext = ''
      }
      return getFileIconWithSuffix(ext)
    }
  },
  watch: {},
  mounted() {},

  beforeDestroy() {},
  methods: {
    /**
     * 下载
     */
    downloadClick() {
      fileUrlDownloadAPI(this.data.url).then(res => {
        const blob = new Blob([res.data], {
          type: ''
        })
        downloadFileWithBuffer(blob, this.data.name)
      }).catch(() => {})
    },

    /**
     * 附件预览
     */
    previewClick() {
      if (canPreviewFile(this.fileName)) {
        previewFile(this.data.url, this.fileName)
      } else {
        this.$previewFile.preview({
          index: this.index || 0,
          data: this.list.map(function(item) {
            return {
              url: item.url,
              name: item.name
            }
          })
        })
      }
    },

    /**
     * 删除
     */
    deleteClick() {
      this.$confirm('确定删除?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          this.$emit('delete', this.data, this.index)
        })
        .catch(() => {
          this.$message({
            type: 'info',
            message: '已取消操作'
          })
        })
    }
  }
}
</script>
<style lang="scss" scoped>
.file-cell {
  height: 38px;
  padding: 8px;
  background-color: #f8faff;
  border-radius: $xr-border-radius-base;
  position: relative;
  margin-bottom: 5px;
  line-height: 20px;

  &__header {
    display: block;
    width: 16px;
  }

  &__body {
    margin-left: 12px;
    color: $xr-color-primary;
    font-size: 14px;
  }

  &__size {
    margin-left: 8px;
    flex-shrink: 0;
    font-size: 12px;
    color: #ccc;
  }

  &__footer {
    display: none;
    margin-left: 15px;
    flex-shrink: 0;
    margin-right: 8px;
    cursor: pointer;
    i {
      color: #ccc;
      padding: 0 2px;
    }
  }

  &:hover {
    .file-cell__footer {
      display: block;
    }
  }
}

.cursor-pointer {
  cursor: pointer;
}
</style>
