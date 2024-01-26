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
      this.initField()
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
      fieldFrom: {},
      fieldRules: {}
    }
  },
  created() {
    this.initField()
  },
  methods: {
    initField() {
      if (this.fieldList.length > 0) {
        this.initLocalField()
        return
      }
      this.initRemoteField()
    },
    initLocalField() {
      let maxIndex = 0
      this.dataFieldList = []
      this.fieldList.forEach(field => {
        this.initSettingValue(field)
        maxIndex = field.xAxis > maxIndex ? field.xAxis : maxIndex
        field.disabled = this.getDisable(field, this.action.type === 'update')
        this.setDefaultValue(field, this.fieldFrom, this.action.type === 'save')
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
    initRemoteField() {
      if (!this.formCode) {
        return
      }
      createFieldListAPI(this.formCode).then(res => {
        this.initRequest(res.data.form)
        this.dataFieldList = res.data.fields
        this.dataFieldList.forEach(fields => {
          fields.forEach(field => {
            this.initSettingValue(field)
            field.value = this.action.detailData[field.fieldCode]
            field.disabled = this.getDisable(field, this.action.type === 'update')
            this.setDefaultValue(field, this.fieldFrom, this.action.type === 'save')
            this.fieldRules[field.fieldCode] = this.getRules(field)
          })
        })
      })
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
          showFormErrorMessage(createForm)
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
    }
  }
}
