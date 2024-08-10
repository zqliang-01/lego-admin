<template>
  <div class="doc-book">
    <xr-header
      :show-search="true"
      icon-class="customer"
      icon-color="#2362fb"
      label="公共知识库"
      placeholder="请输入知识库名称搜索"
      @search="onSearch">
      <template v-slot:ft>
        <el-radio-group v-model="showType" fill="#ff6a00">
          <el-tooltip content="网格展示" placement="bottom-start">
            <el-radio-button label="grid">
                <i class="el-icon-s-grid"/>
            </el-radio-button>
          </el-tooltip>
          <el-tooltip content="表格展示" placement="bottom-start">
            <el-radio-button label="table">
              <i class="el-icon-s-operation"/>
            </el-radio-button>
          </el-tooltip>
        </el-radio-group>
      </template>
    </xr-header>
    <div class="doc-content">
      <el-table
        v-if="showType === 'table'"
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
      <div
        v-else
        v-loading="loading"
        v-empty="dataList.length === 0"
        :style="{ height: tableHeight + 'px' }"
        class="grid-content">
        <div
          v-for="(item, index) in dataList"
          :key="index"
          class="grid-book"
          @click="handleDetail(item.code)">
          <el-card
            :body-style="{ padding: '0px' }"
            shadow="hover">
            <el-image :src="handleCover(item)"/>
            <div class="grid-book-body">
              <div class="grid-book-feature">
                <span>{{ item.name }}</span>
                <p><i :class="'icon-Member-management' | iconPre" />{{ item.creator.name }}</p>
                <p><i :class="'edit' | iconPre" />{{ item.createTime }}</p>
                <p><i :class="'label' | iconPre" />{{ item.description }}</p>
              </div>
            </div>
          </el-card>
        </div>
      </div>
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
import BpmnViewer from '@/components/Bpmn/components/Viewer'

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
      showType: 'grid',
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
      console.log(code)
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

.grid {
  &-content {
    background-color: #fff;
    overflow-y: auto;
    display: flex;
    flex-wrap: wrap;
  }
  &-book {
    width: 33.33%;
    padding: 15px;
    cursor: pointer;
    &-body {
      padding: 14px 14px 5px;
    }
    &-feature {
      overflow: hidden;
    }
    &-feature>span {
      margin-bottom: 8px;
      font-weight: bold;
      position: relative;
      text-overflow: ellipsis;
      overflow: hidden;
      white-space: nowrap;
      display: block;
    }
    &-feature>p {
      margin-bottom: 5px;
      position: relative;
      font-size: 12px;
      color: #666a75;
      text-overflow: ellipsis;
      overflow: hidden;
      white-space: nowrap;
    }
    &-feature>p>i {
      font-size: 12px;
      padding-right: 5px;
      color: #999;
    }
  }
}
</style>
