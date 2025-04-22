<template>
  <el-form
    :model="fieldForm"
    :validate-on-rule-change="false"
    class="form"
    label-position="top">
    <lego-create-sections title="内容信息">
      <form-items
        v-for="(children, index) in fieldList"
        :key="index"
        :field-form="fieldForm"
        :field-list="children"
        @change="handleChangeValue"
      />
    </lego-create-sections>
  </el-form>
</template>

<script setup>
import { ref, reactive, computed, watch, onMounted } from 'vue'
import LegoCreateSections from '@/components/Lego/LegoCreateSections'
import FormItems from '@/components/Common/Form/FormItems'
import { useGenerateRules } from '@/components/Mixins/GenerateRules'
import { getFormFieldValue } from '@/components/Common/Form/utils'
import { permissionRouteTypeListAPI } from '@/api/admin/permission'
import { customFormSimpleListAPI } from '@/api/admin/formField'
import { definitionSimpleValidListAPI } from '@/api/report/definition'

const props = defineProps({
  menuData: {
    type: Object,
    required: true
  },
  fieldForm: {
    type: Object,
    default: () => ({})
  },
  fieldRule: {
    type: Object,
    required: true
  },
  menuType: {
    type: String,
    required: true
  }
})

const emit = defineEmits(['change'])
const { getRules } = useGenerateRules()

const formList = ref([])
const reportList = ref([])
const permissionRouteTypes = ref([])

const formFieldList = reactive([
  [
    { 
      fieldCode: 'form', 
      name: '表单', 
      formType: 'select', 
      clearable: true, 
      tipType: 'tooltip', 
      inputTips: '业务关联表单信息，动态路由时会自动基于表单创建页面内容',
      setting: []
    },
    { 
      fieldCode: 'routeType', 
      name: '路由类型', 
      formType: 'select', 
      precisions: 1, 
      tipType: 'tooltip', 
      inputTips: '模块路由定义，动态路由页面内容受后台控制',
      setting: []
    }
  ]
])

const reportFieldList = reactive([
  [
    { 
      fieldCode: 'relateCode', 
      name: '报表', 
      formType: 'select', 
      clearable: true, 
      tipType: 'tooltip', 
      inputTips: '选择自定义报表，需到报表管理模块进行报表维护',
      setting: []
    },
    { 
      fieldCode: 'routeType', 
      name: '路由类型', 
      formType: 'select', 
      precisions: 1, 
      tipType: 'tooltip', 
      inputTips: '模块路由定义，动态路由页面内容受后台控制',
      setting: []
    }
  ]
])

const fieldList = computed(() => {
  return props.menuType === 'menu' ? formFieldList : reportFieldList
})

const resetForm = () => {
  formFieldList[0].forEach(field => {
    if (field.fieldCode === 'form') {
      field.setting = formList.value
    }
    if (field.fieldCode === 'routeType') {
      field.setting = permissionRouteTypes.value
    }
    field.value = props.menuData[field.fieldCode]
    props.fieldRule[field.fieldCode] = getRules(field)
    props.fieldForm[field.fieldCode] = getFormFieldValue(field, false)
  })

  reportFieldList[0].forEach(field => {
    if (field.fieldCode === 'relateCode') {
      field.setting = reportList.value
    }
    if (field.fieldCode === 'routeType') {
      field.setting = permissionRouteTypes.value
    }
    field.value = props.menuData[field.fieldCode]
    props.fieldRule[field.fieldCode] = getRules(field)
    props.fieldForm[field.fieldCode] = getFormFieldValue(field, false)
  })
}

const initRequest = async () => {
  const [routeTypes, forms, reports] = await Promise.all([
    permissionRouteTypeListAPI(),
    customFormSimpleListAPI(),
    definitionSimpleValidListAPI()
  ])
  
  permissionRouteTypes.value = routeTypes.data
  formList.value = forms.data
  reportList.value = reports.data
  
  resetForm()
}

const handleChangeValue = (field, index, value) => {
  emit('change', field, index, value)
}

watch(() => props.menuType, () => {
  resetForm()
})

watch(() => props.menuData, () => {
  resetForm()
}, { deep: true, immediate: true })

onMounted(() => {
  initRequest()
})
</script>

<style lang="scss" scoped>
</style>
