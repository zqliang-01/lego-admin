<template>
  <div class="system-customer">
    <xr-header
      :show-search="true"
      icon-class="contract"
      icon-color="#1CBAF5"
      label="待办任务"
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
            icon="el-icon-view"
            @click="handleTable('view', scope.row, scope.$index)">流程进度</el-button>
          <el-button
            type="text"
            size="small"
            icon="el-icon-edit"
            @click="handleTable('complete', scope.row, scope.$index)">处理</el-button>
          <el-button
            type="text"
            size="small"
            icon="el-icon-edit"
            @click="handleTable('reject', scope.row, scope.$index)">拒绝</el-button>
        </template>
      </lego-table>
    </div>
    <el-dialog title="流程信息" :visible.sync="processVisible" width="70%" append-to-body>
      <process-viewer
        :key="taskId"
        :instance-id="instanceId"
        :visible="processVisible" />
    </el-dialog>
    <task-detail
      :visible.sync="createShow"
      :task-id="taskId"
      :action="{ type: 'save' }"
      @close="createShow = false"
      @handle="actionHandle"
    />
  </div>
</template>

<script>
import {
  taskUndoListAPI,
  taskRejectAPI
} from '@/api/admin/workflow/task'
import { mapGetters } from 'vuex'
import XrHeader from '@/components/XrHeader'
import LegoTable from '@/components/Lego/LegoTable'
import FieldView from '@/components/Common/Form/FieldView'
import ProcessViewer from '../components/ProcessViewer'
import TaskDetail from '../components/TaskDetail.vue'

export default {
  name: 'WorkflowModel',
  components: {
    XrHeader,
    FieldView,
    LegoTable,
    ProcessViewer,
    TaskDetail
  },
  computed: {
    ...mapGetters(['manage'])
  },
  data() {
    return {
      loading: false,
      isCreate: false,
      processVisible: false,
      createShow: false,
      formCode: '',
      taskId: '',
      instanceId: '',
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
  watch: {
    $route(to, from) {
      this.taskId = this.$route.query.taskId
      if (this.taskId) {
        this.createShow = true
      }
    }
  },
  created() {
    this.refresh()
    this.taskId = this.$route.query.taskId
    if (this.taskId) {
      this.createShow = true
    }
  },
  methods: {
    refresh() {
      this.getList()
    },
    getList(pageSize = this.pageSize, currentPage = this.currentPage) {
      this.loading = true
      taskUndoListAPI({
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
      if (type === 'view') {
        this.taskId = item.id
        this.instanceId = item.instanceId
        this.processVisible = true
        return
      }
      if (type === 'complete') {
        this.taskId = item.id
        this.createShow = true
        return
      }
      if (type === 'reject') {
        this.$confirm('此操作将拒绝任务【' + item.name + '】并回退到上一审批节点，是否继续?', '提示', {
          type: 'warning'
        }).then(() => {
          this.$prompt('', '审批意见', {
            inputErrorMessage: '请填写审批意见！',
            inputValidator: value => {
              if (!value) {
                return '请填写审批意见！'
              }
            }
          }).then(({ value }) => {
            this.loading = true
            taskRejectAPI({
              id: item.id,
              comment: value
            }).then(() => {
              this.loading = false
              this.$message.success('任务已拒绝，流程已回退到上一节点！')
              this.refresh()
            }).catch(() => {
              this.loading = false
            })
          })
        })
        return
      }
      this.action.type = 'update'
      this.action.detailData = item
      this.isCreate = true
    },
    onSearch(value) {
      this.search = value
      this.getList()
    },
    actionHandle(data) {
      this.$message.success(data.msg)
      this.refresh()
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
