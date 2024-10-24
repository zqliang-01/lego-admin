import {
	ACCESS_TOKEN,
	USER_ID
} from '@/store/mutation-types'
import storage from '@/utils/storage'
import * as LoginApi from '@/api/login'

// 登陆成功后执行
const loginSuccess = (commit, { code, token }) => {
  const expiredTime = 30 * 86400
  storage.set(USER_ID, code, expiredTime)
  storage.set(ACCESS_TOKEN, token, expiredTime)
  commit('SET_TOKEN', token)
  commit('SET_USER_ID', code)
}

const user = {
	state: {
		token: '',
		userId: ''
	},

	mutations: {
		SET_TOKEN: (state, value) => {
			state.token = value
		},
		SET_USER_ID: (state, value) => {
			state.userId = value
		}
	},

	actions: {
		// 用户登录
		Login({ commit }, data) {
			return new Promise((resolve, reject) => {
				LoginApi.login(data).then(response => {
					loginSuccess(commit, response.data)
					resolve(response)
				}).catch(reject)
			})
		},
		
		WechatMpLogin({ commit }, code) {
			return new Promise((resolve, reject) => {
				LoginApi.wechatMpLogin(code).then(response => {
					const data = response.data
					if (data.needLogin) {
						resolve(data)
						return;
					}
					loginSuccess(commit, data)
					resolve(data)
				}).catch(reject)
			})
		},

		WechatLogin({ commit }, code) {
			return new Promise((resolve, reject) => {
				LoginApi.wechatLogin(code).then(response => {
					const data = response.data
					if (data.needLogin) {
						resolve(data)
						return;
					}
					loginSuccess(commit, data)
					resolve(data)
				}).catch(reject)
			})
		},
		// 退出登录
		Logout({ commit }, data) {
			const store = this
			return new Promise((resolve, reject) => {
				LoginApi.logout(data).then(response => {
					storage.remove(USER_ID)
					storage.remove(ACCESS_TOKEN)
					commit('SET_TOKEN', '')
					commit('SET_USER_ID', '')
					resolve()
				}).catch(reject)
			})
		}
	}
}

export default user