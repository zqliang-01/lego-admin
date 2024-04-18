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
      @select-all="selectAll"
      @selection-change="handleSelectionChange"
      @row-click="handleRowClick">
      <el-table-column
        show-overflow-tooltip
        type="selection"
        align="center"
        width="55"/>
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
    /** 多选框 只能选一个 */
    radio: {
      type: Boolean,
      default: true
    },
    /** 已选信息 */
    selectedData: {
      type: Array,
      default: () => {
        return []
      }
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
      otherItems: []
    }
  },
  computed: {
  },
  watch: {
  },
  mounted() {
    this.refreshList()
  },
  methods: {
    refreshList() {
      this.currentPage = 1
      this.getList()
    },
    getList() {
      if (!this.queryApiUrl) {
        return
      }
      this.loading = true
      const params = {
        search: this.searchContent,
        pageIndex: this.currentPage,
        pageSize: 10
      }
      postRequest(this.queryApiUrl, params).then(res => {
        this.dataList = res.data.result
        if (this.selectedData && this.selectedData.length > 0) {
          this.checkItemsWithSelectedData()
        } else {
          this.dataList = res.data.result
        }
        this.totalPage = Math.ceil(res.data.totalCount / 10)
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    // 标记选择数据
    checkItemsWithSelectedData() {
      const selectedArray = this.selectedData ? this.selectedData.map(item => {
        item.has = false
        return item
      }) : []

      const selectedRows = []
      this.dataList.forEach((item, index) => {
        selectedArray.forEach((selectedItem, selectedIndex) => {
          if (item.code == selectedItem.code) {
            selectedItem.has = true
            selectedRows.push(item)
          }
        })
      })

      this.otherItems = []
      selectedArray.forEach((selectedItem, selectedIndex) => {
        if (!selectedItem.has) {
          this.otherItems.push(selectedItem)
        }
      })

      this.$nextTick(() => {
        this.$refs.relativeTable.clearSelection()
        selectedRows.forEach(row => {
          this.$refs.relativeTable.toggleRowSelection(row, true)
        })
      })
    },
    /** 列表操作 */
    // 当某一行被点击时会触发该事件
    handleRowClick(row, column, event) {},
    // 当选择项发生变化时会触发该事件
    handleSelectionChange(val) {
      let selectedItem = []
      if (this.radio) {
        val.forEach((row, index) => {
          if (index === val.length - 1) return
          this.$refs.relativeTable.toggleRowSelection(row, false)
        })
        if (val.length > 0) {
          selectedItem = val.length === 1 ? val : [val[val.length - 1]]
        }
      } else {
        selectedItem = this.otherItems.concat(val)
      }
      this.$emit('changeCheckout', selectedItem)
    },
    clearAll() {
      this.$refs.relativeTable.clearSelection()
    },
    // 	当用户手动勾选全选 Checkbox 时触发的事件
    selectAll() {},
    // 进行搜索操作
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
</style>
