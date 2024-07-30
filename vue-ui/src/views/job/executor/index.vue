<template>
  <div class="system-customer">
    <xr-header
      icon-class="my-task"
      :show-search="true"
      :show-save="auth.add"
      icon-color="#1CBAF5"
      label="执行器管理"
      placeholder="请输入名称"
      @search="onSearch"
      @create="onCreate" />
    <div class="customer-content">
      <flexbox class="main-table-header">
        <el-input
          v-model="appname"
          resize="none"
          class="search-item"
          placeholder="请输入AppName" />
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
            :props="item"
            :form-type="item.formType"
            :value="handleDisplayValue(row, item)"
            @clickValue="handleFieldClick(row)" />
        </template>
        <template slot-scope="{ row }">
          <el-button
            v-if="auth.update"
            type="text"
            size="small"
            icon="el-icon-edit"
            @click="handleTable('detail', row)">编辑</el-button>
          <el-button
            v-if="auth.delete"
            type="text"
            size="small"
            icon="el-icon-delete"
            @click="handleTable('delete', row)">删除</el-button>
        </template>
      </lego-table>
    </div>
    <Create
      :visible="showDetail"
      :action="action"
      :field-list="fieldList"
      @handle="actionHandle"
      @close="showDetail = false"
    />
  </div>
</template>

<script>
import {
  executorListAPI,
  executorDeleteAPI
} from '@/api/job/executor'
import { mapGetters } from 'vuex'
import Create from './Create'
import XrHeader from '@/components/XrHeader'
import LegoTable from '@/components/Lego/LegoTable'
import FieldView from '@/components/Common/Form/FieldView'

export default {
  name: 'JobExecutor',
  components: {
    Create,
    XrHeader,
    LegoTable,
    FieldView
  },
  computed: {
    ...mapGetters(['allAuth']),
    auth() {
      if (this.allAuth.job && this.allAuth.job.executor) {
        return this.allAuth.job.executor
      }
      return {}
    }
  },
  data() {
    return {
      loading: false,
      showDetail: false,
      appname: '',
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
          { fieldCode: 'appname', name: 'AppName', formType: 'text', required: true, width: '100' },
          { fieldCode: 'title', name: '名称', formType: 'text', required: true, width: '150' }
        ],
        [
          {
            fieldCode: 'addressType', name: '注册方式', formType: 'select', required: true, precisions: 1,
            setting: [{ code: 0, name: '自动注册' }, { code: 1, name: '手动注册' }], width: '100',
            tipType: 'tooltip', tips: '自动注册：自动根据执行器配置的调度中心地址进行注册。手动注册：自行填写执行器地址进行注册'
          }
        ],
        [
          { fieldCode: 'addressList', name: 'OnLine机器地址', formType: 'textarea', width: '100', clickable: true, stylePercent: 100, tips: '注册类型为手动注册时填写执行器机器地址' }
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
      const param = {
        title: this.search,
        appname: this.appname,
        pageSize: pageSize,
        pageIndex: currentPage
      }
      executorListAPI(param).then(res => {
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
    actionHandle(data) {
      if (data.type === 'save-success') {
        this.getList()
      }
    },
    handleTable(type, item) {
      if (type === 'detail') {
        this.action.type = 'update'
        this.action.detailData = item
        this.showDetail = true
        return
      }
      if (type === 'delete') {
        this.$confirm(`请确认删除执行器【${item.title}】，是否继续?`, '提示', {
          type: 'warning'
        }).then(() => {
          this.loading = true
          executorDeleteAPI(item.id).then(() => {
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
    handleDisplayValue(row, item) {
      if (item.fieldCode === 'addressList') {
        if (row.registryList.length > 0) {
          return '查看（' + row.registryList.length + '）'
        }
      }
      if (item.fieldCode === 'addressType') {
        if (row.addressType === 1) {
          return '手动录入'
        }
        return '自动注册'
      }
      return row[item.fieldCode]
    },
    handleFieldClick(row) {
      var html = '<div>'
      row.registryList.forEach((item, i) => {
        html += (i + 1) + '. <span>' + item + '</span><br>'
      })
      html += '</div>'
      this.$alert(html, '已注册节点', {
        dangerouslyUseHTMLString: true
      })
    },
    onCreate() {
      this.action.type = 'save'
      this.action.detailData = {}
      this.showDetail = true
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
    max-width: 200px;
  }
}

.detail-content {
  min-height: 300px;
}
</style>
