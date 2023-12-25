// 关联取值
export function getJsonData(obj, keys) {
  if (obj && keys) {
    var segmentationIndex = keys.indexOf('.')
    if (segmentationIndex > 0) {
      return getJsonData(obj[keys.substr(0, segmentationIndex)], keys.substr(segmentationIndex + 1))
    } else {
      return obj[keys]
    }
  } else {
    return '--'
  }
}
