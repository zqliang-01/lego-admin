import FadeView from '@/components/FadeView'
import CreateSections from '@/components/CreateSections'
import FormItems from '@/components/Common/Form/FormItems'

import LegoCommonMixin from './LegoCommon'
import { showFormErrorMessage } from '@/components/Common/Form/utils'
import GenerateRulesMixin from '@/components/Mixins/GenerateRules'
import { createFieldListAPI } from '@/api/admin/formField'

export default {
  // 新建编辑
  components: {
    FadeView,
    CreateSections,
    FormItems
  },
  mixins: [GenerateRulesMixin, LegoCommonMixin],
  props: {
    action: {
      type: Object,
      default: () => {
        return {
          type: 'save',
          detailData: {}
        }
      }
    },
    visible: {
      type: Boolean,
      default: false
    },
    title: String,
    formCode: String,
    fieldList: {
      type: Array,
      default: () => {
        return []
      }
    }
  },
  watch: {
    formCode() {
      this.initField()
    }
  },
  computed: {
    saveRequest() {
      return this.action.type === 'update' ? this.updateRequest : this.addRequest
    }
  },
  data() {
    return {
      loading: false,
      actionType: this.action.type,
      dataFieldList: [],
      detailData: {},
      fieldForm: {},
      fieldRules: {}
    }
  },
  methods: {
    initField() {
      this.actionType = this.action.type
      if (this.formCode) {
        return createFieldListAPI(this.formCode)
      }
      return new Promise((resolve, reject) => {
        if (this.formCode) {
          resolve()
        } else {
          this.$message.error('未关联业务表单！')
        }
      })
    },
    initValue() {
      this.dataFieldList.forEach(fields => {
        fields.forEach(field => {
          if (field.show !== false) {
            this.initSettingValue(field)
            field.value = this.detailData[field.fieldCode]
            field.disabled = this.getDisable(field, this.actionType)
            this.setDefaultValue(field, this.fieldForm, this.actionType === 'save')
            this.fieldRules[field.fieldCode] = this.getRules(field)
          }
        })
      })
    },
    /**
     * 保存
     */
    saveClick() {
      const createForm = this.$refs.createForm
      if (createForm) {
        createForm.validate(valid => {
          if (!valid) {
            showFormErrorMessage(createForm)
            return false
          }
          this.handleRequest()
        })
      } else if (this.doRequest) {
        this.doRequest()
        return
      }
    },
    handleRequest() {
      if (this.doRequest) {
        this.doRequest()
        return
      }
      this.loading = true
      this.saveRequest(this.fieldForm).then(() => {
        this.loading = false
        this.close()
        this.$emit('handle', { type: 'save-success' })
      }).catch(() => {
        this.loading = false
      })
    },
    /**
     * 关闭
     */
    close() {
      this.$emit('close')
    }
  }
}
