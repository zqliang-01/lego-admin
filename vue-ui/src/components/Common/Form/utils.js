import { isArray, isObject, isEmpty } from '@/utils/types'
import { separator } from '@/filters/vueNumeralFilter/filters'
import { objDeepCopy } from '@/utils'
import { getDisplay } from '@/utils/address'
import { Message } from 'element-ui'

/**
 * 获取自定义字段展示值
 */
export function getFormFieldShowValue(formType, value, placeholder = '--') {
  if (formType === 'float') {
    return isEmpty(value) ? placeholder : separator(value)
  } else if (formType === 'date') {
    return isEmpty(value) ? placeholder : getDateTime(value)
  } else if (formType === 'percent') {
    return isEmpty(value) ? placeholder : `${value}%`
  } else if (['select', 'user', 'structure', 'entity'].includes(formType)) {
    if (isObject(value)) {
      return isEmpty(value) ? placeholder : value.name
    }
    return isEmpty(value) ? placeholder : value
  } else if (['checkbox', 'multipleUser', 'multipleStructure'].includes(formType)) {
    return isArray(value) ? value.map(item => item.name).join() : placeholder
  } else if (formType === 'address') {
    return getDisplay(value)
  }
  return isEmpty(value) ? placeholder : value
}

/**
 * 获取查询条件展示值
 */
export function getConditionShowValue(item, placeholder = '--') {
  const formType = item.formType
  const value = item.value
  if (isEmpty(value)) {
    return placeholder
  }
  if (formType === 'float') {
    return separator(value)
  } else if (formType === 'date') {
    return getDateTime(value)
  } else if (formType === 'percent') {
    return `${value}%`
  } else if (formType === 'entity') {
    return isEmpty(value) ? '' : value.name
  } else if (['select', 'user'].includes(formType)) {
    const settingValue = item.setting.find(item => item.code === value)
    return isEmpty(settingValue) ? value : settingValue.name
  } else if (formType === 'structure') {
    return getTreeValue(item.setting, value)
  } else if (formType === 'checkbox') {
    if (isArray(value)) {
      const name = value.map(element => {
        const settingValue = item.setting.find(item => item.code === element)
        return isEmpty(settingValue) ? element : settingValue.name
      }).join()
      return isEmpty(name) ? placeholder : name
    }
    const name = item.setting.find(item => item.code === value).name
    return isEmpty(name) ? placeholder : name
  } else if (formType === 'address') {
    return getDisplay(value)
  }
  return value
}

function getTreeValue(trees, value) {
  let name = ''
  trees.forEach(tree => {
    if (tree.childrens.length > 0) {
      const tmp = getTreeValue(tree.childrens, value)
      if (tmp) {
        name = tmp
      }
    } else if (tree.code == value) {
      name = tree.name
    }
  })
  return name
}

/**
 * 获取自定义字段提交值
 */
export function getFormFieldValue(item, isDefault = false) {
  const value = isDefault ? objDeepCopy(item.defaultValue) : objDeepCopy(item.value)
  if (['checkbox', 'multipleUser', 'multipleStructure'].includes(item.formType)) {
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
  if (item.formType === 'entity') {
    return isEmpty(value) ? '' : value.code
  }
  if (isObject(value) && value.hasOwnProperty('code')) {
    return isEmpty(value) ? '' : value.code
  }
  return isEmpty(value) ? undefined : value
}
/**
 * 显示表单error错误
 */
export function showFormErrorMessage(createForm) {
  // 提示第一个error
  if (createForm.fields) {
    for (
      let index = 0;
      index < createForm.fields.length;
      index++
    ) {
      const ruleField = createForm.fields[index]
      if (ruleField.validateState == 'error') {
        Message.error(ruleField.validateMessage)
        break
      }
    }
  }
}
/**
 * 获取date类型展示时间
 */
export function getDateTime(time) {
  if (time) {
    const temps = time.split(' ')
    return temps.length > 0 ? temps[0] : ''
  }
  return time
}
