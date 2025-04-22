<template>
  <el-tabs v-model="currentTab">
    <el-tab-pane
      label="角色员工"
      name="user">
      <role-employee-list
        :role-code="roleCode"
      />
    </el-tab-pane>
    <el-tab-pane
      label="角色权限"
      name="rule">
      <role-auth-list
        :role-code="roleCode"
      />
    </el-tab-pane>
    <el-tab-pane
      label="数据权限"
      name="data">
      <role-data-auth
        :role-code="roleCode"
      />
    </el-tab-pane>
  </el-tabs>
</template>

<script setup>
import { ref } from 'vue'
import RoleEmployeeList from './RoleEmployeeList'
import RoleAuthList from './RoleAuthList'
import RoleDataAuth from './RoleDataAuth'

defineProps({
  roleCode: String
})

const currentTab = ref('user')
</script>

<style lang="scss" scoped>
.el-tabs :deep(.el-tabs__header) {
  padding: 0 17px;
  margin: 0 0 15px !important;
}

.el-tabs :deep(.el-tabs__item) {
  font-size: 13px !important;
  height: 40px !important;
  line-height: 40px !important;
}
</style>
