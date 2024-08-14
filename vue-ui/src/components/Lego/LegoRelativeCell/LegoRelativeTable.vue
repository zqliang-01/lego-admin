<template>
  <div class="cr-body-content">
    <flexbox class="content-header">
      <el-input
        v-model="searchContent"
        class="search-container">
        <el-button
          slot="append"
          icon="el-icon-search"
          @click.native="searchInput"/>
      </el-input>
    </flexbox>
    <el-table
      v-loading="loading"
      ref="relativeTable"
      :data="dataList"
      :height="250"
      stripe
      border
      highlight-current-row
      style="width: 100%"
      @current-change="handleCurrentChange"
      @selection-change="handleSelectionChange">
      <el-table-column
        v-if="multiple"
        reserve-selection
        type="selection"
        width="55">
      </el-table-column>
      <el-table-column
        v-else
        align="center"
        width="35">
        <template slot-scope="scope">
          <el-radio :label="scope.row.code" v-model="selectedCode"></el-radio>
        </template>
      </el-table-column>
      <el-table-column
        v-for="(item, index) in fieldList"
        :key="index"
        :prop="item.field"
        :label="item.name"
        :width="item.width ? item.width : 150"
        show-overflow-tooltip >
        <template slot-scope="{ row }">
          <field-view
            :props="item"
            :form-type="item.formType"
            :value="row[item.fieldCode]"/>
        </template>
      </el-table-column>
    </el-table>
    <div class="table-footer">
      <el-button
        :disabled="currentPage <= 1"
        @click.native="changePage('up')">上一页</el-button>
      <el-button
        :disabled="currentPage == totalPage"
        @click.native="changePage('down')">下一页</el-button>
    </div>
  </div>
</template>
<script type="text/javascript">
import FieldView from '@/components/Common/Form/FieldView'
import { postRequest } from '@/api/crm/common'

export default {
  name: 'LegoRelativeTable',
  components: {
    FieldView
  },
  props: {
    fieldList: {
      type: Array,
      default: () => {
        return []
      }
    },
    searchKey: {
      type: String,
      default: 'search'
    },
    multiple: {
      type: Boolean,
      default: false
    },
    selectedData: {
      type: Array,
      default: () => {
        return []
      }
    },
    showPopover: {
      type: Boolean,
      default: false
    },
    queryApiUrl: String
  },
  data() {
    return {
      loading: false, // 加载进度
      searchContent: '', // 输入内容
      dataList: [], // 表数据
      currentPage: 1, // 当前页数
      totalPage: 1, // 总页数
      selectedCode: '',
      selectedCodes: []
    }
  },
  computed: {
  },
  watch: {
    showPopover: function(val) {
      if (val) {
        this.refreshList()
      }
    }
  },
  mounted() {
    this.refreshList()
  },
  methods: {
    refreshList() {
      if (this.selectedData.length > 0) {
        if (this.multiple) {
          this.selectedCodes = this.selectedData.map(val => val.code)
        } else {
          this.selectedCode = this.selectedData[0].code
        }
      }
      this.currentPage = 1
      this.getList()
    },
    getList() {
      if (!this.queryApiUrl) {
        return
      }
      this.loading = true
      const params = {
        pageIndex: this.currentPage,
        pageSize: 10
      }
      params[this.searchKey] = this.searchContent
      postRequest(this.queryApiUrl, params).then(res => {
        this.dataList = res.data.result
        this.handleInitSelect()
        this.totalPage = Math.ceil(res.data.totalCount / 10)
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    handleInitSelect() {
      if (this.multiple) {
        this.$nextTick(() => {
          this.dataList.forEach(data => {
            if (this.selectedCodes.some(code => code === data.code)) {
              this.$refs.relativeTable.toggleRowSelection(data, true)
            }
          })
        })
      }
    },
    // 当选择项发生变化时会触发该事件
    handleSelectionChange(values) {
      if (this.multiple) {
        this.selectedCodes = values.map(val => val.code)
        this.$emit('changeCheckout', values)
      }
    },
    handleCurrentChange(val) {
      if (!this.multiple && val) {
        this.selectedCode = val.code
        this.$emit('changeCheckout', [val])
      }
    },
    clearAll() {
      this.$refs.relativeTable.clearSelection()
    },
    searchInput() {
      this.currentPage = 1
      this.totalPage = 1
      this.getList()
    },
    changePage(type) {
      if (type == 'up' && this.currentPage > 1) {
        this.currentPage = this.currentPage - 1
      }
      if (type == 'down') {
        this.currentPage = this.currentPage + 1
      }
      if (this.currentPage <= this.totalPage && this.currentPage >= 1) {
        this.getList()
      }
    }
  }
}
</script>
<style lang="scss" scoped>
.cr-body-content {
  position: relative;
  background-color: white;
  border-bottom: 1px solid $xr-border-line-color;
}

.content-header {
  position: relative;
  padding: 10px 20px;
  color: #333;
  font-size: 13px;
  .el-select {
    width: 120px;

    ::v-deep .el-icon-circle-close {
      visibility: hidden;
      display: none;
    }
  }

  .el-date-editor {
    width: 170px;
  }

  .search-container {
    margin: 0 20px;
    width: 200px;
  }

  .xr-btn--orange {
    position: absolute;
    right: 10px;
    top: 15px;
  }
}

//表尾 上一页按钮
.table-footer {
  padding: 8px 20px;
}

.el-table ::v-deep thead th {
  font-weight: 400;
  font-size: 12px;
}

.el-table ::v-deep tbody tr td {
  font-size: 12px;
}

.el-table ::v-deep thead .el-checkbox {
  display: none;
}

body .el-table th.gutter {
  display: table-cell !important;
}

.el-table ::v-deep .el-table__body-wrapper {
  height: calc(100% - 48px) !important;
}
.el-table--border {
  border-left: none;
}
::v-deep .el-radio__label {
  display: none;
}
</style>
