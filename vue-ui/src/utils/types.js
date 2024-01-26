export function isString(obj) {
  return Object.prototype.toString.call(obj) === '[object String]'
}

export function isObject(obj) {
  return Object.prototype.toString.call(obj) === '[object Object]'
}

export function isArray(obj) {
  return Object.prototype.toString.call(obj) === '[object Array]'
}

/**
 * 数据非空验证
 * @desc 验证数据是否为 null undefined [] {} ''
 * @param data
 * @return {boolean}
 */
export function isEmpty(data) {
  if (data === null) return true
  if (data === undefined) return true
  if (Object.prototype.toString.call(data) === '[object Array]') return data.length === 0
  if (Object.prototype.toString.call(data) === '[object Object]') return Object.keys(data).length === 0
  if (typeof data === 'string') return data.trim() === ''
  return false
}

export function getValueObj(obj, value, valueKey) {
  let result = {}
  for (const key in obj) {
    if (!isEmpty(result)) {
      break
    }
    if (isObject(obj[key])) {
      result = getValueObj(obj[key], value, valueKey)
    }
    if (key == valueKey && obj[key] === value) {
      result = obj
    }
  }
  return result
}
