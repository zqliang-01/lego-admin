<template>
  <div class="xr-user-view">
    <div v-if="userData" class="xr-mian">
      <flexbox class="xr-mian__hd user">
        <div class="user-info">
          <div class="user-info__name">
            <span>{{ userData.name }}</span>
            <i v-if="sexIcon" :class="sexIcon | iconPre" />
          </div>
          <div class="user-info__company">
            {{ userData.code }}
          </div>
        </div>

        <xr-avatar
          :src="dataSrc"
          :name="userData.name"
          :size="44"
          class="user-img"/>
      </flexbox>
      <div class="xr-mian__bd">
        <flexbox class="info-cell">
          <i :class="'department' | iconPre" />
          <div class="info-cell__label">部门</div>
          <div class="info-cell__value text-one-line">{{ userData.dept.name }}</div>
        </flexbox>
        <flexbox class="info-cell">
          <i :class="'email' | iconPre" />
          <div class="info-cell__label">开户时间</div>
          <div :title="userData.email" class="info-cell__value text-one-line">{{ userData.createTime }}</div>
        </flexbox>
        <flexbox class="info-cell">
          <i :class="'tie' | iconPre" />
          <div class="info-cell__label">状态</div>
          <div class="info-cell__value text-one-line">{{ userData.enable ? '在网' : '离网' }}</div>
        </flexbox>
        <flexbox class="info-cell">
          <i :class="'b-mobile' | iconPre" />
          <div class="info-cell__label">手机</div>
          <div :title="userData.mobile" class="info-cell__value text-one-line">{{ userData.mobile }}</div>
        </flexbox>
      </div>
    </div>
  </div>
</template>

<script>
import { employeeGetAPI } from '@/api/admin/employee'

export default {
  // 弹窗详情
  name: 'XrUserView',
  components: {},
  props: {
    code: [String, Number],
    data: Object,
    src: String
  },
  data() {
    return {
      userInfo: null
    }
  },
  computed: {
    sexIcon() {
      // 1 男 2 女
      if (this.userData.sex === 1) {
        return 'man'
      } else if (this.userData.sex === 2) {
        return 'woman'
      }
      return ''
    },

    dataSrc() {
      return this.src || this.userData.imageCode
    },

    userData() {
      return this.userInfo || this.data
    }
  },
  watch: {
    code: {
      handler(val) {
        if (val) {
          this.userInfo = null
          this.getUserData()
        }
      },
      immediate: true
    }
  },
  mounted() {},

  beforeDestroy() {},
  methods: {
    getUserData() {
      employeeGetAPI(this.code)
        .then(res => {
          this.userInfo = res.data
        })
        .catch(() => {})
    }
  }
}
</script>

<style lang="scss" scoped>
.xr-user-view {
  min-height: 235px;
}

.xr-mian {
  min-height: 235px;
  &__hd {
    padding: 20px 15px;
    background-color: #F7F8FA;
  }

  &__bd {
    padding-top: 1px solid $xr-border-line-color;
    padding: 20px 15px;
  }
}

.user {
  &-info {
    flex: 1;
    &__name {
      font-size: 16px;
      color: #333;
      font-weight: bold;

      i {
        font-size: 14px;
        margin-left: 8px;
      }

      .wk-woman {
        color: #FF3838;
      }

      .wk-man {
        color: #3875ff;
      }
    }

    &__company {
      margin-top: 8px;
      font-size: 12px;
      color: #666;
    }
  }

  &-img {
    flex-shrink: 0;
    margin-left: 15px;
  }
}

.info-cell {
  font-size: 12px;
  i {
    font-size: 12px;
    color: #999;
    flex-shrink: 0;
  }

  &__label {
    margin-left: 5px;
    color: #666;
    width: 50px;
    flex-shrink: 0;
  }

  &__value {
    color: #333;
    margin-left: 30px;
  }

  & + & {
    margin-top: 15px;
  }
}
</style>
