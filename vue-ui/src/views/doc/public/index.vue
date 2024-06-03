<template>
  <div class="doc-book">
    <xr-header
      :show-search="true"
      icon-class="customer"
      icon-color="#2362fb"
      label="公共知识库"
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
                <div
                  v-if="item.fieldCode === 'name'"
                  @click="handleDetail(row.code)">
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
  bookPublicListAPI
} from '@/api/doc/book'
import { filePreviewUrl } from '@/api/doc/file'
import XrHeader from '@/components/XrHeader'
import FieldView from '@/components/Common/Form/FieldView'
import BpmnViewer from '@/components/bpmn/components/Viewer'

export default {
  name: 'DocPublic',
  components: {
    XrHeader,
    FieldView,
    BpmnViewer
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
          { fieldCode: 'createTime', name: '创建时间', formType: 'text', width: '100', editable: false }
        ],
        [
          { fieldCode: 'creator', name: '创建人', formType: 'user', width: '100' },
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
      bookPublicListAPI({
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
    handleSizeChange(val) {
      this.pageSize = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this.getList()
    },
    handleDetail(code) {
      this.$router.push({
        name: 'DocBook',
        params: {
          bookCode: code
        }
      })
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
    cursor: pointer;

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
      color: $xr-color-primary;
    }
  }
}
</style>
