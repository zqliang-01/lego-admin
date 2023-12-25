const getLocationOrigin = () => {
  return window.location.protocol + '//' + window.location.hostname + (window.location.port ? ':' + window.location.port : '')
}

const companyName = 'LegoAdmin'
const version = 'V1.0.0'
const baiduKey = '0DpVxVNK9fNRuqIRBweKgMrIcuNHElGM' // '百度key'

export default {
  version,
  companyName,
  getLocationOrigin,
  baiduKey
}
