<template>
  <div>
    <el-table
      v-loading="loading"
      :data="dataList"
      :height="tableHeight"
      highlight-current-row
      style="width: 100%">
      <el-table-column
        v-for="(item, index) in fieldList"
        :key="index"
        :min-width="item.width"
        :prop="item.fieldCode"
        :label="item.name"
        show-overflow-tooltip>
        <template slot-scope="{ row, column, $index }">
          <field-view
            :props="item"
            :form-type="item.formType"
            :value="row[item.fieldCode]"/>
        </template>
      </el-table-column>
      <el-table-column
        fixed="right"
        label="操作"
        width="200">
        <template slot-scope="scope">
          <slot :row="scope.row" />
        </template>
      </el-table-column>
    </el-table>
    <div class="p-contianer">
      <el-pagination
        :current-page="currentPage"
        :page-sizes="pageSizes"
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
</template>

<script>
import FieldView from '@/components/NewCom/Form/FieldView'

export default {
  name: 'CustomField',
  components: {
    FieldView
  },
  props: {
    loading: {
      type: Boolean,
      default: false
    },
    dataList: Array,
    fieldList: Array,
    currentPage: {
      type: Number,
      default: 1
    },
    pageSize: {
      type: Number,
      default: 15
    },
    total: {
      type: Number,
      default: 15
    }
  },
  data() {
    return {
      tablePageSize: 15,
      tableCurrentPage: 1,
      tableHeight: document.documentElement.clientHeight - 165, // 表的高度
      pageSizes: [15, 30, 60, 100]
    }
  },
  created() {
    window.onresize = () => {
      self.tableHeight = document.documentElement.clientHeight - 140
    }
    this.tablePageSize = this.pageSize
    this.tableCurrentPage = this.currentPage
    this.getList()
  },
  methods: {
    getList() {
      this.$emit('onList', this.tablePageSize, this.tableCurrentPage)
    },
    handleSizeChange(val) {
      this.tablePageSize = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.tableCurrentPage = val
      this.getList()
    },
    handleTable(type, item, index) {
      this.$emit('onEdit', type, item, index)
    }
  }
}
</script>
