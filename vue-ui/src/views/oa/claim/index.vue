<template>
  <div class="system-customer">
    <xr-header
      :show-search="true"
      icon-class="icon-related-tasks"
      icon-color="#1CBAF5"
      label="待签任务"
      placeholder="请输入任务名称搜索"
      @search="onSearch"/>
    <div class="customer-content">
      <lego-table
        :loading="loading"
        :data-list="dataList"
        :field-list="fieldList"
        :current-page="currentPage"
        :page-size="pageSize"
        :total="total"
        :edit-button-width="250"
        @onList="getList"
        @onEdit="handleTable">
        <template slot-scope="scope">
          <el-button
            type="text"
            size="small"
            icon="el-icon-edit"
            @click="handleTable('claim', scope.row, scope.$index)">签收</el-button>
        </template>
      </lego-table>
    </div>
    <el-dialog title="流程信息" :visible.sync="processVisible" width="70%" append-to-body>
      <process-viewer
        :key="taskId"
        :instance-id="instanceId"
        :visible="processVisible" />
    </el-dialog>
  </div>
</template>

<script>
import {
  taskClaimListAPI,
  taskClaimAPI,
  taskUnClaimAPI
} from '@/api/admin/workflow/task'
import { mapGetters } from 'vuex'
import XrHeader from '@/components/XrHeader'
import LegoTable from '@/components/lego/LegoTable'
import FieldView from '@/components/Common/Form/FieldView'
import ProcessViewer from '../components/ProcessViewer'

export default {
  name: 'WorkflowModel',
  components: {
    XrHeader,
    FieldView,
    LegoTable,
    ProcessViewer
  },
  computed: {
    ...mapGetters(['manage'])
  },
  data() {
    return {
      loading: false,
      processVisible: false,
      taskId: '',
      instanceId: '',
      dataList: [],
      currentPage: 1,
      pageSize: 15,
      total: 0,
      search: '',
      fieldList: [
        [
          { fieldCode: 'definitionName', name: '流程名称', formType: 'text', width: 150 },
          { fieldCode: 'startUser', name: '发起人', formType: 'select', width: 150 }
        ],
        [
          { fieldCode: 'name', name: '任务节点', formType: 'text', width: '150' },
          { fieldCode: 'createTime', name: '发起时间', formType: 'text', width: '150' }
        ],
        [
          { fieldCode: 'assignee', name: '受理人', formType: 'select', width: '100' }
        ]
      ]
    }
  },
  created() {
    this.refresh()
  },
  methods: {
    refresh() {
      this.getList()
    },
    getList(pageSize = this.pageSize, currentPage = this.currentPage) {
      this.loading = true
      taskClaimListAPI({
        name: this.search,
        pageSize: pageSize,
        pageIndex: currentPage
      }).then(res => {
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
    handleTable(type, item, index) {
      if (type === 'claim') {
        this.loading = true
        taskClaimAPI({
          id: item.id
        }).then(() => {
          this.loading = false
          this.$message.success('签收成功！')
          this.refresh()
        }).catch(() => {
          this.loading = false
        })
        return
      }
      if (type === 'unClaim') {
        this.loading = true
        taskUnClaimAPI({
          id: item.id
        }).then(() => {
          this.loading = false
          this.$message.success('拒签成功！')
          this.refresh()
        }).catch(() => {
          this.loading = false
        })
        return
      }
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
</style>../components/TaskDetail.vue
