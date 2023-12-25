<template>
  <div class="navbar">
    <img
      v-src="logoUrl"
      :key="logo"
      class="logo"
      @click="enterMainPage" >
    <div class="nav-title">
      系统设置
    </div>
    <div
      class="back-home"
      @click="enterHome">返回首页</div>
    <div
      class="go-out"
      @click="enterLogin">退出系统</div>
  </div>
</template>

<script>
import { filePreviewUrl } from '@/api/common'
import { Loading } from 'element-ui'
import { mapGetters } from 'vuex'

export default {
  components: {},
  props: {
    navIndex: {
      type: [Number, String],
      default: 0
    }
  },
  data() {
    return {}
  },
  computed: {
    ...mapGetters(['logo', 'navActiveIndex']),
    logoUrl() {
      if (this.logo) {
        return filePreviewUrl + this.logo
      }
      return require('@/assets/img/logo.png')
    }
  },
  mounted() {
    this.$store.commit('SET_NAVACTIVEINDEX', this.navIndex)
  },
  methods: {
    enterHome() {
      this.$router.replace({
        path: '/'
      })
    },
    enterLogin() {
      this.$confirm('退出登录？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          var loading = Loading.service({
            target: document.getElementById('#app')
          })
          this.$store
            .dispatch('LogOut')
            .then(() => {
              loading.close()
              location.reload()
            })
            .catch(() => {
              loading.close()
              location.reload()
            })
        })
        .catch(() => {})
    },

    /**
     * 有客户权限点击logo 进入仪表盘
     */
    enterMainPage() {
      this.$router.push('/')
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
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

