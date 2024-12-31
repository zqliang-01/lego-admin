export default {
  methods: {
    /**
     * 根据类型获取条件
     */
    getConditionByFormType(formType) {
      if (['text', 'textarea', 'website', 'mobile', 'email'].includes(formType)) {
        return [
          { label: '等于', disabled: false, type: 'equals' },
          { label: '不等于', disabled: false, type: 'notEquals' },
          { label: '模糊匹配', disabled: false, type: 'like' },
          { label: '包含', disabled: false, type: 'contains' },
          { label: '不包含', disabled: false, type: 'notContains' },
          { label: '开始于', disabled: false, type: 'startWith' },
          { label: '结束于', disabled: false, type: 'endWith' },
          { label: '为空', disabled: false, type: 'isNull' },
          { label: '不为空', disabled: false, type: 'isNotNull' }
        ]
      }
      if (['select', 'entity'].includes(formType)) {
        return [
          { label: '等于', disabled: false, type: 'equals' },
          { label: '不等于', disabled: false, type: 'notEquals' },
          { label: '为空', disabled: false, type: 'isNull' },
          { label: '不为空', disabled: false, type: 'isNotNull' }
        ]
      }
      if (formType == 'boolean') {
        return [
          { label: '等于', disabled: false, type: 'equals' },
          { label: '不等于', disabled: false, type: 'notEquals' }
        ]
      }
      if (formType == 'checkbox') {
        return [
          { label: '等于', disabled: false, type: 'equals' },
          { label: '不等于', disabled: false, type: 'notEquals' },
          { label: '包含', disabled: false, type: 'contains' },
          { label: '不包含', disabled: false, type: 'notContains' },
          { label: '为空', disabled: false, type: 'isNull' },
          { label: '不为空', disabled: false, type: 'isNotNull' }
        ]
      }
      if (['number', 'float', 'percent'].includes(formType)) {
        return [
          { label: '等于', disabled: false, type: 'equals' },
          { label: '不等于', disabled: false, type: 'notEquals' },
          { label: '大于', disabled: false, type: 'greaterThan' },
          { label: '大于等于', disabled: false, type: 'greaterThanOrEquals' },
          { label: '小于', disabled: false, type: 'lessThan' },
          { label: '小于等于', disabled: false, type: 'lessThanOrEquals' },
          { label: '为空', disabled: false, type: 'isNull' },
          { label: '不为空', disabled: false, type: 'isNotNull' }
        ]
      }
      if (['date', 'datetime'].includes(formType)) {
        return [
          { value: 'is', label: '等于', disabled: false, type: 'equals' },
          { value: 'isNot', label: '不等于', disabled: false, type: 'notEquals' },
          { value: 'lt', label: '早于', disabled: false, type: 'lessThan' },
          { value: 'gt', label: '晚于', disabled: false, type: 'greaterThan' },
          { value: 'egt', label: '不早于', disabled: false, type: 'greaterThanOrEquals' },
          { value: 'elt', label: '不晚于', disabled: false, type: 'lessThanOrEquals' },
          { label: '为空', disabled: false, type: 'isNull' },
          { label: '不为空', disabled: false, type: 'isNotNull' }
        ]
      }
      if (['user', 'structure'].includes(formType)) {
        return [
          { label: '包含', disabled: false, type: 'contains' },
          { label: '不包含', disabled: false, type: 'notContains' },
          { label: '为空', disabled: false, type: 'isNull' },
          { label: '不为空', disabled: false, type: 'isNotNull' }
        ]
      }
      if (formType == 'address') {
        return [
          { label: '等于', disabled: false, type: 'equals' }
        ]
      }
      return []
    }
  }
}
