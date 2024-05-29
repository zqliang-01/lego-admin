<template>
  <div class="doc-book">
    <xr-header
      :show-search="true"
      icon-class="recycle-bin"
      icon-color="#1CBAF5"
      label="回收站"
      placeholder="请输入知识库名称搜索"
      @search="onSearch"/>
    <div class="doc-content">
      <el-table
        v-loading="loading"
        :data="dataList"
        :height="tableHeight"
        highlight-current-row
        style="width: 100%">
        <template v-for="(fields, r) in fieldList">
          <template v-for="(item, index) in fields">
            <el-table-column
              v-if="item.fieldCode !== 'cover'"
              :key="r + '-' + index"
              :min-width="item.width"
              :prop="item.fieldCode"
              :label="item.name"
              show-overflow-tooltip>
              <template slot-scope="{ row }">
                <div v-if="item.fieldCode === 'name'">
                  <flexbox class="corver">
                    <img v-src="handleCover(row)" class="corver-img" alt="封面图">
                    <div class="corver-name text-one-line">{{ row.name }}</div>
                  </flexbox>
                </div>
                <field-view
                  v-else
                  :props="item"
                  :form-type="item.formType"
                  :value="row[item.fieldCode]"/>
              </template>
            </el-table-column>
          </template>
        </template>
        <el-table-column
          fixed="right"
          label="操作">
          <template slot-scope="scope">
            <el-button
              v-if="auth.enable"
              type="text"
              size="small"
              icon="el-icon-refresh-right"
              @click="handleTable('enable', scope.row, scope.$index)">恢复</el-button>
            <el-button
              v-if="auth.delete"
              type="text"
              size="small"
              icon="el-icon-delete"
              @click="handleTable('delete', scope.row, scope.$index)">彻底删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="p-contianer">
        <el-pagination
          :current-page="currentPage"
          :page-sizes="[15, 30, 60, 100]"
          :page-size="pageSize"
          :total="total"
          :pager-count="5"
          class="p-bar"
          background
          layout="prev, pager, next, sizes, total, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"/>
      </div>
    </div>
  </div>
</template>

<script>
import {
  bookDisableListAPI,
  bookEnableAPI,
  bookDeleteAPI
} from '@/api/doc/book'
import { filePreviewUrl } from '@/api/doc/file'
import { mapGetters } from 'vuex'
import XrHeader from '@/components/XrHeader'
import FieldView from '@/components/Common/Form/FieldView'
import BpmnViewer from '@/components/bpmn/components/Viewer'

export default {
  name: 'DocRecycle',
  components: {
    XrHeader,
    FieldView,
    BpmnViewer
  },
  computed: {
    ...mapGetters(['allAuth']),
    auth() {
      if (this.allAuth.doc && this.allAuth.doc.recycle) {
        return this.allAuth.doc.recycle
      }
      return {}
    }
  },
  data() {
    return {
      loading: false,
      dataList: [],
      currentPage: 1,
      pageSize: 15,
      total: 0,
      search: '',
      fieldList: [
        [
          { fieldCode: 'cover', name: '封面', formType: 'doc_image', width: '150' },
          { fieldCode: 'name', name: '名称', formType: 'text', width: '150', clickable: true },
          { fieldCode: 'updateTime', name: '删除时间', formType: 'text', width: '100', editable: false }
        ],
        [
          { fieldCode: 'open', name: '是否公开', formType: 'boolean_value', width: '50' },
          { fieldCode: 'description', name: '描述', formType: 'text', width: '150' }
        ]
      ],
      coverImage: require('@/assets/img/doc/book_cover.png'),
      tableHeight: document.documentElement.clientHeight - 165
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      bookDisableListAPI({
        search: this.search,
        pageIndex: this.currentPage,
        pageSize: this.pageSize
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
      if (type === 'delete') {
        this.$confirm(`请确认彻底删除知识库【${item.name}】，是否继续?`, '提示', {
          type: 'warning'
        }).then(() => {
          this.loading = true
          bookDeleteAPI(item.code).then(() => {
            this.loading = false
            this.$message.success('删除成功！')
            this.getList()
          }).catch(() => {
            this.loading = false
          })
        })
        return
      }
      if (type === 'enable') {
        this.$confirm(`请确认恢复知识库【${item.name}】，是否继续?`, '提示', {
          type: 'warning'
        }).then(() => {
          bookEnableAPI(item.code).then(() => {
            this.loading = false
            this.$message.success('恢复成功！')
            this.getList()
          }).catch(() => {
            this.loading = false
          })
        })
        return
      }
    },
    handleSizeChange(val) {
      this.tablePageSize = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.tableCurrentPage = val
      this.getList()
    },
    handleCover(row) {
      if (row.cover) {
        return filePreviewUrl + row.cover
      }
      return this.coverImage
    },
    onSearch(value) {
      this.search = value
      this.getList()
    }
  }
}
</script>

<style lang="scss" scoped>
.doc-book {
  height: 100%;
}
.doc-content {
  border-top: 1px solid $xr-border-line-color;
  border-bottom: 1px solid $xr-border-line-color;

  .corver {
    padding: 5px;

    &-img {
      flex-shrink: 0;
      width: 100px;
      height: 50px;
      overflow: hidden;
      border-radius: 3px;
    }

    &-name {
      flex: 1;
      margin-left: 8px;
      font-weight: bold;
      color: $xr-color-text-placeholder;
    }
  }
}
</style>
