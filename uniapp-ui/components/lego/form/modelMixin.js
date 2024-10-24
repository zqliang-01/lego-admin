import * as UserApi from '@/api/user'
import * as DeptApi from '@/api/dept'
import * as DictAPI from '@/api/form/dictionary'
import { postRequest, codeGetRequest, codeGenerate } from '@/api/form/common'
import { isEmpty, isObject, isArray, objDeepCopy, calculateStrLength } from '@/utils/util'

export default {
	data() {
		return {
			appCode: '',
			actionType: 'add',
			fieldForm: {
				comment: ''
			},
			fieldList: [],
			addRequest: {},
			updateRequest: {},
			detailRequest: {}
		}
	},
  computed: {
		labelWidth() {
			let num = 8
			this.fieldList.forEach(fields => {
				fields.forEach(field => {
					let tmpNum = calculateStrLength(field.name)
					if (tmpNum > num) {
						num = tmpNum;
					}
				})
			})
			return num < 8 ? (num / 2 * 18 + 10) : (num / 2 * 18)
		},
    saveRequest() {
      return this.actionType === 'update' ? this.updateRequest : this.addRequest
    }
  },
  methods: {
    setDefaultValue(field, fieldForm, isCreate) {
      if (isCreate && field.unique && field.codeGenerator && field.codeGenerator.code) {
        codeGenerate(field.codeGenerator.code).then(res => {
					this.$set(fieldForm, field.fieldCode, res.data)
        })
      } else {
				const value = this.getFormFieldValue(field, isCreate);
				this.$set(fieldForm, field.fieldCode, value);
      }
    },
		getFormFieldValue(item, isDefault = false) {
			const value = isDefault ? objDeepCopy(item.defaultValue) : objDeepCopy(item.value)
			if (['checkbox', 'multiple_user', 'multiple_structure'].includes(item.formType)) {
				if (isEmpty(value)) {
					return []
				}
				return value.map(item => {
					if (isObject(item) && item.hasOwnProperty('code')) {
						return item.code
					}
					return item
				})
			}
			if (isObject(value) && value.hasOwnProperty('code')) {
				return isEmpty(value) ? '' : value.code
			}
			return isEmpty(value) ? '' : value
		},
		initRequest(form) {
			this.appCode = form.appCode
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
				DictAPI.list(this.appCode, field.optionDictType).then(res => {
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
		getDisable(field, type) {
			if (field.disabled) {
				return true
			}
			return field.unique && type === 'update'
		},
		commonChange(item, value) {
			this.fieldForm[item.fieldCode] = value
		}
	}
}