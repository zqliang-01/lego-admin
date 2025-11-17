<template>
  <div v-loading="loading" class="content-table">
    <flexbox class="content-table-header">
      <div class="content-table-header-reminder">
        <div v-if="selectionList.length > 0" style="flex: 1">
          已选中
          <span>
            {{ selectionList.length }}
          </span>
          项
        </div>
        <el-button v-if="selectionList.length > 0"  plain type="danger" @click="removeEmployees">
          移除用户
        </el-button>
      </div>
      <el-button
        v-if="manage.role.update"
        size="medium"
        class="xr-btn--orange"
        type="primary"
        @click="addEmployees"> 关联员工 </el-button>
    </flexbox>
    <el-table
      ref="elTable"
      :data="tableData"
      :height="tableHeight"
      row-key="code"
      style="width: 100%"
      @selection-change="(values) => selectionList = values">
      <el-table-column fixed="left" reserve-selection type="selection" width={50} />
      <el-table-column
        v-for="(item, index) in tableList"
        :prop="item.fieldCode"
        :label="item.name"
        :key="index"
        show-overflow-tooltip>
        <template slot-scope="{ row }">
          <field-view
            :item="item"
            :form-type="item.formType"
            :value="row[item.fieldCode]">
            <span
              v-if="item.fieldCode === 'code'"
              slot="leftContent"
              :style="{'background-color' : getStatusColor(row.enable) }"
              class="status-mark"/>
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
        :page-size.sync="pageSize"
        :total="total"
        class="p-bar"
        background
        layout="prev, pager, next, sizes, total, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"/>
    </div>
    <role-employee-dialog
      :visible.sync="visible"
      :code="operationCode"
      :codes="operationCodes"
      @success="editSuccess"
    />
  </div>
</template>
<script>
import { mapGetters } from 'vuex'
import Reminder from '@/components/Reminder'
import { employeeListAPI, employeeRoleModifyAPI } from '@/api/admin/employee'
import FieldView from '@/components/Common/Form/FieldView'
import RoleEmployeeDialog from './RoleEmployeeDialog'
export default {
  components: {
    Reminder,
    FieldView,
    RoleEmployeeDialog
  },
  props: {
    roleCode: String,
    name: String
  },
  computed: {
    ...mapGetters(['manage'])
  },
  data() {
    return {
      loading: false,
      visible: false,
      operationType: '',
      operationCode: '',
      operationCodes: [],
      selectionList: [],
      tableData: [],
      total: 0,
      pageSize: 15,
      currentPage: 1,
      pageSizes: [15, 30, 45, 60],
      tableHeight: document.documentElement.clientHeight - 280, // 表的高度
      tableList: [
        { fieldCode: 'code', name: '工号', formType: 'text' },
        { fieldCode: 'name', name: '姓名', formType: 'text' },
        { fieldCode: 'dept', name: '部门', formType: 'select' },
        { fieldCode: 'roles', name: '角色', formType: 'checkbox' }
      ]
    }
  },
  watch: {
    roleCode() {
      this.refreshUserList()
    },
    name() {
      this.refreshUserList()
    }
  },
  mounted() {
    window.onresize = () => {
      this.tableHeight = document.documentElement.clientHeight - 305
    }
  },
  methods: {
    refreshUserList() {
      this.loading = true
      const param = {
        name: this.name,
        roleCode: this.roleCode,
        pageIndex: this.currentPage,
        pageSize: this.pageSize
      }
      employeeListAPI(param).then(res => {
        this.loading = false
        this.total = res.data.totalCount
        this.tableData = res.data.result
      }).catch(() => {
        this.loading = false
      })
    },
    editRole(data) {
      if (!this.roleCode) {
        this.$message.error('请先选择角色信息！')
        return
      }
      this.visible = true
      this.operationType = 'role'
      this.operationCode = data.code
      this.operationCodes = data.roles.map(item => {
        return item.code
      })
    },
    addEmployees() {
      if (!this.roleCode) {
        this.$message.error('请先选择角色信息！')
        return
      }
      this.visible = true
      this.operationType = 'employee'
      this.operationCode = this.roleCode
      this.operationCodes = []
    },
    removeEmployees() {
      this.$confirm('确认移除所选用户？').then(() => {
        employeeRoleModifyAPI({
          roleCode: this.roleCode,
          employeeCodes: this.selectionList.map(item => item.code),
          action: 'remove'
        }).then(() => {
          this.$refs.elTable.clearSelection()
          this.refreshUserList()
          this.$message.success('操作成功！')
        })
      })
    },
    editSuccess() {
      this.refreshUserList()
    },
    handleSizeChange(val) {
      this.pageSize = val
      this.refreshUserList()
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this.refreshUserList()
    },
    getStatusColor(status) {
      if (status) {
        return '#46CDCF'
      }
      return '#CCCCCC'
    }
  }
}
</script>
<style lang="scss" scoped>
.content-table {
  overflow: hidden;
}
.content-table > .el-button {
  float: right;
  margin-bottom: 15px;
  margin-right: 30px;
}
.content-box .content-table-span {
  color: #2362FB;
  margin-left: 5px;
  cursor: pointer;
  font-size: 10px;
}

.content-table-header {
  padding: 0 15px 5px;
  .content-table-header-reminder {
    flex: 1;
    margin-right: 5px;
    display: flex;
    align-items: center;
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

.status-mark {
  display: inline-block;
  width: 6px;
  height: 6px;
  margin-right: 5px;
  border-radius: 3px;
}
</style>
