<template>
  <div class="section">
    <div class="section">
      <div class="section-title">系统功能更新设置</div>
      <div class="section-content" v-loading="versionLoading">
        <div class="name">
          系统版本
          <el-tooltip effect="dark" placement="top" content="显示系统版本信息，检查更新会自动检测是否有需要执行的SQL脚本">
            <i :class="'help lego-help-tips' | iconPre"/>
          </el-tooltip>
        </div>
        <el-input v-model="version" disabled>
          <el-button
            plain
            slot="append"
            type="primary"
            icon="el-icon-refresh"
            @click="handleCheckUpdate">检查更新</el-button>
        </el-input>
      </div>
      <div class="section-content" v-loading="packageLoading">
        <div class="name">
          应用模块导入
          <el-tooltip effect="dark" placement="top" content="选择导入的模块源码文件包进行模块导入">
            <i :class="'help lego-help-tips' | iconPre"/>
          </el-tooltip>
        </div>
        <div class="path">
          项目路径
          <span class="value">({{ rootPath }})</span>
        </div>
        <el-input v-model="packageFile.name" disabled>
          <el-button
            plain
            slot="append"
            type="primary"
            @click="handleSelectFile">选择源码包</el-button>
        </el-input>
        <input
          ref="packageFileInput"
          accept="*/*.zip"
          type="file"
          class="file-input"
          @change="handleFileUpload">
        <el-button
          type="primary"
          class="upload-button"
          icon="el-icon-upload"
          @click="handleImportPackage">导入</el-button>
      </div>
    </div>
  </div>
</template>

<script>
import {
  systemCheckUpdateAPI,
  systemUpdateAPI,
  appPackageUploadAPI
} from '@/api/admin/config'

export default {
  name: 'UpdateInfo',
  props: {
    systemInfo: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      versionLoading: false,
      version: '',
      rootPath: '',
      packageFile: { name: '' },
      packageLoading: false
    }
  },
  watch: {
    systemInfo: {
      handler(val) {
        this.version = val.version
        this.rootPath = val.rootPath
      },
      deep: true,
      immediate: true
    }
  },
  methods: {
    handleCheckUpdate() {
      this.versionLoading = true
      systemCheckUpdateAPI().then(res => {
        this.versionLoading = false
        if (res.data.needUpdate) {
          this.$confirm('发现新版本' + res.data.newVersion + '，是否执行更新！', '提示').then(() => {
            this.handleUpdate(res.data.newVersion)
          }).catch(() => {})
          return
        }
        this.version = res.data.version
        this.$message.success('当前已经是最新版本！')
      }).catch(() => {
        this.versionLoading = false
      })
    },
    handleUpdate(newVersion) {
      this.versionLoading = true
      systemUpdateAPI().then(res => {
        this.getDetail()
        this.versionLoading = false
        this.$message.success('更新成功，已更新到版本' + newVersion)
        return
      }).catch(() => {
        this.versionLoading = false
      })
    },
    handleSelectFile() {
      this.$refs.packageFileInput.value = null
      this.$refs.packageFileInput.click()
    },
    handleFileUpload(event) {
      const file = event.target.files[0]
      if (file) {
        this.packageFile = file
        this.$refs.packageFileInput.value = ''
      }
    },
    handleImportPackage() {
      if (!this.rootPath) {
        this.$message.error('未配置项目路径，请先配置项目路径！')
        return
      }
      if (!this.packageFile.name) {
        this.$message.error('请选择应用模块源码包文件！')
        return
      }
      this.packageLoading = true
      appPackageUploadAPI({ file: this.packageFile }).then(res => {
        this.packageLoading = false
        this.$message.success('导入成功！')
      }).catch(() => {
        this.packageLoading = false
      })
    }
  }
}
</script>

<style lang="scss" scoped>
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
  .path {
    font-size: 14px;
    margin-bottom: 10px;
    .value {
      color: $xr-color-text-placeholder;
    }
  }

  .el-input {
    width: 300px;
  }
}

.upload-button {
  margin-left: 10px;
}

.file-input {
  display: none;
}
</style>

