<template>
  <div v-loading="loading">
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
      :visible.sync="createShow"
      :task-id="taskId"
      :action="{ type: 'view' }"
      @close="createShow = false"
    />
    <start-detail
      :visible.sync="startVisible"
      :form-code="startFormCode"
      :process-name="processName"
      :detail-code="startDetailCode"
      :action="{type: 'view'}"
      @close="startVisible = false"
    />
  </div>
</template>

<script>
import {
  instanceGetFormAPI,
  instanceProcessNodeListAPI
} from '@/api/admin/workflow/instance'
import {
  taskHistoryListAPI
} from '@/api/admin/workflow/task'
import BpmnViewer from '@/components/Bpmn/components/Viewer'
import TaskDetail from './TaskDetail'
import StartDetail from './StartDetial'

export default {
  name: 'ProcessViewer',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    instanceId: String
  },
  components: {
    TaskDetail,
    BpmnViewer,
    StartDetail
  },
  data() {
    return {
      loading: false,
      startVisible: false,
      startDetailData: {},
      startFormCode: '',
      startDetailCode: '',
      processName: '',
      taskHisList: [],
      processXml: '',
      processNodeInfo: {},
      createShow: false,
      detailShow: false,
      taskId: ''
    }
  },
  watch: {
    visible(val) {
      if (val) {
        this.init()
      }
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
          this.processName = res.data.name
          this.processXml = res.data.xml
          this.processNodeInfo = res.data
          this.loading = false
        }).catch(() => {
          this.loading = false
        })
      }
    },
    handleElementClick(taskKey, taskType) {
      if (taskType === 'bpmn:StartEvent') {
        instanceGetFormAPI(this.instanceId).then(res => {
          if (res.data.code) {
            this.startFormCode = res.data.formKey
            this.startDetailCode = res.data.code
            this.startVisible = true
          }
        })
        this.detailShow = false
        return
      }
      if (taskType === 'bpmn:UserTask') {
        taskHistoryListAPI({
          instanceId: this.instanceId,
          key: taskKey
        }).then(res => {
          this.taskHisList = res.data
          this.detailShow = this.taskHisList.length > 0
        })
        return
      }
      this.detailShow = false
    },
    handlePopoClose() {
      if (this.detailShow) {
        this.detailShow = false
      }
    },
    handleTable(type, row) {
      if (type === 'viewDetail') {
        this.taskId = row.id
        this.createShow = true
      }
    }
  }
}
</script>
