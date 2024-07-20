<template>
  <div
    v-loading="loading"
    class="content-body">
    <div class="data-radio">
      <reminder class="all-user-reminder" content="控制角色可查询到的数据范围" />
      <el-radio-group v-model="dataScope">
        <el-radio :label="0">本人</el-radio>
        <el-radio :label="1">本部门</el-radio>
        <el-radio :label="2">本部门及下级部门</el-radio>
        <el-radio :label="3">全部</el-radio>
      </el-radio-group>
    </div>
    <el-button
      v-if="manage.role.auth"
      size="medium"
      type="primary"
      class="save-button"
      @click="ruleSubmit"> 保存 </el-button>
  </div>
</template>
<script>
import {
  roleGetAPI,
  roleAuthDataScopeAPI
} from '@/api/admin/role'
import { mapGetters } from 'vuex'
import Reminder from '@/components/Reminder'
export default {
  components: {
    Reminder
  },
  props: {
    roleCode: String
  },
  computed: {
    ...mapGetters(['manage'])
  },
  data() {
    return {
      loading: false,
      dataScope: 0
    }
  },
  watch: {
    roleCode() {
      this.getDataScope()
    }
  },
  mounted() {
    this.getDataScope()
  },
  methods: {
    getDataScope() {
      if (!this.roleCode) {
        return
      }
      this.loading = true
      roleGetAPI(this.roleCode).then(res => {
        this.dataScope = res.data.dataScope
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    ruleSubmit() {
      this.loading = true
      roleAuthDataScopeAPI({
        roleCode: this.roleCode,
        dataScope: this.dataScope
      }).then(res => {
        this.loading = false
        this.$message.success('保存成功！')
      }).catch(() => {
        this.loading = false
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.content-body {
  padding-bottom: 25px;
  height: calc(100% - 61px);
  overflow: hidden;
  position: relative;
}
.save-button {
  padding: 10px 30px;
  position: absolute;
  top: 0;
  right: 20px;
  z-index: 3;
}
.data-radio {
  padding: 5px 30px;
}
.data-radio .el-radio {
  display: block;
  margin: 20px 0;
}
</style>
