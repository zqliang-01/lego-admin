<template>
  <div class="content">
    <xr-header
      :icon-class="'double-gear'"
      :show-search="true"
      :show-save="manage.sharding.config.add ? true : false"
      icon-color="#1CBAF5"
      label="分片规则配置"
      placeholder="请输入名称搜索"
      @search="onSearch"
      @create="onCreate"/>
    <div class="table">
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
            v-if="manage.sharding.config.update"
            type="text"
            size="small"
            icon="el-icon-edit"
            @click="handleTable('edit', scope.row)">编辑</el-button>
        </template>
      </lego-table>
    </div>

    <create
      v-if="isCreate"
      :field-list="fieldList"
      :action="action"
      @handle="actionHandle"
      @close="isCreate = false"
    />
  </div>
</template>

<script>
import {
  configListAPI
} from '@/api/admin/sharding/config'
import Create from './Create'
import XrHeader from '@/components/XrHeader'
import LegoTable from '@/components/LegoTable'
import { mapGetters } from 'vuex'

export default {
  name: 'ShardingConfig',
  components: {
    XrHeader,
    LegoTable,
    Create
  },
  computed: {
    ...mapGetters(['manage'])
  },
  data() {
    return {
      loading: false,
      isCreate: false,
      dataList: [],
      currentPage: 1,
      pageSize: 15,
      total: 0,
      searchValue: '',
      action: {
        type: 'update',
        detailData: {}
      },
      fieldList: [
        { fieldCode: 'code', name: '编码', formType: 'text', width: '150', unique: true, required: true, xAxis: 0, yAxis: 0 },
        { fieldCode: 'name', name: '名称', formType: 'text', width: '150', required: true, xAxis: 0, yAxis: 1 },
        { fieldCode: 'description', name: '备注', formType: 'text', width: '150', xAxis: 1, yAxis: 0 },
        { fieldCode: 'enable', name: '状态', formType: 'boolean_value', width: '150', xAxis: 1, yAxis: 1 }
      ]
    }
  },
  methods: {
    getList(pageSize = this.pageSize, currentPage = this.currentPage) {
      this.loading = true
      configListAPI({
        search: this.searchValue,
        pageSize: pageSize,
        currentPage: currentPage
      }).then(res => {
        this.dataList = res.data.result
        this.total = res.data.totalCount
        this.pageSize = res.data.pageSize
        this.currentPage = res.data.pageIndex
        this.loading = false
      })
        .catch(() => {
          this.loading = false
        })
    },
    handleTable(type, row) {
      if (type === 'edit') {
        this.action.type = 'update'
        this.action.detailData = row
        this.isCreate = true
      }
    },
    onSearch(value) {
      this.currentPage = 1
      this.searchValue = value
      this.getList()
    },
    onCreate() {
      this.action.type = 'save'
      this.action.detailData = {}
      this.isCreate = true
    },
    actionHandle(data) {
      if (data.type === 'save-success') {
        this.getList()
      }
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.content {
  height: 100%;
  .table {
    border-top: 1px solid $xr-border-line-color;
    border-bottom: 1px solid $xr-border-line-color;
  }
}
</style>
