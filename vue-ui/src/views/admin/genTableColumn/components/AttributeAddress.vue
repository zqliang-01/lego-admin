<template>
  <div v-if="fieldForm.attributes">
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
import { getFormFieldValue } from '@/components/Common/Form/utils'
import { genTableMetaColumnListAPI } from '@/api/admin/genTableColumn'
export default {
  components: {
    FormItems
  },
  mixins: [GenerateRulesMixin],
  props: {
    tableCode: String,
    currentData: {
      type: Object,
      default: () => {
        return {}
      }
    },
    fieldForm: {
      type: Object,
      default: () => {
        return {}
      }
    },
    fieldRule: {
      type: Object,
      default: () => {
        return {}
      }
    },
    selected: {
      type: Array,
      default: () => {
        return []
      }
    }
  },
  data() {
    return {
      columnList: [],
      fieldList: [
        [
          { fieldCode: 'attributes.province', name: '省字段', formType: 'select', filterable: true, required: true },
          { fieldCode: 'attributes.city', name: '市字段', formType: 'select', filterable: true, required: true }
        ],
        [
          { fieldCode: 'attributes.area', name: '区字段', formType: 'select', filterable: true, required: true },
          { fieldCode: 'attributes.detail', name: '详细地址字段', formType: 'select', filterable: true, required: true }
        ]
      ]
    }
  },
  watch: {
    'fieldForm.formType'(value) {
      this.initSetting()
    },
    currentData: {
      handler() {
        this.initSetting()
      },
      deep: true,
      immediate: true
    }
  },
  mounted() {
  },
  methods: {
    initSetting() {
      genTableMetaColumnListAPI(this.tableCode).then(res => {
        this.columnList = res.data.filter(column => {
          return !this.selected.some(select => select.name === column.columnName)
        }).map(data => {
          return {
            code: data.columnName,
            name: data.columnName
          }
        })
        this.resetForm()
      })
    },
    resetForm() {
      if (this.fieldForm.formType === 'address') {
        this.fieldList.forEach(fields => {
          fields.forEach(field => {
            field.setting = this.columnList
            var lists = field.fieldCode.split('.') || []
            this.$set(this.fieldRule, field.fieldCode, this.getRules(field))
            this.$set(field, 'value', lists.reduce((data, key) => data ? data[key] : undefined, this.currentData))
            let currentObj = this.fieldForm
            lists.forEach((element, index) => {
              if (index === lists.length - 1) {
                this.$set(currentObj, element, getFormFieldValue(field, false))
              } else if (currentObj[element]) {
                currentObj = currentObj[element]
              } else {
                this.$set(currentObj, element, {})
                currentObj = currentObj[element]
              }
            })
          })
        })
      }
    }
  }
}
</script>

<style lang="scss" scoped>
</style>
