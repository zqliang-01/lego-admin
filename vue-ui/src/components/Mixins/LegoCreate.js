import XrCreate from '../XrCreate'
import CreateSections from '../CreateSections'
import FormItems from '../NewCom/Form/FormItems'

import { showFormErrorMessage } from '../NewCom/Form/utils'
import GenerateRulesMixin from './GenerateRules'
import LegoCommonMixin from './LegoCommon'
import { createFieldListAPI } from '@/api/admin/formField'
import { getMenuAuth, getFormAuth } from '@/utils/auth'

export default {
  // 新建编辑
  components: {
    XrCreate,
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
          id: '',
          detailData: {}
        }
      }
    },
    title: String,
    menuCode: String,
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
      this.initRemoteField()
    }
  },
  computed: {
    saveRequest() {
      return this.action.type === 'update' ? this.updateRequest : this.addRequest
    },
    auth() {
      if (this.menuCode) {
        const menu = getMenuAuth(this.menuCode)
        this.$emit('update:formCode', menu.formCode)
        return menu
      }
      return getFormAuth(this.formCode)
    }
  },
  data() {
    return {
      loading: false,
      dataFieldList: [],
      detailData: {},
      fieldFrom: {},
      fieldRules: {}
    }
  },
  methods: {
    initField() {
      return createFieldListAPI(this.formCode)
    },
    initValue() {
      this.dataFieldList.forEach(fields => {
        fields.forEach(field => {
          if (field.show !== false) {
            this.initSettingValue(field)
            field.value = this.detailData[field.fieldCode]
            field.disabled = this.getDisable(field, this.action.type)
            this.setDefaultValue(field, this.fieldFrom, this.action.type === 'save')
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
      createForm.validate(valid => {
        if (!valid) {
          showFormErrorMessage(createForm)
          return false
        }
        if (this.doRequest) {
          this.doRequest()
          return
        }
        this.loading = true
        this.saveRequest(this.fieldFrom).then(() => {
          this.loading = false
          this.close()
          this.$emit('handle', { type: 'save-success' })
        }).catch(() => {
          this.loading = false
        })
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
