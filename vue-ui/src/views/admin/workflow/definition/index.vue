<template>
  <div class="system-customer">
    <xr-header
      :icon-class="'double-gear'"
      :show-search="true"
      :show-save="false"
      icon-color="#1CBAF5"
      label="部署管理"
      placeholder="请输入部署名称搜索"
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
        @onEdit="handleTable"
        @onClickField="handleField">
        <template slot-scope="scope">
          <el-button
            v-if="manage.workflow.definition.update"
            type="text"
            size="small"
            icon="el-icon-price-tag"
            @click="handleTable('version', scope.row, scope.$index)">版本管理</el-button>
          <el-button
            v-if="manage.workflow.definition.delete"
            type="text"
            size="small"
            icon="el-icon-delete"
            @click="handleTable('delete', scope.row, scope.$index)">删除</el-button>
        </template>
      </lego-table>
    </div>

    <version-dialog
      :visible.sync="versionVisible"
      :definition-key="definitionKey"
      @save-success="handleSaveSuccess"
    />
    <el-dialog title="流程信息" :visible.sync="processVisible" width="70%" append-to-body>
      <bpmn-viewer :key="definitionKey" :xml.sync="definitionXml"/>
    </el-dialog>
  </div>
</template>

<script>
import {
  definitionListAPI,
  definitionDeleteAPI,
  definitionStartAPI,
  definitionBpmnXmlGetAPI
} from '@/api/admin/workflow/definition'
import { mapGetters } from 'vuex'
import XrHeader from '@/components/XrHeader'
import LegoTable from '@/components/Lego/LegoTable'
import VersionDialog from './VersionDialog'
import BpmnViewer from '@/components/Bpmn/components/Viewer'

export default {
  name: 'WorkflowDefinition',
  components: {
    XrHeader,
    VersionDialog,
    LegoTable,
    BpmnViewer
  },
  computed: {
    ...mapGetters(['manage'])
  },
  data() {
    return {
      loading: false,
      versionVisible: false,
      processVisible: false,
      definitionXml: '',
      definitionKey: '',
      dataList: [],
      currentPage: 1,
      pageSize: 15,
      total: 0,
      search: '',
      fieldList: [
        [
          { fieldCode: 'key', name: '模型标识', formType: 'text', width: '150' },
          { fieldCode: 'name', name: '名称', formType: 'text', width: '150', clickable: true }
        ],
        [
          { fieldCode: 'deploymentTime', name: '发布时间', formType: 'text', width: '150', editable: false },
          { fieldCode: 'version', name: '版本', formType: 'text', width: '100', editable: false },
          { fieldCode: 'active', name: '是否激活', formType: 'boolean_value', width: '100', editable: false }
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
      definitionListAPI({
        name: this.search,
        pageIndex: currentPage,
        pageSize: pageSize
      }).then(res => {
        this.dataList = res.data.result
        this.total = res.data.totalCount
        this.currentPage = res.data.pageIndex
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    handleTable(type, item, index) {
      if (type === 'version') {
        this.definitionKey = item.key
        this.versionVisible = true
        return
      }
      if (type === 'delete') {
        this.$confirm(`此操作将永久删除[${item.name}]的[${item.version}]版本，是否继续?`, '提示', {
          type: 'warning'
        }).then(() => {
          this.loading = true
          definitionDeleteAPI(item.id).then(() => {
            this.loading = false
            this.$message.success('删除成功！')
            this.getList()
          }).catch(() => {
            this.loading = false
          })
        })
        return
      }
      if (type === 'start') {
        this.loading = true
        definitionStartAPI({
          definitionId: item.id
        }).then(() => {
          this.loading = false
          this.$message.success('任务发布成功！')
          this.getList()
        }).catch(() => {
          this.loading = false
        })
        return
      }
    },
    handleField(data, row) {
      if (data && data.field.fieldCode === 'name') {
        definitionBpmnXmlGetAPI(row.id).then(res => {
          this.definitionKey = row.key
          this.definitionXml = res.data
          this.processVisible = true
        })
        return
      }
    },
    onSearch(value) {
      this.search = value
      this.getList()
    },
    handleSaveSuccess() {
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
</style>
