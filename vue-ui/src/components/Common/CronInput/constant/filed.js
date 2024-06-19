const JAN = 'JAN'
const FEB = 'FEB'
const MAR = 'MAR'
const APR = 'APR'
const MAY = 'MAY'
const JUN = 'JUN' // 1 - 6
const JUL = 'JUL'
const AUG = 'AUG'
const SEP = 'SEP'
const OCT = 'OCT'
const NOV = 'NOV'
const DEC = 'DEC' // 7 - 12
const MONTHS = [JAN, FEB, MAR, APR, MAY, JUN, JUL, AUG, SEP, OCT, NOV, DEC]
/** 7 days of week */
const SUN = 'SUN'
const MON = 'MON'
const TUE = 'TUE'
const WED = 'WED'
const THU = 'THU'
const FRI = 'FRI'
const SAT = 'SAT'
const DAYS_OF_WEEK = [SUN, MON, TUE, WED, THU, FRI, SAT]
/** symbols */
const EVERY = '*'
const PERIOD = '/'
const RANGE = '-'
const FIXED = ','
const UNFIXED = '?'
const LAST = 'L'
const WORK_DAY = 'W'
const WEEK_DAY = '#'
const CALENDAR = 'C'
const BASE_SYMBOL = EVERY + ' ' + PERIOD + ' ' + RANGE + ' ' + FIXED
const DAY_OF_MONTH_SYMBOL = BASE_SYMBOL + ' ' + LAST + ' ' + WORK_DAY + ' ' + CALENDAR
const DAY_OF_WEEK_SYMBOL = BASE_SYMBOL + ' ' + UNFIXED + ' ' + LAST + ' ' + WEEK_DAY + ' ' + CALENDAR
const EMPTY = ''
const LAST_WORK_DAY = 'LW'
// current year like 2019
const CUR_YEAR = new Date().getFullYear()
const UPPER_LIMIT_YEAR = 2099
// default cron expression
const DEFAULT_CRON_EXPRESSION = '0 0 12 * * ?'

export {
  JAN, FEB, MAR, APR, MAY, JUN, JUL, AUG, SEP, OCT, NOV, DEC,
  MONTHS, SUN, MON, TUE, WED, THU, FRI, SAT, DAYS_OF_WEEK, EVERY, PERIOD, RANGE, FIXED, UNFIXED, LAST,
  WORK_DAY, WEEK_DAY, CALENDAR, BASE_SYMBOL, DAY_OF_MONTH_SYMBOL, DAY_OF_WEEK_SYMBOL, EMPTY, LAST_WORK_DAY,
  CUR_YEAR, UPPER_LIMIT_YEAR, DEFAULT_CRON_EXPRESSION
}
