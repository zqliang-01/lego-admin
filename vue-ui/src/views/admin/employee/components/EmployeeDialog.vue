<template>
  <el-dialog
    v-loading="loading"
    :title="title"
    :visible="visible"
    :close-on-click-modal="false"
    :modal-append-to-body="true"
    :append-to-body="true"
    width="60%"
    @close="handleCancel">
    <el-form
      ref="employeeDialogRef"
      :inline="true"
      :model="submitForm"
      :rules="dialogRules"
      class="new-dialog-form"
      label-width="80px"
      label-position="top">
      <el-form-item
        v-for="(item, index) in fieldList"
        :label="item.value"
        :prop="item.field"
        :key="index">
        <span slot="label">{{ item.value }}</span>
        <el-select
          v-if="item.type == 'select'"
          v-model="submitForm[item.field]"
          :disabled="item.disabled"
          filterable
          style="width: 100%;">
          <el-option
            v-for="optionItem in optionsList[item.optionCode]"
            :key="optionItem.code"
            :label="optionItem.name"
            :value="optionItem.code" />
        </el-select>
        <el-select
          v-else-if="item.type == 'multipleSelect'"
          v-model="submitForm[item.field]"
          :disabled="item.disabled"
          multiple
          filterable
          style="width: 100%;">
          <el-option
            v-for="optionItem in optionsList[item.optionCode]"
            :key="optionItem.code"
            :label="optionItem.name"
            :value="optionItem.code" />
        </el-select>
        <select-tree
          v-else-if="item.type == 'selectTree'"
          v-model="submitForm[item.field]"
          :options="optionsList[item.optionCode]"
          :disabled="item.disabled"
          filterable
        />
        <el-switch
          v-else-if="item.type == 'boolean'"
          v-model="submitForm[item.field]"
          :disabled="item.disabled" />
        <el-input
          v-else
          v-model="submitForm[item.field]"
          :disabled="item.disabled"/>
      </el-form-item>
    </el-form>
    <span
      slot="footer"
      class="dialog-footer">
      <el-button
        type="primary"
        @click="submit">保 存</el-button>
      <el-button @click="handleCancel">取 消</el-button>
    </span>
  </el-dialog>
</template>
<script>
import { employeeAddAPI, employeeModifyAPI } from '@/api/admin/employee'
import SelectTree from '@/components/SelectTree'

export default {
  name: 'EmployeeDialog',
  components: {
    SelectTree
  },
  props: {
    creatable: {
      type: Boolean,
      default: true
    },
    visible: {
      type: Boolean,
      default: false
    },
    optionsList: Object,
    data: Object
  },
  data() {
    return {
      loading: false,
      submitForm: {
        code: '',
        name: '',
        dept: '',
        roles: [],
        enable: true
      },
      dialogRules: {
        name: [
          { required: true, message: '姓名不能为空', trigger: 'blur' }
        ],
        dept: [
          { required: true, message: '部门不能为空', trigger: 'blur' }
        ],
        roles: [
          { required: true, message: '角色不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    title() {
      return this.creatable ? '创建员工' : '修改员工'
    },
    fieldList() {
      return [
        { field: 'code', value: '工号', disabled: true },
        { field: 'name', value: '姓名', disabled: false },
        { field: 'dept', value: '部门', type: 'selectTree', optionCode: 'deptList', disabled: false },
        { field: 'roles', value: '角色', type: 'multipleSelect', optionCode: 'roleList', disabled: false },
        { field: 'enable', value: '状态', type: 'boolean', disabled: false }
      ]
    }
  },
  watch: {
    data: {
      handler() {
        if (!this.data) {
          return
        }
        this.fieldList.forEach(field => {
          let value = this.data[field.field]
          if (value && field.type === 'selectTree') {
            this.submitForm[field.field] = value.code
          } else if (value && field.type === 'multipleSelect') {
            this.submitForm[field.field] = []
            value.forEach(element => {
              this.submitForm[field.field].push(element.code)
            })
          } else {
            if (!value && field.type != 'boolean') {
              value = ''
            }
            this.submitForm[field.field] = value
          }
        })
      },
      deep: true,
      immediate: true
    }
  },
  methods: {
    handleCancel() {
      this.$emit('update:visible', false)
    },
    submit() {
      this.$refs.employeeDialogRef.validate(valid => {
        if (!valid) {
          return
        }
        this.loading = true
        if (this.creatable) {
          employeeAddAPI(this.submitForm).then(res => {
            this.loading = false
            this.$message.success('新增成功')
            this.$emit('success')
          })
            .catch(() => {
              this.loading = false
            })
          return
        }
        employeeModifyAPI(this.submitForm).then(res => {
          this.loading = false
          this.$message.success('更新成功')
          this.$emit('success')
        })
          .catch(() => {
            this.loading = false
          })
      })
    }
  }
}
</script>
<style lang="scss" scoped>
/* 新建和编辑 */
.new-dialog-form {
  height: 47vh;
  overflow-y: auto;
  padding: 20px;
}
.new-dialog-form /deep/ .el-form-item {
  width: 50%;
  margin: 0;
  padding-bottom: 10px;
}
.new-dialog-form /deep/ .el-form-item .el-form-item__label {
  padding: 0;
}
.new-dialog-form {
  /deep/ .el-form-item:nth-child(even) {
    padding-left: 15px;
  }

  /deep/ .el-form-item:nth-child(odd) {
    padding-right: 15px;
  }
}
</style>
