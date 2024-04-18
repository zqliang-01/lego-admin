<template>
  <div>
    <xr-header
      :show-search="true"
      placeholder="请输入姓名搜索"
      @search="getEmployeeList"/>
    <el-table
      v-loading="loading"
      ref="employeeTable"
      :data="employeeList"
      height="300"
      row-key="code"
      @current-change="changeCurrentUser"
      @selection-change="handleSelectionChange">
      <el-table-column
        v-if="multiple"
        reserve-selection
        type="selection"
        width="55">
      </el-table-column>
      <el-table-column
        v-else
        width="30">
        <template slot-scope="scope">
          <el-radio :label="scope.row.code" v-model="selectedCode"></el-radio>
        </template>
      </el-table-column>
      <el-table-column
        v-for="(item, index) in fieldList"
        :key="index"
        :min-width="item.width"
        :prop="item.fieldCode"
        :label="item.name"
        show-overflow-tooltip>
        <template slot-scope="{ row }">
          <field-view
            :props="item"
            :form-type="item.formType"
            :value="row[item.fieldCode]"/>
        </template>
      </el-table-column>
    </el-table>
    <div class="p-contianer">
      <el-pagination
        :current-page="currentPage"
        :page-sizes="pageSizes"
        :page-size.sync="pageSize"
        :total="total"
        class="p-bar"
        background
        layout="prev, pager, next, sizes, total, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </div>
  </div>
</template>

<script>
import { employeeListAPI } from '@/api/admin/employee'
import SashFormLayout from '@/components/layout/SashFormLayout'
import FieldView from '@/components/Common/Form/FieldView'
import LegoTable from '@/components/lego/LegoTable'
import XrHeader from '@/components/XrHeader'

export default {
  name: 'UserSelect',
  components: {
    SashFormLayout,
    FieldView,
    LegoTable,
    XrHeader
  },
  props: {
    multiple: {
      type: Boolean,
      default: false
    },
    value: [String, Array],
    deptCode: String
  },
  watch: {
    deptCode() {
      this.currentPage = 1
      this.getEmployeeList()
    }
  },
  data() {
    return {
      loading: false,
      dataValue: '',
      selectedCode: '',
      selectedCodes: [],
      employeeList: [],
      currentPage: 1,
      pageSize: 1,
      total: 0,
      pageSizes: [1, 15, 30, 45, 60],
      tableHeight: window.innerHeight,
      fieldList: [
        { fieldCode: 'code', name: '工号', formType: 'text', width: '100' },
        { fieldCode: 'name', name: '姓名', formType: 'text', width: '100' },
        { fieldCode: 'dept', name: '部门', formType: 'select', width: '100' },
        { fieldCode: 'roles', name: '角色', formType: 'checkbox', width: '150' },
        { fieldCode: 'createTime', name: '创建时间', formType: 'text', width: '150' }
      ]
    }
  },
  mounted() {
    if (this.multiple) {
      this.selectedCodes = this.value
    } else {
      this.selectedCode = this.value
    }
    this.getEmployeeList()
  },
  methods: {
    getEmployeeList(name) {
      this.loading = true
      employeeListAPI({
        name: name,
        deptCode: this.deptCode,
        pageSize: this.pageSize,
        pageIndex: this.currentPage
      }).then(res => {
        this.employeeList = res.data.result
        this.total = res.data.totalCount
        this.pageSize = res.data.pageSize
        this.currentPage = res.data.pageIndex
        this.handleInitSelect()
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    handleInitSelect() {
      if (this.multiple) {
        this.$nextTick(() => {
          this.employeeList.forEach(employee => {
            if (this.selectedCodes.some(code => code === employee.code)) {
              this.$refs.employeeTable.toggleRowSelection(employee, true)
            }
          })
        })
      }
    },
    handleSizeChange(val) {
      this.pageSize = val
      this.getEmployeeList()
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this.getEmployeeList()
    },
    changeCurrentUser(val) {
      if (!this.multiple) {
        this.selectedCode = val.code
        this.$emit('selected', val.code)
      }
    },
    handleSelectionChange(values) {
      if (this.multiple) {
        this.selectedCodes = values.map(val => {
          return val.code
        })
        this.$emit('selected', this.selectedCodes)
      }
    }
  }
}
</script>
<style lang="scss" scoped>
.el-table ::v-deep .el-table-column--selection .cell {
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

</style>

