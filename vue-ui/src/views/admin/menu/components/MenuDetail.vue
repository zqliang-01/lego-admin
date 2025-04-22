<template>
  <div
    v-loading="loading"
    class="jurisdiction-box">
    <div class="jurisdiction-edit">
      <el-button
        v-if="manage.permission.add && operationType === 'update'"
        :icon="Plus"
        @click="handleAdd">
        新增子菜单
      </el-button>
      <el-button
        v-if="operationType === 'add'"
        class="xr-btn--orange"
        type="primary"
        @click="resetForm">
        取消
      </el-button>
      <el-button
        v-if="showDelete"
        class="xr-btn--red"
        type="primary"
        @click="handleDelete">
        删除
      </el-button>
      <el-button
        v-if="manage.permission.update || manage.permission.add"
        type="primary"
        @click="handleSubmit">
        保存
      </el-button>
    </div>
    <div class="jurisdiction-content">
      <el-form
        ref="createForm"
        :model="fieldForm"
        :rules="fieldRule"
        :validate-on-rule-change="false"
        class="form"
        label-position="top">
        <lego-create-sections title="基本信息">
          <form-items
            v-for="(children, index) in baseFieldList"
            :key="index"
            :field-form="fieldForm"
            :field-list="children"
            @change="handleChangeValue"
          />
        </lego-create-sections>
        <content-detail
          v-if="showContentDetail"
          :menu-type="menuType"
          :menu-data="menuData"
          :field-form="fieldForm"
          :field-rule="fieldRule"
          @change="handleChangeValue"
        />
      </el-form>
      <extend-detail
        v-if="showExtendDetail"
        :form-code="formCode"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, watch, onMounted } from 'vue'
import { useStore } from 'vuex'
import { Plus } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import LegoCreateSections from '@/components/Lego/LegoCreateSections'
import FormItems from '@/components/Common/Form/FormItems'
import ExtendDetail from './ExtendDetail'
import ContentDetail from './ContentDetail'
import { isEmpty } from '@/utils/types'
import { getFormFieldValue, showFormErrorMessage } from '@/components/Common/Form/utils'
import { useGenerateRules } from '@/components/Mixins/GenerateRules'
import {
  permissionTypeListAPI,
  permissionModifyAPI,
  permissionAddAPI,
  permissionDeleteAPI
} from '@/api/admin/permission'

const props = defineProps({
  menuList: {
    type: Array,
    default: () => []
  },
  menuData: {
    type: Object,
    default: () => ({})
  }
})

const emit = defineEmits(['success', 'deleteSuccess'])
const store = useStore()
const createForm = ref(null)

const loading = ref(false)
const operationType = ref('update')
const menuType = ref('')
const formCode = ref('')
const permissionTypes = ref([])

const baseFieldList = reactive([
  [
    { fieldCode: 'parentCode', name: '上级菜单', formType: 'structure', stylePercent: 100 }
  ],
  [
    { fieldCode: 'code', name: '编码', formType: 'text', unique: true, required: true, tipType: 'tooltip', inputTips: '需与代码生成的权限编码一致，编码格式：模块_一级菜单_二级菜单' },
    { fieldCode: 'name', name: '名称', formType: 'text', required: true }
  ],
  [
    { fieldCode: 'icon', name: '图标', formType: 'icon' },
    { fieldCode: 'type', name: '类型', formType: 'select', precisions: 1, required: true }
  ],
  [
    { fieldCode: 'sn', name: '序号', formType: 'number', tipType: 'tooltip', inputTips: '定义菜单的显示顺序，序号越小排序越靠前' }
  ]
])

const fieldForm = reactive({})
const fieldRule = reactive({})
const { getRules } = useGenerateRules()

