import request from '@/utils/request'

export const postRequest = (url, data) => {
	return request.post(url, data)
}

export const getRequest = (url, data) => {
	return request.get(url, data)
}

export const codeGetRequest = (url, code) => {
	return request.get(url + '/' + code)
}

export const codeGenerate = (code) => {
	return request.get('/back-end/sys-code-generator/generate/' + code)
}