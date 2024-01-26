const getLocationOrigin = () => {
  return window.location.protocol + '//' + window.location.hostname + (window.location.port ? ':' + window.location.port : '')
}

const companyName = 'LegoAdmin'
const version = 'V1.0.0'

export default {
  version,
  companyName,
  getLocationOrigin
}
