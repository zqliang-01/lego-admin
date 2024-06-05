import { mapGetters } from 'vuex'

import { getFormFieldValue } from '@/components/Common/Form/utils'
import { codeGeneratorGenerateAPI } from '@/api/admin/codeGenerator'
import { employeeSimpleListAPI } from '@/api/admin/employee'
import { deptSimpleListAPI } from '@/api/admin/dept'
import { postRequest, fileRequest, codeGetRequest } from '@/api/crm/common'
import { dictSimpleListAPI } from '@/api/dictionary'

export default {
  computed: {
    ...mapGetters([
      'navActiveIndex'
    ])
  },
  data() {
    return {
      appCode: ''
    }
  },
  methods: {
    setDefaultValue(field, fieldForm, isCreate) {
      if (isCreate && field.unique && field.codeGenerator && field.codeGenerator.code) {
        codeGeneratorGenerateAPI(field.codeGenerator.code).then(res => {
          this.$set(fieldForm, field.fieldCode, res.data)
        })
      } else {
        const value = getFormFieldValue(field, isCreate)
        this.$set(fieldForm, field.fieldCode, value)
      }
    },
    initRequest(form) {
      this.appCode = form.appCode
      if (this.listRequest) {
        this.listRequest = function doRequest(data) {
          return postRequest(form.queryApiUrl, data)
        }
      }
      if (this.detailRequest) {
        this.detailRequest = function doRequest(data) {
          return codeGetRequest(form.detailApiUrl, data)
        }
      }
      if (this.exportRequest) {
        this.exportRequest = function doRequest(data) {
          return fileRequest(form.exportApiUrl, data)
        }
      }
      if (this.exportAllRequest) {
        this.exportAllRequest = function doRequest(data) {
          return fileRequest(form.exportAllApiUrl, data)
        }
      }
      if (this.addRequest) {
        this.addRequest = function doRequest(data) {
          return postRequest(form.addApiUrl, data)
        }
      }
      if (this.updateRequest) {
        this.updateRequest = function doRequest(data) {
          return postRequest(form.updateApiUrl, data)
        }
      }
      if (this.deleteRequest) {
        this.deleteRequest = function doRequest(data) {
          return postRequest(form.deleteApiUrl, data)
        }
      }
    },
    initSettingValue(field) {
      if (!this.appCode) {
        this.appCode = this.navActiveIndex
      }
      if (field.optionDataType === 'dict' &&
        field.optionDictType) {
        dictSimpleListAPI(this.appCode, field.optionDictType).then(res => {
          field.setting = res.data
        })
      }
      if (['user', 'multiple_user'].includes(field.formType)) {
        employeeSimpleListAPI().then(res => {
          field.setting = res.data
        })
      }
      if (field.formType == 'structure') {
        deptSimpleListAPI().then(res => {
          field.setting = res.data
        })
      }
    },
    getDisable(field, type) {
      if (field.disabled) {
        return true
      }
      if (field.unique && type === 'update') {
        return true
      }
      if (type === 'view') {
        return true
      }
      return false
    }
  }
}
