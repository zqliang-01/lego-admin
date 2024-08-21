<template>
  <div class="system-customer">
    <xr-header
      icon-class="plan"
      :show-search="true"
      icon-color="#1CBAF5"
      label="日志管理"
      placeholder="请输入操作内容"
      @search="onSearch">
      <template v-if="auth.delete" v-slot:ft>
      <el-button
        class="xr-btn--orange"
        icon="el-icon-delete"
        type="primary"
        @click="handleFieldClick(true)">
      清理
    </el-button>
      </template>
    </xr-header>
    <div class="customer-content">
      <flexbox class="main-table-header">
        <el-select
          v-model="executorCode"
          clearable
          filterable
          placeholder="选择执行器"
          class="search-item"
          @change="handleExecutorChange">
          <el-option
            v-for="(item, index) in executorList"
            :key="index"
            :label="item.name "
            :value="item.code"/>
        </el-select>
        <el-select
          v-model="taskCode"
          clearable
          filterable
          placeholder="选择任务"
          class="search-item"
          @change="refresh">
          <el-option
            v-for="(item, index) in taskList"
            :key="index"
            :label="item.name "
            :value="item.code"/>
        </el-select>
        <el-select
          v-model="logStatus"
          clearable
          filterable
          placeholder="选择状态"
          class="search-item"
          @change="refresh">
          <el-option
            v-for="(item, index) in logStatusList"
            :key="index"
            :label="item.name "
            :value="item.code"/>
        </el-select>
        <el-date-picker
          v-model="dateTime"
          type="datetimerange"
          value-format="yyyy-MM-dd HH:mm:ss"
          range-separator="至"
          start-placeholder="开始时间"
          end-placeholder="结束时间"
          class="search-item"
          @change="refresh" />
      </flexbox>
      <lego-table
        :loading="loading"
        :data-list="dataList"
        :field-list="fieldList"
        :current-page="currentPage"
        :page-size="pageSize"
        :total="total"
        :table-heigh-overly="-50"
        :edit-button-width="150"
        @onList="getList">
        <template v-slot:column="{ row, item }">
          <field-view
            :item="item"
            :form-type="item.formType"
            :value="handleDisplayValue(row, item)"
            @clickValue="handleFieldClick(false, row[item.fieldCode], row)" />
        </template>
        <template slot-scope="{ row }">
          <el-button
            type="text"
            size="small"
            icon="el-icon-view"
            v-if="row.triggerCode === 200"
            @click="handleTable('detail', row)">详情</el-button>
          <el-button
            type="text"
            size="small"
            icon="el-icon-s-release"
            v-if="auth.delete && row.triggerCode === 200 && row.handleCode === 0"
            @click="handleTable('kill', row)">终止任务</el-button>
        </template>
      </lego-table>
    </div>
    <el-dialog
      :title="isClear ? '日志清理' : '详细信息'"
      :visible.sync="showDetail"
      :width="isClear ? '400px' : '50%'">
      <div v-if="isClear">
        <flexbox class="nav-dialog-div">
          <label>清理方式：</label>
          <el-select
            v-model="clearType"
            style="width: 70%"
            placeholder="选择日志清理方式">
            <el-option label="清理一个月之前日志数据" :value="1"/>
            <el-option label="清理三个月之前日志数据" :value="2"/>
            <el-option label="清理六个月之前日志数据" :value="3"/>
            <el-option label="清理一年之前日志数据" :value="4"/>
            <el-option label="清理一千条以前日志数据" :value="5"/>
            <el-option label="清理一万条以前日志数据" :value="6"/>
            <el-option label="清理三万条以前日志数据" :value="7"/>
            <el-option label="清理十万条以前日志数据" :value="8"/>
            <el-option label="清理所有日志数据" :value="9"/>
          </el-select>
        </flexbox>
      </div>
      <div v-else class="detail-content" v-html="showValue"/>
      <span
        v-if="isClear"
        slot="footer"
        class="dialog-footer">
        <el-button type="primary" @click="handleClear">保 存</el-button>
        <el-button @click="showDetail = !showDetail">取 消</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  logListAPI,
  conditionListAPI,
  taskListAPI,
  logClearAPI,
  logKillAPI
} from '@/api/job/log'
import { mapGetters } from 'vuex'
import XrHeader from '@/components/XrHeader'
import LegoTable from '@/components/Lego/LegoTable'
import FieldView from '@/components/Common/Form/FieldView'

