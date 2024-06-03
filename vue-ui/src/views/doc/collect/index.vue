<template>
  <div class="collect">
    <xr-header
      :show-search="true"
      icon-class="contract"
      icon-color="#ff9800"
      label="我的收藏"
      placeholder="请输入内容名称搜索"
      @search="onSearch"/>
    <div class="collect-body">
      <div class="collect-content">
        <div class="files-head">
          <i class="el-icon-folder-opened" />
          <label v-text="'我收藏的内容'" /> 共<span>{{ total }}</span>个文件
        </div>
        <el-table
          v-loading="loading"
          :data="dataList"
          :height="tableHeight"
          highlight-current-row
          style="width: 100%">
          <template v-for="(fields, r) in fieldList">
            <el-table-column
              v-for="(item, index) in fields"
              :key="r + '-' + index"
              :min-width="item.width"
              :prop="item.fieldCode"
              :label="item.name"
              show-overflow-tooltip>
              <template slot-scope="{ row }">
                <div
                  v-if="item.fieldCode === 'name'"
                  @click="handleDetail(row)">
                  <flexbox class="file">
                    <i v-if="row.type === 'page'" :class="'doc' | iconPre" class="file-icon"/>
                    <i v-else-if="row.type === 'folder'" :class="'folder' | iconPre" class="file-icon"/>
                    <img v-else v-src="handleFileImage(row.name)" class="file-img">
                    <div class="file-name text-one-line">{{ row.name }}</div>
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
          <el-table-column
            show-overflow-tooltip
            width="80"
            label="操作"
            align="center">
            <template slot-scope="scope">
              <el-button
                type="text"
                style="padding: 0;"
                @click.stop="handleCollect(scope.row)">
                <i class="lego lego-focus-on icon-collect" />
              </el-button>
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
  </div>
</template>
<script>
import {
  collectListAPI,
  collectDeleteAPI
} from '@/api/doc/collect'
import { getFileIconWithSuffix } from '@/utils'
import FieldView from '@/components/Common/Form/FieldView'
import XrHeader from '@/components/XrHeader'

export default {
  name: 'DocCollect',
  components: {
    XrHeader,
    FieldView
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
          { fieldCode: 'name', name: '名称', formType: 'text', width: '150' },
          { fieldCode: 'book', name: '归属知识库', formType: 'select', width: '150' }
        ],
        [
          { fieldCode: 'createTime', name: '收藏时间', formType: 'text', width: '150' }
        ]
      ],
      tableHeight: document.documentElement.clientHeight - 245
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      collectListAPI({
        name: this.search,
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
    handleFileImage(name) {
      const temps = name ? name.split('.') : []
      var ext = ''
      if (temps.length > 0) {
        ext = temps[temps.length - 1]
      } else {
        ext = ''
      }
      return getFileIconWithSuffix(ext)
    },
    handleDetail(row) {
      this.$router.push({
        name: 'DocBookNode',
        params: {
          bookCode: row.book.code,
          nodeCode: row.node.code
        }
      })
    },
    handleCollect(row) {
      this.$confirm(`请确认取消收藏内容【${row.name}】，是否继续?`, '提示', {
        type: 'warning'
      }).then(() => {
        this.loading = true
        collectDeleteAPI(row.code).then(() => {
          this.loading = false
          this.$message.success('取消收藏成功！')
          this.getList()
        }).catch(() => {
          this.loading = false
        })
      })
    },
    onSearch(value) {
      this.search = value
      this.getList()
    }
  }
}
</script>
<style lang="scss" scoped>
.collect {
  height: 100%;
  &-body {
    position: relative;
    height: calc(100% - 75px);
    padding: 0px 15px;
    .collect-content {
      position: relative;
      height: 100%;
      overflow: hidden;
      background-color: white;
      border: 1px solid #e6e6e6;
      border-radius: 4px;
      padding: 10px;
    }
  }
  .file {
    padding: 5px;
    cursor: pointer;

    &-icon {
      flex-shrink: 0;
      width: 20px;
      height: 20px;
      line-height: 20px;
      font-size: 20px;
      color: $xr-color-text-regular;
    }
    &-img {
      flex-shrink: 0;
      width: 20px;
    }

    &-name {
      flex: 1;
      margin-left: 8px;
    }
  }
  .files-head {
    margin: 8px 0;
    padding: 0 5px;
    overflow: hidden;
    float: left;
    line-height: 32px;
    font-size: 14px;
    i {
      font-size: 22px;
      vertical-align: middle;
      color: #333;
    }
    label {
      font-size: 16px;
      font-weight: 550;
      margin-right: 5px;
      margin-left: 4px;
      vertical-align: middle;
    }
    span {
      font-size: 18px;
      font-weight: 550;
      color: #ff6161;
      margin: 0 3px;
    }
  }
}
</style>
