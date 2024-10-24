import {
  regexIsNumber,
  regexIsMoneyNumber,
  regexIsMobile,
  regexIsEmail
} from '@/utils/regex'
import { isEmpty, isObject, isArray } from '@/utils/util'

export default {
  methods: {
    /**
     * 生成单个字段的验证规则
     * @param item 字段信息
     * @returns {[]}
     */
    getRules(item) {
      const tempList = []

      // 验证必填
      if (item.required) {
        tempList.push({
          required: true,
          message: item.name + '不能为空',
          trigger: ['blur', 'change']
        })

        if (item.formType == 'checkbox') {
          tempList.push({
            validator: ({ item }, value, callback) => {
              if (!isArray(value) || value.length === 0) {
                callback(new Error(item.name + '不能为空'))
              } else {
                const emptyObj = value.find(valueItem => isEmpty(valueItem))
                emptyObj === '' ? callback(new Error(item.name + '不能为空')) : callback()
              }
            },
            item: item,
            trigger: ['blur', 'change']
          })
        }
      }

      // 特殊类型
      if (['percent', 'number'].includes(item.formType)) {
        const validateNumber = (rule, value, callback) => {
          if (item.hasOwnProperty('precisions')) {
            this._getNumberRule(rule, value, callback)
          } else {
            if (isEmpty(value) || regexIsNumber(value)) {
              callback()
            } else {
              callback(new Error('数字的整数部分须少于15位，小数部分须少于4位'))
            }
          }
        }
        tempList.push({
          validator: validateNumber,
          item: item,
          trigger: ['blur']
        })
      } else if (item.formType === 'floatnumber') {
        const validateMoneyNumber = (rule, value, callback) => {
          if (item.hasOwnProperty('precisions')) {
            this._getNumberRule(rule, value, callback)
          } else {
            if (isEmpty(value) || regexIsMoneyNumber(value)) {
              callback()
            } else {
              callback(new Error('货币的整数部分须少于15位，小数部分须少于2位'))
            }
          }
        }
        tempList.push({
          validator: validateMoneyNumber,
          item: item,
          trigger: ['blur']
        })
      } else if (item.formType === 'mobile') {
        const validateMobile = (rule, value, callback) => {
          if (isEmpty(value) || regexIsMobile(value)) {
            callback()
          } else {
            callback(new Error('手机格式有误'))
          }
        }
        tempList.push({
          validator: validateMobile,
          item: item,
          trigger: ['blur']
        })
      } else if (item.formType === 'email') {
        const validateEmail = (rule, value, callback) => {
          if (isEmpty(value) || regexIsEmail(value)) {
            callback()
          } else {
            callback(new Error('邮箱格式有误'))
          }
        }
        tempList.push({
          validator: validateEmail,
          item: item,
          trigger: ['blur']
        })
      } else if (item.formType === 'location' && item.isNull == 1) {
        const validateLocation = (rule, value, callback) => {
          if (!isObject(value) || (
            isObject(value) && isEmpty(value.lat) && isEmpty(value.lng) && isEmpty(value.address)
          )) {
            callback(new Error(item.name + '不能为空'))
          } else {
            callback()
          }
        }
        tempList.push({
          validator: validateLocation,
          item: item,
          trigger: ['change']
        })
      }

      return tempList
    },

    /**
     * 获取数值规则
     */
    _getNumberRule(rule, value, callback) {
      const field = rule.item

      const arr = String(value).split('.')

      const len = String(value)
        .replace('.', '')
        .replace('-', '')
        .length
      const maxlength = field.formType === 'percent' ? 10 : 15

      const min = isEmpty(field.minNumRestrict) ? -Infinity : Number(field.minNumRestrict || -Infinity)
      const max = isEmpty(field.maxNumRestrict) ? Infinity : Number(field.maxNumRestrict || Infinity)

      if (len > maxlength) {
        callback(new Error(`最多支持${maxlength}位数字（包含小数位）`))
      } else if (isEmpty(field.precisions) && String(value).includes('.')) {
        // null 不支持小数  0 不限制小数位
        callback(new Error(`不支持小数`))
      } else if (arr.length > 1 && arr[1].length > Number(field.precisions)) {
        callback(new Error(`小数位不能大于${field.precisions}`))
      } else if (value < min) {
        callback(new Error(`不能小于${min}`))
      } else if (value > max) {
        callback(new Error(`不能大于${max}`))
      } else {
        callback()
      }
    }
  }
}
