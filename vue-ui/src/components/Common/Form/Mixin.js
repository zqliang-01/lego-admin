import { isEmpty } from '@/utils/types'

export default {
  methods: {
    /**
     * 判断是否为普通 整句 文本框
     * @param formType 字段类型
     */
    isTrimInput(formType) {
      return [
        'text',
        'mobile',
        'email',
        'website'
      ].includes(formType)
    },

    /**
     * 获取类型图标
     */
    getInputIcon(formType) {
      return {
        mobile: 'icon-mobile',
        email: 'icon-email-outline',
        website: 'icon-link'
      }[formType]
    },

    /**
     * 获取输入最大长度
     */
    getInputMaxlength(formType) {
      if (formType === 'website') {
        return 800
      }
      return 100
    },

    /**
     * 常规组件change事件
     */
    commonChange(item, index, value) {
      this.$emit('change', item, index, value)
    },
    /**
     * 判断是空值
     */
    isEmptyValue(value) {
      return value === null || value == undefined
    },

    /**
     * 获取提示语
     */
    getTips(data) {
      const tips = data.tips || data.inputTips
      if (data.tipType == 'tooltip') {
        return isEmpty(tips) ? '' : tips
      }
      return isEmpty(tips) ? '' : `（${tips}）`
    },

    /**
     * 判断展示
     */
    getShowValue(item) {
      if (item.hasOwnProperty('show')) {
        return item.show
      }
      return true
    }
  }
}
