<template>
  <div class="system-customer">
    <xr-header
      :icon-class="'double-gear'"
      :show-search="true"
      icon-color="#1CBAF5"
      label="模型管理"
      placeholder="请输入模型名称搜索"
      @search="onSearch"/>
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
            @click="handleTable('view', scope.row, scope.$index)">查看流程进度</el-button>
          <el-button
            type="text"
            size="small"
            icon="el-icon-edit"
            @click="handleTable('complete', scope.row, scope.$index)">完工</el-button>
        </template>
      </lego-table>
    </div>
    <el-dialog title="流程信息" :visible.sync="processVisible" width="70%" append-to-body>
      <bpmn-viewer
        :key="taskId"
        :xml.sync="processXml"
        :process-node-info="processNodeInfo"
        :style="{height: '400px'}"/>
    </el-dialog>
    <complete-form
      v-if="createShow"
      title="任务表单"
      :task-id="taskId"
      :form-code="formCode"
      :action="{ type: 'save' }"
      @close="createShow = false"
      @handle="actionHandle"
    />
  </div>
</template>

<script>
import {
  taskUndoListAPI,
  taskProcessNodeListAPI
} from '@/api/admin/workflow/task'
import { mapGetters } from 'vuex'
import XrHeader from '@/components/XrHeader'
import LegoTable from '@/components/LegoTable'
import FieldView from '@/components/NewCom/Form/FieldView'
import BpmnViewer from '@/components/bpmn/components/Viewer'
import CompleteForm from '../CompleteForm.vue'

export default {
  name: 'WorkflowModel',
  components: {
    XrHeader,
    FieldView,
    LegoTable,
    BpmnViewer,
    CompleteForm
  },
  computed: {
    ...mapGetters(['manage'])
  },
  data() {
    return {
      loading: false,
      isCreate: false,
      processXml: '',
      processNodeInfo: '',
      processVisible: false,
      createShow: false,
      formCode: '',
      taskId: '',
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
          { fieldCode: 'id', name: '任务ID', formType: 'text', width: '260', unique: true, required: true },
          { fieldCode: 'name', name: '名称', formType: 'text', width: '150', required: true }
        ],
        [
          { fieldCode: 'createTime', name: '开始时间', formType: 'text', width: '150' },
          { fieldCode: 'assignee', name: '受理人', formType: 'text', width: '100', required: true }
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
    getList() {
      this.loading = true
      taskUndoListAPI({
        instanceId: this.search,
        pageIndex: this.currentPage,
        pageSize: this.pageSize
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
        this.loading = true
        taskProcessNodeListAPI(item.instanceId).then(res => {
          this.taskId = item.id
          this.processXml = res.data.xml
          this.processNodeInfo = res.data
          this.processVisible = true
          this.loading = false
        }).catch(() => {
          this.loading = false
        })
        return
      }
      if (type === 'complete') {
        this.taskId = item.id
        this.formCode = item.formCode
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
      if (data.type === 'save-success') {
        this.$message.success('任务已完工完成！')
        this.refresh()
      }
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
</style>
