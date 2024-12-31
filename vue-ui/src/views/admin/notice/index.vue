<template>
  <div class="content">
    <xr-header
      icon-class="announcement"
      :show-search="true"
      :show-save="manage.notice.add ? true : false"
      icon-color="#1CBAF5"
      label="公告管理"
      placeholder="请输入公告标题搜索"
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
        :edit-button-width="200"
        @onList="getList"
        @onEdit="handleTable">
        <template slot-scope="scope">
          <el-button
            v-if="manage.notice.add && !scope.row.published"
            type="text"
            size="small"
            icon="el-icon-edit"
            @click="handleTable('edit', scope.row)">编辑</el-button>
          <el-button
            v-if="manage.notice.publish && !scope.row.published"
            type="text"
            size="small"
            icon="el-icon-video-play"
            @click="handleTable('publish', scope.row)">发布</el-button>
          <el-button
            type="text"
            size="small"
            icon="el-icon-view"
            @click="handleTable('view', scope.row)">预览</el-button>
        </template>
      </lego-table>
    </div>

    <el-dialog title="公告信息预览" :visible.sync="contentVisible" width="70%" append-to-body>
      <div class="notice-content">
        <div v-html="content"/>
      </div>
    </el-dialog>

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
  noticeListAPI,
  noticePublishAPI
} from '@/api/admin/noticeTemplate'
import Create from './Create'
import XrHeader from '@/components/XrHeader'
import LegoTable from '@/components/Lego/LegoTable'
import { mapGetters } from 'vuex'

export default {
  name: 'Notice',
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
      content: '',
      contentVisible: false,
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
          { fieldCode: 'code', name: '编码', formType: 'text', width: '100', disabled: true, unique: true },
          { fieldCode: 'name', name: '标题', formType: 'text', width: '150', required: true }
        ],
        [
          { fieldCode: 'employees', name: '接收员工', formType: 'multipleUser', width: '150' },
          { fieldCode: 'depts', name: '接收部门', formType: 'multipleStructure', width: '150' }
        ],
        [
          { fieldCode: 'creator', name: '创建人', formType: 'select', width: '150', editable: false },
          { fieldCode: 'content', name: '内容', formType: 'richTextEditor', width: '150', stylePercent: 100, visible: false, required: true }
        ],
        [
          { fieldCode: 'published', name: '发布状态', formType: 'boolean', width: '150', editable: false },
          { fieldCode: 'publishedTime', name: '发布时间', formType: 'datetime', width: '150', editable: false }
        ]
      ]
    }
  },
  mounted() {
    this.getList()
  },
  methods: {
    getList(pageSize = this.pageSize, currentPage = this.currentPage) {
      this.loading = true
      noticeListAPI({
        name: this.searchValue,
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
      if (type === 'publish') {
        this.$confirm('公告发布后将不可修改，是否确认发布公告【' + row.name + '】?', '提示', {
          type: 'warning'
        }).then(() => {
          this.loading = true
          noticePublishAPI(row.code).then(() => {
            this.loading = false
            this.$message.success('公告发布成功！')
            this.getList()
          }).catch(() => {
            this.loading = false
          })
        })
        return
      }
      if (type === 'edit') {
        this.action.detailData = row
        this.action.type = 'update'
        this.isCreate = true
        return
      }
      this.content = row.content
      this.contentVisible = true
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
.notice-content {
  min-height: 400px;
  max-height: 500px;
  overflow-y: auto;
}
</style>
