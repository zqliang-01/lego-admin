<template>
  <el-dialog
    v-loading="loading"
    :visible="visible"
    :append-to-body="true"
    :close-on-click-modal="false"
    :modal-append-to-body="true"
    :title="type == 'role' ? '编辑角色' : '关联员工'"
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
        :label="type == 'role' ? '设置角色' : '选择员工'"
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
import { roleSimpleListAPI } from '@/api/admin/role'
import { objDeepCopy } from '@/utils'

export default {
  name: 'RoleDialog',
  props: {
    type: String,
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
      if (this.type == 'role') {
        roleSimpleListAPI().then(res => {
          this.optionsList = res.data
        })
        return
      }
      employeeSimpleListAPI().then(res => {
        this.optionsList = res.data
      })
    }
  },
  methods: {
    getSubmitForm() {
      const forms = []
      if (this.type == 'role') {
        forms.push({ code: this.code, codes: this.submitForm.codes, cleanable: true })
        return forms
      }
      this.submitForm.codes.forEach(item => {
        forms.push({ code: item, codes: [this.code], cleanable: false })
      })
      return forms
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
