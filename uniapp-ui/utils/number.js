import numeral from '@/components/numeral'

export const abbreviate = value => numeral(value).format('0.0a')

export const bytes = value => numeral(value).format('0 b')

export const exposedNumeral = (value, format) => numeral(value).format(format)

export const exponential = value => numeral(value).format('0.[00]e+0')

export const ordinal = value => numeral(value).format('Oo')

export const percentage = value => numeral(value).format('0.[00]%')

export const separator = value => numeral(value).format('0,0.00')

export const separatorInt = value => numeral(value).format('0,0')

export const currency = value => numeral(value).format('$0,0.00')

export const separatorUnit = (value, unit = '万') => {
  if (parseFloat(value) > 100 * 10000) {
    return numeral(Math.round(parseFloat(value) / 100) / 100).format('0,0.00') + unit
  } else {
    return numeral(value).format('0,0.00')
  }
}

export const separatorIntUnit = (value, unit = '万') => {
  if (parseFloat(value) > 100 * 10000) {
    return numeral(Math.round(parseFloat(value) / 100) / 100).format('0,0') + unit
  } else {
    return numeral(value).format('0,0')
  }
}

