<template>
  <div>
    <div class="page-query">
      <el-form ref="queryForm" :model="queryForm.param">
        <item
          v-for="(item, index) in paramList"
          :key="index"
          :code="item.sqlKey"
          :required="item.required"
          :width="80"
          :label="item.name">
          <el-input
            v-if="item.type == 'string'"
            v-model="queryForm.param[item.sqlKey]"
            class="input-width"
            placeholder="请输入"
          />
          <el-select
            v-if="item.type == 'select'"
            v-model="queryForm.param[item.sqlKey]"
            class="input-width"
            filterable
            clearable>
            <el-option
              v-for="option in item.setting"
              :key="option.code"
              :value="option.code"
              :label="option.name"
            />
          </el-select>
          <el-date-picker
            v-if="item.type == 'date'"
            v-model="queryForm.param[item.sqlKey]"
            value-format="yyyy-MM-dd"
            type="date"
            class="input-width"/>
          <el-switch
            v-if="item.type == 'boolean'"
            v-model="queryForm.param[item.sqlKey]" />
        </item>
        <div class="page-query-btn">
          <el-button
            type="primary"
            @click="init">查询</el-button>
          <el-button
            v-if="auth.export"
            type="warning"
            @click="handleExport">导出</el-button>
        </div>
      </el-form>
    </div>
    <div class="page-body">
      <el-table
        v-loading="loading"
        border
        :height="tableHeight"
        :data="dataList">
        <el-table-column show-overflow-tooltip
          v-for="(item, index) in columnList"
          :key="index"
          :min-width="100"
          :prop="item.sqlKey"
          :label="item.name"
          :align="item.align"
        />
      </el-table>
      <div class="p-contianer">
        <el-pagination
          :current-page="queryForm.pageIndex"
          :page-sizes="pageSizes"
          :page-size.sync="queryForm.pageSize"
          :total="dataTotal"
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
  openPageAPI,
  openExportAPI,
  openConditionListAPI
} from '@/api/report/open'
import { definitionPermissionGetAPI } from '@/api/report/definition'
import Item from './item'
import { getMenuAuth } from '@/utils/auth'
import { downloadExcelWithResData } from '@/utils'

export default {
  name: 'LegoReportIndex',
  components: { Item },
  data() {
    return {
      loading: false,
      title: '',
      columnList: [],
      paramList: [],
      dataList: [],
      dataTotal: 0,
      pageSizes: [15, 30, 60, 100],
      tableHeight: document.documentElement.clientHeight - 250,
      queryForm: {
        permissionCode: '',
        param: {},
        pageIndex: 1,
        pageSize: 15
      }
    }
  },
  computed: {
    auth() {
      return getMenuAuth(this.$route.params.menuCode)
    }
  },
  watch: {
    $route: function(val) {
      this.getDefinition()
    }
  },
  mounted() {
    this.queryForm.permissionCode = this.$route.params.menuCode
    this.getDefinition()
    this.init()
  },
  methods: {
    init() {
      this.queryForm.pageIndex = 1
      this.handleQuery()
    },
    getDefinition() {
      definitionPermissionGetAPI({
        permissionCode: this.$route.params.menuCode
      }).then(res => {
        this.title = res.data.name
        this.columnList = res.data.titles
        this.queryForm.permissionCode = this.$route.params.menuCode
        this.paramList = this.initParamList(res.data.params)
      })
    },
    initParamList(params) {
      params.forEach(param => {
        if (param.type === 'select') {
          openConditionListAPI({
            permissionCode: this.$route.params.menuCode,
            conditionCode: param.code
          }).then(res => {
            param.setting = res.data
            this.$set(this.queryForm.param, param.sqlKey, param.defaultValue)
          })
        } else if (param.type === 'boolean') {
          this.$set(this.queryForm.param, param.sqlKey, param.defaultValue === 'true')
        } else {
          this.$set(this.queryForm.param, param.sqlKey, param.defaultValue)
        }
      })
      return params
    },
    handleQuery() {
      const queryForm = this.$refs.queryForm
      if (queryForm) {
        queryForm.validate(valid => {
          if (!valid) {
            return false
          }
          this.loading = true
          openPageAPI(this.queryForm).then((res) => {
            this.loading = false
            this.dataList = res.data.records
            this.dataTotal = parseInt(res.data.total)
          }).catch(() => {
            this.loading = false
          })
        })
      }
    },
    handleExport() {
      this.loading = true
      openExportAPI(this.queryForm).then(res => {
        this.loading = false
        downloadExcelWithResData(res)
      }).then(() => {
        this.loading = false
      })
    },
    handleSizeChange(val) {
      this.queryForm.pageSize = val
      this.handleQuery()
    },
    handleCurrentChange(val) {
      this.queryForm.pageIndex = val
      this.handleQuery()
    }
  }
}
</script>
<style lang="scss" scoped>
.input-width {
  width: 150px;
}
::v-deep .el-form-item {
  margin-bottom: 0px;
}
.page-query {
  position: relative;
  margin: 15px;
  padding: 10px 15px;
  background: no-repeat top right, #ffffff;
  background-size: auto 80px;
  border-radius: 12px;
  box-shadow: 0 4px 5px 0px rgba(152, 152, 152, 0.1);
}

.page-query-btn {
  padding: 10px;
  display: inline-block;
}

.page-body {
  margin: 15px;
  padding: 15px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 4px 5px 0px rgba(152, 152, 152, 0.1);
}
::v-deep .el-dialog__body{
  text-align: center;
  .el-radio__label{
    font-size: 18px;
  }
}
</style>