export default {
  name: 'WorkflowModel',
  components: {
    XrHeader,
    LegoTable,
    FieldView
  },
  computed: {
    ...mapGetters(['allAuth']),
    auth() {
      if (this.allAuth.job && this.allAuth.job.log) {
        return this.allAuth.job.log
      }
      return {}
    },
    filterTime() {
      if (this.dateTime.length > 0) {
        return this.dateTime[0] + ' - ' + this.dateTime[1]
      }
      return ''
    }
  },
  data() {
    return {
      loading: false,
      showDetail: false,
      showValue: '',
      isClear: false,
      clearType: 1,
      dateTime: [],
      executorCode: '',
      executorList: [],
      logStatus: -1,
      logStatusList: [
        { code: -1, name: '全部' },
        { code: 1, name: '成功' },
        { code: 2, name: '失败' },
        { code: 3, name: '进行中' }
      ],
      taskCode: '',
      taskList: [],
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
          { fieldCode: 'jobId', name: '任务ID', formType: 'text', width: '50' },
          { fieldCode: 'triggerTime', name: '调度时间', formType: 'datetime', width: '100' }
        ],
        [
          { fieldCode: 'triggerCode', name: '调度结果', formType: 'text', width: '50' },
          { fieldCode: 'triggerMsg', name: '调度备注', formType: 'text', width: '50', clickable: true }
        ],
        [
          { fieldCode: 'handleTime', name: '执行时间', formType: 'datetime', width: '100' },
          { fieldCode: 'handleCode', name: '执行结果', formType: 'text', width: '50' }
        ],
        [
          { fieldCode: 'handleMsg', name: '执行备注', formType: 'text', width: '50', clickable: true }
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
    })
    if (this.$route.params.executorId) {
      this.executorCode = this.$route.params.executorId
      taskListAPI({ jobGroup: this.executorCode }).then(res => {
        this.taskList = res.data.map(item => {
          return {
            code: item.id,
            name: item.jobDesc
          }
        })
        this.taskCode = this.$route.params.jobId
        this.refresh()
      })
    } else {
      this.refresh()
    }
  },
  methods: {
    refresh() {
      this.getList()
    },
    getList(pageSize = this.pageSize, currentPage = this.currentPage) {
      this.loading = true
      const param = {
        description: this.search,
        filterTime: this.filterTime,
        jobGroup: this.executorCode,
        jobId: this.taskCode,
        logStatus: this.logStatus,
        pageSize: pageSize,
        pageIndex: currentPage
      }
      logListAPI(param).then(res => {
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
      if (type === 'detail') {
        this.$router.push({
          name: 'jobLogDetail',
          params: { logId: item.id }
        })
      }
      if (type === 'kill') {
        this.loading = true
        logKillAPI(item.id).then(() => {
          this.loading = false
          this.$message.success('任务终止成功！')
          this.refresh()
        }).catch(() => {
          this.loading = false
        })
      }
    },
    handleDisplayValue(row, item) {
      if (['triggerMsg', 'handleMsg'].includes(item.fieldCode)) {
        if (row[item.fieldCode]) {
          return '查看'
        }
      }
      if (['triggerCode', 'handleCode'].includes(item.fieldCode)) {
        if (row[item.fieldCode] === 200) {
          return '成功'
        }
        if (row[item.fieldCode] !== 0) {
          return '失败'
        }
        return ''
      }
      return row[item.fieldCode]
    },
    handleClear() {
      this.loading = true
      logClearAPI({
        jobGroup: 0,
        jobId: 0,
        type: this.clearType
      }).then(() => {
        this.loading = false
        this.showDetail = false
        this.$message.success('清理成功！')
        this.refresh()
      }).catch(() => {
        this.loading = false
      })
    },
    handleFieldClick(isClear, data) {
      this.showValue = data
      this.showDetail = true
      this.isClear = isClear
    },
    handleExecutorChange(val) {
      this.taskCode = ''
      if (val) {
        taskListAPI({ jobGroup: val }).then(res => {
          this.taskList = res.data.map(item => {
            return {
              code: item.id,
              name: item.jobDesc
            }
          })
        })
      } else {
        this.taskList = []
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

.main-table-header {
  height: 50px;
  background-color: white;
  position: relative;

  .search-item {
    margin-left: 15px;
  }
}

.detail-content {
  min-height: 300px;
}
</style>
