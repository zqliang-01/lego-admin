<template>
  <div>
    <lego-list-head
      :search.sync="search"
      :menu-code="menuCode"
      :create-fun="createClick"
      title="线索（自定义）"
      placeholder="请输入名称"
      main-title="新建线索"
      @on-handle="listHeadHandle"
      @on-search="commonSearch"
      @on-export="exportInfos" />
    <div
      v-empty="formErrorMsg != null || !auth.read"
      xs-empty-icon="nopermission"
      :xs-empty-text="auth.read ? formErrorMsg : '暂无权限'"
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
        :data="pageList"
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
          <template slot-scope="{ row, column }">
            <field-view
              :props="item"
              :form-type="item.formType"
              :value="row[column.property]"
              @clickEntity="handleEntityClick" />
          </template>
        </el-table-column>
        <el-table-column
          :resizable="false"
          fixed="right"
          width="40">
          <template slot="header">
            <field-set
              :form-code="formCode"
              @change="setSave"/>
          </template>
        </el-table-column>
        <empty
          slot="empty"
          :props="{
            buttonTitle: '新建线索',
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
    <Lego-all-detail
      :visible.sync="relativeEntity.show"
      :detail-code.sync="relativeEntity.code"
      :form-code="relativeEntity.formCode"
      :page-codes="relativeEntity.pageCodes"
      class="d-view"
      @handle="actionHandle"
      @hide-view="relativeEntity.show=false"/>
    <!-- 新建 -->
    <lego-all-create
      :visible.sync="createShow"
      :form-code="formCode"
      @close="createShow = false"
      @handle="actionHandle"
    />
  </div>
</template>

<script>
import {
  leadListAPI,
  leadDeleteAPI,
  leadExcelExportAPI,
  leadExcelAllExportAPI
} from '@/api/crm/lead'
import IndexMixin from '@/components/lego/mixins/LegoIndex'

export default {
  name: 'CrmLeadIndex',
  mixins: [IndexMixin],
  data() {
    return {
      createShow: false,
      menuCode: 'crm_lead', // 菜单编码
      unionKey: 'code', // 表格对象唯一键
      listRequest: leadListAPI,
      deleteRequest: leadDeleteAPI,
      exportRequest: leadExcelExportAPI,
      exportAllRequest: leadExcelAllExportAPI
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
