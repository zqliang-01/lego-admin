<template>
  <el-form
    :model="fieldForm"
    :validate-on-rule-change="false"
    class="form"
    label-position="top">
    <create-sections title="内容信息">
      <form-items
        v-for="(children, index) in fieldList"
        :key="index"
        :field-form="fieldForm"
        :field-list="children"
        @change="handleChangeValue"
      />
    </create-sections>
  </el-form>
</template>
<script>
import CreateSections from '@/components/CreateSections'
import FormItems from '@/components/Common/Form/FormItems'
import GenerateRulesMixin from '@/components/Mixins/GenerateRules'
import { getFormFieldValue } from '@/components/Common/Form/utils'
import { permissionRouteTypeListAPI } from '@/api/admin/permission'
import { customFormSimpleListAPI } from '@/api/admin/formField'
import { definitionSimpleValidListAPI } from '@/api/report/definition'

export default {
  components: {
    CreateSections,
    FormItems
  },
  mixins: [GenerateRulesMixin],
  props: {
    menuData: Object,
    fieldForm: {
      type: Object,
      default: () => {
        return {}
      }
    },
    fieldRule: Object,
    menuType: String
  },
  computed: {
    fieldList() {
      if (this.menuType === 'menu') {
        return this.formFieldList
      }
      return this.reportFieldList
    }
  },
  data() {
    return {
      formList: [],
      reportList: [],
      permissionRouteTypes: [],
      formFieldList: [
        [
          { fieldCode: 'form', name: '表单', formType: 'select', clearable: true, tipType: 'tooltip', inputTips: '业务关联表单信息，动态路由时会自动基于表单创建页面内容' },
          { fieldCode: 'routeType', name: '路由类型', formType: 'select', precisions: 1, tipType: 'tooltip', inputTips: '模块路由定义，动态路由页面内容受后台控制' }
        ]
      ],
      reportFieldList: [
        [
          { fieldCode: 'reportCode', name: '报表', formType: 'select', clearable: true, tipType: 'tooltip', inputTips: '选择自定义报表，需到报表管理模块进行报表维护' },
          { fieldCode: 'routeType', name: '路由类型', formType: 'select', precisions: 1, tipType: 'tooltip', inputTips: '模块路由定义，动态路由页面内容受后台控制' }
        ]
      ]
    }
  },
  watch: {
    menuType() {
      this.resetForm()
    },
    menuData: {
      handler() {
        this.resetForm()
      },
      deep: true,
      immediate: true
    }
  },
  mounted() {
    this.initRequest()
  },
  methods: {
    async initRequest() {
      await permissionRouteTypeListAPI().then(res => {
        this.permissionRouteTypes = res.data
      })
      await customFormSimpleListAPI().then(res => {
        this.formList = res.data
      })
      await definitionSimpleValidListAPI().then(res => {
        this.reportList = res.data
      })
      this.resetForm()
    },
    resetForm() {
      this.formFieldList.forEach(fields => {
        fields.forEach(field => {
          if (field.fieldCode === 'form') {
            this.$set(field, 'setting', this.formList)
          }
          if (field.fieldCode === 'routeType') {
            this.$set(field, 'setting', this.permissionRouteTypes)
          }
          this.$set(field, 'value', this.menuData[field.fieldCode])
          this.$set(this.fieldRule, field.fieldCode, this.getRules(field))
          this.$set(this.fieldForm, field.fieldCode, getFormFieldValue(field, false))
        })
      })
      this.reportFieldList.forEach(fields => {
        fields.forEach(field => {
          if (field.fieldCode === 'reportCode') {
            this.$set(field, 'setting', this.reportList)
          }
          if (field.fieldCode === 'routeType') {
            this.$set(field, 'setting', this.permissionRouteTypes)
          }
          this.$set(field, 'value', this.menuData[field.fieldCode])
          this.$set(this.fieldRule, field.fieldCode, this.getRules(field))
          this.$set(this.fieldForm, field.fieldCode, getFormFieldValue(field, false))
        })
      })
    },
    handleChangeValue(field, index, value) {
      this.$emit('change', field, index, value)
    }
  }
}
</script>

<style lang="scss" scoped>
</style>
