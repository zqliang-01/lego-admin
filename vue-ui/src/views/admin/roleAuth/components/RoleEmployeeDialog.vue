<template>
  <el-dialog
    v-loading="loading"
    :visible="visible"
    :append-to-body="true"
    :close-on-click-modal="false"
    :modal-append-to-body="true"
    title="关联员工"
    width="30%"
    @close="handleCancel">
    <el-form
      ref="dialogRef"
      :inline="true"
      :model="submitForm"
      :rules="dialogRules"
      class="new-dialog-form"
      label-position="top">
      <el-form-item
        label="选择员工"
        prop="codes">
        <el-select
          v-model="submitForm.codes"
          filterable
          multiple
          style="width: 100%;">
          <el-option
            v-for="optionItem in optionsList"
            :key="optionItem.code"
            :label="optionItem.name"
            :value="optionItem.code" />
        </el-select>
      </el-form-item>
    </el-form>
    <span
      slot="footer"
      class="dialog-footer">
      <el-button
        type="primary"
        @click="handleConfirm">确 定</el-button>
      <el-button @click="handleCancel">取 消</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { employeeRoleModifyAPI, employeeSimpleListAPI } from '@/api/admin/employee'
import { objDeepCopy } from '@/utils'

export default {
  name: 'RoleDialog',
  props: {
    code: String,
    codes: Array,
    visible: {
      type: Boolean,
      required: true,
      default: false
    }
  },
  data() {
    return {
      loading: false,
      optionsList: [],
      submitForm: {
        codes: ''
      },
      dialogRules: {
      }
    }
  },
  watch: {
    codes() {
      this.submitForm = { codes: objDeepCopy(this.codes) }
      employeeSimpleListAPI().then(res => {
        this.optionsList = res.data
      })
    }
  },
  methods: {
    getSubmitForm() {
      return { roleCode: this.code, employeeCodes: this.submitForm.codes, action: 'add' }
    },
    handleConfirm() {
      this.$refs.dialogRef.validate(valid => {
        if (!valid) {
          return
        }
        this.loading = true
        employeeRoleModifyAPI(this.getSubmitForm()).then(res => {
          this.loading = false
          this.$message.success('操作成功')
          this.$emit('success')
          this.handleCancel()
        }).catch(() => {
          this.loading = false
        })
      })
    },
    handleCancel() {
      this.$emit('update:visible', false)
    }
  }
}
</script>

<style lang="scss" scoped>
.new-dialog-form {
  padding: 0px;
}
.new-dialog-form ::v-deep .el-form-item {
  width: 100%;
  margin: 0;
  padding-bottom: 10px;
}
.new-dialog-form ::v-deep .el-form-item .el-form-item__label {
  padding: 0;
}
</style>
