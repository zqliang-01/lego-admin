import { mapGetters } from 'vuex'

import { getFormFieldValue } from '../NewCom/Form/utils'
import { codeGeneratorGenerateAPI } from '@/api/admin/codeGenerator'
import { employeeSimpleListAPI } from '@/api/admin/employee'
import { depSimpleListAPI } from '@/api/admin/dept'
import { postRequest, fileRequest, codeGetRequest, dictListAPI } from '@/api/crm/common'
import { getFormAuth } from '@/utils/auth'

export default {
  computed: {
    ...mapGetters([
      'navActiveIndex'
    ])
  },
  methods: {
    setDefaultValue(field, fieldFrom, isCreate) {
      if (isCreate && field.unique && field.codeGenerator && field.codeGenerator.code) {
        codeGeneratorGenerateAPI(field.codeGenerator.code).then(res => {
          this.$set(fieldFrom, field.fieldCode, res.data)
        })
      } else {
        const value = getFormFieldValue(field, isCreate)
        this.$set(fieldFrom, field.fieldCode, value)
      }
    },
    initRequest(formApi) {
      if (this.listRequest) {
        this.listRequest = function doRequest(data) {
          return postRequest(formApi.queryApiUrl, data)
        }
      }
      if (this.detailRequest) {
        this.detailRequest = function doRequest(data) {
          return codeGetRequest(formApi.detailApiUrl, data)
        }
      }
      if (this.exportRequest) {
        this.exportRequest = function doRequest(data) {
          return fileRequest(formApi.exportApiUrl, data)
        }
      }
      if (this.exportAllRequest) {
        this.exportAllRequest = function doRequest(data) {
          return fileRequest(formApi.exportAllApiUrl, data)
        }
      }
      if (this.addRequest) {
        this.addRequest = function doRequest(data) {
          return postRequest(formApi.addApiUrl, data)
        }
      }
      if (this.updateRequest) {
        this.updateRequest = function doRequest(data) {
          return postRequest(formApi.updateApiUrl, data)
        }
      }
      if (this.deleteRequest) {
        this.deleteRequest = function doRequest(data) {
          return postRequest(formApi.deleteApiUrl, data)
        }
      }
    },
    initSettingValue(field) {
      if (this.navActiveIndex &&
        field.optionDataType === 'dict' &&
        field.optionDictType) {
        dictListAPI(this.navActiveIndex, field.optionDictType).then(res => {
          field.setting = res.data
        })
      }
      if (field.formType == 'user') {
        employeeSimpleListAPI().then(res => {
          field.setting = res.data
        })
      }
      if (field.formType == 'structure') {
        depSimpleListAPI().then(res => {
          field.setting = res.data
        })
      }
    },
    getDisable(field, isUpdate) {
      let disable = field.unique
      if (field.formType === 'entity') {
        const au = getFormAuth(field.relativeForm.code)
        disable = !au.update
      }
      return isUpdate && disable
    }
  }
}
