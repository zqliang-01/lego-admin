<template>
  <div class="system-customer">
    <xr-header
      :icon-class="'icon-list'"
      :show-search="true"
      :show-save="manage.customForm.update ? true : false"
      icon-color="#1CBAF5"
      label="自定义表单设置"
      @search="onSearch"
      @create="onCreate" />
    <div class="customer-content">
      <el-table
        v-loading="loading"
        :data="formList"
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
              :value="row[item.fieldCode]">
              <img
                v-if="item.fieldCode == 'name'"
                slot="leftContent"
                :src="getCustomFieldIcon( row.label)"
                class="table-item-icon" >
            </field-view>
          </template>
        </el-table-column>

        <el-table-column
          fixed="right"
          label="操作"
          width="200">
          <template slot-scope="scope">
            <el-button
              v-if="manage.customForm.update"
              type="text"
              size="small"
              @click="handleCustomField('edit', scope.row, scope.$index)">编辑</el-button>
            <el-button
              v-if="manage.customForm.design"
              type="text"
              size="small"
              @click="handleCustomField('design', scope.row, scope.$index)">设计表单</el-button>
            <el-button
              v-if="manage.customForm.update"
              type="text"
              size="small"
              @click="handleCustomField('delete', scope.row, scope.$index)">删除</el-button>
          </template>
        </el-table-column>
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

    <!-- 新建和编辑 -->
    <el-dialog
      v-if="formDialogShow"
      :title="formDialogTitle"
      :visible.sync="formDialogShow"
      :close-on-click-modal="false"
      :modal-append-to-body="true"
      :append-to-body="true"
      width="35%">
      <el-form
        ref="dialogRef"
        :inline="true"
        :model="formModel"
        :rules="formDialogRules"
        class="new-dialog-form"
        label-width="80px"
        label-position="top">
        <el-form-item label="数据表" prop="tableCode" >
          <el-select
            v-model="formModel.tableCode"
            :disabled="formDialogType != 'add'"
            style="width: 100%;"
            @change="handleTableChange">
            <el-option
              v-for="item in tableList"
              :key="item.code"
              :label="item.name"
              :value="item.code"/>
          </el-select>
        </el-form-item>
        <el-form-item label="名称" prop="name" >
          <el-input v-model="formModel.name"/>
        </el-form-item>
        <el-form-item label="菜单" prop="permissionCode" >
          <select-tree
            v-model="formModel.permissionCode"
            :options="permissions"
          />
        </el-form-item>
      </el-form>
      <span
        slot="footer"
        class="dialog-footer">
        <el-button
          type="primary"
          @click="formDialogSubmit">保 存</el-button>
        <el-button @click="formDialogShow = false">取 消</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  customFormListAPI,
  customFormModifyAPI,
  customFormAddAPI,
  customFormDeleteAPI
} from '@/api/admin/formField'
import { genTableNotExistsListAPI } from '@/api/admin/genTable'
import { permissionMenuListAPI } from '@/api/admin/permission'
import { mapGetters } from 'vuex'
import XrHeader from '@/components/XrHeader'
import SelectTree from '@/components/SelectTree'
import FieldView from '@/components/NewCom/Form/FieldView'

