<template>
  <div v-loading="loading">
    <el-upload
      v-if="!imageCode"
      :show-file-list="false"
      :http-request="handleSubmit"
      :disabled="disabled"
      drag
      class="upload"
      action="http"
      accept="image/png, image/jpeg, image/gif, image/jpg">
      <i class="el-icon-plus uploader-icon"/>
    </el-upload>
    <div
      v-else
      class="upload-show">
      <img v-src="imageUrl">
      <i
        v-if="!disabled"
        class="el-icon-zoom-in icon-view"
        @click="handleView"/>
      <i
        v-if="!disabled"
        class="el-icon-remove icon-delete"
        @click="handleDelete"/>
    </div>
  </div>
</template>

<script>
import { isEmpty } from '@/utils/types'
import {
  fileUploadAPI,
  filePreviewUrl
} from '@/api/common'

export default {
  name: 'ImageUpload',
  components: {
  },
  props: {
    value: [String, Number],
    disabled: Boolean
  },
  data() {
    return {
      loading: false,
      showPreview: false,
      imageCode: ''
    }
  },
  watch: {
    value: {
      handler(newVal, oldVal) {
        if (this.value !== this.imageCode) {
          if (isEmpty(this.value)) {
            this.imageCode = ''
          } else {
            this.imageCode = this.value
          }
        }
      },
      immediate: true
    }
  },
  computed: {
    imageUrl() {
      return filePreviewUrl + this.imageCode
    }
  },
  created() {
  },
  methods: {
    handleDelete() {
      this.imageCode = ''
      this.$emit('input', '')
      this.$emit('change', '')
    },
    handleView() {
      if (this.imageCode) {
        this.$previewImage.preview({
          data: [this.imageCode]
        })
      }
    },
    handleSubmit(data) {
      this.loading = true
      fileUploadAPI({
        file: data.file,
        permissionCode: 'manage',
        entityCode: 'picture'
      }).then(res => {
        this.imageCode = res.data || ''
        this.$emit('input', this.imageCode)
        this.$emit('change', this.imageCode)
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 100px;
  height: 100px;
  line-height: 100px;
  text-align: center;
}
::v-deep .upload .el-upload-dragger {
  width: 100px;
  height: 100px;
}
.upload-show {
  position: relative;
  display: block;
  width: 100px;
  height: 100px;
  img {
    width: 100%;
    height: 100%;
    border-radius: 3px;
  }

  .icon-delete {
    position: absolute;
    top: -10px;
    right: -8px;
    color: red;
    font-size: 20px;
    display: none;
  }
  .icon-view {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    color: #fff;
    font-size: 20px;
    display: none;
  }
}
.upload-show:hover {
  .icon-delete {
    display: block;
  }
  .icon-view {
    display: block;
    cursor: pointer;
  }
}
</style>

