<template>
  <div>
    <lego-list-head
      :search.sync="search"
      :menu-code="menuCode"
      :create-fun="createClick"
      title="客户"
      placeholder="请输入名称"
      main-title="新建客户"
      @on-handle="listHeadHandle"
      @on-search="commonSearch"
      @on-export="exportInfos"/>
    <div
      v-empty="formErrorMsg != null || !auth.read"
      :xs-empty-text="auth.read ? formErrorMsg : '暂无权限'"
      xs-empty-icon="nopermission"
      class="lego-container">
      <lego-table-head
        ref="legoTableHead"
        :sort-data="sortData"
        :field-list="fieldList"
        :form-code="formCode"
        :menu-code="menuCode"
        @filter="handleFilter"
        @handle="actionHandle"
        @scene="handleScene"/>
      <el-table
        v-loading="loading"
        id="lego-table"
        :row-height="40"
        :data="list"
        :height="tableHeight"
        :cell-class-name="cellClassName"
        :row-key="unionKey"
        class="n-table--border"
        stripe
        border
        highlight-current-row
        style="width: 100%;z-index: 1;"
        @row-click="handleRowClick"
        @sort-change="sortChange"
        @header-dragend="handleHeaderDragend"
        @selection-change="handleSelectionChange">
        <el-table-column
          show-overflow-tooltip
          reserve-selection
          type="selection"
          align="center"
          width="55"/>
        <el-table-column
          v-for="(item, index) in fieldList"
          :key="index"
          :fixed="index==0"
          :prop="item.fieldCode"
          :label="item.name"
          :min-width="item.width"
          sortable="custom"
          show-overflow-tooltip>
          <template slot-scope="{ row, column, $index }">
            <field-view
              :props="item"
              :form-type="item.formType"
              :value="row[column.property]" >
              <template slot-scope="{ data }">
                {{ fieldFormatter(row, column, item) }}
              </template>
            </field-view>
          </template>
        </el-table-column>
        <el-table-column
          :resizable="false"
          fixed="right"
          width="40">
          <template
            slot="header"
            slot-scope="slot">
            <field-set
              :menu-code="menuCode"
              @change="setSave"/>
          </template>
        </el-table-column>
        <empty
          slot="empty"
          :props="{
            buttonTitle: '新建客户',
            showButton: auth.add
          }"
          @click="createClick"
        />
      </el-table>
      <div class="p-contianer">
        <el-pagination
          :current-page="currentPage"
          :page-sizes="pageSizes"
          :page-size.sync="pageSize"
          :total="total"
          :pager-count="5"
          class="p-bar"
          background
          layout="prev, pager, next, sizes, total, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"/>
      </div>
    </div>
    <detail
      v-if="showDetail"
      :code.sync="rowID"
      :union-key="unionKey"
      :menu-code="menuCode"
      :field-list="fieldList"
      :page-list="list"
      :page-index.sync="rowIndex"
      class="d-view"
      @handle="actionHandle"
      @hide-view="showDetail=false"/>
    <!-- 新建 -->
    <create
      v-if="createShow"
      :field-list="fieldList"
      @close="createShow = false"
      @handle="actionHandle"
    />
  </div>
</template>

<script>
import Create from './Create'
import Detail from './Detail'
import TableMixin from '@/components/Mixins/LegoIndex'
import {
  customerListAPI,
  customerDeleteAPI,
  customerExcelExportAPI,
  customerExcelAllExportAPI
} from '@/api/crm/customer'

export default {
  name: 'CrmCustomerIndex',
  components: {
    Detail,
    Create
  },
  mixins: [TableMixin],
  data() {
    return {
      createShow: false,
      menuCode: 'crm:customer', // 菜单编码
      unionKey: 'code', // 表格对象唯一键
      highlightColumnKey: 'code', // 表格需要高亮点击的列key
      listRequest: customerListAPI,
      deleteRequest: customerDeleteAPI,
      exportRequest: customerExcelExportAPI,
      exportAllRequest: customerExcelAllExportAPI
    }
  },
  methods: {
    /**
     * 新建点击
     */
    createClick() {
      this.createShow = true
    }
  }
}
</script>

<style lang="scss" scoped>
@import '@/styles/lego-table.scss';
</style>
