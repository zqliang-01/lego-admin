import * as UserApi from '@/api/manage/user'
import * as DeptApi from '@/api/manage/dept'
import * as DictAPI from '@/api/form/dictionary'
import { postRequest, codeGetRequest, codeGenerate } from '@/api/form/common'
import { calculateStrLength, isArray } from '@/utils/util'
import { getSubmitValue } from '@/utils/form'

export default {
	data() {
		return {
			actionType: 'add',
			fieldForm: {
				comment: ''
			},
			fieldList: [],
			addRequest: {},
			listRequest: {},
			updateRequest: {},
			detailRequest: {}
		}
	},
  computed: {
    saveRequest() {
      return this.actionType === 'update' ? this.updateRequest : this.addRequest
    },
		labelWidth() {
			let num = 8
			this.fieldList.forEach(fields => {
				if (isArray(fields)) {
					fields.forEach(field => {
						let tmpNum = calculateStrLength(field.name)
						if (tmpNum > num && !field.simpleType) {
							num = tmpNum;
						}
					})
				}
			})
			return num * 15
		}
  },
  methods: {
    setDefaultValue(field, fieldForm, isCreate) {
      if (isCreate && field.unique && field.codeGenerator && field.codeGenerator.code) {
        codeGenerate(field.codeGenerator.code).then(res => {
					this.$set(fieldForm, field.fieldCode, res.data)
        })
      } else {
				const value = getSubmitValue(field, isCreate);
				this.$set(fieldForm, field.fieldCode, value);
      }
    },
		initRequest(form) {
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
		},
		initSettingValue(field) {
			if (field.optionDataType === 'dict' && field.optionDictType) {
				DictAPI.list(field.optionDictType).then(res => {
					field.setting = res.data
				})
			}
			if (['user', 'multiple_user'].includes(field.formType)) {
				UserApi.simpleList().then(res => {
					field.setting = res.data
				})
			}
			if (field.formType == 'structure') {
				DeptApi.simpleList().then(res => {
					field.setting = res.data
				})
			}
		},
		getDisable(field) {
			if (field.disabled) {
				return true
			}
			return field.unique && this.actionType === 'update'
		},
		commonChange(item, value) {
			this.fieldForm[item.fieldCode] = value
		}
	}
}