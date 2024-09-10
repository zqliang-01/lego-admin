import request from '@/utils/request'

export function openPageAPI(data) {
  return request({
    url: '/back-end/report-open/page',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function openDashBoardAPI(data) {
  return request({
    url: '/back-end/report-open/dash-board',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function openConditionListAPI(data) {
  return request({
    url: '/back-end/report-open/list-condition',
    data: data
  })
}

export function openExportAPI(data) {
  return request({
    url: '/back-end/report-open/export',
    method: 'post',
    data: data,
    responseType: 'blob',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
