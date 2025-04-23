<template>
  <div>
    <flexbox
      v-if="selectionList.length > 0"
      class="selection-bar">
      <div class="selected—title">已选中 <span class="selected—count">{{ selectionList.length }}</span> 项</div>
      <flexbox class="selection-items-box">
        <el-button
          v-for="(item, index) in selectionInfo"
          :icon="item.icon"
          :key="index"
          type="primary"
          @click="selectionBarClick(item.type)">{{ item.name }}</el-button>
      </flexbox>
    </flexbox>
    <flexbox
      v-else
      justify="space-between" class="table-top">
      <div class="table-top__title">
        <span v-if="dept.name != null">{{ dept.name }} - </span>
        <span>员工列表</span>
        <reminder class="all-user-reminder" content="未添加部门和角色的员工无法正常登录系统" />
      </div>
      <div>
        <el-button
          v-if="manage.users.add"
          type="text"
          icon="el-icon-circle-plus"
          @click="addEmployee">添加员工</el-button>
        <el-button
          v-if="manage.users.update"
          type="text"
          icon="el-icon-edit"
          @click="updateDept">修改部门</el-button>
      </div>
    </flexbox>
    <div class="flex-box">
      <el-table
        v-loading="loading"
        ref="employeeTable"
        :data="employeeList"
        :height="tableHeight"
        @selection-change="handleSelectionChange"
        @row-click="rowClick">
        <el-table-column
          type="selection"
          width="55" />
        <el-table-column
          v-for="(item, index) in fieldList"
          :key="index"
          :min-width="item.width"
          :prop="item.fieldCode"
          :label="item.name"
          show-overflow-tooltip>
          <template #default="{ row }">
            <field-view
              :item="item"
              :form-type="item.formType"
              :value="row[item.fieldCode]"
              :class="{'status-name': item.fieldCode === 'code'}">
              <span slot="leftContent" :style="{'background-color' : getStatusColor(row.enable)}" class="status-mark" />
            </field-view>
          </template>
        </el-table-column>
      </el-table>
      <div class="p-contianer">
        <div class="status-des">
          <div class="status-des-item">
            <span :style="{'background-color' : getStatusColor(true)}" /> 激活
          </div>
          <div class="status-des-item">
            <span :style="{'background-color' : getStatusColor(false)}" /> 未激活
          </div>
        </div>
        <el-pagination
          :current-page="currentPage"
          :page-sizes="pageSizes"
          :page-size="pageSize"
          :total="total"
          class="p-bar"
          background
          layout="prev, pager, next, sizes, total, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange" />
      </div>
    </div>

    <employee-dialog
      :creatable="creatable"
      :visible="employeeCreateDialog"
      :options-list="optionsList"
      :data="currentEmployee"
      @update:visible="val => employeeCreateDialog = val"
      @success="editSuccess" />

    <dept-dialog
      :creatable="creatable"
      :visible="deptCreateDialog"
      :data="dept"
      :options-list="optionsList"
      @update:visible="val => deptCreateDialog = val"
      @success="editDeptSuccess"/>
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { useStore } from 'vuex'
import Reminder from '@/components/Reminder'
import DeptDialog from './DeptDialog'
import EmployeeDialog from './EmployeeDialog'
import FieldView from '@/components/Common/Form/FieldView'
import { employeeListAPI, employeeResetPasswordAPI } from '@/api/admin/employee'

const props = defineProps({
  name: String,
  dept: Object,
  type: String,
  optionsList: Object
})

const emit = defineEmits(['success'])

const store = useStore()
const loading = ref(false)
const creatable = ref(true)
const employeeList = ref([])
const employeeCreateDialog = ref(false)
const deptCreateDialog = ref(false)
const currentEmployee = ref({})
const selectionList = ref([])
const tableHeight = ref(document.documentElement.clientHeight - 220)
const currentPage = ref(1)
const pageSize = ref(15)
const pageSizes = ref([15, 30, 45, 60])
const total = ref(0)
const employeeTable = ref(null)

const fieldList = ref([
  { fieldCode: 'code', name: '工号', formType: 'text', width: '150' },
  { fieldCode: 'name', name: '姓名', formType: 'text', width: '150' },
  { fieldCode: 'dept', name: '部门', formType: 'select', width: '150' },
  { fieldCode: 'roles', name: '角色', formType: 'checkbox', width: '150' },
  { fieldCode: 'createTime', name: '创建时间', formType: 'text', width: '150' },
  { fieldCode: 'enable', name: '状态', formType: 'boolean', width: '100' }
])

const manage = computed(() => store.getters.manage)
const selectionInfo = computed(() => [
  {
    name: '重置密码',
    type: 'resetPassword',
    icon: 'circle-password'
  }
])

