<template>
  <div class="system-customer">
    <xr-header
      :show-search="true"
      icon-class="icon-task-state"
      icon-color="#1CBAF5"
      label="已办任务"
      placeholder="请输入任务名称搜索"
      @search="onSearch" />
    <div class="customer-content">
      <lego-table
        :loading="loading"
        :data-list="dataList"
        :field-list="fieldList"
        :current-page="currentPage"
        :page-size="pageSize"
        :total="total"
        @onList="getList"
        @onEdit="handleTable">
        <template slot-scope="scope">
          <el-button
            type="text"
            size="small"
            icon="el-icon-view"
            @click="handleTable('view', scope.row, scope.$index)">流程图</el-button>
          <el-button
            v-if="scope.row.formCode !== ''"
            type="text"
            size="small"
            icon="el-icon-view"
            @click="handleTable('form', scope.row, scope.$index)">表单</el-button>
        </template>
      </lego-table>
    </div>
    <el-dialog title="流程信息" :visible.sync="processVisible" width="70%" append-to-body>
      <process-viewer
        :key="taskId"
        :instance-id="instanceId" />
    </el-dialog>
    <task-detail
      v-if="createShow"
      title="任务表单"
      :task-id="taskId"
      :action="{ type: 'view' }"
      @close="createShow = false"
      @handle="actionHandle"
    />
  </div>
</template>

<script>
import {
  taskCompletedListAPI
} from '@/api/admin/workflow/task'
import { mapGetters } from 'vuex'
import XrHeader from '@/components/XrHeader'
import LegoTable from '@/components/lego/LegoTable'
import FieldView from '@/components/NewCom/Form/FieldView'
import BpmnViewer from '@/components/bpmn/components/Viewer'
import TaskDetail from '../components/TaskDetail.vue'
import ProcessViewer from '../components/ProcessViewer'

export default {
  name: 'WorkflowModel',
  components: {
    XrHeader,
    FieldView,
    LegoTable,
    BpmnViewer,
    TaskDetail,
    ProcessViewer
  },
  computed: {
    ...mapGetters(['manage'])
  },
  data() {
    return {
      loading: false,
      isCreate: false,
      taskId: '',
      instanceId: '',
      processVisible: false,
      createShow: false,
      formCode: '',
      detailCode: '',
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
          { fieldCode: 'name', name: '任务节点', formType: 'text', width: 150 },
          { fieldCode: 'assignee', name: '受理人', formType: 'select', width: 100 }
        ],
        [
          { fieldCode: 'createTime', name: '开始时间', formType: 'text', width: 150 },
          { fieldCode: 'endTime', name: '完工时间', formType: 'text', width: 150 }
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
      taskCompletedListAPI({
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
      if (type === 'form') {
        this.taskId = item.id
        this.createShow = true
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
