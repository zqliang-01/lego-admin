<template>
  <div class="system-customer">
    <xr-header
      icon-class="icon-full-setting"
      :show-search="true"
      :show-save="auth.add"
      icon-color="#1CBAF5"
      label="任务管理"
      placeholder="请输入任务描述"
      @search="onSearch"
      @create="onCreate" />
    <div class="customer-content">
      <flexbox class="main-table-header">
        <el-select
          v-model="searchForm.jobGroup"
          clearable
          filterable
          placeholder="选择执行器"
          class="search-item"
          @change="handleSearchChange">
          <el-option
            v-for="(item, index) in executorList"
            :key="index"
            :label="item.name "
            :value="item.code"/>
        </el-select>
        <el-select
          v-model="searchForm.triggerStatus"
          placeholder="选择状态"
          class="search-item"
          @change="handleSearchChange">
          <el-option
            v-for="(item, index) in statusList"
            :key="index"
            :label="item.name "
            :value="item.code"/>
        </el-select>
        <el-input
          v-model="searchForm.executorHandler"
          resize="none"
          class="search-item"
          placeholder="请输入运行对象" />
        <el-input
          v-model="searchForm.author"
          resize="none"
          class="search-item"
          placeholder="请输入负责人" />
      </flexbox>
      <lego-table
        :loading="loading"
        :data-list="dataList"
        :field-list="fieldList"
        :current-page="currentPage"
        :page-size="pageSize"
        :total="total"
        :table-heigh-overly="-50"
        :edit-button-width="120"
        @onList="getList"
        @onEdit="handleTable">
        <template slot-scope="{ row }">
          <el-dropdown
            size="mini"
            split-button type="primary"
            @command="handleTable($event, row)">
            操作
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item v-if="auth.update" command="trigger">执行一次</el-dropdown-item>
              <el-dropdown-item v-if="authLog.read" command="log">查询日志</el-dropdown-item>
              <el-dropdown-item command="regist">注册节点</el-dropdown-item>
              <el-dropdown-item command="nextTriggerTime">下次执行时间</el-dropdown-item>
              <el-dropdown-item v-if="auth.update && row.glueType !== 'BEAN'" divided command="shell">编辑脚本</el-dropdown-item>
              <el-dropdown-item v-if="auth.update && row.triggerStatus === 0" divided command="start">启动</el-dropdown-item>
              <el-dropdown-item v-if="auth.update && row.triggerStatus !== 0" divided command="stop">停止</el-dropdown-item>
              <el-dropdown-item v-if="auth.update" command="edit">编辑</el-dropdown-item>
              <el-dropdown-item v-if="auth.delete" command="delete">删除</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </template>
      </lego-table>
    </div>

    <Create
      :visible="showDetail"
      :action="action"
      :executor-code="searchForm.jobGroup"
      @handle="actionHandle"
      @close="showDetail = false"
    />

    <el-dialog
      :visible.sync="showTrigger"
      append-to-body
      title="执行一次"
      width="500px">
      <flexbox class="nav-dialog-div">
        <label>任务参数</label>
        <el-input
          v-model="triggerParam"
          type="textarea"
          :rows="2"
          placeholder="请输入任务参数" />
      </flexbox>
      <flexbox class="nav-dialog-div">
        <label>机器地址</label>
        <el-input
          v-model="triggerAddress"
          type="textarea"
          :rows="2"
          placeholder="请输入本次执行的机器地址，为空则从执行器获取" />
      </flexbox>
      <span slot="footer" class="dialog-footer">
        <el-button @click="showTrigger = false">取 消</el-button>
        <el-button type="primary" @click="handleTrigger">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  taskListAPI,
  conditionListAPI,
  taskDeleteAPI,
  taskStopAPI,
  taskStartAPI,
  taskTriggerAPI,
  taskNextTriggerTimeAPI
} from '@/api/job/task'
import { executorGetAPI } from '@/api/job/executor'
import { mapGetters } from 'vuex'
import XrHeader from '@/components/XrHeader'
import LegoTable from '@/components/Lego/LegoTable'
import Create from './Create'

