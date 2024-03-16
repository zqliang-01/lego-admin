<template>
  <div class="system-customer">
    <xr-header
      :show-search="true"
      icon-class="my-task"
      icon-color="#1CBAF5"
      label="我的流程"
      placeholder="请输入流程名称搜索"
      @search="onSearch"/>
    <div class="customer-content">
      <lego-table
        :loading="loading"
        :data-list="dataList"
        :field-list="fieldList"
        :current-page="currentPage"
        :page-size="pageSize"
        :total="total"
        :edit-button-width="100"
        @onList="getList"
        @onClickField="handleField">
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.status.code === 'running'"
            type="text"
            size="small"
            icon="el-icon-edit"
            @click="handleTable('stop', scope.row, scope.$index)">终止</el-button>
        </template>
      </lego-table>
    </div>
    <el-dialog title="流程信息" :visible.sync="processVisible" width="70%" append-to-body>
      <process-viewer
        :key="instanceId"
        :instance-id="instanceId" />
    </el-dialog>
  </div>
</template>

<script>
import {
  instanceListAPI,
  instanceStopAPI
} from '@/api/admin/workflow/instance'
import { mapGetters } from 'vuex'
import XrHeader from '@/components/XrHeader'
import LegoTable from '@/components/LegoTable'
import FieldView from '@/components/NewCom/Form/FieldView'
import ProcessViewer from '../components/ProcessViewer'

export default {
  name: 'WorkflowInstance',
  components: {
    XrHeader,
    LegoTable,
    FieldView,
    ProcessViewer
  },
  computed: {
    ...mapGetters(['manage'])
  },
  data() {
    return {
      loading: false,
      processVisible: false,
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
          { fieldCode: 'name', name: '流程名称', formType: 'text', width: '150', clickable: true }
        ],
        [
          { fieldCode: 'status', name: '状态', formType: 'select', width: '100' },
          { fieldCode: 'startUser', name: '发起人', formType: 'select', width: '100' }
        ],
        [
          { fieldCode: 'startTime', name: '发起时间', formType: 'text', width: '150' },
          { fieldCode: 'endTime', name: '结束时间', formType: 'text', width: '150' }
        ],
        [
          { fieldCode: 'version', name: '流程版本', formType: 'text', width: '100' },
          { fieldCode: 'duration', name: '耗时', formType: 'text', width: '150' }
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
      instanceListAPI({
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
    handleSizeChange(val) {
      this.pageSize = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this.getList()
    },
    handleField(data, row) {
      if (data && data.field.fieldCode === 'name') {
        this.instanceId = row.id
        this.processVisible = true
        return
      }
    },
    handleTable(type, item) {
      if (type === 'stop') {
        this.$confirm('此操作将取消流程【' + item.name + '】，是否继续?', '提示', {
          type: 'warning'
        }).then(() => {
          this.loading = true
          instanceStopAPI(item.id).then(() => {
            this.$message.success('流程终止成功！')
            this.loading = false
            this.getList()
          }).catch(() => {
            this.loading = false
          })
        })
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
</style>
