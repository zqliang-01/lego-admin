<template>
  <el-dialog
    title="部署版本信息"
    :visible="visible"
    :close-on-click-modal="false"
    :modal-append-to-body="true"
    :append-to-body="true"
    width="60%"
    @close="handleCancel">
    <el-table
      v-loading="loading"
      :data="dataList"
      :height="tableHeight"
      highlight-current-row
      style="width: 100%">
      <el-table-column
        v-for="(item, index) in fieldList"
        :key="index"
        :min-width="item.width"
        :prop="item.fieldCode"
        :label="item.name"
        show-overflow-tooltip>
        <template slot-scope="scope">
          <field-view
            :props="item"
            :form-type="item.formType"
            :value="scope.row[item.fieldCode]"
            @clickValue="handleField($event, scope.row, scope.$index)">
          </field-view>
        </template>
      </el-table-column>
      <el-table-column
        fixed="right"
        label="操作"
        width="150">
        <template slot-scope="scope">
          <el-button
            v-if="manage.workflow.definition.update"
            type="text"
            size="small"
            @click="handleTable('edit', scope.row, scope.$index)">{{ scope.row.active | statusName }}</el-button>
          <el-button
            v-if="manage.workflow.definition.delete"
            type="text"
            size="small"
            icon="el-icon-delete"
            @click="handleTable('delete', scope.row, scope.$index)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="p-contianer">
      <el-pagination
        :current-page="currentPage"
        :page-sizes="pageSizes"
        :page-size.sync="pageSize"
        :total="total"
        :pager-count="5"
        class="p-bar"
        background
        layout="prev, pager, next, sizes, total, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"/>
    </div>
    <el-dialog title="流程信息" :visible.sync="processVisible" width="70%" append-to-body>
      <bpmn-viewer :key="definitionId" :xml.sync="definitionXml" :style="{height: '400px'}"/>
    </el-dialog>
  </el-dialog>
</template>
<script>
import {
  definitionDeleteAPI,
  definitionActiveAPI,
  definitionSuspendAPI,
  definitionHistoryListAPI,
  definitionBpmnXmlGetAPI
} from '@/api/admin/workflow/definition'
import FieldView from '@/components/NewCom/Form/FieldView'
import BpmnViewer from '@/components/bpmn/components/Viewer'
import { mapGetters } from 'vuex'

export default {
  name: 'DefinitionVersionDialog',
  components: {
    FieldView,
    BpmnViewer
  },
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    definitionKey: String
  },
  data() {
    return {
      loading: false,
      processVisible: false,
      definitionXml: '',
      definitionId: '',
      dataList: [],
      currentPage: 1,
      pageSize: 15,
      pageSizes: [15, 30, 60, 100],
      total: 0,
      tableHeight: document.documentElement.clientHeight - 300, // 表的高度
      fieldList: [
        { fieldCode: 'name', name: '名称', formType: 'text', width: '180', required: true, clickable: true, xAxis: 0, yAxis: 1 },
        { fieldCode: 'key', name: '模型标识', formType: 'text', width: '150', required: true, xAxis: 1, yAxis: 0 },
        { fieldCode: 'version', name: '版本', formType: 'text', width: '50', required: true, editable: false },
        { fieldCode: 'active', name: '是否激活', formType: 'boolean_value', width: '100', editable: false }
      ]
    }
  },
  computed: {
    ...mapGetters(['manage'])
  },
  filters: {
    statusName(status) {
      return status ? '挂起' : '激活'
    }
  },
  watch: {
    definitionKey() {
      this.refresh()
    }
  },
  created() {
    window.onresize = () => {
      self.tableHeight = document.documentElement.clientHeight - 140
    }
    this.refresh()
  },
  methods: {
    handleCancel() {
      this.$emit('update:visible', false)
    },
    refresh() {
      this.loading = true
      definitionHistoryListAPI({
        key: this.definitionKey,
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
      this.refresh()
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this.refresh()
    },
    handleTable(type, item, index) {
      if (type === 'edit') {
        this.loading = true
        if (item.active) {
          definitionSuspendAPI(item.id).then(() => {
            this.loading = false
            this.$message.success('挂起成功！')
            this.refresh()
            this.$emit('save-success')
          }).catch(() => {
            this.loading = false
          })
        } else {
          definitionActiveAPI(item.id).then(() => {
            this.loading = false
            this.$message.success('激活成功！')
            this.refresh()
            this.$emit('save-success')
          }).catch(() => {
            this.loading = false
          })
        }
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
            this.refresh()
            this.$emit('save-success')
          }).catch(() => {
            this.loading = false
          })
        })
        return
      }
    },
    handleField(data, row) {
      if (data && data.field.fieldCode === 'name') {
        definitionBpmnXmlGetAPI(row.id).then(res => {
          this.definitionId = row.id
          this.definitionXml = res.data
          this.processVisible = true
        })
        return
      }
    }
  }
}
</script>
<style lang="scss" scoped>
</style>
