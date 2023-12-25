import numeral from 'numeral'

const abbreviate = value => numeral(value).format('0.0a')

const bytes = value => numeral(value).format('0 b')

const exposedNumeral = (value, format) => numeral(value).format(format)

const exponential = value => numeral(value).format('0.[00]e+0')

const ordinal = value => numeral(value).format('Oo')

const percentage = value => numeral(value).format('0.[00]%')

const separator = value => numeral(value).format('0,0.00')

const separatorInt = value => numeral(value).format('0,0')

const currency = value => numeral(value).format('$0,0.00')

const separatorUnit = (value, unit = '万') => {
  if (parseFloat(value) > 100 * 10000) {
    return numeral(Math.round(parseFloat(value) / 100) / 100).format('0,0.00') + unit
  } else {
    return numeral(value).format('0,0.00')
  }
}

const separatorIntUnit = (value, unit = '万') => {
  if (parseFloat(value) > 100 * 10000) {
    return numeral(Math.round(parseFloat(value) / 100) / 100).format('0,0') + unit
  } else {
    return numeral(value).format('0,0')
  }
}

export {
  abbreviate,
  bytes,
  exponential,
  exposedNumeral,
  ordinal,
  percentage,
  separator,
  separatorInt,
  currency,
  separatorUnit,
  separatorIntUnit
}
