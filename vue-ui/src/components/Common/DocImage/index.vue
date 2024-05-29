<template>
  <div v-loading="loading">
    <el-upload
      v-if="!imageCode"
      :show-file-list="false"
      :http-request="fileUpload"
      drag
      class="upload"
      action="http"
      accept="image/png, image/jpeg, image/gif, image/jpg">
      <i class="el-icon-plus uploader-icon"/>
    </el-upload>
    <div
      v-else
      class="upload-show">
      <img v-src="logoUrl">
      <i
        class="el-icon-remove icon-delete"
        @click="deleteImage"/>
    </div>
    <edit-image
      :fixed-number="[20, 10]"
      :show="showEditImage"
      :image="editImage"
      :file="editFile"
      title="编辑图片"
      preview-width="180px"
      preview-height="90px"
      preview-radius="3px"
      width="550px"
      save-button-title="确定"
      @save="submiteImage"
      @close="showEditImage=false"/>
  </div>
</template>

<script>
import { fileUploadAPI, filePreviewUrl } from '@/api/doc/file'
import EditImage from '@/components/EditImage'
import { isEmpty } from '@/utils/types'

export default {
  name: 'SystemConfig',
  components: {
    EditImage
  },
  props: {
    value: [String, Number],
    disabled: Boolean
  },
  data() {
    return {
      loading: false,
      showEditImage: false,
      editImage: null,
      editFile: null,
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
    logoUrl() {
      return filePreviewUrl + this.imageCode
    }
  },
  created() {
  },
  methods: {
    fileUpload(content) {
      const reader = new FileReader()
      var self = this
      reader.onload = function(e) {
        let result
        if (typeof e.target.result === 'object') {
          result = window.URL.createObjectURL(new Blob([e.target.result]))
        } else {
          result = e.target.result
        }
        self.editImage = result
        self.editFile = content.file
        self.showEditImage = true
      }
      reader.readAsDataURL(content.file)
    },
    deleteImage() {
      this.imageCode = ''
      this.$emit('input', '')
      this.$emit('change', '')
    },
    submiteImage(data) {
      this.loading = true
      fileUploadAPI({
        file: new File([data.blob], data.file.name)
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
  width: 200px;
  height: 100px;
  line-height: 100px;
  text-align: center;
}
::v-deep .upload .el-upload-dragger {
  width: 200px;
  height: 100px;
}
.upload-show {
  position: relative;
  display: block;
  width: 200px;
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
}
.upload-show:hover {
  .icon-delete {
    display: block;
  }
}
</style>

