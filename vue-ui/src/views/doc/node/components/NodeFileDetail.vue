<template>
  <div class="page-container" v-loading="loading">
    <node-detail-head
      :node-detail="nodeFile"
      @onClickEdit="handleEdit"
      @onDeleteSuccess="handleDelete"
      @onDownload="handleDownload"
    />
    <div class="content">
      <div
        v-if="nodeFile.file.type.code === 'image'"
        class="file-image">
        <img v-src="imageUrl"/>
      </div>
      <div
        v-else
        class="file-icon">
        <img v-src="fileIcon"/>
        <p>{{ nodeFile.name + fileSize }}</p>
        <p>暂未支持该类型文件预览，敬请期待！</p>
      </div>
    </div>
  </div>
</template>
<script>
import {
  nodeGetAPI,
  nodeModifyAPI
} from '@/api/doc/node'
import {
  filePreviewUrl,
  fileDownloadAPI
} from '@/api/doc/file'
import {
  fileSize,
  getFileIconWithSuffix,
  downloadFileWithBuffer
} from '@/utils'
import NodeDetailHead from './NodeDetailHead'

export default {
  name: 'DocNodeFile',
  components: {
    NodeDetailHead
  },
  props: {
    node: {
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
  computed: {
    imageUrl() {
      if (this.nodeFile.file.type.code == 'image') {
        return filePreviewUrl + this.nodeFile.file.code
      }
      return ''
    },
    fileIcon() {
      var ext = ''
      const temps = this.nodeFile.name ? this.nodeFile.name.split('.') : []
      if (temps.length > 0) {
        ext = temps[temps.length - 1]
      }
      return getFileIconWithSuffix(ext)
    },
    fileSize() {
      return '（' + fileSize(this.nodeFile.file.size) + '）'
    }
  },
  data() {
    return {
      loading: false,
      nodeFile: {
        name: '',
        createTime: '',
        creator: {},
        file: {
          type: {
            code: ''
          }
        }
      }
    }
  },
  watch: {
    node: {
      handler(val) {
        this.init()
      },
      deep: true,
      immediate: true
    }
  },
  methods: {
    init() {
      this.loading = true
      nodeGetAPI(this.node.code).then(res => {
        this.nodeFile = res.data
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    handleEdit() {
      this.$prompt('文件名称', '文件重命名', {
        inputValue: this.nodeFile.name,
        inputErrorMessage: '请填写文件名称！',
        inputValidator: value => {
          if (!value) {
            return '请填写文件名称！'
          }
        }
      }).then(({ value }) => {
        this.loading = true
        nodeModifyAPI({
          code: this.nodeFile.code,
          name: value
        }).then(res => {
          this.loading = false
          this.$message.success('重命名成功！')
          this.$emit('onRename', value)
        }).catch(() => {
          this.loading = false
        })
      })
    },
    handleDelete() {
      this.$emit('onDelete')
    },
    handleDownload() {
      this.loading = true
      fileDownloadAPI(this.nodeFile.file.code).then(res => {
        downloadFileWithBuffer(res.data, this.nodeFile.name)
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    }
  }
}
</script>
<style scoped lang="scss">
.page-container {
  height: 100%;
  width: 100%;
  .content {
    width: 100%;
    height: calc(100% - 30px);
    overflow: auto;
    .file-image {
      padding-top: 10px;
      img {
        max-width: 100%;
      }
    }
    .file-icon {
      padding-top: 150px;
      text-align: center;
      img {
        display: block;
        width: 100px;
        margin: 0 auto 20px auto;
      }
      p {
        margin: 3px 0;
        color: #aaa;
        font-size: 13px;
      }
    }
  }
}
</style>
