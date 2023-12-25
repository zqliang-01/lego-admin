<template>
  <div class="system-customer">
    <xr-header
      :icon-class="'double-gear'"
      :show-search="true"
      :show-save="manage.genTable.update ? true : false"
      icon-color="#1CBAF5"
      label="代码生成设置"
      placeholder="请输入表名搜索"
      @search="onSearch"
      @create="onCreate"/>
    <div class="customer-content">
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
          <template slot-scope="{ row, column, $index }">
            <field-view
              :props="item"
              :form-type="item.formType"
              :value="row[item.fieldCode]">
              <img
                v-if="item.fieldCode == 'code'"
                slot="leftContent"
                :src="getTableIcon()"
                class="table-item-icon" >
            </field-view>
          </template>
        </el-table-column>
        <el-table-column
          fixed="right"
          label="操作"
          width="300">
          <template slot-scope="scope">
            <el-button
              v-if="manage.genTable.update"
              type="text"
              size="small"
              @click="handleTable('edit', scope.row, scope.$index)">编辑</el-button>
            <el-button
              v-if="manage.genTable.design"
              type="text"
              size="small"
              icon="el-icon-edit"
              @click="handleTable('design', scope.row, scope.$index)">设计字段</el-button>
            <el-button
              v-if="manage.genTable.update"
              type="text"
              size="small"
              icon="el-icon-view"
              @click="handleTable('preview', scope.row, scope.$index)">预览代码</el-button>
            <el-button
              v-if="manage.genTable.update"
              type="text"
              size="small"
              icon="el-icon-download"
              @click="handleTable('download', scope.row, scope.$index)">下载代码</el-button>
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
    </div>

    <code-preview
      :visible.sync="previewVisible"
      :data="previewData"
      title="代码预览"
    />

    <gen-table-create
      v-if="isCreate"
      :field-list="fieldList"
      :table-name-list="tableNameList"
      :action="action"
      @handle="actionHandle"
      @close="isCreate = false"
    />
  </div>
</template>

<script>
import {
  genTableListAPI,
  genTableNameListAPI,
  genTablePreviewAPI,
  genTableDownloadAPI
} from '@/api/admin/genTable'
import { mapGetters } from 'vuex'
import XrHeader from '@/components/XrHeader'
import FieldView from '@/components/NewCom/Form/FieldView'
import GenTableCreate from './Create'
import CodePreview from './CodePreview'
import LegoTable from '@/components/LegoTable'
import { downloadFileWithBuffer } from '@/utils'

import 'highlight.js/styles/github-gist.css'
const hljs = require('highlight.js/lib/core')
hljs.registerLanguage('java', require('highlight.js/lib/languages/java'))
hljs.registerLanguage('xml', require('highlight.js/lib/languages/xml'))
hljs.registerLanguage('html', require('highlight.js/lib/languages/xml'))
hljs.registerLanguage('vue', require('highlight.js/lib/languages/xml'))
hljs.registerLanguage('javascript', require('highlight.js/lib/languages/javascript'))
hljs.registerLanguage('sql', require('highlight.js/lib/languages/sql'))

