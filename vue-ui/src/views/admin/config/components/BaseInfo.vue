<template>
  <div class="section">
    <div class="section-title">系统基本信息设置</div>
    <div class="section-content">
      <div class="name">系统名称</div>
      <el-input v-model="companyName" :maxlength="50"/>
    </div>
    <div class="section-content">
      <div class="name">系统logo</div>
      <el-upload
        v-if="!companyLogo"
        :show-file-list="false"
        :http-request="fileUpload"
        drag
        class="upload"
        action="http"
        accept="image/png, image/jpeg, image/gif, image/jpg">
        <i class="el-icon-plus uploader-icon"/>
      </el-upload>
      <div v-else class="upload-show">
        <img v-src="logoUrl">
        <i class="el-icon-remove icon-delete" @click="deleteCompanyLogo"/>
      </div>
      <el-button
        v-if="systemSaveAuth"
        class="save-button"
        type="primary"
        @click="save">保存</el-button>
    </div>
    <edit-image
      :fixed-number="[15, 4]"
      :show="showEditImage"
      :image="editImage"
      :file="editFile"
      title="编辑系统logo"
      preview-width="150px"
      preview-height="40px"
      preview-radius="0"
      width="550px"
      save-button-title="确定"
      @save="submiteImage"
      @close="showEditImage=false"/>
  </div>
</template>

<script>
import { systemInfoModifyAPI } from '@/api/admin/config'
import { fileUploadAPI, filePreviewUrl } from '@/api/common'

import EditImage from '@/components/EditImage'
import { mapGetters } from 'vuex'

export default {
  name: 'BaseInfo',
  components: {
    EditImage
  },
  props: {
    systemInfo: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      loading: false,
      showEditImage: false,
      editImage: null,
      editFile: null,
      companyName: '',
      companyLogo: ''
    }
  },
  watch: {
    systemInfo: {
      handler(val) {
        this.companyName = val.companyName || ''
        this.companyLogo = val.companyLogo || ''
      },
      deep: true,
      immediate: true
    }
  },
  computed: {
    ...mapGetters(['manage']),
    systemSaveAuth() {
      return this.manage && this.manage.system && this.manage.system.update
    },
    logoUrl() {
      return filePreviewUrl + this.companyLogo
    }
  },
  methods: {
    /**
     * 附件上传
     */
    fileUpload(content) {
      const reader = new FileReader()
      var self = this
      reader.onload = function(e) {
        let result
        if (typeof e.target.result === 'object') {
          // 把Array Buffer转化为blob 如果是base64不需要
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
    deleteCompanyLogo() {
      this.companyLogo = ''
    },
    submiteImage(data) {
      this.loading = true
      fileUploadAPI({
        file: new File([data.blob], data.file.name),
        entityCode: 'manage',
        permissionCode: 'manage'
      }).then(res => {
        this.companyLogo = res.data || ''
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    save() {
      if (!this.companyName) {
        this.$message.error('系统名称不能为空')
      } else {
        this.loading = true
        systemInfoModifyAPI({
          name: this.companyName,
          logo: this.companyLogo
        }).then(res => {
          this.loading = false
          this.$message.success('操作成功')
          this.getDetail()
        }).catch(() => {
          this.loading = false
        })
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.save-button {
  margin-top: 10px;
  margin-left: 250px;
}

.section + .section {
  margin-top: 50px;
}

.section-title {
  color: #333;
  font-weight: bold;
  font-size: 14px;
  margin-bottom: 30px;
}

.section-content {
  margin-bottom: 30px;
  .name {
    color: #333;
    font-size: 14px;
    margin-bottom: 10px;
  }

  .el-input {
    width: 300px;
  }
}

.uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 300px;
  height: 80px;
  line-height: 80px;
  text-align: center;
}
.upload ::v-deep .el-upload-dragger {
  width: 300px;
  height: 80px;
}
.upload-show {
  position: relative;
  display: block;
  width: 300px;
  height: 80px;
  img {
    width: 100%;
    height: 100%;
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

