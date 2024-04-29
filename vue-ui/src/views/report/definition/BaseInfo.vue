<template>
  <div>
      <form-items
        v-for="(children, index) in fieldList"
        :key="index"
        :field-form="fieldForm"
        :field-list="children"
      />
  </div>
</template>
<script>
import FormItems from '@/components/Common/Form/FormItems'
import GenerateRulesMixin from '@/components/Mixins/GenerateRules'

export default {
  name: 'DefinitionBaseInfo',
  components: {
    FormItems
  },
  mixins: [GenerateRulesMixin],
  props: {
    fieldForm: Object,
    fieldRules: Object
  },
  data() {
    return {
      fieldList: [
        [
          { fieldCode: 'code', name: '编码', formType: 'text', stylePercent: 100 / 3, disabled: true, unique: true },
          { fieldCode: 'name', name: '名称', formType: 'text', stylePercent: 100 / 3, required: true },
          { fieldCode: 'dataSource', name: '数据源', formType: 'select', setting: [{ code: 'default', name: '默认' }],
            stylePercent: 100 / 3, required: true, tipType: 'tooltip', inputTips: '数据源定义数据脚本所查询的数据库' }
        ],
        [
          { fieldCode: 'maxExportSize', name: '最大导出数量', formType: 'number', stylePercent: 100 / 3, required: true },
          { fieldCode: 'sn', name: '序号', formType: 'number', stylePercent: 100 / 3, required: true },
          { fieldCode: 'enable', name: '状态', formType: 'boolean_value', stylePercent: 100 / 3 }
        ]
      ]
    }
  },
  created() {
    this.fieldList.forEach(fields => {
      fields.forEach(field => {
        this.$set(this.fieldRules, field.fieldCode, this.getRules(field))
      })
    })
  },
  methods: {
  }
}
</script>
<style lang="scss" scoped>
</style>
