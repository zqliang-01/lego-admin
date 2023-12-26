<template>
  <div
    v-loading="loading"
    class="edit-user-info">
    <div class="head">
      <span :class="'user icon' | iconPre" />
      <span class="text">个人信息</span>
    </div>
    <el-form
      ref="userForm"
      :model="form"
      :rules="rules"
      label-position="left"
      label-width="120px">
      <el-form-item label="头像">
        <flexbox class="user-box">
          <xr-avatar
            :name="userInfo.name"
            :size="70"
            :src="userInfo.imageCode"
            class="user-img" />
          <div class="change-avatar" @click="handleChangeAvatar">
            更换头像
          </div>
        </flexbox>
      </el-form-item>
      <el-form-item prop="code" label="编码">
        <el-input v-model="form.code" :maxlength="30" :disabled="true" />
      </el-form-item>
      <el-form-item prop="name" label="姓名">
        <el-input v-model="form.name" :maxlength="30" />
      </el-form-item>
      <el-form-item label="部门">
        <el-input v-model="userInfo.dept.name" :maxlength="30" :disabled="true" />
      </el-form-item>
      <el-form-item label="开户时间">
        <el-input v-model="userInfo.createTime" :maxlength="30" :disabled="true" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSave">保存</el-button>
      </el-form-item>
    </el-form>

    <input
      id="inputFile"
      type="file"
      accept="image/png, image/jpeg, image/gif, image/jpg"
      style="display: none;"
      @change="uploadFile">
    <edit-image
      :show="showEditImage"
      :file="editFile"
      :image="editImage"
      @save="submitImage"
      @close="showEditImage=false"/>
  </div>
</template>

<script>
import {
  employeeCurrentModifyAPI
} from '@/api/user/personCenter'
import {
  fileUploadAPI
} from '@/api/common'
import { mapGetters } from 'vuex'
import EditImage from '@/components/EditImage'

export default {
  name: 'EditUserInfo',
  components: {
    EditImage
  },
  data() {
    return {
      rules: {
        name: [{ required: true, message: '请填写姓名', trigger: 'blur' }]
      },
      imageCode: '',
      form: {},
      loading: false,
      showEditImage: false,
      editFile: null,
      editImage: null
    }
  },
  computed: {
    ...mapGetters([
      'userInfo'
    ])
  },
  watch: {
    userInfo: {
      handler() {
        this.initData()
      },
      deep: true,
      immediate: true
    }
  },
  methods: {
    initData() {
      this.form.code = this.userInfo.code
      this.form.name = this.userInfo.name
      this.form.imageCode = this.userInfo.imageCode
    },
    handleChangeAvatar() {
      document.getElementById('inputFile').click()
    },
    /**
     * 图片操作
     * @param event
     */
    uploadFile(event) {
      const files = event.target.files
      const file = files[0]
      const reader = new FileReader()
      const self = this
      reader.onload = function(e) {
        let result
        if (typeof e.target.result === 'object') {
          // 把Array Buffer转化为blob 如果是base64不需要
          result = window.URL.createObjectURL(new Blob([e.target.result]))
        } else {
          result = e.target.result
        }
        self.editImage = result
        self.editFile = file
        self.showEditImage = true
        e.target.value = ''
        event.target.value = ''
      }
      reader.readAsDataURL(file)
    },
    /**
     * 上传提交头像修改
     * @param data
     */
    submitImage(data) {
      this.loading = true
      fileUploadAPI({
        file: new File([data.blob], data.file.name),
        entityCode: 'manage',
        permissionCode: 'manage'
      }).then(res => {
        this.imageCode = res.data || ''
        this.$set(this.userInfo, 'imageCode', res.data || '')
        this.loading = false
        this.$emit('change')
      }).catch(() => {
        this.loading = false
      })
    },
    /**
     * 个人信息编辑
     */
    handleSave() {
      if (this.imageCode) {
        this.form.imageCode = this.imageCode
      }
      this.$refs.userForm.validate(valid => {
        if (valid) {
          this.loading = true
          employeeCurrentModifyAPI(this.form).then(() => {
            this.loading = false
            this.$message.success('保存成功')
            this.$emit('change')
          }).catch(() => {
            this.loading = false
          })
        } else {
          return false
        }
      })
    }
  }
}
</script>

<style scoped lang="scss">
  @import "./style";
  .edit-user-info {
    width: 100%;
    background-color: white;
    padding: 22px 25px;
  }
</style>
