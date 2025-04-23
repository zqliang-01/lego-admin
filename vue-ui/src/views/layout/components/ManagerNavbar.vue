<template>
  <div class="navbar">
    <img
      v-src="logoUrl"
      :key="logo"
      class="logo"
      @click="enterMainPage"
    >
    <div class="nav-title">
      管理后台
    </div>
    <div
      class="back-home"
      @click="enterHome"
    >
      返回首页
    </div>
    <div
      class="go-out"
      @click="enterLogin"
    >
      退出系统
    </div>
  </div>
</template>

<script setup>
import { filePreviewUrl } from '@/api/common'
import { ElLoading, ElMessageBox } from 'element-plus'
import { computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useStore } from 'vuex'

// Props 定义
const props = defineProps({
  navIndex: {
    type: [Number, String],
    default: 0
  }
})

// 使用 store 和 router
const store = useStore()
const router = useRouter()

// 计算属性
const logo = computed(() => store.getters.logo)
const logoUrl = computed(() => {
  if (logo.value) {
    return filePreviewUrl + logo.value
  }
  return new URL('@/assets/img/logo.png', import.meta.url).href
})

// 方法
const enterHome = () => {
  router.replace({
    path: '/'
  })
}

const enterLogin = async() => {
  try {
    await ElMessageBox.confirm('退出登录？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    const loading = ElLoading.service({
      target: document.getElementById('#app')
    })

    try {
      await store.dispatch('LogOut')
      loading.close()
      location.reload()
    } catch (error) {
      loading.close()
      location.reload()
    }
  } catch {
    // 用户取消操作
  }
}

const enterMainPage = () => {
  router.push('/')
}

// 生命周期钩子
onMounted(() => {
  store.commit('SET_NAVACTIVEINDEX', props.navIndex)
})
</script>

<style lang="scss" scoped>
.navbar {
  height: 60px;
  min-height: 60px;
  display: flex;
  align-items: center;
  padding: 0 60px 0 30px;
  background-color: white;
  .logo {
    width: 150px;
    height: 40px;
    display: block;
    flex-shrink: 0;
    margin-right: 60px;
    cursor: pointer;
  }
  .nav-title {
    flex: 1;
    font-size: 16px;
    color: #333333;
  }
}

.back-home {
  width: 94px;
  height: 36px;
  line-height: 36px;
  background-color: #2362FB;
  border-radius: 3px;
  text-align: center;
  color: #fff;
  font-size: 14px;
  margin-right: 10px;
  cursor: pointer;
}

.go-out {
  width: 94px;
  height: 36px;
  line-height: 36px;
  background-color: #c2c2c2;
  border-radius: 3px;
  text-align: center;
  color: #fff;
  font-size: 14px;
  cursor: pointer;
}
</style>