const manage = computed(() => store.getters.manage)
const requestAPI = computed(() => operationType.value === 'update' ? permissionModifyAPI : permissionAddAPI)
const showDelete = computed(() => operationType.value === 'update' && manage.value.permission.delete && !isEmpty(props.menuData))
const showContentDetail = computed(() => fieldForm.type === 'menu' || fieldForm.type === 'report')
const showExtendDetail = computed(() => fieldForm.type === 'menu' && fieldForm.routeType === 'dynamic')

watch(() => props.menuData, () => {
  resetForm()
}, { deep: true, immediate: true })

onMounted(() => {
  permissionTypeListAPI().then(res => {
    permissionTypes.value = res.data
    resetForm()
  })
})

const resetForm = () => {
  menuType.value = props.menuData.type ? props.menuData.type.code : ''
  formCode.value = props.menuData.form ? props.menuData.form.code : ''
  operationType.value = 'update'
  baseFieldList.forEach(fields => {
    fields.forEach(field => {
      if (field.fieldCode === 'parentCode') {
        field.setting = props.menuList
      }
      if (field.fieldCode === 'type') {
        field.setting = permissionTypes.value
      }
      if (field.fieldCode === 'code') {
        field.disabled = operationType.value === 'update'
      }
      fieldRule[field.fieldCode] = getRules(field)
      field.value = props.menuData[field.fieldCode]
      fieldForm[field.fieldCode] = getFormFieldValue(field, false)
    })
  })
}

const handleChangeValue = (field, index, value) => {
  if (field.fieldCode === 'type') {
    menuType.value = value
  }
  if (field.fieldCode === 'form') {
    formCode.value = value
  }
}

const submitRequest = () => {
  loading.value = true
  requestAPI.value(fieldForm).then(() => {
    loading.value = false
    resetForm()
    ElMessage.success('提交成功！')
    emit('success')
  }).catch(() => {
    loading.value = false
  })
}

const handleSubmit = () => {
  loading.value = true
  createForm.value.validate(valid => {
    if (!valid) {
      loading.value = false
      showFormErrorMessage(createForm.value)
      return false
    }
    fieldForm.createAuth = false
    if (fieldForm.type === 'menu' && operationType.value === 'add') {
      ElMessageBox.confirm('是否需要自动创建按钮权限（如：新增、修改、删除等）?', '提示', {
        confirmButtonText: '需要',
        cancelButtonText: '不需要',
        type: 'info'
      }).then(() => {
        fieldForm.createAuth = true
        submitRequest()
      }).catch(() => {
        submitRequest()
      })
      return
    }
    submitRequest()
  })
}

const handleAdd = () => {
  formCode.value = ''
  operationType.value = 'add'
  baseFieldList.forEach(fields => {
    fields.forEach(field => {
      if (field.fieldCode === 'code') {
        field.disabled = operationType.value === 'update'
      }
      if (field.fieldCode === 'parentCode') {
        field.value = props.menuData.code
        fieldForm[field.fieldCode] = props.menuData.code
      } else {
        field.value = ''
        fieldForm[field.fieldCode] = ''
      }
    })
  })
}

const handleDelete = () => {
  if (!props.menuData) {
    ElMessage.error('请选择需要删除的菜单！')
    return
  }
  ElMessageBox.confirm(`此操作将永久删除[${props.menuData.name}]菜单，是否继续?`, '提示', {
    type: 'warning'
  }).then(() => {
    loading.value = true
    permissionDeleteAPI(props.menuData.code).then(() => {
      loading.value = false
      ElMessage.success('删除成功！')
      emit('deleteSuccess')
    }).catch(() => {
      loading.value = false
    })
  })
}
</script>

<style lang="scss" scoped>
.jurisdiction-box {
  padding: 10px;
  overflow: auto;
  position: relative;
}
.jurisdiction-edit {
  text-align: right;
  padding: 10px 30px;
  position: absolute;
  top: 10;
  right: 20px;
  z-index: 3;
}
.jurisdiction-content {
  padding-top: 10px;
  position: relative;
}
</style>
