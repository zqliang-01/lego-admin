<template>
  <el-dialog
    v-loading="loading"
    :model-value="visible"
    :append-to-body="true"
    :close-on-click-modal="false"
    :modal-append-to-body="true"
    :title="creatable ? '创建角色' : '修改角色'"
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
    <template #footer>
      <span class="dialog-footer">
        <el-button
          type="primary"
          @click="handleConfirm">确 定</el-button>
        <el-button @click="handleCancel">取 消</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { ElMessage } from 'element-plus'
import {
  roleAddAPI,
  roleModifyAPI
} from '@/api/admin/role'
import { objDeepCopy } from '@/utils'

const props = defineProps({
  creatable: {
    type: Boolean,
    default: true
  },
  role: Object,
  visible: {
    type: Boolean,
    required: true,
    default: false
  },
  optionsList: Object
})

const emit = defineEmits(['update:visible', 'success'])

const loading = ref(false)
const dialogRef = ref(null)
const submitForm = ref({
  code: '',
  name: ''
})

const dialogRules = ref({
  code: [
    { required: true, message: '编码不能为空', trigger: 'blur' }
  ],
  name: [
    { required: true, message: '名称不能为空', trigger: 'blur' }
  ]
})

const roleRequest = computed(() => props.creatable ? roleAddAPI : roleModifyAPI)

const fieldList = computed(() => [
  { field: 'code', value: '角色编码', disabled: !props.creatable },
  { field: 'name', value: '角色名称', disabled: false }
])

watch(() => props.visible, (val) => {
  if (props.role && !props.creatable) {
    submitForm.value = objDeepCopy(props.role)
    return
  }
  submitForm.value = { code: '', name: '' }
})

const handleConfirm = () => {
  dialogRef.value.validate(valid => {
    if (!valid) {
      return
    }
    loading.value = true
    roleRequest.value(submitForm.value).then(res => {
      loading.value = false
      ElMessage.success('操作成功')
      emit('success')
      handleCancel()
    }).catch(() => {
      loading.value = false
    })
  })
}

const handleCancel = () => {
  emit('update:visible', false)
}
</script>

<style lang="scss" scoped>
.new-dialog-form {
  padding: 0px;
}
.new-dialog-form :deep(.el-form-item) {
  width: 100%;
  margin: 0;
  padding-bottom: 10px;
}
.new-dialog-form :deep(.el-form-item .el-form-item__label) {
  padding: 0;
}
</style>
