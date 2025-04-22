<template>
  <div class="employee-dep-management">
    <xr-header
      :icon-class="'s-seas'"
      placeholder="请输入员工名称"
      show-search
      icon-color="#26D4DA"
      label="员工与部门管理"
      @search="headerSearch"/>
    <div class="system-content">
      <!-- 左边导航栏 -->
      <div
        v-loading="loading"
        class="system-nav">
        <div class="system-nav__title">
          企业组织架构
        </div>
        <div class="system-nav__content">
          <div class="section">
            <div class="section__title">员工</div>
            <div class="section__content">
              <flexbox
                v-for="(item, index) in employeeMenu"
                :key="index"
                :class="employeeMenuClass(item)"
                @click="changeUserClick(item)">
                <i :class="item.icon | iconPre" class="menu-item__icon" />
                <div class="menu-item__content">
                  {{ item.label }}&nbsp;
                  <span v-if="item.count > 0" style="color: #999;">({{ item.count }})</span>
                </div>
              </flexbox>
            </div>
          </div>
          <div class="section">
            <div class="section__title">部门
              <el-button
                v-if="manage.users.add"
                type="text"
                icon="el-icon-circle-plus"
                class="add-btn"
                @click="addDept">创建部门</el-button>
            </div>
            <div class="section__content">
              <el-tree
                ref="deptTree"
                :props="deptTreeProps"
                :expand-on-click-node="false"
                :data="optionsList.deptList"
                node-key="code"
                highlight-current
                default-expand-all
                @node-click="changeDeptClick">
                <flexbox slot-scope="{ node }" :class="{ 'is-current': node.isCurrent}" class="node-data">
                  <i v-if="node.level == 1" :class="'box-double' | iconPre" />
                  <span v-else class="node-data__mark" />
                  <div class="node-data__label text-one-line ">{{ node.label }}</div>
                  <i
                    v-if="node.childNodes && node.childNodes.length"
                    :class="[{ 'is-close': !node.expanded }, {'lego lego-up-unfold': true}]"
                    @click="node.expanded = !node.expanded"/>
                </flexbox>
              </el-tree>
            </div>
          </div>
        </div>
      </div>
      <!-- 右边内容 -->
      <div class="system-view-table flex-index">
        <employee-list
          :dept="currentDept"
          :name="employeeName"
          :type="currentMenuData.type"
          :options-list="optionsList"
          @success="editSuccess"/>
      </div>
    </div>
    <dept-dialog
      :creatable="creatable"
      :visible.sync="deptCreateDialog"
      :options-list="optionsList"
      @success="editSuccess"/>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useStore } from 'vuex'
import XrHeader from '@/components/XrHeader'
import EmployeeList from './components/EmployeeList'
import DeptDialog from './components/DeptDialog'
import { deptListAPI } from '@/api/admin/dept'
import { employeeSimpleListAPI } from '@/api/admin/employee'
import { roleSimpleListAPI } from '@/api/admin/role'

const store = useStore()
const loading = ref(false)
const creatable = ref(true)
const employeeName = ref('')
const currentDept = ref({})
const currentMenuData = ref({})
const deptCreateDialog = ref(false)
const deptTree = ref(null)

const optionsList = ref({
  deptList: [],
  roleList: [],
  employeeList: []
})

const deptTreeProps = {
  label: 'name',
  children: 'childrens'
}

const employeeMenu = ref([
  {
    icon: 'employees',
    label: '所有员工',
    type: 'all',
    count: 0
  },
  {
    icon: 'active-employee',
    label: '激活员工',
    type: 'enable',
    count: 0
  },
  {
    icon: 'inactive-employee',
    label: '未激活员工',
    type: 'disable',
    count: 0
  }
])

const manage = computed(() => store.getters.manage)

onMounted(() => {
  listDept()
  roleSimpleListAPI().then(res => {
    optionsList.value.roleList = res.data
  })
  employeeSimpleListAPI().then(res => {
    optionsList.value.employeeList = res.data
  })
})

const headerSearch = (search) => {
  employeeName.value = search
}

const listDept = () => {
  deptListAPI().then(res => {
    optionsList.value.deptList = res.data
  })
}

