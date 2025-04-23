<template>
  <el-dialog
    v-loading="loading"
    :title="title"
    :model-value="visible"
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
    <template #footer>
      <span class="dialog-footer">
        <el-button
          type="primary"
          @click="submit">保 存</el-button>
        <el-button @click="handleCancel">取 消</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { ElMessage } from 'element-plus'
import SelectTree from '@/components/Common/SelectTree'
import { employeeAddAPI, employeeModifyAPI } from '@/api/admin/employee'

const props = defineProps({
  creatable: {
    type: Boolean,
    default: true
  },
  visible: {
    type: Boolean,
    required: true
  },
  optionsList: {
    type: Object,
    required: true
  },
  data: {
    type: Object,
    default: () => ({})
  }
})

const emit = defineEmits(['update:visible', 'success'])

const loading = ref(false)
const employeeDialogRef = ref(null)
const submitForm = ref({
  code: '',
  name: '',
  dept: '',
  roles: [],
  enable: true
})

const dialogRules = ref({
  name: [
    { required: true, message: '姓名不能为空', trigger: 'blur' }
  ],
  dept: [
    { required: true, message: '部门不能为空', trigger: 'blur' }
  ],
  roles: [
    { required: true, message: '角色不能为空', trigger: 'blur' }
  ]
})

const title = computed(() => props.creatable ? '创建员工' : '修改员工')

const fieldList = computed(() => [
  { field: 'code', value: '工号', disabled: true },
  { field: 'name', value: '姓名', disabled: false },
  { field: 'dept', value: '部门', type: 'selectTree', optionCode: 'deptList', disabled: false },
  { field: 'roles', value: '角色', type: 'multipleSelect', optionCode: 'roleList', disabled: false },
  { field: 'enable', value: '状态', type: 'boolean', disabled: false }
])

watch(() => props.data, (val) => {
  if (!val) return

  fieldList.value.forEach(field => {
    let value = val[field.field]
    if (value && field.type === 'selectTree') {
      submitForm.value[field.field] = value.code
    } else if (value && field.type === 'multipleSelect') {
      submitForm.value[field.field] = []
      value.forEach(element => {
        submitForm.value[field.field].push(element.code)
      })
    } else {
      if (!value && field.type != 'boolean') {
        value = ''
      }
      submitForm.value[field.field] = value
    }
  })
}, { deep: true, immediate: true })

const handleCancel = () => {
  emit('update:visible', false)
}

const submit = () => {
  employeeDialogRef.value.validate(valid => {
    if (!valid) return

    loading.value = true
    const requestAPI = props.creatable ? employeeAddAPI : employeeModifyAPI

    requestAPI(submitForm.value).then(() => {
      loading.value = false
      ElMessage.success(props.creatable ? '新增成功' : '更新成功')
      emit('success')
    }).catch(() => {
      loading.value = false
    })
  })
}
</script>

<style lang="scss" scoped>
.new-dialog-form {
  height: 47vh;
  overflow-y: auto;
  padding: 20px;
}

.new-dialog-form :deep(.el-form-item) {
  width: 50%;
  margin: 0;
  padding-bottom: 10px;

  .el-form-item__label {
    padding: 0;
  }

  &:nth-child(even) {
    padding-left: 15px;
  }

  &:nth-child(odd) {
    padding-right: 15px;
  }
}
</style>
