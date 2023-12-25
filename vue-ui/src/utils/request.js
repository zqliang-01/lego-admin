import axios from 'axios'
import {
  Message,
  MessageBox
} from 'element-ui'
import {
  removeAuth
} from '@/utils/auth'
import qs from 'qs'
import { debounce } from 'throttle-debounce'

/**
 * 检查dom是否忽略
 * @param {*} e
 */
const clearCacheEnterLogin = debounce(500, () => {
  removeAuth().then(() => {
    location.reload() // 为了重新实例化vue-router对象 避免bug
  }).catch(() => {
    location.reload()
  })
})

const errorMessage = debounce(500, (message, type = 'error') => {
  Message({
    message: message,
    duration: 1500,
    type: type
  })
})

const confirmMessage = debounce(1000, (message) => {
  MessageBox.confirm(message, '提示', {
    confirmButtonText: '确定',
    showCancelButton: false,
    closeOnClickModal: false,
    closeOnPressEscape: false,
    showClose: false,
    type: 'warning'
  }).then(() => {
    if ((window.app.$route && window.app.$route.name !== 'login') || !window.app.$route) {
      clearCacheEnterLogin()
    } else {
      removeAuth()
    }
  }).catch(() => {
  })
})

axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded;charset=UTF-8'
// 创建axios实例
const service = axios.create({
  baseURL: process.env.BASE_API, // api 的 base_url
  timeout: 600000 // 请求超时时间
})

// request拦截器
service.interceptors.request.use(
  config => {
    const flag = config.headers['Content-Type'] && config.headers['Content-Type'].indexOf('application/json') !== -1
    if (!flag) {
      const mult = config.headers['Content-Type'] && config.headers['Content-Type'].indexOf('multipart/form-data') !== -1
      if (mult) {
        config.data = config.data
      } else {
        config.data = qs.stringify(config.data)
      }
    } else {
      if (config.data === undefined || config.data === null) {
        // 不传参的情况下 json类型的提交数据，校准为 空对象
        config.data = {}
      }
    }
    if (config.method === 'get' && config.data) {
      config.url += '?' + config.data
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// response 拦截器
service.interceptors.response.use(
  response => {
    /**
     * code为非200是抛错 可结合自己业务进行修改
     */
    const res = response.data
    if (response.status === 200 && response.config.responseType === 'blob') { // 文件类型特殊处理
      if (response.headers['content-disposition'] || (response.headers['content-type'] && response.headers['content-type'].indexOf('application/pdf') != -1)) {
        return response
      } else {
        const resultBlob = new Blob([response.data], { type: 'application/json' })
        const fr = new FileReader()
        fr.onload = function() {
          const result = JSON.parse(this.result)
          if (result.msg) {
            errorMessage(result.msg, result.code == 1 ? 'success' : 'error')
          }
        }
        fr.readAsText(resultBlob)
      }
    } else if (res.code !== 200) {
      if (res.code === 302) {
        if (res.data && res.data.extra === 1) {
          confirmMessage(`您的账号${res.data.extraTime}在别处登录。如非本人操作，则密码可能已泄漏，建议修改密码`)
        } else {
          clearCacheEnterLogin()
        }
      } else if (res.msg) {
        errorMessage(res.msg)
      }
      return Promise.reject(res)
    } else {
      return res
    }
  },
  error => {
    if (error.response) {
      const response = error.response
      if (response.data && response.data.msg) {
        errorMessage(response.data.msg)
      } else if (response.status == 504) {
        errorMessage('网络错误，请检查您的网络')
      } else {
        errorMessage('异常[' + response.status + ']')
      }
    }
    return Promise.reject(error)
  }
)

export default service
