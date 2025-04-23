<template>
  <el-dialog
    v-loading="loading"
    title="初始化字段选择"
    :visible="visible"
    :close-on-click-modal="false"
    :modal-append-to-body="true"
    :append-to-body="true"
    width="60%"
    @close="handleCancel">
    <el-table
      ref="SyncColumnTable"
      :data="columnData"
      :height="tableHeight"
      tooltip-effect="dark"
      style="width: 100%"
      @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" />
      <el-table-column prop="columnName" label="字段名称" width="120" />
      <el-table-column prop="columnComment" label="字段描述" width="120" />
      <el-table-column prop="columnType" label="物理类型" />
      <el-table-column label="是否主键">
        <template slot-scope="scope">{{ scope.row.pk ? '是' : '否' }}</template>
      </el-table-column>
      <el-table-column prop="required" label="是否必填">
        <template slot-scope="scope">{{ scope.row.required ? '是' : '否' }}</template>
      </el-table-column>
      <el-table-column prop="unique" label="是否唯一">
        <template slot-scope="scope">{{ scope.row.unique ? '是' : '否' }}</template>
      </el-table-column>
    </el-table>
    <span
      slot="footer"
      class="dialog-footer">
      <el-button
        type="primary"
        @click="handleSubmit">保 存</el-button>
      <el-button @click="handleCancel">取 消</el-button>
    </span>
  </el-dialog>
</template>
<script>
import { genTableSyncAPI } from '@/api/admin/genTable'
import { genTableMetaColumnListAPI } from '@/api/admin/genTableColumn'
import SelectTree from '@/components/Common/SelectTree'
import { ElLoading as Loading } from 'element-plus'

export default {
  name: 'TableSyncDialog',
  components: {
    SelectTree
  },
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    tableCode: String,
    selected: {
      type: Array,
      default: () => {
        return []
      }
    }
  },
  data() {
    return {
      loading: false,
      columnData: [],
      submitData: [],
      tableHeight: document.documentElement.clientHeight - 320
    }
  },
  watch: {
    visible(val) {
      if (val) {
        this.loading = true
        genTableMetaColumnListAPI(this.tableCode).then(res => {
          this.columnData = res.data
          this.handleDefaultSelection()
          this.loading = false
        }).catch(() => {
          this.loading = false
        })
      }
    }
  },
  methods: {
    handleCancel() {
      this.$emit('update:visible', false)
    },
    handleSelectionChange(val) {
      this.submitData = val.map(c => c.columnName)
    },
    handleDefaultSelection() {
      this.columnData.forEach(row => {
        if (this.selected.some(select => select.name === row.columnName)) {
          this.$nextTick(() => {
            this.$refs.SyncColumnTable.toggleRowSelection(row, true)
          })
        }
      })
    },
    handleSubmit() {
      if (this.submitData.length === 0) {
        this.$message.error('请选择初始化字段！')
        return
      }
      const loading = Loading.service({ fullscreen: true, text: '初始化中，请稍后...' })
      genTableSyncAPI(this.tableCode, this.submitData).then(() => {
        loading.close()
        this.$message.success('初始化成功')
        this.$emit('success')
        this.$emit('update:visible', false)
      }).catch(() => {
        loading.close()
      })
    }
  }
}
</script>
<style lang="scss" scoped>
</style>
