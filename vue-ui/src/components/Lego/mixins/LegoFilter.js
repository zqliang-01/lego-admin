import Field from '@/components/Common/Form/Field'
import LegoConditionMixin from './LegoCondition'
export default {
  name: 'Index',
  components: {
    Field
  },
  mixins: [LegoConditionMixin],
  props: {
    filterObj: {
      type: Object,
      default: () => {
        return { form: [] }
      }
    },
    dialogVisible: {
      type: Boolean,
      required: true,
      default: false
    },
    fieldList: {
      type: Array,
      required: true,
      default: () => {
        return []
      }
    }
  },
  data() {
    return {
      filterForm: [],
      visible: false,
      saveName: null
    }
  },
  methods: {
    handleFieldChange(item, index, value) {
      if (item.formType == 'entity' && value && value != item.value) {
        item.value = { code: value.code, name: value.name }
      }
    },
    conditionChange(value, formItem) {
      formItem.type = value
    },
    showCalCondition(formType) {
      return this.getConditionByFormType(formType).length > 0
    },
    getValueSpan(formType) {
      return this.showCalCondition(formType) ? 8 : 13
    },
    fieldFocus() {
      this.$el.click()
    },
    fieldChange(formItem) {
      const field = this.fieldList.find(item => {
        return item.fieldCode === formItem.fieldCode
      })
      if (field) {
        formItem.name = field.name
        formItem.formType = field.formType
        formItem.setting = field.setting
        formItem.request = field.request
        formItem.relativeForm = field.relativeForm
        formItem.type = 'is'
        formItem.fieldForm = {}
        if (field.formType === 'boolean_value') {
          formItem.fieldForm = { [formItem.fieldCode]: false }
        }
        const conditions = this.getConditionByFormType(formItem.formType)
        if (conditions.length > 0) {
          formItem.type = conditions[0].type
        }
      }
    },
    handleCancel() {
      this.$emit('update:dialogVisible', false)
    },
    handleAdd() {
      this.filterForm.push({
        name: '',
        fieldCode: '',
        formType: '',
        fieldForm: {},
        type: '',
        setting: []
      })
    },
    handleDelete(index) {
      this.$confirm('您确定要删除这一条数据吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          this.filterForm.splice(index, 1)
        })
        .catch(() => {})
    }
  }
}