export default {
  name: 'CustomField',
  components: {
    XrHeader,
    SelectTree,
    FieldView
  },
  computed: {
    ...mapGetters(['manage'])
  },
  data() {
    return {
      loading: false,
      tableHeight: document.documentElement.clientHeight - 165, // 表的高度
      // 自定义字段设置
      formList: [],
      tableList: [],
      currentPage: 1,
      pageSize: 15,
      pageSizes: [15, 30, 60, 100],
      total: 0,
      search: '', // 搜索内容
      formModel: {
        code: '',
        name: '',
        tableCode: '',
        permissionCode: ''
      },
      permissions: [],
      fieldList: [
        { fieldCode: 'name', name: '表单名称', formType: 'text', width: '150' },
        { fieldCode: 'table', name: '数据表', formType: 'select', width: '150' },
        { fieldCode: 'permission', name: '归属菜单', formType: 'select', width: '150' },
        { fieldCode: 'createTime', name: '创建时间', formType: 'text', width: '150' }
      ],
      formDialogShow: false,
      formDialogType: 'add',
      formDialogTitle: '新增表单',
      formDialogRules: {
        name: [
          { required: true, message: '表单名称不能为空', trigger: 'blur' }
        ],
        tableCode: [
          { required: true, message: '数据表不能为空', trigger: 'blur' }
        ],
        permissionCode: [
          { required: true, message: '菜单不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  watch: {
    formDialogType(val) {
      if (val === 'add') {
        this.formDialogTitle = '新增表单'
        return
      }
      this.formDialogTitle = '修改表单'
    }
  },
  created() {
    // 控制table的高度
    window.onresize = () => {
      self.tableHeight = document.documentElement.clientHeight - 140
    }
    permissionMenuListAPI().then(res => {
      this.permissions = res.data
    })
    this.refresh()
  },

  methods: {
    refresh() {
      genTableNotExistsListAPI().then(res => {
        this.tableList = res.data
      })
      this.getList()
    },
    /**
     * 详情
     */
    getList(value) {
      this.loading = true
      customFormListAPI({ name: value })
        .then(res => {
          this.formList = res.data.result
          this.total = res.data.totalCount
          this.currentPage = res.data.pageIndex
          this.loading = false
        })
        .catch(() => {
          this.loading = false
        })
    },

    /**
     * 更改每页展示数量
     * @param {*} val
     */
    handleSizeChange(val) {
      this.pageSize = val
      this.getList()
    },

    /**
     * 更改当前页数
     * @param {*} val
     */
    handleCurrentChange(val) {
      this.currentPage = val
      this.getList()
    },

    handleTableChange(val) {
      const table = this.tableList.find(table => table.code === val)
      if (table) {
        this.formModel.permissionCode = table.permissionCode
        this.formModel.name = table.name + '表单'
      }
    },
    /**
     * 列表的编辑和预览
     */
    handleCustomField(type, item, index) {
      if (type === 'design') {
        this.$router.push({
          name: 'customField',
          params: {
            formCode: item.code,
            tableName: item.table.name,
            tableCode: item.table.code
          }
        })
        return
      }
      if (type === 'delete') {
        this.$confirm('此操作将永久删除[' + item.name + ']表单，是否继续?', '提示', {
          type: 'warning'
        }).then(() => {
          this.loading = true
          customFormDeleteAPI(item.code).then(res => {
            this.$message.success('删除成功！')
            this.refresh()
            this.loading = false
          })
            .catch(() => {
              this.loading = false
            })
        })
        return
      }
      this.formDialogType = 'edit'
      this.formDialogShow = true
      this.formModel.code = item.code
      this.formModel.name = item.name
      this.formModel.tableCode = item.table.code
      this.formModel.permissionCode = ''
      if (item.permission) {
        this.formModel.permissionCode = item.permission.code
      }
    },

    /**
     * 根据自定义字段types 获取展示icon
     */
    getCustomFieldIcon(label) {
      return require('@/assets/img/crm/product.png')
    },

    onSearch(value) {
      this.getList(value)
    },

    onCreate() {
      this.formDialogType = 'add'
      this.formDialogShow = true
      this.formModel = {}
    },

    // 用户新建
    formDialogSubmit() {
      this.$refs.dialogRef.validate(valid => {
        if (!valid) {
          return
        }
        this.loading = true
        if (this.formDialogType === 'edit') {
          customFormModifyAPI(this.formModel).then(res => {
            this.$message.success('操作成功')
            this.getList()
            this.formDialogShow = false
            this.loading = false
          })
            .catch(() => {
              this.loading = false
            })
          return
        }
        customFormAddAPI(this.formModel).then(res => {
          this.$message.success('操作成功')
          this.refresh()
          this.formDialogShow = false
          this.loading = false
        })
          .catch(() => {
            this.loading = false
          })
      })
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.system-customer {
  height: 100%;
}
.customer-content {
  border-top: 1px solid $xr-border-line-color;
  border-bottom: 1px solid $xr-border-line-color;
}

.table-item-label {
  vertical-align:middle;
}
.table-item-icon {
  width: 30px;
  height: 30px;
  margin-right: 8px;
  vertical-align:middle;
  border-radius: 4px;
}

.table-item-time {
  color: #999;
}

.el-table {
  /deep/ .el-table__body td {
    height: 60px;
  }
}

/* 新建和编辑 */
.new-dialog-form {
  overflow-y: auto;
  padding: 20px;
}
.new-dialog-form /deep/ .el-form-item {
  width: 100%;
  margin: 0;
  padding-bottom: 10px;
}
.new-dialog-form /deep/ .el-form-item .el-form-item__label {
  padding: 0;
}
</style>