export default {
  name: 'CustomField',
  components: {
    XrHeader,
    FieldView,
    LegoTable,
    GenTableCreate,
    CodePreview
  },
  computed: {
    ...mapGetters(['manage'])
  },
  data() {
    return {
      loading: false,
      isCreate: false,
      previewVisible: false,
      tableHeight: document.documentElement.clientHeight - 165, // 表的高度
      dataList: [],
      tableNameList: [],
      currentPage: 1,
      pageSize: 15,
      pageSizes: [15, 30, 60, 100],
      total: 0,
      search: '',
      action: {
        type: 'update',
        detailData: {}
      },
      previewData: [],
      fieldList: [
        { fieldCode: 'code', name: '表名', formType: 'select', width: '150', unique: true, required: true, xAxis: 0, yAxis: 0 },
        { fieldCode: 'name', name: '功能名称', formType: 'text', width: '150', required: true, xAxis: 0, yAxis: 1, tipType: 'tooltip', inputTips: '功能菜单名称，对应前台菜单名' },
        { fieldCode: 'comment', name: '描述', formType: 'text', width: '150', xAxis: 1, yAxis: 0 },
        { fieldCode: 'packageName', name: '包名', formType: 'text', width: '150', required: true, xAxis: 1, yAxis: 1, tipType: 'tooltip', inputTips: '生成在哪个java包下，例如 com.lego.system' },
        { fieldCode: 'appCode', name: '模块编码', formType: 'text', width: '100', required: true, xAxis: 2, yAxis: 0, tipType: 'tooltip', inputTips: '可理解为子系统名，对应前台应用以及后端微服务模块' },
        { fieldCode: 'permissionCode', name: '权限编码', formType: 'text', width: '100', required: true, xAxis: 2, yAxis: 1, tipType: 'tooltip', inputTips: '权限编码，格式为-模块编码:一级菜单:二级菜单:...' },
        { fieldCode: 'className', name: '类名', formType: 'text', width: '100', required: true, xAxis: 3, yAxis: 0 },
        { fieldCode: 'fieldName', name: '属性名称', formType: 'text', width: '100', required: true, xAxis: 3, yAxis: 1, tipType: 'tooltip', inputTips: '属性命名，涉及JAVA类属性命名以及前端路由名等' },
        { fieldCode: 'urlName', name: '资源名称', formType: 'text', width: '100', required: true, xAxis: 4, yAxis: 0, tipType: 'tooltip', inputTips: 'Url地址路径，前端api以及后端Controller使用' }
        // { fieldCode: 'path', name: '生成路径', formType: 'text', width: '150', xAxis: 4, yAxis: 1, tipType: 'tooltip', inputTips: '填写磁盘绝对路径，若不填写，则生成到当前Web项目下' }
      ]
    }
  },
  created() {
    window.onresize = () => {
      self.tableHeight = document.documentElement.clientHeight - 140
    }
    this.refresh()
  },
  methods: {
    refresh() {
      this.getList()
      genTableNameListAPI().then(res => {
        this.tableNameList = res.data
      })
    },
    getList() {
      this.loading = true
      genTableListAPI({
        code: this.search,
        pageIndex: this.currentPage,
        pageSize: this.pageSize
      }).then(res => {
        this.dataList = res.data.result
        this.total = res.data.totalCount
        this.currentPage = res.data.pageIndex
        this.loading = false
      })
        .catch(() => {
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
    handleTable(type, item, index) {
      if (type === 'design') {
        this.$router.push({
          name: 'genTableColumn',
          params: { tableCode: item.code }
        })
        return
      }
      if (type === 'preview') {
        this.loading = true
        genTablePreviewAPI(item.code).then(response => {
          this.loading = false
          this.previewData = response.data
          this.previewVisible = true
        }).catch(() => {
          this.loading = false
        })
        return
      }
      if (type === 'download') {
        this.loading = true
        genTableDownloadAPI(item.code).then(res => {
          downloadFileWithBuffer(res.data, item.code + '.zip')
          this.loading = false
        }).catch(() => {
          this.loading = false
        })
        return
      }
      this.action.type = 'update'
      this.action.detailData = item
      this.isCreate = true
    },
    getTableIcon() {
      return require('@/assets/img/crm/product.png')
    },
    onSearch(value) {
      this.search = value
      this.getList()
    },
    onCreate() {
      this.action.type = 'save'
      this.action.detailData = {}
      this.isCreate = true
    },
    actionHandle(data) {
      if (data.type === 'save-success') {
        this.refresh()
      }
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.system-customer {
  height: 100%;
}
.customer-content {
  border-top: 1px solid $xr-border-line-color;
  border-bottom: 1px solid $xr-border-line-color;
}

.table-item-label {
  vertical-align:middle;
}
.table-item-icon {
  width: 30px;
  height: 30px;
  margin-right: 8px;
  vertical-align:middle;
  border-radius: 4px;
}

.table-item-time {
  color: #999;
}

.el-table {
  /deep/ .el-table__body td {
    height: 60px;
  }
}

/* 新建和编辑 */
.new-dialog-form {
  height: 47vh;
  overflow-y: auto;
  padding: 20px;
}
.new-dialog-form /deep/ .el-form-item {
  width: 49%;
  margin: 0;
  padding-bottom: 10px;
}
.new-dialog-form /deep/ .el-form-item .el-form-item__label {
  padding: 0;
}
.new-dialog-form {
  /deep/ .el-form-item:nth-child(even) {
    padding-left: 15px;
  }

  /deep/ .el-form-item:nth-child(odd) {
    padding-right: 15px;
  }
 .new-dialog-form
  /deep/
  .el-form-item
  .el-form-item__content
  .el-select-group__wrap:not(:last-of-type)::after {
  display: none;
}
.new-dialog-form /deep/ .el-form-item .el-form-item__content .el-select-group {
  padding-left: 10px;
}
.new-dialog-form
  /deep/
  .el-form-item
  .el-form-item__content
  .el-select-group__title {
  border-bottom: 1px solid #e4e7ed;
  padding: 0 0 7px;
  margin: 0 20px 5px;
}
}
</style>