const addDept = () => {
  deptCreateDialog.value = true
}

const changeDeptClick = (data) => {
  currentDept.value = data
}

const editSuccess = () => {
  deptCreateDialog.value = false
  listDept()
}

const employeeMenuClass = (item) => {
  return ['menu-item', { 'is-select': currentMenuData.value && currentMenuData.value.type == item.type }]
}

const changeUserClick = (item) => {
  currentMenuData.value = item
  currentDept.value = {}
}
</script>

<style lang="scss" scoped>
@import '@/views/login/index.scss';
.employee-dep-management {
  padding: 0 15px;
  height: 100%;
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
}
.system-content {
  position: relative;
  height: 100%;
  flex: 1;
  display: flex;
  overflow: hidden;
}
.system-nav {
  width: 280px;
  height: 100%;
  overflow: auto;
  margin-right: 10px;
  background: #fff;
  border: 1px solid $xr-border-line-color;
  border-radius: $xr-border-radius-base;

  &__title {
    padding: 15px;
    font-size: 16px;
    font-weight: 600;
    border-bottom: 1px solid $xr-border-line-color;
  }

  &__content {
    overflow: auto;
    height: calc(100% - 50px);
    overflow-y: overlay;
    overflow-x: overlay;
  }
}
// 菜单
.section {
  position: relative;
  &__title {
    position: relative;
    font-size: 14px;
    font-weight: 600;
    color: #333;
    padding: 15px;
    .add-btn {
      position: absolute;
      right: 10px;
      top: 10px;
    }
  }
}
.system-view-table {
  background: #fff;
  border: 1px solid $xr-border-line-color;
  border-radius: $xr-border-radius-base;
  position: absolute;
  top: 0;
  left: 295px;
  bottom: 0;
  right: 0;
}
/* 详情 */
.employee-dep-management :deep(.el-dialog__wrapper) {
  margin-top: 60px !important;
}

.el-tree :deep(.el-tree-node__content) {
  height: 40px;

  .node-data {
    height: 40px;
    padding: 0 15px;
    position: relative;
    border-radius: $xr-border-radius-base;

    .lego {
      font-size: 14px;
      color: #8a94a6;
      flex-shrink: 0;
    }

    .lego-box-double {
      margin-right: 8px;
    }

    &__mark {
      display: inline-block;
      width: 6px;
      height: 6px;
      border-radius: 50%;
      background-color: #e6e6e6;
      margin-right: 8px;
      flex-shrink: 0;
    }

    &__label {
      flex: 1;
      color: #333;
      font-size: 14px;
      margin-right: 8px;
    }

    .lego-up-unfold {
      margin-left: 8px;
      transition: transform 0.3s;
    }

    .lego-up-unfold.is-close {
      transform: rotateZ(180deg);
    }
  }

  .node-data.is-current,
  .node-data:hover {
    background-color: $xr--background-color-base;
  }
}
.el-tree :deep(.el-tree-node__expand-icon) {
  display: none;
}
.system-nav :deep(.el-tree-node > .el-tree-node__children) {
  overflow: visible;
}
.system-nav :deep(.el-tree > .el-tree-node) {
  min-width: 100%;
  display: inline-block !important;
}

.system-nav
  :deep(
    .el-tree--highlight-current
    .el-tree-node.is-current
    > .el-tree-node__content) {
  background-color: white;
}

.system-nav :deep(.el-tree-node__content:hover) {
  background-color: white;
}
/* 设置flex布局 */
.flex-index {
  display: flex;
  flex-direction: column;
}

.menu-item {
  position: relative;
  cursor: pointer;
  padding: 12px 15px;
  color: #333;

  &__icon {
    font-size: 14px;
    margin-right: 8px;
    color: #8a94a6;
  }

  &__content {
    font-size: 14px;
  }
}

.menu-item:hover,
.menu-item.is-select {
  background-color: $xr--background-color-base;
}

.menu-item:hover::before,
.menu-item.is-select::before {
  content: ' ';
  position: absolute;
  top: 0;
  left: 0;
  bottom: 0;
  width: 2px;
  background-color: #5383ed;
}
</style>
