<template>
  <div class="content">
    <xr-header
      icon-class="print"
      :show-search="true"
      :show-save="manage.printTemplate.add ? true : false"
      icon-color="#1CBAF5"
      label="打印模板设置"
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
            type="text"
            size="small"
            icon="el-icon-edit"
            @click="handleTable('edit', scope.row)">编辑</el-button>
          <el-button
            type="text"
            size="small"
            icon="el-icon-brush"
            @click="handleTable('design', scope.row)">设计模板</el-button>
          <el-button
            type="text"
            size="small"
            icon="el-icon-delete"
            @click="handleTable('delete', scope.row)">删除</el-button>
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
  printTemplateListAPI,
  printTemplateDeleteAPI
} from '@/api/admin/printTemplate'
import { customFormSimpleListAPI } from '@/api/admin/formField'
import Create from './Create'
import XrHeader from '@/components/XrHeader'
import LegoTable from '@/components/Lego/LegoTable'
import { mapGetters } from 'vuex'

export default {
  name: 'PrintTemplate',
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
      dialogVisible: false,
      dataList: [],
      currentPage: 1,
      pageSize: 15,
      total: 0,
      searchValue: '',
      action: {
        type: 'update',
        detailData: {
          code: ''
        }
      },
      fieldList: [
        [
          { fieldCode: 'code', name: '模板编码', formType: 'text', width: '150', disabled: true, unique: true },
          { fieldCode: 'name', name: '模板名称', formType: 'text', width: '150', required: true }
        ],
        [
          { fieldCode: 'form', name: '关联表单', formType: 'select', width: '150', required: true },
          { fieldCode: 'creator', name: '创建人', formType: 'select', width: '150', editable: false }
        ]
      ]
    }
  },
  mounted() {
    this.fieldList.forEach(fields => {
      fields.map(field => {
        if (field.fieldCode === 'form') {
          customFormSimpleListAPI().then(res => {
            field.setting = res.data
          })
        }
      })
    })
    this.getList()
  },
  methods: {
    getList(pageSize = this.pageSize, currentPage = this.currentPage) {
      this.loading = true
      printTemplateListAPI({
        search: this.searchValue,
        pageSize: pageSize,
        pageIndex: currentPage
      }).then(res => {
        this.dataList = res.data.result
        this.total = res.data.totalCount
        this.pageSize = res.data.pageSize
        this.currentPage = res.data.pageIndex
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    handleTable(type, row) {
      if (type === 'design') {
        this.$router.push({
          name: 'printTemplateDetail',
          params: {
            templateCode: row.code
          }
        })
        return
      }
      if (type === 'delete') {
        this.$confirm('此操作将永久删除[' + row.name + ']打印模板，是否继续?', '提示', {
          type: 'warning'
        }).then(() => {
          this.loading = true
          printTemplateDeleteAPI(row.code).then(res => {
            this.$message.success('删除成功！')
            this.getList()
            this.loading = false
          }).catch(() => {
            this.loading = false
          })
        })
        return
      }
      this.action.detailData = row
      this.action.type = 'update'
      this.isCreate = true
    },
    onSearch(value) {
      this.currentPage = 1
      this.searchValue = value
      this.getList()
    },
    onCreate() {
      this.action.type = 'save'
      this.action.detailData = {
        code: ''
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
