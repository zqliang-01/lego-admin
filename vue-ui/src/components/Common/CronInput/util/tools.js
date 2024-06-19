import { NUMBER } from '../constant/reg'

const sortNum = (a, b) => {
  return a - b
}

const isNumber = (str) => {
  return new RegExp(NUMBER).test(str)
}

export { sortNum, isNumber }
