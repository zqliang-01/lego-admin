<template>
  <el-form
    :model="fieldForm"
    :validate-on-rule-change="false"
    class="form"
    label-position="top">
    <create-sections title="扩展信息">
      <form-items
        v-for="(children, i) in fieldList"
        :key="i"
        :field-form="fieldForm"
        :field-list="children"
      />
    </create-sections>
  </el-form>
</template>
<script>
import CreateSections from '@/components/CreateSections'
import FormItems from '@/components/NewCom/Form/FormItems'
import { customFormGetAPI } from '@/api/admin/formField'
import { getFormFieldValue } from '@/components/NewCom/Form/utils'

export default {
  components: {
    CreateSections,
    FormItems
  },
  props: {
    formCode: String
  },
  data() {
    return {
      fieldForm: {},
      fieldList: [
        [
          { fieldCode: 'queryApiUrl', name: '查询API', formType: 'text', disabled: true },
          { fieldCode: 'detailApiUrl', name: '详情API', formType: 'text', disabled: true }
        ],
        [
          { fieldCode: 'addApiUrl', name: '新增API', formType: 'text', disabled: true },
          { fieldCode: 'updateApiUrl', name: '修改API', formType: 'text', disabled: true }
        ],
        [
          { fieldCode: 'deleteApiUrl', name: '删除API', formType: 'text', disabled: true },
          { fieldCode: 'simpleApiUrl', name: '简讯API', formType: 'text', disabled: true }
        ],
        [
          { fieldCode: 'exportApiUrl', name: '部分导出API', formType: 'text', disabled: true },
          { fieldCode: 'exportAllApiUrl', name: '全部导出API', formType: 'text', disabled: true }
        ]
      ]
    }
  },
  watch: {
    formCode() {
      this.resetExtendForm()
    }
  },
  mounted() {
    this.resetExtendForm()
  },
  methods: {
    resetExtendForm() {
      if (this.formCode) {
        customFormGetAPI(this.formCode).then(res => {
          this.fieldList.forEach(fields => {
            fields.forEach(field => {
              field.value = res.data[field.fieldCode]
              this.$set(this.fieldForm, field.fieldCode, getFormFieldValue(field, false))
            })
          })
        })
      } else {
        this.fieldList.forEach(fields => {
          fields.forEach(field => {
            this.$set(this.fieldForm, field.fieldCode, '')
          })
        })
      }
    }
  }
}
</script>

<style lang="scss" scoped>
</style>
