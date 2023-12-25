<template>
  <div class="edit-pwd">
    <div class="head">
      <span :class="'circle-password icon' | iconPre" />
      <span class="text">账号密码</span>
    </div>
    <el-form
      v-loading="loading"
      ref="form"
      :model="form"
      :rules="rules"
      label-position="left"
      label-width="120px">
      <el-form-item label="原密码" prop="originalPassword">
        <el-input
          v-model.trim="form.originalPassword"
          :maxlength="20"
          type="password" />
      </el-form-item>
      <el-form-item label="新密码" prop="password">
        <el-input
          v-model.trim="form.password"
          :maxlength="20"
          type="password" />
      </el-form-item>
      <el-form-item label="确认密码" prop="confirmPassword">
        <el-input
          v-model.trim="form.confirmPassword"
          :maxlength="20"
          type="password" />
      </el-form-item>
      <el-form-item>
        <el-button
          type="primary"
          @click="handleSave">保存</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import { employeePasswordModifyAPI } from '@/api/user/personCenter'
import { removeAuth } from '@/utils/auth'

export default {
  name: 'EditPwd',
  data() {
    const pwdReg = /^(?=.*[a-zA-Z])(?=.*\d).{6,20}$/
    return {
      loading: false,
      form: {},
      rules: {
        originalPassword: [
          { required: true, message: '请输入原密码', trigger: 'blur' },
          { pattern: pwdReg, message: '密码必须由6-20位字母、数字组成', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入新密码', trigger: 'blur' },
          { pattern: pwdReg, message: '密码必须由6-20位字母、数字组成', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, message: '请再次输入密码', trigger: 'blur' },
          { validator: this.validatedConfirmPwd, trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    ...mapGetters([
      'userInfo'
    ])
  },
  methods: {
    validatedConfirmPwd(rule, value, callback) {
      if (value === '') {
        callback(new Error('请再次输入密码'))
      } else if (value !== this.form.password) {
        callback(new Error('两次输入密码不一致!'))
      } else {
        callback()
      }
    },
    handleSave() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.loading = true
          const params = {
            code: this.userInfo.code,
            originalPassword: this.form.originalPassword,
            password: this.form.password
          }
          employeePasswordModifyAPI(params).then(() => {
            this.loading = false
            removeAuth().then(() => {
              this.$confirm('修改成功, 请重新登录', '提示', {
                confirmButtonText: '确定',
                showCancelButton: false,
                type: 'warning'
              }).then(() => {
                this.$store.dispatch('LogOut')
                  .then(() => {
                    location.reload()
                  })
                  .catch(() => {
                    location.reload()
                  })
              }).catch(() => {
                this.$store.dispatch('LogOut')
                  .then(() => {
                    location.reload()
                  })
                  .catch(() => {
                    location.reload()
                  })
              })
            })
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
  .edit-pwd {
    width: 100%;
    background-color: white;
    padding: 22px 25px;
  }
</style>
