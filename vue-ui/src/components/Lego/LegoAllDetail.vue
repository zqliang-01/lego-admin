<template>
  <el-drawer
    :visible.sync="open"
    size="75%"
    append-to-body
    :with-mask="false"
    :withHeader="false"
    :wrapperClosable="false"
    :destroy-on-close="true"
    :modal-append-to-body="false"
    custom-class="lego-detail"
    @open="handleOpen">
    <el-button
      class="close-btn xr-btn--orange"
      :style="{top: topIndex + 'px'}"
      type="primary"
      icon="el-icon-close"
      @click="handleClose"/>
    <div
      v-loading="loading"
      v-empty="!hasPermission"
      xs-empty-icon="nopermission"
      xs-empty-text="无操作权限"
      style="height: 100%;">
      <component
        v-if="hasPermission"
        :is="componentName"
        :auth="auth"
        :visible.sync="visible"
        :detail-form="detailForm"
        :page-codes="pageCodes"
        v-on="$listeners"/>
    </div>
  </el-drawer>
</template>
<script type="text/javascript">
import LegoDetail from './LegoDetail'
import Detail from './loader/allDetail'
import { getMenuAuth } from '@/utils/auth'
import { customFormPermissionGetAPI } from '@/api/admin/formField'
import { mapGetters } from 'vuex'

export default {
  name: 'LegoAllDetail',
  components: {
    ...Detail,
    LegoDetail
  },
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    formCode: {
      type: String,
      default: ''
    },
    pageCodes: {
      type: Array,
      default: function() {
        return []
      }
    },
    detailCode: String,
    menuCode: String,
    localName: String
  },
  computed: {
    ...mapGetters([
      'activeIndex'
    ])
  },
  watch: {
    visible(val) {
      this.open = val
    }
  },
  data() {
    return {
      open: false,
      loading: false,
      hasPermission: false,
      topIndex: 160,
      auth: {},
      detailForm: {},
      componentName: 'LegoDetail'
    }
  },
  methods: {
    handleOpen() {
      this.detailForm = {
        formCode: this.formCode,
        detailCode: this.detailCode
      }
      if (this.formCode) {
        this.loading = true
        this.hasPermission = false
        customFormPermissionGetAPI(this.formCode).then(res => {
          this.loading = false
          this.auth = getMenuAuth(res.data.permissionCode)
          if (!this.auth || !this.auth.code) {
            this.hasPermission = false
            return
          }
          this.hasPermission = true
          if (this.auth.dynamicRoute) {
            this.componentName = 'LegoDetail'
          } else {
            this.componentName = res.data.className + 'Detail'
          }
          this.topIndex = this.activeIndex
          this.$store.commit('SET_ACTIVEINDEX', 50)
        }).catch(() => {
          this.loading = false
        })
      } else if (this.menuCode) {
        this.auth = getMenuAuth(this.menuCode)
        if (!this.auth || !this.auth.code) {
          this.hasPermission = false
          return
        }
        if (!this.localName) {
          this.$message.error('未注册的组件[' + this.localName + ']操作失败！')
          this.hasPermission = false
          return
        }
        this.hasPermission = true
        this.componentName = this.localName
        this.topIndex = this.activeIndex
        this.$store.commit('SET_ACTIVEINDEX', 50)
        return
      } else {
        this.hasPermission = false
      }
    },
    handleClose() {
      this.$emit('hide-view')
      this.$store.commit('SET_ACTIVEINDEX', -50)
    }
  }
}
</script>
<style lang="scss" scoped>
.close-btn {
  position: fixed;
  z-index: 100;
  margin-left: -39.5px;
  border-top-right-radius: 0;
  border-bottom-right-radius: 0;
  padding: 6px;

  ::v-deep i {
    font-size: 26px;
    margin-right: 0;
  }
}
</style>
