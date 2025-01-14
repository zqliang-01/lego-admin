import { separator } from '@/utils/number'
import { getDate } from '@/utils/date'
import { getDisplay } from '@/utils/data/address'
import { isArray, isObject, isEmpty, objDeepCopy } from '@/utils/util'

/**
 * 获取自定义字段展示值
 */
export function getShowValue(formType, value, placeholder = '--') {
	if (formType === 'float') {
		return isEmpty(value) ? placeholder : separator(value)
	} else if (formType === 'date') {
		return isEmpty(value) ? placeholder : getDate(value)
	} else if (formType === 'percent') {
		return isEmpty(value) ? placeholder : value
	} else if (['select', 'user', 'structure', 'entity'].includes(formType)) {
		if (isObject(value)) {
			return isEmpty(value) ? placeholder : value.name
		}
		return isEmpty(value) ? placeholder : value
	} else if (['checkbox', 'multiple_user', 'multiple_structure'].includes(formType)) {
		return isArray(value) ? value.map(item => item.name).join() : placeholder
	} else if (formType === 'address') {
    return getDisplay(value)
  } else if (formType === 'boolean') {
    return value ? '是' : '否'
  }
	return isEmpty(value) ? placeholder : value
}

/**
 * 获取自定义字段提交值
 */
export function getSubmitValue(item, isDefault = false) {
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
	if (item.formType === 'date') {
		return isEmpty(value) ? '' : getDate(value)
	}
	if (item.formType === 'address') {
		return isEmpty(value) ? {} : value
	}
	if (isObject(value) && value.hasOwnProperty('code')) {
		return isEmpty(value) ? '' : value.code
	}
	return isEmpty(value) ? '' : String(value)
}