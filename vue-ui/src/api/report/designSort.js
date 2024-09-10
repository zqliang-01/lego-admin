import request from '@/utils/request'

export function designSortChangeAPI(data) {
  return request({
    url: '/back-end/report-design-sort/change',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
