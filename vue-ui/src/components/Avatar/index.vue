<template>
  <el-avatar
    v-if="disabled"
    slot="reference"
    v-bind="$attrs"
    :src="imageCache[imageCode]"
    :key="imageCode"
    :style="{ fontSize: fontSize, background: background }"
    :class="{ 'cursor-pointer': !disabled }"
    :size="size"
    fit="fill">{{ showName }}</el-avatar>

  <el-popover
    v-else
    v-model="popoverShow"
    :visible-arrow="false"
    :trigger="trigger"
    :disabled="popoverDisabled"
    placement="bottom"
    width="250"
    popper-class="no-padding-popover">
    <user-view
      v-loading="loading"
      :data="userData"
      :imageCode="imageCode" />
    <el-avatar
      slot="reference"
      v-bind="$attrs"
      :src="imageCache[imageCode]"
      :key="imageCode"
      :style="{ fontSize: fontSize, background: background }"
      :class="{ 'cursor-pointer': !disabled }"
      :size="size"
      fit="fill">{{ showName }}</el-avatar>
  </el-popover>
</template>

<script>
import { employeeGetAPI } from '@/api/admin/employee'
import { getImageData } from '@/utils'
import { mapGetters } from 'vuex'

export default {
  // Avatar 头像
  name: 'Avatar',
  components: {
    UserView: () => import('../UserView')
  },
  inheritAttrs: false,
  props: {
    name: String,
    code: [Number, String],
    size: {
      type: [Number, String],
      default: 38
    },
    imageCode: String,
    disabled: {
      type: Boolean,
      default: true
    },
    trigger: {
      type: String,
      default: 'click'
    },
    background: {
      type: String,
      default: '#2362FB'
    }
  },
  data() {
    return {
      popoverShow: false,
      loading: false,
      userData: null
    }
  },
  computed: {
    ...mapGetters(['imageCache']),
    fontSize() {
      if (this.size <= 30) {
        return '12px'
      }
      return '14px'
    },
    showName() {
      return this.name && this.name.length > 2 ? this.name.slice(-2) : this.name
    },
    popoverDisabled() {
      if (this.disabled) {
        return true
      }
      return !this.code
    }
  },
  watch: {
    popoverShow(val) {
      if (!this.userData) {
        this.getUserData()
      }
    },
    imageCode: {
      handler() {
        this.handleImage()
      },
      immediate: true
    }
  },
  created() {},
  beforeDestroy() {},
  methods: {
    handleImage() {
      if (this.imageCode) {
        if (!this.imageCache.hasOwnProperty(this.imageCode)) {
          getImageData(this.imageCode).then(data => {
            this.imageCache[this.imageCode] = data.src
            this.$store.commit('SET_IMAGECACHE', this.imageCache)
            this.$forceUpdate()
          }).catch(() => {
            delete this.imageCache[this.imageCode]
            this.$store.commit('SET_IMAGECACHE', this.imageCache)
          })
        }
      }
    },

    getUserData() {
      employeeGetAPI(this.code)
        .then(res => {
          this.userData = res.data
        })
        .catch(() => {})
    }
  }
}
</script>

<style lang="scss" scoped>
.cursor-pointer {
  cursor: pointer;
}

.el-avatar {
  ::v-deep img {
    width: 100%;
    background: white !important;
  }
}
</style>
