import data from './data/address-data.js'

export function getChildren(value, list = data) {
  for (let index = 0; index < list.length; index++) {
    const item = list[index]
    if (item.code === value) {
      return item.children
    }
  }
  return []
}

export function getName(value) {
  return getDepName(value, data)
}

export function getDepName(value, list) {
  let name = ''
  if (value && list) {
    for (let index = 0; index < list.length; index++) {
      const item = list[index]
      if (item.code === value) {
        return item.name
      }
      if (!name && item.children && item.children.length > 0) {
        name = getDepName(value, item.children)
      }
    }
  }
  return name
}

export function getDisplay(address) {
  if (address) {
    const province = getName(address.province)
    const city = getName(address.city)
    const area = getName(address.area)
    const detail = address.detail ? address.detail : ''
    return province + city + area + detail
  }
  return ''
}
