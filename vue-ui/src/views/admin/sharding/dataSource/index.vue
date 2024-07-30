<template>
  <div class="content">
    <xr-header
      :icon-class="'double-gear'"
      :show-search="true"
      :show-save="manage.sharding.dataSource.add ? true : false"
      icon-color="#1CBAF5"
      label="分片数据源配置"
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
            v-if="manage.sharding.dataSource.update"
            type="text"
            size="small"
            icon="el-icon-edit"
            @click="handleTable('edit', scope.row)">编辑</el-button>
          <el-button
            v-if="manage.sharding.dataSource.update"
            type="text"
            size="small"
            icon="el-icon-edit"
            @click="handleTable('property', scope.row)">设置属性</el-button>
        </template>
      </lego-table>
    </div>

    <Create
      :visible="isCreate"
      :field-list="fieldList"
      :action="action"
      :title="action.type === 'update' ? '编辑数据源' : '新建数据源'"
      @handle="actionHandle"
      @close="isCreate = false"
    />

    <properties-dialog
      :visible.sync="dialogVisible"
      :entity-id="action.detailData.id"
      :template-code="action.detailData.template.code"
      @onSuccess="dialogVisible = false"
    />
  </div>
</template>

<script>
import {
  templateSimpleListAPI
} from '@/api/admin/sharding/template'
import { dataSourceListAPI } from '@/api/admin/sharding/dataSource'
import XrHeader from '@/components/XrHeader'
import LegoTable from '@/components/Lego/LegoTable'
import Create from './Create'
import PropertiesDialog from '../PropertiesDialog'
import { mapGetters } from 'vuex'

export default {
  name: 'ShardingDataSource',
  components: {
    XrHeader,
    LegoTable,
    Create,
    PropertiesDialog
  },
  computed: {
    ...mapGetters(['manage'])
  },
  data() {
    return {
      loading: false,
      isCreate: false,
      dialogVisible: false,
      dataList: [],
      searchValue: '',
      action: {
        type: 'update',
        detailData: {
          code: '',
          template: {
            code: ''
          }
        }
      },
      currentPage: 1,
      pageSize: 15,
      total: 0,
      fieldList: [
        [
          { fieldCode: 'code', name: '编码', formType: 'text', width: '150', unique: true, required: true },
          { fieldCode: 'name', name: '名称', formType: 'text', width: '150', required: true }
        ],
        [
          { fieldCode: 'description', name: '备注', formType: 'text', width: '150' },
          { fieldCode: 'template', name: '模板', formType: 'select', width: '150', required: true }
        ],
        [
          { fieldCode: 'enable', name: '状态', formType: 'boolean_value', width: '150' }
        ]
      ]
    }
  },
  mounted() {
    templateSimpleListAPI({
      typeCode: 'DataSource'
    }).then(res => {
      this.fieldList.forEach(element => {
        const field = element.find(field => field.fieldCode == 'template')
        if (field) {
          field.setting = res.data
        }
      })
    })
    this.getList()
  },
  methods: {
    getList(pageSize = this.pageSize, currentPage = this.currentPage) {
      this.loading = true
      dataSourceListAPI({
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
      this.action.detailData = row
      if (type === 'edit') {
        this.action.type = 'update'
        this.isCreate = true
      }
      if (type === 'property') {
        this.dialogVisible = true
      }
    },
    onSearch(value) {
      this.currentPage = 1
      this.searchValue = value
      this.getList()
    },
    onCreate() {
      this.action.type = 'save'
      this.action.detailData = {
        code: '',
        template: {
          code: ''
        }
      }
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
