import store from '@/store'
import Lockr from 'lockr'
import axios from 'axios'

const cache = {
  /**
   * 载入全部登陆信息
   */
  loadingCache: function() {
    if (Lockr.get('Admin-Token') && !axios.defaults.headers['Admin-Token']) {
      /** 将用户信息放入缓存 */
      const userInfo = Lockr.get('loginUserInfo')
      if (userInfo) {
        store.commit('SET_USERINFO', userInfo)
      }
    }
    store.commit('SET_APPNAME', Lockr.get('systemName'))
    store.commit('SET_APPLOGO', Lockr.get('systemLogo'))
  },
  /**
   * 请求和更新登录缓存
   */
  updateAxiosCache: function() {
    axios.defaults.headers['Admin-Token'] = Lockr.get('Admin-Token')
    store.dispatch('GetUserInfo')
    store.dispatch('GetSystemInfo')
  },
  updateAxiosHeaders: function(token) {
    const newToken = token || Lockr.get('Admin-Token')

    if (token) {
      Lockr.set('Admin-Token', token)
    }

    if (newToken && axios.defaults.headers['Admin-Token'] !== newToken) {
      axios.defaults.headers['Admin-Token'] = newToken
      return true // token 变动
    }
  },
  /**
   * 移除登录信息
   * @param {*}
   */
  rmAxiosCache: function() {
    Lockr.rm('Admin-Token')
  }
}

export default cache
