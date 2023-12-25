<template>
  <div
    v-loading="loading"
    class="edit-user-info">
    <div class="head">
      <span :class="'user icon' | iconPre" />
      <span class="text">个人信息</span>
    </div>
    <el-form
      ref="form"
      :model="form"
      :rules="rules"
      label-position="left"
      label-width="120px">
      <el-form-item label="头像">
        <flexbox class="user-box">
          <xr-avatar
            :name="userInfo.name"
            :size="70"
            :src="userInfo.img"
            class="user-img" />
          <div class="change-avatar" @click="handleChangeAvatar">
            更换头像
          </div>
        </flexbox>
      </el-form-item>
      <el-form-item
        v-for="(item, index) in fieldList"
        :key="index"
        :prop="item.field"
        :label="item.label">
        <el-select
          v-if="item.type == 'select'"
          v-model="form[item.field]"
          :disabled="item.disabled">
          <el-option
            v-for="option in item.setting"
            :key="option.value"
            :label="option.label"
            :value="option.value" />
        </el-select>
        <el-input
          v-else
          v-model="form[item.field]"
          :maxlength="30"
          :disabled="item.disabled" />
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
  adminUsersUpdateImgAPI,
  employeeModifyAPI
} from '@/api/user/personCenter'
import { mapGetters } from 'vuex'
import EditImage from '@/components/EditImage'

export default {
  name: 'EditUserInfo',
  components: {
    EditImage
  },
  data() {
    return {
      fieldList: [
        { label: '工号', field: 'code', disabled: true },
        { label: '姓名', field: 'name' },
        { label: '部门', field: 'dept', disabled: true },
        { label: '开户时间', field: 'createTime', disabled: true }
      ],
      rules: {
        name: [{ required: true, message: '请填写姓名', trigger: 'blur' }]
      },
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
      this.fieldList.map(field => {
        let value = this.userInfo[field.field]
        if (value.hasOwnProperty('name')) {
          value = value.name
        }
        this.form[field.field] = value
      })
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
      }
      reader.readAsDataURL(file)
    },
    /**
     * 上传提交头像修改
     * @param data
     */
    submitImage(data) {
      this.loading = true
      const param = new FormData()
      param.append('code', this.form.code)
      param.append('file', data.blob, data.file.name)
      adminUsersUpdateImgAPI(param).then(() => {
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
      this.form.enable = true
      this.$refs.form.validate(valid => {
        if (valid) {
          this.loading = true
          employeeModifyAPI(this.form).then(() => {
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
