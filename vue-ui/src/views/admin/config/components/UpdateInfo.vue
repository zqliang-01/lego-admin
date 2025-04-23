<template>
  <div class="section">
    <div class="section">
      <div class="section-title">系统功能更新设置</div>
      <div class="section-content" v-loading="versionLoading">
        <div class="name">
          系统版本
          <el-tooltip effect="dark" placement="top" content="显示系统版本信息，检查更新会自动检测是否有需要执行的SQL脚本">
            <i :class="iconPre('help lego-help-tips')"/>
          </el-tooltip>
        </div>
        <el-input v-model="version" disabled>
          <template #append>
            <el-button
              plain
              type="primary"
              icon="el-icon-refresh"
              @click="handleCheckUpdate">检查更新</el-button>
          </template>
        </el-input>
      </div>
      <div v-if="rootPath" class="section-content" v-loading="packageLoading">
        <div class="name">
          应用模块导入
          <el-tooltip effect="dark" placement="top" content="选择导入的模块源码文件包进行模块导入">
            <i :class="iconPre('help lego-help-tips')"/>
          </el-tooltip>
        </div>
        <div class="path">
          项目路径
          <span class="value">({{ rootPath }})</span>
        </div>
        <el-input v-model="packageFile.name" disabled>
          <template #append>
            <el-button
              plain
              type="primary"
              @click="handleSelectFile">选择源码包</el-button>
          </template>
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

<script setup>
import { ref, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  systemCheckUpdateAPI,
  systemUpdateAPI,
  appPackageUploadAPI
} from '@/api/admin/config'

const props = defineProps({
  systemInfo: {
    type: Object,
    required: true
  }
})

const versionLoading = ref(false)
const version = ref('')
const rootPath = ref('')
const packageFile = ref({ name: '' })
const packageLoading = ref(false)
const packageFileInput = ref(null)

const iconPre = (icon) => {
  if (!icon) return ''
  if (icon.startsWith('el-icon')) {
    return icon
  }
  if (icon.startsWith('lego-')) {
    return `lego ${icon}`
  }
  return `el-icon-${icon}`
}

watch(() => props.systemInfo, (val) => {
  version.value = val.version || ''
  rootPath.value = val.rootPath || ''
}, { deep: true, immediate: true })

const handleCheckUpdate = () => {
  versionLoading.value = true
  systemCheckUpdateAPI().then(res => {
    versionLoading.value = false
    if (res.data.needUpdate) {
      ElMessageBox.confirm(
        '发现新版本' + res.data.newVersion + '，是否执行更新！',
        '提示'
      ).then(() => {
        handleUpdate(res.data.newVersion)
      }).catch(() => {})
      return
    }
    version.value = res.data.version
    ElMessage.success('当前已经是最新版本！')
  }).catch(() => {
    versionLoading.value = false
  })
}

const handleUpdate = (newVersion) => {
  versionLoading.value = true
  systemUpdateAPI().then(res => {
    versionLoading.value = false
    ElMessage.success('更新成功，已更新到版本' + newVersion)
  }).catch(() => {
    versionLoading.value = false
  })
}

const handleSelectFile = () => {
  packageFileInput.value.value = null
  packageFileInput.value.click()
}

const handleFileUpload = (event) => {
  const file = event.target.files[0]
  if (file) {
    packageFile.value = file
    packageFileInput.value.value = ''
  }
}

const handleImportPackage = () => {
  if (!rootPath.value) {
    ElMessage.error('未配置项目路径，请先配置项目路径！')
    return
  }
  if (!packageFile.value.name) {
    ElMessage.error('请选择应用模块源码包文件！')
    return
  }
  packageLoading.value = true
  appPackageUploadAPI({ file: packageFile.value }).then(res => {
    packageLoading.value = false
    ElMessage.success('导入成功！')
  }).catch(() => {
    packageLoading.value = false
  })
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
