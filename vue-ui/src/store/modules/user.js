import { loginAPI, logoutAPI } from '@/api/login'
import { permissionCurrentGetAPI } from '@/api/admin/permission'
import { resetRouter } from '@/router'
import { employeeCurrentGetAPI } from '@/api/user/personCenter'
import { addAuth, removeAuth } from '@/utils/auth'
import Lockr from 'lockr'

const user = {
  state: {
    userInfo: null, // 用户信息
    allAuth: null, // 总权限信息 默认空 调整动态路由
    authList: null,
    manage: {} // 管理后台权限信息
  },
  mutations: {
    SET_USERINFO: (state, userInfo) => {
      state.userInfo = userInfo
      localStorage.setItem('loginUserInfo', JSON.stringify(userInfo))
    },
    SET_ALLAUTH: (state, allAuth) => {
      state.allAuth = allAuth
    },
    SET_AUTHLIST: (state, authList) => {
      state.authList = authList
    },
    SET_MANAGE: (state, manage) => {
      state.manage = manage
    },
    SET_AUTH: (state, data) => {
      const token = data.token
      Lockr.set('Admin-Token', token)
      addAuth(token)
    }
  },

  actions: {
    // 登录
    Login({
      commit,
      dispatch
    }, userInfo) {
      return new Promise((resolve, reject) => {
        loginAPI(userInfo).then(res => {
          const data = res.data || {}
          if (data.needInit) {
            resolve(res)
            return
          }
          commit('SET_AUTH', data)
          dispatch('GetUserInfo')
          resolve(res)
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 获取权限
    getAuth({
      commit,
      dispatch
    }) {
      return new Promise((resolve, reject) => {
        permissionCurrentGetAPI().then((response) => {
          const data = response.data
          Lockr.set('authList', data)
          commit('SET_ALLAUTH', data)
          commit('SET_MANAGE', data.manage)
          dispatch('GetSystemInfo')
          resolve(data)
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 获取用户信息
    GetUserInfo({
      commit,
      dispatch
    }) {
      return new Promise((resolve, reject) => {
        employeeCurrentGetAPI().then(response => {
          // 邮件信息 走之前的逻辑
          commit('SET_USERINFO', response.data)
          dispatch('GetSystemInfo')
          dispatch('GetAllModule')
          dispatch('GetHeaderModule')
          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 登出
    LogOut({
      commit
    }) {
      return new Promise((resolve, reject) => {
        logoutAPI().then(() => {
          /** flush 清空localStorage .rm('authKey') 按照key清除 */
          removeAuth()
          resetRouter()
          resolve()
        }).catch(error => {
          removeAuth()
          resetRouter()
          reject(error)
        })
      })
    }
  }
}

export default user
