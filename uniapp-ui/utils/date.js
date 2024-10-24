import moment from '@/components/moment'

/**
 * 今天
 */
export function getTodayDate() {
  const start = moment().format('YYYY-MM-DD')
  return [start, start]
}

/**
 * 昨天
 */
export function getYesterday() {
  const start = moment().subtract(1, 'day').format('YYYY-MM-DD')
  return [start, start]
}

/**
 * 本周
 */
export function getWeekDate() {
  const start = moment().startOf('week').format('YYYY-MM-DD')
  const end = moment().format('YYYY-MM-DD')
  return [start, end]
}

/**
 * 上周
 */
export function getLastWeekDate() {
  const weekOfDay = new Date().getDay()
  const start = moment().subtract(weekOfDay + 7 - 1, 'days').format('YYYY-MM-DD')
  const end = moment().subtract(weekOfDay, 'days').format('YYYY-MM-DD')
  return [start, end]
}

/**
 * 本月
 */
export function getMonth() {
  const start = moment().startOf('month').format('YYYY-MM-DD')
  const end = moment().format('YYYY-MM-DD')
  return [start, end]
}

/**
 * 上月
 */
export function getLastMonth() {
  const start = moment().subtract(1, 'month').startOf('month').format('YYYY-MM-DD')
  const end = moment().subtract(1, 'month').endOf('month').format('YYYY-MM-DD')
  return [start, end]
}

/**
 * 本年
 */
export function getYear() {
  const start = moment().year(moment().year()).startOf('year').format('YYYY-MM-DD')
  const end = moment().year(moment().year()).endOf('year').format('YYYY-MM-DD')
  return [start, end]
}

/**
 * 去年
 */
export function getLastYear() {
  const start = moment().subtract(1, 'year').startOf('year').format('YYYY-MM-DD')
  const end = moment().subtract(1, 'year').endOf('year').format('YYYY-MM-DD')
  return [start, end]
}
