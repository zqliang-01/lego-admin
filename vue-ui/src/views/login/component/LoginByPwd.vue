<template>
  <div class="login-by-pwd">
    <el-form class="login-from">
      <el-form-item>
        <el-input
          ref="username"
          v-model.trim="form.username"
          :class="{ error: !validateRes.username }"
          placeholder="请输入用户名"
          autofocus
          type="text"
          @focus="focusKey = 'username'"
          @blur="checkFromItem('username', form.username)"
          @keyup.enter="debouncedHandleLogin"
        />
      </el-form-item>
      <el-form-item>
        <el-input
          ref="password"
          v-model.trim="form.password"
          :maxlength="20"
          :class="{ error: !validateRes.password }"
          placeholder="请输入密码"
          type="password"
          @focus="focusKey = 'password'"
          @keyup.enter="debouncedHandleLogin"
          @blur="checkForm"
        />
      </el-form-item>
      <el-form-item prop="code">
        <el-input
          ref="code"
          v-model="form.code"
          :class="{ error: !validateRes.code }"
          auto-complete="off"
          placeholder="请输入验证码"
          style="width: 63%"
          @focus="focusKey = 'code'"
          @keyup.enter="debouncedHandleLogin"
          @blur="checkForm"
        />
        <div class="login-code">
          <img :src="codeUrl" class="login-code-img" @click="getCode">
        </div>
      </el-form-item>
    </el-form>

    <div class="cell login-action">
      <div class="cell-box">
        <el-checkbox v-model="rememberMe"> 记住账号 </el-checkbox>
      </div>
      <div class="empty">&nbsp;</div>
    </div>

    <div :class="{ ok: !Boolean(errorInfo) }" class="error-info">
      <div v-if="errorInfo" class="box">
        <img src="@/assets/login/error.png" alt="" class="icon" >
        <span>{{ errorInfo }}</span>
      </div>
    </div>

    <div class="control">
      <div class="btn" @click="debouncedHandleLogin">登&nbsp;录</div>
    </div>
  </div>
</template>

<script setup>
import { getCodeImg, systemInitAPI } from '@/api/login'
import { ElLoading, ElMessage } from 'element-plus'
import { debounce } from 'throttle-debounce'
import { nextTick, onMounted, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useStore } from 'vuex'
import { checkFromItem, clearError, setError } from './Mixins'

const router = useRouter()
const route = useRoute()
const store = useStore()

const pwdReg = /^(?=.*[a-zA-Z])(?=.*\d).{6,20}$/

const username = ref(null)
const password = ref(null)
const code = ref(null)
const redirect = ref(undefined)
const rememberMe = ref(false)
const codeUrl = ref('')
const captchaCode = ref('')
const focusKey = ref('')
const errorInfo = ref(null)

const form = ref({
  username: 'test',
  password: 'test123',
  code: '',
  token: ''
})

const validateRes = ref({
  username: true,
  password: true,
  code: true
})

const rules = {
  username: [{ required: true, msg: '用户名不能为空' }],
  password: [{ required: true, msg: '密码不能为空' }, { reg: pwdReg, msg: '密码由6-20位字母、数字组成' }],
  code: [{ required: true, msg: '验证码不能为空' }]
}

// 监听路由变化
watch(() => route.query, (query) => {
  redirect.value = query.redirect
}, { immediate: true })

// 初始化
onMounted(() => {
  nextTick(() => {
    username.value.focus()
  })
  getCode()
  const account = localStorage.getItem('account')
  if (account) {
    form.value = {
      username: account,
      password: ''
    }
  } else if (route.query.username) {
    form.value = {
      username: route.query.username,
      password: route.query.password || ''
    }
  }
})

// 防抖登录方法
const debouncedHandleLogin = debounce(300, handleLogin)

// 获取验证码
const getCode = async() => {
  try {
    const res = await getCodeImg()
    codeUrl.value = 'data:image/gif;base64,' + res.data.image
    captchaCode.value = res.data.code
    form.value.token = res.data.token
  } catch (error) {
    console.error('获取验证码失败:', error)
  }
}

// 登录方法
const handleLogin = async() => {
  const flag = checkForm()
  if (!flag) return
  if (rememberMe.value) {
    localStorage.setItem('account', form.value.username)
  } else {
    localStorage.removeItem('account')
  }

  const loading = ElLoading.service({
    target: document.querySelector('.login-main-content')
  })

  try {
    const res = await store.dispatch('Login', form.value)
    if (res.data.needInit) {
      loading.close()
      try {
        await ElMessageBox.confirm(
          '检测到系统尚未初始化，是否执行初始化操作，注意！初始化操作将重置系统数据，请确认是否继续！',
          '提示'
        )
        handleInit()
      } catch {
        // 用户取消操作
      }
      return
    }
    router.push({ path: redirect.value || '/' })
  } catch (error) {
    loading.close()
  }
}

// 系统初始化
const handleInit = async() => {
  const loading = ElLoading.service({
    text: '系统初始化中，请稍后。。。'
  })
  try {
    const res = await systemInitAPI()
    loading.close()
    ElMessage.alert('系统初始化成功，当前系统版本' + res.data + '，请重新登陆！', '提示')
  } catch (error) {
    loading.close()
  }
}

// 校验表单
const checkForm = () => {
  clearError()
  const arr = ['username', 'password', 'code']
  for (let i = 0; i < arr.length; i++) {
    const res = checkFromItem(arr[i], form.value[arr[i]] || null, rules)
    if (!res) return false
  }
  if (form.value.code.toLowerCase() !== captchaCode.value.toLowerCase()) {
    setError('code', '验证码不一致，请重新输入！')
    return false
  }
  return true
}
</script>

<style scoped lang="scss">
@import "../index";

.login-by-pwd {
  .forget-pwd {
    height: 50px;
    color: #999;
    line-height: 50px;
    cursor: pointer;
    padding: 0 5px;
    display: block;
    &:hover {
      color: #3e6bea;
    }
  }
}

.login-code {
  width: 33%;
  height: 50px;
  float: right;
  img {
    cursor: pointer;
    vertical-align: middle;
  }
}
.login-action {
  .login-by-code {
    font-size: 14px;
    color: #3e6bea;
    cursor: pointer;
    &:hover {
      text-decoration: underline;
    }
  }

  :deep(.el-checkbox) {
    .el-checkbox__inner {
      width: 14px;
      height: 14px;
      &::after {
        top: 2px;
        left: 5px;
      }
    }
    .el-checkbox__label {
      font-size: 14px;
    }
  }
}

.control {
  .others {
    font-size: 14px;
    .el-dropdown {
      font-size: 14px;
    }
    .register {
      cursor: pointer;
      &:hover {
        text-decoration: underline;
      }
    }
  }
}

.center-tips {
  font-size: 12px;
  color: #999;
  margin-top: 10px;
  .el-icon-warning {
    color: #f9a74e;
    font-size: 14px;
  }
}
</style>
