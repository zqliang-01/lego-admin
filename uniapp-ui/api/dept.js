import request from '@/utils/request'

const api = {
  simpleList: '/back-end/sys-dept/list-simple'
}

export const simpleList = () => {
  return request.get(api.simpleList)
}