watch(() => props.dept, () => {
  currentPage.value = 1
  listEmployee()
}, { deep: true, immediate: true })

watch(() => props.type, () => {
  currentPage.value = 1
  listEmployee()
})

watch(() => props.name, () => {
  currentPage.value = 1
  listEmployee()
})

const addEmployee = () => {
  creatable.value = true
  currentEmployee.value = { dept: props.dept, enable: true }
  employeeCreateDialog.value = true
}

const updateDept = () => {
  if (!props.dept.code) {
    ElMessage.error('请先选择部门信息！')
    return
  }
  creatable.value = false
  deptCreateDialog.value = true
}

const listEmployee = () => {
  loading.value = true
  const param = {
    name: props.name,
    deptCode: props.dept.code,
    pageIndex: currentPage.value,
    pageSize: pageSize.value
  }
  if (props.type === 'enable') {
    param.enable = true
  }
  if (props.type === 'disable') {
    param.enable = false
  }
  employeeListAPI(param).then(res => {
    loading.value = false
    total.value = res.data.totalCount
    employeeList.value = res.data.result
  }).catch(() => {
    loading.value = false
  })
}

const handleSelectionChange = (val) => {
  selectionList.value = val
}

const editSuccess = () => {
  employeeCreateDialog.value = false
  listEmployee()
}

const editDeptSuccess = () => {
  deptCreateDialog.value = false
  emit('success')
}

const selectionBarClick = (type) => {
  if (type === 'resetPassword') {
    const codes = selectionList.value.map(item => {
      return item.code
    })
    loading.value = true
    employeeResetPasswordAPI(codes).then(res => {
      employeeTable.value.clearSelection()
      ElMessage.success('密码重置成功！')
      loading.value = false
    }).catch(() => {
      loading.value = false
    })
  }
}

const handleSizeChange = (val) => {
  pageSize.value = val
  listEmployee()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  listEmployee()
}

const rowClick = (row, column, event) => {
  if (column.property === 'code' && manage.value.users.update) {
    creatable.value = false
    currentEmployee.value = row
    employeeCreateDialog.value = true
  }
}

const getStatusColor = (status) => {
  if (status) {
    return '#46CDCF'
  }
  return '#CCCCCC'
}
</script>

<style lang="scss" scoped>
.selection-bar {
  font-size: 12px;
  height: 50px;
  padding: 0 20px;
  color: #777;

  .selected—title {
    flex-shrink: 0;
    padding-right: 20px;
    color: #333;
    .selected—count {
      color: $xr-color-primary;
    }
  }
}
.selection-items-box {
  overflow-x: auto;
  overflow-y: hidden;
  padding: 0 15px;
  .el-button {
    color: #666;
    background-color: $xr--background-color-base;
    border-color: $xr--background-color-base;
    font-size: 12px;
    height: 28px;
    border-radius: 14px;
    :deep i {
      font-size: 12px;
      margin-right: 5px;
    }
  }

  .el-button--primary:hover {
    background: $xr-color-primary;
    border-color: $xr-color-primary;
    color: #ffffff;
  }

  .el-button + .el-button {
    margin-left: 15px;
  }
}
.table-top {
  padding: 0 30px;
  height: 50px;

  &__title {
    font-size: 16px;
    color: #333;
  }

  .el-dropdown {
    margin-left: 7px;
  }
}

.all-user-reminder {
  width: auto;
  margin-left: 5px;
  display: inline-block;
}

.el-table :deep .el-table-column--selection .cell {
  padding-left: 14px;
}
.flex-box {
  flex: 1;
}
.p-contianer {
  position: relative;
  background-color: white;
  height: 44px;
  .p-bar {
    float: right;
    margin: 5px 100px 0 0;
    font-size: 14px !important;
  }
}
.el-table {
  border-top: 1px solid $xr-border-line-color;
}

.status-des {
  font-size: 12px;
  color: #777777;
  margin: 0 5px;
  position: absolute;
  left: 0;
  top: 7px;
  .status-des-item {
    margin: 8px;
    display: inline-block;
    span {
      display: inline-block;
      width: 6px;
      height: 6px;
      border-radius: 3px;
      margin-right: 5px;
    }
  }
}

.status-name {
  .status-mark {
    display: inline-block;
    width: 6px;
    height: 6px;
    margin-right: 5px;
    border-radius: 3px;
  }
  color: $xr-color-primary;
  cursor: pointer;
  .main-mark {
    background-color: #ff6a00;
    color: white;
    border-radius: 2px;
    font-size: 12px;
    padding: 0px 4px;
    margin: 0 3px;
  }
}
</style>
