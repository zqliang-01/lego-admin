<template>
  <component
    v-if="hasPermission"
    :visible="visible"
    :loading="loading"
    :is="componentName"
    :form-code="formCode"
    v-bind="$attrs"
    v-on="$listeners"/>
</template>

<script type="text/javascript">
import LegoCreate from './LegoCreate'
import Create from './loader/allCreate'
import { getMenuAuth } from '@/utils/auth'
import { customFormPermissionGetAPI } from '@/api/admin/formField'

export default {
  name: 'LegoAllCreate',
  components: {
    ...Create,
    LegoCreate
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
    menuCode: String,
    localName: String
  },
  watch: {
    visible(val) {
      if (val) {
        this.handleOpen()
      }
    }
  },
  data() {
    return {
      open: false,
      loading: false,
      hasPermission: false,
      componentName: ''
    }
  },
  methods: {
    handleOpen() {
      if (this.formCode) {
        this.loading = true
        this.hasPermission = false
        customFormPermissionGetAPI(this.formCode).then(res => {
          this.loading = false
          const auth = getMenuAuth(res.data.permissionCode)
          if (!auth || !auth.add) {
            this.$message.error('无[' + this.formCode + ']操作权限！')
            this.$emit('close')
            return
          }
          this.hasPermission = true
          if (auth.dynamicRoute) {
            this.componentName = 'LegoCreate'
          } else {
            this.componentName = res.data.className + 'Create'
          }
        }).catch(() => {
          this.loading = false
          this.$emit('close')
        })
      } else if (this.menuCode) {
        const auth = getMenuAuth(this.menuCode)
        if (!auth || !auth.add) {
          this.$message.error('无[' + this.menuCode + ']操作权限！')
          this.$emit('close')
          return
        }
        if (!this.localName) {
          this.$message.error('未注册的组件[' + this.localName + ']操作失败！')
          this.$emit('close')
          return
        }
        this.hasPermission = true
        this.componentName = this.localName
        return
      } else {
        this.hasPermission = false
      }
    }
  }
}
</script>
<style lang="scss" scoped>
</style>
