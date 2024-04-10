<template>
  <div class="content">
    <xr-header
      :icon-class="'double-gear'"
      :show-search="true"
      :show-save="manage.sharding.template.add ? true : false"
      icon-color="#1CBAF5"
      label="分片模板配置"
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
        :edit-button-width="100"
        @onList="getList"
        @onEdit="handleTable">
        <template slot-scope="scope">
          <el-button
            v-if="manage.sharding.template.update"
            type="text"
            size="small"
            icon="el-icon-edit"
            @click="handleTable('edit', scope.row)">编辑</el-button>
        </template>
      </lego-table>
    </div>

    <Create
      :visible="isCreate"
      :field-list="fieldList"
      :action="action"
      @handle="actionHandle"
      @close="isCreate = false"
    />
  </div>
</template>

<script>
import {
  templateListAPI,
  templateTypeListAPI
} from '@/api/admin/sharding/template'
import XrHeader from '@/components/XrHeader'
import LegoTable from '@/components/lego/LegoTable'
import Create from './Create'
import { mapGetters } from 'vuex'

export default {
  name: 'ShardingTemplate',
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
        [
          { fieldCode: 'code', name: '编码', formType: 'text', width: '150', unique: true, required: true },
          { fieldCode: 'name', name: '名称', formType: 'text', width: '150', required: true }
        ],
        [
          { fieldCode: 'description', name: '备注', formType: 'text', width: '150' },
          { fieldCode: 'type', name: '类型', formType: 'select', width: '150', required: true }
        ],
        [
          { fieldCode: 'enable', name: '状态', formType: 'boolean_value', width: '150' }
        ],
        [
          { fieldCode: 'json', name: '模板内容', formType: 'jsonEditor', width: '150', stylePercent: 100 }
        ]
      ]
    }
  },
  mounted() {
    templateTypeListAPI().then(res => {
      this.fieldList.forEach(element => {
        const field = element.find(field => field.fieldCode == 'type')
        if (field) {
          field.setting = res.data
        }
      })
    })
  },
  methods: {
    getList(pageSize = this.pageSize, currentPage = this.currentPage) {
      this.loading = true
      templateListAPI({
        search: this.searchValue,
        pageSize: pageSize,
        pageIndex: currentPage
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

<style lang="scss" scoped>
.content {
  height: 100%;
  .table {
    border-top: 1px solid $xr-border-line-color;
    border-bottom: 1px solid $xr-border-line-color;
  }
}
</style>
