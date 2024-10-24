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
    @close="handleClose"
    @open="handleOpen">
    <el-button
      class="close-btn xr-btn--orange"
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
import crmDetail from '@/views/crm/components/detail'
import { getMenuAuth } from '@/utils/auth'
import { customFormPermissionGetAPI } from '@/api/admin/formField'

export default {
  name: 'LegoAllDetail',
  components: {
    LegoDetail,
    ...crmDetail
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
    detailCode: String
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
      auth: {},
      detailForm: {},
      componentName: 'LegoDetail'
    }
  },
  methods: {
    handleOpen() {
      this.loading = true
      this.detailForm = {
        formCode: this.formCode,
        detailCode: this.detailCode
      }
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
      }).catch(() => {
        this.loading = false
      })
    },
    handleClose() {
      this.$emit('hide-view')
    }
  }
}
</script>
<style lang="scss" scoped>
::v-deep .lego-detail {
  min-width: 926px;
  border-radius: 4px;
  background-color: #f5f6f9;
}

::v-deep .el-drawer__body {
  overflow: hidden !important;
}

.close-btn {
  position: fixed;
  top: 160px;
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
