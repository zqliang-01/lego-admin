<template>
  <flexbox align="flex-start" class="person-center">
    <div class="left">
      <flexbox class="user-box">
        <xr-avatar
          :name="userInfo.name"
          :size="50"
          :imageCode="userInfo.imageCode"
          class="user-img" />
        <span class="username">
          {{ userInfo.name }}
        </span>
      </flexbox>
      <ul class="nav-list">
        <li
          v-for="(item, index) in navList"
          :key="index"
          :class="{active: selectedIndex === index}"
          class="nav-list-item"
          @click="selectedIndex = index">
          <span :class="item.icon | iconPre" class="lego icon" />
          <span class="text">
            {{ item.label }}
          </span>
        </li>
      </ul>
    </div>
    <div class="right">
      <edit-user-info
        v-if="selectedIndex === 0"
        @change="getDetail" />
      <edit-pwd v-if="selectedIndex === 1" />
    </div>
  </flexbox>
</template>

<script>
import { mapGetters } from 'vuex'
import EditUserInfo from './components/EditUserInfo'
import EditPwd from './components/EditPwd'
export default {
  name: 'PersonCenter',
  components: {
    EditUserInfo,
    EditPwd
  },
  data() {
    return {
      selectedIndex: 0 // 0 个人信息 1 账号密码 2 名片信息
    }
  },
  computed: {
    ...mapGetters([
      'userInfo'
    ]),
    navList() {
      const navs = [
        { label: '个人信息', icon: 'user' },
        { label: '账号密码', icon: 'circle-password' }
      ]
      return navs
    }
  },
  created() {
    this.getDetail()
  },
  methods: {
    getDetail() {
      this.loading = true
      this.$store.dispatch('GetUserInfo').then(() => {
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    }
  }
}
</script>

<style scoped lang="scss">
  .person-center {
    width: 1180px;
    margin: 0 auto;
    .left {
      width: 300px;
      background-color: white;
      padding-bottom: 100px;
      border: 1px solid #e6e6e6;
      border-radius: $xr-border-radius-base;
      margin-right: 20px;
      .user-box {
        width: 376px;
        padding: 15px 20px 20px;
        .user-img {
          margin-right: 22px;
        }
        .username {
          flex: 1;
          font-size: 14px;
          color: #666;
        }
      }
      .nav-list {
        width: 100%;
        &-item {
          height: 50px;
          color: #666;
          padding: 0 20px;
          display: flex;
          align-items: center;
          justify-content: flex-start;
          cursor: pointer;
          .icon {
            margin-right: 10px;
          }
          &:hover, &.active {
            color: #333;
            background-color: #F1F5F8;
          }
        }
      }
    }
    .right {
      flex: 1;
      border: 1px solid #e6e6e6;
      border-radius: $xr-border-radius-base;
      overflow: hidden;
    }
  }
</style>
