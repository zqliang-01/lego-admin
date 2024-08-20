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
      <flexbox
        v-if="downloadList.length > 0"
        class="selection-bar">
        <div class="selected—title">已选中 <span class="selected—count">{{ downloadList.length }}</span> 项</div>
        <flexbox class="selection-items-box">
          <el-button
            type="primary"
            icon="el-icon-download"
            @click.native="handleBatchDownloadJava">批量下载java代码</el-button>
        </flexbox>
      </flexbox>
      <lego-table
        selection
        :loading="loading"
        :data-list="dataList"
        :field-list="fieldList"
        :edit-button-width="300"
        :current-page="currentPage"
        :page-size="pageSize"
        :total="total"
        :table-heigh-overly="tableHeighOverly"
        @onList="getList"
        @onSelectionChange="handleSelectionChange">
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
  genTableListAPI,
  genTablePreviewAPI,
  genTableDownloadAPI,
  genTableNameListAPI,
  genTableBatchDownloadJavaAPI
} from '@/api/admin/genTable'
import { dataSourceSimpleListAPI } from '@/api/admin/sharding/dataSource'
import { mapGetters } from 'vuex'
import XrHeader from '@/components/XrHeader'
import FieldView from '@/components/Common/Form/FieldView'
import LegoTable from '@/components/Lego/LegoTable'
import CodePreview from './CodePreview'
import Create from './Create'
import { downloadFileWithBuffer } from '@/utils'

export default {
  name: 'CustomField',
  components: {
    XrHeader,
    FieldView,
    LegoTable,
    CodePreview,
    Create
  },
  computed: {
    ...mapGetters(['manage'])
  },
  data() {
    return {
      loading: false,
      isCreate: false,
      previewVisible: false,
      tableHeighOverly: 0,
      dataList: [],
      downloadList: [],
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
          { fieldCode: 'dataSource', name: '数据源', formType: 'select', filterable: true, width: '100', unique: true, tipType: 'tooltip', inputTips: '数据表所在数据库' },
          { fieldCode: 'code', name: '表名', formType: 'select', filterable: true, width: '150', unique: true, required: true, tipType: 'tooltip', inputTips: '数据库表名，会自动读取数据库表' }
        ],
        [
          { fieldCode: 'name', name: '功能名称', formType: 'text', width: '150', required: true, tipType: 'tooltip', inputTips: '功能菜单名称，对应前台菜单名' },
          { fieldCode: 'urlName', name: '资源名称', formType: 'text', width: '100', required: true, xAxis: 4, tipType: 'tooltip', inputTips: 'Url地址路径，前端api以及后端Controller使用' }
        ],
        [
          { fieldCode: 'packageName', name: '包名', formType: 'text', width: '150', required: true, tipType: 'tooltip', inputTips: 'JAVA文件包名，例如 com.lego.system' },
          { fieldCode: 'appCode', name: '模块编码', formType: 'text', width: '100', required: true, tipType: 'tooltip', inputTips: '可理解为子系统名，对应前台应用以及后端微服务模块' }
        ],
        [
          { fieldCode: 'permissionCode', name: '权限编码', formType: 'text', width: '100', required: true, tipType: 'tooltip', inputTips: '权限编码，格式为-模块编码_一级菜单_二级菜单_...' },
          { fieldCode: 'className', name: '类名', formType: 'text', width: '100', required: true, tipType: 'tooltip', inputTips: 'JAVA类名' }
        ],
        [
          { fieldCode: 'fieldName', name: '属性名称', formType: 'text', width: '100', required: true, tipType: 'tooltip', inputTips: '属性命名，涉及JAVA类属性命名以及前端路由名等' },
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
      this.fieldList.map(fields => {
        fields.map(field => {
          this.$set(field, 'disabled', false)
          this.initSetting(field)
        })
      })
      this.getList()
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
    handleSelectionChange(values) {
      this.downloadList = values.map(val => val.code)
      if (this.downloadList.length > 0) {
        this.tableHeighOverly = -50
      } else {
        this.tableHeighOverly = 0
      }
    },
    handleBatchDownloadJava() {
      if (this.downloadList.length < 1) {
        this.$message.error('请选择需要下载的数据表信息！')
        return
      }
      this.loading = true
      genTableBatchDownloadJavaAPI(this.downloadList).then(res => {
        downloadFileWithBuffer(res.data, 'lego_gen_java.zip')
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
    async initSetting(field) {
      if (field.fieldCode === 'code') {
        await genTableNameListAPI().then(res => {
          field.setting = res.data
        })
      }
      if (field.fieldCode === 'dataSource') {
        await dataSourceSimpleListAPI().then(res => {
          field.setting = res.data
        })
      }
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
  background-color: #ffffff;
  border-top: 1px solid $xr-border-line-color;
  border-bottom: 1px solid $xr-border-line-color;
}
.selection-bar {
  font-size: 12px;
  height: 50px;
  padding: 0 20px;
  color: #777;

  .selected—title {
    flex-shrink: 0;
    padding-right: 20px;
    color: #333;
    .selected—count {
      color: $xr-color-primary;
    }
  }
}
.selection-items-box {
  overflow-x: auto;
  overflow-y: hidden;
  padding: 0 15px;

  .el-button {
    color: #666;
    background-color: #f9f9f9;
    border-color: #e5e5e5;
    font-size: 12px;
    height: 28px;
    border-radius: 4px;
    padding: 8px 12px;
    ::v-deep i {
      font-size: 12px;
      margin-right: 5px;
    }
  }

  .el-button--primary:hover {
    background: $xr-color-primary;
    border-color: $xr-color-primary;
    color: #ffffff;
  }

  .el-button + .el-button {
    margin-left: 15px;
  }
}
</style>
