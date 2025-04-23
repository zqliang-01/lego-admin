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
    <EditImage
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

<script setup>
import { systemInfoModifyAPI } from '@/api/admin/config'
import { filePreviewUrl, fileUploadAPI } from '@/api/common'
import EditImage from '@/components/EditImage'
import { ElMessage } from 'element-plus'
import { computed, ref, watch } from 'vue'
import { useStore } from 'vuex'


const props = defineProps({
  systemInfo: {
    type: Object,
    required: true
  }
})

const store = useStore()
const loading = ref(false)
const showEditImage = ref(false)
const editImage = ref(null)
const editFile = ref(null)
const companyName = ref('')
const companyLogo = ref('')

const systemSaveAuth = computed(() => {
  const manage = store.getters.manage
  return manage && manage.system && manage.system.update
})

const logoUrl = computed(() => filePreviewUrl + companyLogo.value)

watch(() => props.systemInfo, (val) => {
  companyName.value = val.companyName || ''
  companyLogo.value = val.companyLogo || ''
}, { deep: true, immediate: true })

const fileUpload = (content) => {
  const reader = new FileReader()
  reader.onload = function(e) {
    let result
    if (typeof e.target.result === 'object') {
      result = window.URL.createObjectURL(new Blob([e.target.result]))
    } else {
      result = e.target.result
    }
    editImage.value = result
    editFile.value = content.file
    showEditImage.value = true
  }
  reader.readAsDataURL(content.file)
}

const deleteCompanyLogo = () => {
  companyLogo.value = ''
}

const submiteImage = (data) => {
  loading.value = true
  fileUploadAPI({
    file: new File([data.blob], data.file.name),
    entityCode: 'manage',
    permissionCode: 'manage'
  }).then(res => {
    companyLogo.value = res.data || ''
    loading.value = false
  }).catch(() => {
    loading.value = false
  })
}

const save = () => {
  if (!companyName.value) {
    ElMessage.error('系统名称不能为空')
  } else {
    loading.value = true
    systemInfoModifyAPI({
      name: companyName.value,
      logo: companyLogo.value
    }).then(res => {
      loading.value = false
      ElMessage.success('操作成功')
      emit('update')
    }).catch(() => {
      loading.value = false
    })
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
.upload :deep(.el-upload-dragger) {
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
