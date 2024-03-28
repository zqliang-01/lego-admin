<template>
  <div class="system-customer">
    <xr-header
      :icon-class="'double-gear'"
      :show-search="true"
      :show-save="manage.genTable.update"
      icon-color="#1CBAF5"
      label="代码生成设置"
      placeholder="请输入表名搜索"
      @search="onSearch"
      @create="onCreate"/>
    <div class="customer-content">
      <lego-table
        :loading="loading"
        :data-list="dataList"
        :field-list="fieldList"
        :edit-button-width="300"
        :current-page="currentPage"
        :page-size="pageSize"
        :total="total"
        @onList="getList"
        @onEdit="handleTable">
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
      </lego-table>
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
import LegoTable from '@/components/lego/LegoTable'
import GenTableCreate from './Create'
import CodePreview from './CodePreview'
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
      dataList: [],
      tableNameList: [],
      currentPage: 1,
      pageSize: 15,
      total: 0,
      search: '',
      action: {
        type: 'update',
        detailData: {}
      },
      previewData: [],
      fieldList: [
        [
          { fieldCode: 'code', name: '表名', formType: 'select', filterable: true, width: '150', unique: true, required: true, tipType: 'tooltip', inputTips: '数据库表名，会自动读取数据库表' },
          { fieldCode: 'name', name: '功能名称', formType: 'text', width: '150', required: true, tipType: 'tooltip', inputTips: '功能菜单名称，对应前台菜单名' }
        ],
        [
          { fieldCode: 'urlName', name: '资源名称', formType: 'text', width: '100', required: true, xAxis: 4, tipType: 'tooltip', inputTips: 'Url地址路径，前端api以及后端Controller使用' },
          { fieldCode: 'packageName', name: '包名', formType: 'text', width: '150', required: true, tipType: 'tooltip', inputTips: 'JAVA文件包名，例如 com.lego.system' }
        ],
        [
          { fieldCode: 'appCode', name: '模块编码', formType: 'text', width: '100', required: true, tipType: 'tooltip', inputTips: '可理解为子系统名，对应前台应用以及后端微服务模块' },
          { fieldCode: 'permissionCode', name: '权限编码', formType: 'text', width: '100', required: true, tipType: 'tooltip', inputTips: '权限编码，格式为-模块编码_一级菜单_二级菜单_...' }
        ],
        [
          { fieldCode: 'className', name: '类名', formType: 'text', width: '100', required: true, tipType: 'tooltip', inputTips: 'JAVA类名' },
          { fieldCode: 'fieldName', name: '属性名称', formType: 'text', width: '100', required: true, tipType: 'tooltip', inputTips: '属性命名，涉及JAVA类属性命名以及前端路由名等' }
        ],
        [
          { fieldCode: 'comment', name: '描述', formType: 'text', width: '150' }
          // { fieldCode: 'path', name: '生成路径', formType: 'text', width: '150', tipType: 'tooltip', inputTips: '填写磁盘绝对路径，若不填写，则生成到当前Web项目下' }
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
      genTableNameListAPI().then(res => {
        this.tableNameList = res.data
      })
    },
    getList(pageSize = this.pageSize, currentPage = this.currentPage) {
      this.loading = true
      genTableListAPI({
        code: this.search,
        pageSize: pageSize,
        pageIndex: currentPage
      }).then(res => {
        this.dataList = res.data.result
        this.total = res.data.totalCount
        this.currentPage = res.data.pageIndex
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
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

<style lang="scss" scoped>
.system-customer {
  height: 100%;
}
.customer-content {
  border-top: 1px solid $xr-border-line-color;
  border-bottom: 1px solid $xr-border-line-color;
}
</style>
