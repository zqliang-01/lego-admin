<template>
  <div class="system-customer">
    <xr-header
      :icon-class="'double-gear'"
      :show-search="true"
      :show-save="manage.genTable.update"
      icon-color="#1CBAF5"
      label="模型管理"
      placeholder="请输入模型名称搜索"
      @search="onSearch"
      @create="onCreate"/>
    <div class="customer-content">
      <lego-table
        :loading="loading"
        :data-list="dataList"
        :field-list="fieldList"
        :current-page="currentPage"
        :page-size="pageSize"
        :total="total"
        :edit-button-width="300"
        @onList="getList"
        @onEdit="handleTable"
        @onClickField="handleField">
        <template slot-scope="scope">
          <el-button
            v-if="manage.workflow.model.update"
            type="text"
            size="small"
            icon="el-icon-edit"
            @click="handleTable('edit', scope.row, scope.$index)">编辑</el-button>
          <el-button
            v-if="manage.workflow.model.update"
            type="text"
            size="small"
            icon="el-icon-brush"
            @click="handleTable('design', scope.row, scope.$index)">设计流程</el-button>
          <el-button
            v-if="manage.workflow.model.deploy"
            type="text"
            size="small"
            icon="el-icon-video-play"
            @click="handleTable('depoly', scope.row, scope.$index)">部署</el-button>
          <el-button
            v-if="manage.workflow.model.delete"
            type="text"
            size="small"
            icon="el-icon-delete"
            @click="handleTable('delete', scope.row, scope.$index)">删除</el-button>
        </template>
      </lego-table>
    </div>
    <Create
      v-if="isCreate"
      :field-list="fieldList"
      :action="action"
      @handle="actionHandle"
      @close="isCreate = false"
    />
    <el-dialog title="流程信息" :visible.sync="processVisible" width="70%" append-to-body>
      <bpmn-viewer :key="modelId" :xml.sync="processXml" :style="{height: '400px'}"/>
    </el-dialog>
  </div>
</template>

<script>
import {
  modelListAPI,
  modelDeployAPI,
  modelDeleteAPI,
  modelBpmnXmlGetAPI
} from '@/api/admin/workflow/model'
import { mapGetters } from 'vuex'
import Create from './Create'
import XrHeader from '@/components/XrHeader'
import LegoTable from '@/components/LegoTable'
import FieldView from '@/components/NewCom/Form/FieldView'
import BpmnViewer from '@/components/bpmn/components/Viewer'

export default {
  name: 'WorkflowModel',
  components: {
    XrHeader,
    LegoTable,
    FieldView,
    Create,
    BpmnViewer
  },
  computed: {
    ...mapGetters(['manage'])
  },
  data() {
    return {
      loading: false,
      isCreate: false,
      processXml: '',
      processVisible: false,
      previewVisible: false,
      modelId: '',
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
          { fieldCode: 'id', name: '模型ID', formType: 'text', width: '260', unique: true, show: false },
          { fieldCode: 'key', name: '模型标识', formType: 'text', width: '150', required: true },
          { fieldCode: 'name', name: '名称', formType: 'text', width: '150', required: true, clickable: true }
        ],
        [
          { fieldCode: 'description', name: '备注', formType: 'text', width: '100', required: true },
          { fieldCode: 'version', name: '版本', formType: 'text', width: '100', required: true, show: false },
          { fieldCode: 'createTime', name: '创建时间', formType: 'text', width: '150', show: false }
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
      modelListAPI({
        name: this.search,
        pageIndex: this.currentPage,
        pageSize: this.pageSize
      }).then(res => {
        this.dataList = res.data.result
        this.total = res.data.totalCount
        this.currentPage = res.data.pageIndex
        this.loading = false
      })
        .catch(() => {
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
      if (type === 'design') {
        this.$router.push({
          name: 'modelDesign',
          params: {
            modelId: item.id
          }
        })
        return
      }
      if (type === 'depoly') {
        this.loading = true
        modelDeployAPI(item.id).then(() => {
          this.loading = true
          this.$message.success('部署成功！')
          this.refresh()
        }).catch(() => {
          this.loading = false
        })
        return
      }
      if (type === 'delete') {
        this.$confirm(`此操作将永久删除[${item.name}]的[${item.version}]版本，是否继续?`, '提示', {
          type: 'warning'
        }).then(() => {
          modelDeleteAPI(item.id).then(() => {
            this.$message.success('删除成功！')
            this.refresh()
          })
        })
        return
      }
      this.action.type = 'update'
      this.action.detailData = item
      this.isCreate = true
    },
    handleField(data, row) {
      if (data && data.field.fieldCode === 'name') {
        modelBpmnXmlGetAPI(row.id).then(res => {
          this.modelId = row.id
          this.processXml = res.data
          this.processVisible = true
        })
        return
      }
    },
    onSearch(value) {
      this.search = value
      this.getList()
    },
    onCreate() {
      this.action.type = 'save'
      this.action.detailData = {}
      this.isCreate = true
    },
    actionHandle(data) {
      if (data.type === 'save-success') {
        this.$message.success('模型保存成功！')
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
