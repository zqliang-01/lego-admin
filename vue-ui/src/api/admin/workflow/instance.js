import request from '@/utils/request'

/**
 * 列表
 */
export function instanceListAPI(data) {
  return request({
    url: '/back-end/flowable-instance/list',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function instanceProcessNodeListAPI(instanceId) {
  return request({
    url: '/back-end/flowable-instance/list-process-node/' + instanceId
  })
}

export function instanceStopAPI(instanceId) {
  return request({
    url: '/back-end/flowable-instance/stop/' + instanceId,
    method: 'post'
  })
}

export function instanceGetFormAPI(instanceId) {
  return request({
    url: '/back-end/flowable-instance/get-start-form/' + instanceId
  })
}
