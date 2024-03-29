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
      ref="deptDialogRef"
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
          filterable
          multiple
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
import { depAddAPI, depModifyAPI } from '@/api/admin/dept'
import SelectTree from '@/components/NewCom/SelectTree'

export default {
  name: 'DeptDialog',
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
        parentCode: '',
        leader: '',
        enable: true
      },
      dialogRules: {
        code: [
          { required: true, message: '工号不能为空', trigger: 'blur' }
        ],
        name: [
          { required: true, message: '姓名不能为空', trigger: 'blur' }
        ],
        leader: [
          { required: true, message: '负责人不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    title() {
      return this.creatable ? '创建部门' : '修改部门'
    },
    fieldList() {
      return [
        { field: 'code', value: '部门编码', disabled: !this.creatable },
        { field: 'name', value: '部门姓名', disabled: false },
        { field: 'parentCode', value: '上级部门', type: 'selectTree', optionCode: 'deptList', disabled: false },
        { field: 'leader', value: '部门负责人', type: 'select', optionCode: 'employeeList', disabled: false },
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
          if (value && field.type === 'select') {
            this.submitForm[field.field] = value.code
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
      this.$refs.deptDialogRef.validate(valid => {
        if (!valid) {
          return
        }
        this.loading = true
        if (this.creatable) {
          depAddAPI(this.submitForm).then(res => {
            this.loading = false
            this.$emit('success')
            this.$message.success('新增成功')
          })
            .catch(() => {
              this.loading = false
            })
          return
        }
        depModifyAPI(this.submitForm).then(res => {
          this.loading = false
          this.$emit('success')
          this.$message.success('更新成功')
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
.new-dialog-form ::v-deep .el-form-item {
  width: 50%;
  margin: 0;
  padding-bottom: 10px;
}
.new-dialog-form ::v-deep .el-form-item .el-form-item__label {
  padding: 0;
}
.new-dialog-form {
  ::v-deep .el-form-item:nth-child(even) {
    padding-left: 15px;
  }

  ::v-deep .el-form-item:nth-child(odd) {
    padding-right: 15px;
  }
}
</style>
