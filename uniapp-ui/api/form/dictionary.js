import request from '@/utils/request'

export const list = (type) => {
  return request.get('/back-end/sys-dict/list-simple?typeCode=' + type)
}
