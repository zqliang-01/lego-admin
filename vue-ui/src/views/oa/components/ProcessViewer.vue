<template>
  <div :loading="loading">
    <el-popover
      placement="top"
      title="操作记录"
      v-model="detailShow">
      <el-table :data="taskHisList" size="mini" border header-cell-class-name="table-header-gray">
        <el-table-column label="ID" header-align="center" align="center" type="index" width="25px" />
        <el-table-column label="名称" prop="name" width="100px" align="center"/>
        <el-table-column label="开始时间" prop="createTime" width="90px" align="center"/>
        <el-table-column label="办结时间" prop="endTime" width="90px" align="center"/>
        <el-table-column label="耗时" prop="duration" width="100px" align="center"/>
        <el-table-column label="受理人" prop="assignee.name" width="100px" align="center"/>
        <el-table-column label="操作" width="100px" align="center">
          <template slot-scope="scope">
            <el-button
              type="text"
              size="small"
              icon="el-icon-view"
              @click="handleTable('viewDetail', scope.row)">查看明细</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-popover>
    <bpmn-viewer
      :key="instanceId"
      :xml.sync="processXml"
      :process-node-info="processNodeInfo"
      @clickElement="handleElementClick"/>
    <task-detail
      v-if="createShow"
      :title="taskName"
      :task-id="taskId"
      :form-code="formCode"
      :detail-code="detailCode"
      :comment="comment"
      :action="{ type: 'view' }"
      @close="createShow = false"
    />
  </div>
</template>

<script>
import {
  instanceProcessNodeListAPI
} from '@/api/admin/workflow/instance'
import {
  taskHistoryListAPI,
  taskFormDetailGetAPI
} from '@/api/admin/workflow/task'
import BpmnViewer from '@/components/bpmn/components/Viewer'
import TaskDetail from './TaskDetail.vue'

export default {
  name: 'ProcessViewer',
  props: {
    instanceId: String
  },
  components: {
    TaskDetail,
    BpmnViewer
  },
  data() {
    return {
      loading: false,
      taskHisList: [],
      processXml: '',
      processNodeInfo: {},
      createShow: false,
      detailShow: false,
      taskId: '',
      taskName: '',
      formCode: '',
      detailCode: '',
      comment: ''
    }
  },
  mounted() {
    this.init()
  },
  methods: {
    init() {
      if (this.instanceId) {
        this.loading = true
        instanceProcessNodeListAPI(this.instanceId).then(res => {
          this.processXml = res.data.xml
          this.processNodeInfo = res.data
          this.loading = false
        }).catch(() => {
          this.loading = false
        })
      }
    },
    handleElementClick(taskKey) {
      taskHistoryListAPI({
        instanceId: this.instanceId,
        key: taskKey
      }).then(res => {
        this.taskHisList = res.data
        this.detailShow = this.taskHisList.length > 0
      })
    },
    handlePopoClose() {
      if (this.detailShow) {
        this.detailShow = false
      }
    },
    handleTable(type, row) {
      if (type === 'viewDetail') {
        taskFormDetailGetAPI(row.id).then(res => {
          this.taskId = row.id
          this.taskName = row.name
          if (res.data) {
            this.detailCode = res.data.code
            this.formCode = res.data.formKey
            this.comment = res.data.comment
          }
          this.createShow = true
        })
      }
    }
  }
}
</script>./TaskDetail.vue
