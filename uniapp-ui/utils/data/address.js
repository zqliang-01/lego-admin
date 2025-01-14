import data from './json/address.json'

export function getNode(value, list = data) {
  let data = undefined
	if (value) {
		for (let index = 0; index < list.length; index++) {
			const item = list[index]
			if (item.code === value) {
				return item
			} else if (item.children && item.children.length > 0) {
				data = getNode(value, item.children)
			}
		}
	}
  return data
}

export function getChildren(value, list = data) {
	if (value) {
		for (let index = 0; index < list.length; index++) {
			const item = list[index]
			if (item.code === value) {
				return item.children
			} else if (item.children && item.children.length > 0) {
				return getChildren(value, item.children)
			}
		}
	}
  return data
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
