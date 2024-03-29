<template>
  <div class="system-customer">
    <xr-header
      :icon-class="'double-gear'"
      :show-search="true"
      icon-color="#1CBAF5"
      label="日志管理"
      placeholder="请输入操作内容"
      @search="onSearch" />
    <div class="customer-content">
      <flexbox class="main-table-header">
        <el-date-picker
          v-model="dateTime"
          type="daterange"
          value-format="yyyy-MM-dd"
          range-separator="-"
          start-placeholder="开始时间"
          end-placeholder="结束时间"/>
        <el-select
          v-model="operatorCode"
          clearable
          filterable
          placeholder="选择人员">
          <el-option
            v-for="(item, index) in employeeList"
            :key="index"
            :label="item.name "
            :value="item.code"/>
        </el-select>
      </flexbox>
      <lego-table
        :loading="loading"
        :data-list="dataList"
        :field-list="fieldList"
        :current-page="currentPage"
        :page-size="pageSize"
        :total="total"
        :table-heigh-overly="-50"
        :edit-button-width="100"
        @onList="getList"
        @onEdit="handleTable">
        <template slot-scope="scope">
          <el-button
            type="text"
            size="small"
            icon="el-icon-view"
            @click="handleTable('detail', scope.row)">详情</el-button>
        </template>
      </lego-table>
    </div>
    <el-dialog title="详细信息" :visible.sync="showDetail" append-to-body>
      <p style="white-space: pre-wrap">{{ logDetail.description }}</p>
    </el-dialog>
  </div>
</template>

<script>
import {
  operationLogListAPI
} from '@/api/admin/log'
import { employeeSimpleListAPI } from '@/api/admin/employee'
import { mapGetters } from 'vuex'
import XrHeader from '@/components/XrHeader'
import LegoTable from '@/components/lego/LegoTable'

export default {
  name: 'WorkflowModel',
  components: {
    XrHeader,
    LegoTable
  },
  computed: {
    ...mapGetters(['manage'])
  },
  data() {
    return {
      loading: false,
      showDetail: false,
      logDetail: '',
      dateTime: [],
      operatorCode: '',
      employeeList: [],
      dataList: [],
      currentPage: 1,
      pageSize: 15,
      total: 0,
      search: '',
      action: {
        type: 'update',
        detailData: {}
      },
      fieldList: [
        [
          { fieldCode: 'operator', name: '用户', formType: 'select', width: '100' },
          { fieldCode: 'createTime', name: '时间', formType: 'text', width: '150' }
        ],
        [
          { fieldCode: 'action', name: '行为', formType: 'text', width: '100' },
          { fieldCode: 'entityCode', name: '操作对象', formType: 'text', width: '100' }
        ],
        [
          { fieldCode: 'permission', name: '功能模块', formType: 'select', width: '100' },
          { fieldCode: 'description', name: '操作详情', formType: 'text', width: '300' }
        ]
      ]
    }
  },
  created() {
    employeeSimpleListAPI().then(res => {
      this.employeeList = res.data
    })
    this.refresh()
  },
  methods: {
    refresh() {
      this.getList()
    },
    getList(pageSize = this.pageSize, currentPage = this.currentPage) {
      this.loading = true
      const param = {
        description: this.search,
        operatorCode: this.operatorCode,
        pageSize: pageSize,
        pageIndex: currentPage
      }
      if (this.dateTime && this.dateTime.length > 0 && this.dateTime.length === 2) {
        param.startTime = this.dateTime[0]
        param.endTime = this.dateTime[1]
      }
      operationLogListAPI(param).then(res => {
        this.dataList = res.data.result
        this.total = res.data.totalCount
        this.currentPage = res.data.pageIndex
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    handleSizeChange(val) {
      this.pageSize = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this.getList()
    },
    handleTable(type, item) {
      this.showDetail = true
      this.logDetail = item
    },
    handleDetailValue(key) {
      return this.logDetail[key]
    },
    onSearch(value) {
      this.search = value
      this.getList()
    }
  }
}
</script>

<style lang="scss" scoped>
.system-customer {
  height: 100%;
}
.customer-content {
  border-top: 1px solid $xr-border-line-color;
  border-bottom: 1px solid $xr-border-line-color;
}

.main-table-header {
  height: 50px;
  background-color: white;
  position: relative;

  .el-date-editor--daterange {
    width: 300px;
    margin: 0 20px;
  }
}

</style>
