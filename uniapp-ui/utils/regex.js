
/** 判断输入的是number */
export function regexIsNumber(nubmer) {
  var regex = /^[0-9]+.?[0-9]*/
  if (!regex.test(nubmer)) {
    return false
  }
  return true
}

/** 判断输入的是货币 货币的整数部分须少于15位，小数部分须少于2位*/
export function regexIsMoneyNumber(nubmer) {
  var regex = /^([-+]?\d{1,15})(\.\d{0,2})?$/
  if (!regex.test(nubmer)) {
    return false
  }
  return true
}

/** 判断输入的是电话*/
export function regexIsMobile(mobile) {
  var regex = /^1\d{10}$/
  if (!regex.test(mobile)) {
    return false
  }
  return true
}

/** 判断输入的是邮箱*/
export function regexIsEmail(email) {
  var regex = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/
  if (!regex.test(email)) {
    return false
  }
  return true
}

