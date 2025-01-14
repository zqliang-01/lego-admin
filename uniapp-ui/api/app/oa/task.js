import request from '@/utils/request'

const api = {
	undoList: '/back-end/flowable-task/list-undo',
	finishedList: '/back-end/flowable-task/list-completed'
}

export const undoList = (param) => {
  return request.post(api.undoList, param)
}

export const finishedList = (param) => {
  return request.post(api.finishedList, param)
}
