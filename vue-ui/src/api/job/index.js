import request from '@/utils/request'

export function reportGetAPI() {
  return request({
    url: '/back-end/job-index/dashboard'
  })
}

export function lineChartGetAPI(data) {
  return request({
    url: '/back-end/job-index/chartInfo',
    data: data
  })
}
