/**
 * 用户输入内容验证类
 */

// 是否为空
export const isEmpty = (str) => {
  return str.trim() == ''
}

/** 判断输入的是货币 货币的整数部分须少于15位，小数部分须少于2位*/
export function isMoneyNumber(nubmer) {
  var regex = /^([-+]?\d{1,15})(\.\d{0,2})?$/
  if (!regex.test(nubmer)) {
    return false
  }
  return true
}

/** 判断输入的是邮箱*/
export function isEmail(email) {
  var regex = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/
  if (!regex.test(email)) {
    return false
  }
  return true
}


/**
 * 匹配phone
 */
export const isPhone = (str) => {
  const reg = /^((0\d{2,3}-\d{7,8})|(1[3456789]\d{9}))$/
  return reg.test(str)
}

/**
 * 匹配phone
 */
export const isMobile = (str) => {
  const reg = /^(1[3456789]\d{9})$/
  return reg.test(str)
}

/**
 * 判断数值类型，包括整数和浮点数
 */
export const isNumber = (str) => {
  if (isDouble(str) || isInteger(str)) return true
  return false
}

/**
 * 判断是否为正整数(只能输入数字[0-9])
 */
export const isPositiveInteger = (str) => {
  return /(^[0-9]\d*$)/.test(str)
}

/**
 * 匹配integer
 */
export const isInteger = (str) => {
  if (str == null || str == "") return false
  var result = str.match(/^[-\+]?\d+$/)
  if (result == null) return false
  return true
}

/**
 * 匹配double或float
 */
export const isDouble = (str) => {
  if (str == null || str == "") return false
  var result = str.match(/^[-\+]?\d+(\.\d+)?$/)
  if (result == null) return false
  return true
}
