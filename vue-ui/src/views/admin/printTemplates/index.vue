<template>
  <flexbox
    class="main"
    direction="column"
    align="stretch">
    <xr-header
      :icon-class="'print'"
      icon-color="#26d4da"
      label="自定义打印模板" />
    <div class="main__bd">
      <div class="se-table-header">
        <el-button
          class="se-table-header-button xr-btn--orange"
          type="primary"
          icon="el-icon-plus"
          @click="addClick">新建打印模板</el-button>
      </div>
      <el-table
        v-loading="loading"
        id="examine-table"
        :data="list"
        :height="tableHeight"
        :cell-class-name="cellClassName"
        class="main-table"
        stripe
        highlight-current-row
        style="width: 100%"
        @row-click="handleRowClick">
        <el-table-column
          v-for="(item, index) in fieldList"
          :key="index"
          :formatter="fieldFormatter"
          :prop="item.prop"
          :min-width="item.width"
          :label="item.label"
          show-overflow-tooltip/>
        <el-table-column
          fixed="right"
          label="操作"
          width="250">
          <template slot-scope="scope">
            <el-button
              type="text"
              size="small"
              @click="handleClick('edit', scope)">编辑名称</el-button>
            <el-button
              type="text"
              size="small"
              @click="handleClick('copy', scope)">复制</el-button>
            <el-button
              type="text"
              size="small"
              @click="handleClick('delete', scope)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="p-contianer">
        <el-pagination
          :current-page="currentPage"
          :page-sizes="pageSizes"
          :page-size.sync="pageSize"
          :total="total"
          class="p-bar"
          background
          layout="prev, pager, next, sizes, total, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"/>
      </div>
      <template-type-add
        :visible.sync="templateAddShow"
        :detail="editData"
        @save="refreshList"
        @next="createNext"/>
    </div>
  </flexbox>
</template>

<script>
import XrHeader from '@/components/XrHeader'
import TemplateTypeAdd from './components/TemplateTypeAdd'

import {
  printTemplateListAPI,
  printDeleteTemplateAPI,
  printCopyTemplateAPI } from '@/api/admin/printTemplate'

export default {
  components: {
    XrHeader,
    TemplateTypeAdd
  },
  data() {
    return {
      loading: false, // 加载动画
      tableHeight: document.documentElement.clientHeight - 240, // 表的高度
      list: [],
      fieldList: [
        {
          prop: 'templateName',
          label: '模板名称',
          width: 150
        },
        {
          prop: 'type',
          label: '关联对象',
          width: 150
        },
        {
          prop: 'createTime',
          label: '创建时间',
          width: 150
        },
        {
          prop: 'createUserName',
          label: '创建人',
          width: 150
        },
        {
          prop: 'updateTime',
          label: '更新时间',
          width: 150
        }
      ],
      currentPage: 1,
      pageSize: 10,
      pageSizes: [10, 20, 30, 40],
      total: 0,
      // 新建
      editData: null,
      templateAddShow: false
    }
  },
  mounted() {
    // 控制table的高度
    window.onresize = () => {
      this.tableHeight = document.documentElement.clientHeight - 240
    }

    this.getList()
  },
  methods: {
    /**
     * 更改每页展示数量
     */
    handleSizeChange(val) {
      this.pageSize = val
      this.getList()
    },

    /**
     * 更改当前页数
     */
    handleCurrentChange(val) {
      this.currentPage = val
      this.getList()
    },

    refreshList() {
      this.handleCurrentChange(1)
    },

    /**
     * 数据获取
     */
    getList() {
      this.loading = true
      printTemplateListAPI({
        page: this.currentPage,
        limit: this.pageSize
      })
        .then(res => {
          this.list = res.data.list

          this.total = res.data.totalRow

          this.loading = false
        })
        .catch(() => {
          this.loading = false
        })
    },

    /**
     * 格式化字段
     */
    fieldFormatter(row, column) {
      if (column.property === 'type') {
        return {
          5: '商机',
          6: '合同',
          7: '回款',
          19: '供应商'
        }[row[column.property]]
      }
      return row[column.property]
    },

    /**
     * 通过回调控制class
     */
    cellClassName({ row, column, rowIndex, columnIndex }) {
      if (column.property === 'templateName') {
        return 'can-visit--underline'
      } else {
        return ''
      }
    },

    /**
     * 当某一行被点击时会触发该事件
     */
    handleRowClick(row, column, event) {
      if (column.property === 'templateName') {
        this.$router.push({
          name: 'crmPrintDetail',
          query: {
            handle: 'detail',
            templateName: row.templateName,
            templateId: row.templateId,
            type: row.type
          }
        })
      }
    },

    /** ***** 操作 ******/
    addClick() {
      this.editData = null
      this.templateAddShow = true
    },

    createNext(data) {
      this.$router.push({
        name: 'crmPrintDetail',
        query: {
          handle: 'create',
          ...data
        }
      })
    },

    handleClick(type, scope) {
      if (type === 'edit') {
        this.editData = scope.row
        this.templateAddShow = true
      } else if (type === 'delete') {
        // 启用停用
        this.$confirm('确定删除?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
          .then(() => {
            this.loading = true
            printDeleteTemplateAPI({
              templateId: scope.row['templateId']
            })
              .then(res => {
                this.list.splice(scope.$index, 1)
                if (this.list.length == 0) {
                  this.currentPage = this.currentPage - 1 > 0 ? this.currentPage - 1 : 1
                }
                this.getList()
                this.$message({
                  type: 'success',
                  message: '操作成功'
                })
                this.loading = false
              })
              .catch(() => {
                this.loading = false
              })
          })
          .catch(() => {
            this.$message({
              type: 'info',
              message: '已取消操作'
            })
          })
      } else if (type === 'copy') {
        this.loading = true
        printCopyTemplateAPI({
          templateId: scope.row['templateId']
        })
          .then(res => {
            this.getList()
            this.$message({
              type: 'success',
              message: '操作成功'
            })
            this.loading = false
          })
          .catch(() => {
            this.loading = false
          })
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.main {
  height:100%;

  ::v-deep .xr-header {
    padding: 15px 30px;
  }

  &__bd {
    border-top: 1px solid $xr-border-line-color;
    border-bottom: 1px solid $xr-border-line-color;
  }
}

.main-table {
  border-top: 1px solid #e6e6e6;
}

.se-table-header {
  height: 50px;
  background-color: white;
  position: relative;
  .se-table-header-button {
    float: right;
    margin-right: 20px;
    margin-top: 10px;
  }
}

.p-contianer {
  position: relative;
  background-color: white;
  height: 44px;
  .p-bar {
    float: right;
    margin: 5px 100px 0 0;
    font-size: 14px !important;
  }
}

</style>

