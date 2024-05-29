<template>
  <flexbox class="upload-cell">
    <img
      :src="fileIcon"
      class="upload-cell__header">
    <div class="upload-cell__body">
      <div class="text-one-line">{{ file.name }}<span style="color: #999;">{{ fileSize }}</span></div>
      <el-progress
        :stroke-width="3"
        :show-text="false"
        :percentage="complete ? 100 : percentage"/>
    </div>
    <div class="upload-cell__footer">
      <el-button
        :icon="handleIcon" @click="cancelRequest"/>
    </div>
  </flexbox>
</template>

<script>
import { fileUploadAPI } from '@/api/common'
import { getFileTypeIcon, fileSize } from '@/utils'

import axios from 'axios'

export default {
  name: 'UploadCell',
  components: {},
  props: {
    file: {
      type: File,
      required: true
    },
    index: Number,
    request: Function,
    complete: {
      type: Boolean,
      default: false
    },
    params: Object
  },
  data() {
    return {
      percentage: 0,
      cancel: null
    }
  },
  computed: {
    handleIcon() {
      return this.complete ? 'el-icon-check' : 'el-icon-close'
    },

    fileIcon() {
      return getFileTypeIcon(this.file)
    },

    fileSize() {
      return `（${fileSize(this.file.size)}）`
    }
  },
  watch: {
    file: {
      handler() {
        if (this.file && !this.complete && this.percentage == 0) {
          this.uploadFile()
        }
      },
      immediate: true
    }
  },
  mounted() {},

  beforeDestroy() {},
  methods: {
    uploadFile() {
      const request = this.request ? this.request : fileUploadAPI
      const params = this.params ? this.params : {}
      params.file = this.file

      const CancelToken = axios.CancelToken

      const self = this
      const config = {
        onUploadProgress: progressEvent => {
          const percentage = (progressEvent.loaded / progressEvent.total * 100 | 0)
          this.percentage = percentage
        },
        cancelToken: new CancelToken(function executor(c) {
          self.cancel = c
          self.$emit('cancelTokenChange', c, self.index)
        })
      }
      request(params, config)
        .then(res => {
          this.$emit('completed', this.index, res)
        })
        .catch(res => {
          this.$message.error('文件上传失败！' + res)
        })
    },

    cancelRequest() {
      if (!this.complete) {
        this.cancel()
        this.$emit('delete', this.index)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.upload-cell {
  border-radius: 4px;
  background-color: #F0F0F0;
  padding: 8px 15px;
  font-size: 12px;

  &__header {
    width: 17px;
    height: 20px;
    margin-right: 15px;
    flex-shrink: 0;
  }

  &__body {
    flex: 1;
    width: 0;
    .el-progress {
      margin-top: 8px;
    }
  }

  &__footer {
    flex-shrink: 0;
    margin-left: 10px;

    .el-button {
      border: none;
      outline: none;
      background: transparent;
      font-weight: bold;
      padding: 0;
      ::v-deep i {
        font-size: 15px;
        font-weight: bold;
      }
    }
  }
}
</style>