export default {
  name: 'JobTask',
  components: {
    XrHeader,
    LegoTable,
    Create
  },
  computed: {
    ...mapGetters(['allAuth']),
    auth() {
      if (this.allAuth.job && this.allAuth.job.task) {
        return this.allAuth.job.task
      }
      return {}
    },
    authLog() {
      if (this.allAuth.job && this.allAuth.job.log) {
        return this.allAuth.job.log
      }
      return {}
    }
  },
  data() {
    return {
      loading: false,
      showDetail: false,
      showTrigger: false,
      triggerParam: '',
      triggerAddress: '',
      taskDetail: '',
      searchForm: {
        jobGroup: '',
        jobDesc: '',
        triggerStatus: -1
      },
      executorList: [],
      statusList: [
        { code: -1, name: '全部' },
        { code: 1, name: '生效' },
        { code: 0, name: '失效' }
      ],
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
          { fieldCode: 'id', name: '任务ID', formType: 'text', width: '50' },
          { fieldCode: 'jobDesc', name: '任务描述', formType: 'text', width: '150' }
        ],
        [
          { fieldCode: 'scheduleType', name: '调度类型', formType: 'text', width: '80' },
          { fieldCode: 'scheduleConf', name: '调度周期', formType: 'text', width: '150' }
        ],
        [
          { fieldCode: 'glueType', name: '运行模式', formType: 'text', width: '120' },
          { fieldCode: 'executorHandler', name: '运行对象', formType: 'text', width: '150' }
        ],
        [
          { fieldCode: 'author', name: '负责人', formType: 'user', width: '100' },
          { fieldCode: 'triggerStatus', name: '状态', activeValue: 1, inactiveValue: 0, formType: 'boolean', width: '80' }
        ]
      ]
    }
  },
  created() {
    conditionListAPI().then(res => {
      this.executorList = res.data.jobGroupList.map(item => {
        return {
          code: item.id,
          name: item.title
        }
      })
      if (this.executorList.length > 0) {
        this.searchForm.jobGroup = this.executorList[0].code
      }
      this.refresh()
    })
  },
  methods: {
    refresh() {
      this.getList()
    },
    getList(pageSize = this.pageSize, currentPage = this.currentPage) {
      this.loading = true
      this.searchForm.pageSize = pageSize
      this.searchForm.pageIndex = currentPage
      taskListAPI(this.searchForm).then(res => {
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
    handleTable(type, item) {
      if (type === 'trigger') {
        this.showTrigger = true
        this.triggerParam = ''
        this.triggerAddress = ''
        this.taskDetail = item
        return
      }
      if (type === 'log') {
        this.$router.push({
          name: 'jobLogList',
          params: {
            jobId: item.id,
            executorId: item.jobGroup
          }
        })
        return
      }
      if (type === 'shell') {
        this.$router.push({
          name: 'jobTaskScript',
          params: {
            jobId: item.id
          }
        })
        return
      }
      if (type === 'regist') {
        this.loading = true
        executorGetAPI(item.jobGroup).then(res => {
          this.loading = false
          var html = '<div>'
          res.data.registryList.forEach((item, i) => {
            html += (i + 1) + '. <span>' + item + '</span><br>'
          })
          html += '</div>'
          this.$alert(html, '已注册节点', {
            dangerouslyUseHTMLString: true
          })
        }).catch(() => {
          this.loading = false
        })
        return
      }
      if (type === 'nextTriggerTime') {
        this.loading = true
        taskNextTriggerTimeAPI({
          scheduleType: item.scheduleType,
          scheduleConf: item.scheduleConf
        }).then(res => {
          this.loading = false
          var html = '<div>'
          res.data.forEach(item => {
            html += '<span>' + item + '</span><br>'
          })
          html += '</div>'
          this.$alert(html, '下次执行时间', {
            dangerouslyUseHTMLString: true
          })
        }).catch(() => {
          this.loading = false
        })
        return
      }
      if (type === 'start') {
        this.$confirm(`请确认启动【${item.jobDesc}】，是否继续?`, '提示', {
          type: 'warning'
        }).then(() => {
          this.loading = true
          taskStartAPI(item.id).then(() => {
            this.loading = false
            this.$message.success('启动成功！')
            this.getList()
          }).catch(() => {
            this.loading = false
          })
        })
        return
      }
      if (type === 'stop') {
        this.$confirm(`请确认停止【${item.jobDesc}】，是否继续?`, '提示', {
          type: 'warning'
        }).then(() => {
          this.loading = true
          taskStopAPI(item.id).then(() => {
            this.loading = false
            this.$message.success('停止成功！')
            this.getList()
          }).catch(() => {
            this.loading = false
          })
        })
        return
      }
      if (type === 'edit') {
        this.action.type = 'update'
        this.action.time = new Date().getTime()
        this.action.detailData = item
        this.showDetail = true
        return
      }
      if (type === 'delete') {
        this.$confirm(`请确认删除【${item.jobDesc}】，是否继续?`, '提示', {
          type: 'warning'
        }).then(() => {
          this.loading = true
          taskDeleteAPI(item.id).then(() => {
            this.loading = false
            this.$message.success('删除成功！')
            this.getList()
          }).catch(() => {
            this.loading = false
          })
        })
        return
      }
    },
    handleTrigger() {
      taskTriggerAPI({
        id: this.taskDetail.id,
        executorParam: this.triggerParam,
        addressList: this.triggerAddress
      }).then(() => {
        this.loading = false
        this.showTrigger = false
        this.$message.success('触发成功，执行结果请查看日志！')
      }).catch(() => {
        this.loading = false
      })
    },
    handleSearchChange() {
      this.getList()
    },
    onCreate() {
      this.action.type = 'save'
      this.action.detailData = {}
      this.showDetail = true
    },
    onSearch(value) {
      this.searchForm.jobDesc = value
      this.getList()
    },
    actionHandle(data) {
      if (data.type === 'save-success') {
        this.getList()
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

.nav-dialog-div {
  margin-bottom: 20px;
  label {
    width: 90px;
    display: block;
  }
  .el-input,
  .el-select,
  .user-select {
    flex: 1;
  }
}
.main-table-header {
  height: 50px;
  background-color: white;
  position: relative;

  .el-date-editor--daterange {
    margin: 0 20px;
  }
  .search-item {
    margin-left: 15px;
    max-width: 200px;
  }
}
</style>
