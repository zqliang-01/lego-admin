<template>
  <el-avatar
    v-if="disabled"
    slot="reference"
    v-bind="$attrs"
    :src="imageCache[src]"
    :key="src"
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
    <xr-user-view
      v-loading="loading"
      :data="userData"
      :src="imageCache[src]" />
    <el-avatar
      slot="reference"
      v-bind="$attrs"
      :src="imageCache[src]"
      :key="src"
      :style="{ fontSize: fontSize, background: background }"
      :class="{ 'cursor-pointer': !disabled }"
      :size="size"
      fit="fill">{{ showName }}</el-avatar>
  </el-popover>
</template>

<script>
import { employeeGetAPI } from '@/api/admin/employee'
import { getImageData } from '@/utils'
import XRTheme from '@/styles/xr-theme.scss'
import { mapGetters } from 'vuex'

export default {
  // Avatar 头像
  name: 'XrAvatar',
  components: {
    XrUserView: () => import('../XrUserView')
  },
  inheritAttrs: false,
  props: {
    name: String,
    code: [Number, String],
    size: {
      type: [Number, String],
      default: 38
    },
    src: String,
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
      default: XRTheme.xrColorPrimary
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
    src: {
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
      if (this.src) {
        if (!this.imageCache.hasOwnProperty(this.src)) {
          getImageData(this.src).then(data => {
            this.$set(this.imageCache, this.src, data.src)
            this.$store.commit('SET_IMAGECACHE', this.imageCache)
          }).catch(() => {
            delete this.imageCache[this.src]
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
  /deep/ img {
    width: 100%;
    background: white !important;
  }
}
</style>
