import request from '@/utils/request'

const api = {
  listEntity: '/back-end/sys-operation-log/list-entity'
}

export const listEntity = (param) => {
  return request.post(api.listEntity, param, {
	  header: {
		  'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
	  }
  })
}
