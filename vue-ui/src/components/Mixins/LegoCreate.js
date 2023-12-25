import XrCreate from '../XrCreate'
import CreateSections from '../CreateSections'
import FormItems from '../NewCom/Form/FormItems'

import { getFormFieldValue } from '../NewCom/Form/utils'
import GenerateRulesMixin from './GenerateRules'
import { codeGeneratorGenerateAPI } from '@/api/admin/codeGenerator'

export default {
  // 新建编辑
  components: {
    XrCreate,
    CreateSections,
    FormItems
  },
  mixins: [GenerateRulesMixin],
  props: {
    action: {
      type: Object,
      default: () => {
        return {
          type: 'save',
          id: '',
          detailData: {}
        }
      }
    },
    fieldList: {
      type: Array,
      default: () => {
        return []
      }
    }
  },
  data() {
    return {
      loading: false,
      test: [],
      dataFieldList: [],
      fieldFrom: {},
      fieldRules: {}
    }
  },
  methods: {
    initField() {
      let maxIndex = 0
      this.fieldList.forEach(field => {
        maxIndex = field.xAxis > maxIndex ? field.xAxis : maxIndex
        field.disabled = this.action.type === 'update' && field.unique
        this.getDefaultValue(field).then(res => {
          this.$set(this.fieldFrom, field.fieldCode, res)
        })
        this.fieldRules[field.fieldCode] = this.getRules(field)
      })
      for (var i = 0; i < maxIndex + 1; ++i) {
        const xFieldList = this.fieldList.filter(field => field.xAxis === i).sort(function(a, b) {
          return a.yAxis - b.yAxis
        })
        if (xFieldList) {
          this.dataFieldList.push(xFieldList)
        }
      }
    },

    async getDefaultValue(field) {
      const isCreate = this.action.type === 'save'
      let value = getFormFieldValue(field, isCreate)
      if (isCreate && field.unique && field.codeGenerator && field.codeGenerator.code) {
        await codeGeneratorGenerateAPI(field.codeGenerator.code).then(res => {
          value = res.data
        })
      }
      return value
    },

    /**
     * 保存
     */
    saveClick() {
      this.loading = true
      const createForm = this.$refs.createForm
      createForm.validate(valid => {
        if (!valid) {
          this.loading = false
          this.getFormErrorMessage(createForm)
          return false
        }
        this.saveRequest(this.fieldFrom)
          .then(() => {
            this.loading = false
            this.close()
            // 保存成功
            this.$emit('handle', { type: 'save-success' })
          })
          .catch(() => {
            this.loading = false
          })
      })
    },
    /**
     * 关闭
     */
    close() {
      this.$emit('close')
    },
    /**
     * 获取error错误
     */
    getFormErrorMessage(createForm) {
      // 提示第一个error
      if (createForm.fields) {
        for (
          let index = 0;
          index < createForm.fields.length;
          index++
        ) {
          const ruleField = createForm.fields[index]
          if (ruleField.validateState == 'error') {
            this.$message.error(ruleField.validateMessage)
            break
          }
        }
      }
    }
  }
}
