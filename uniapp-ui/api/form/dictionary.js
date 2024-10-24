import request from '@/utils/request'

export const list = (model, type) => {
  return request.get('/back-end/' + model + '-dict/list-simple?typeCode=' + type)
}
