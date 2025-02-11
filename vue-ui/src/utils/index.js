const Base64 = require('js-base64').Base64
export function parseTime(time, cFormat) {
  if (arguments.length === 0) {
    return null
  }
  const format = cFormat || '{y}-{m}-{d} {h}:{i}:{s}'
  let date
  if (typeof time === 'object') {
    date = time
  } else {
    if (('' + time).length === 10) time = parseInt(time) * 1000
    date = new Date(time)
  }
  const formatObj = {
    y: date.getFullYear(),
    m: date.getMonth() + 1,
    d: date.getDate(),
    h: date.getHours(),
    i: date.getMinutes(),
    s: date.getSeconds(),
    a: date.getDay()
  }
  const time_str = format.replace(/{(y|m|d|h|i|s|a)+}/g, (result, key) => {
    let value = formatObj[key]
    // Note: getDay() returns 0 on Sunday
    if (key === 'a') {
      return ['日', '一', '二', '三', '四', '五', '六'][value]
    }
    if (result.length > 0 && value < 10) {
      value = '0' + value
    }
    return value || 0
  })
  return time_str
}

export function isExternal(path) {
  return /^(https?:|mailto:|tel:)/.test(path)
}

/** 压缩文件
 * quality压缩百分比 0.3
 */
export function compressImage(file, quality, callback) {
  // quality 设置为0.3
  quality = quality || 0.3
  const reader = new FileReader()
  reader.onload = function(event) {
    var result = event.target.result
    if (file.size > 204800 && file.type !== 'image/gif' && quality < 1) { // 大于200Kb
      const img = new Image()
      img.src = result
      img.onload = function() {
        // // 如果图片大于四百万像素，计算压缩比并将大小压至400万以下
        // var initSize = img.src.length
        var width = img.width
        var height = img.height

        var ratio
        if ((ratio = width * height / 4000000) > 1) {
          ratio = Math.sqrt(ratio)
          width /= ratio
          height /= ratio
        } else {
          ratio = 1
        }
        var canvas = document.createElement('canvas')
        canvas.width = width
        canvas.height = height
        // 铺底色
        var ctx = canvas.getContext('2d')
        ctx.fillStyle = '#fff'
        ctx.fillRect(0, 0, canvas.width, canvas.height)
        // 如果图片像素大于100万则使用瓦片绘制
        var count
        if ((count = width * height / 1000000) > 1) {
          count = ~~(Math.sqrt(count) + 1)
          // 计算要分成多少块瓦片
          // 计算每块瓦片的宽和高
          var nw = ~~(width / count)
          var nh = ~~(height / count)
          var tCanvas = document.createElement('canvas')
          tCanvas.width = nw
          tCanvas.height = nh
          for (var i = 0; i < count; i++) {
            for (var j = 0; j < count; j++) {
              var tctx = tCanvas.getContext('2d')
              tctx.drawImage(img, i * nw * ratio, j * nh * ratio, nw * ratio, nh * ratio, 0, 0, nw, nh)

              ctx.drawImage(tCanvas, i * nw, j * nh, nw, nh)
            }
          }
          tCanvas.width = tCanvas.height = 0
        } else {
          ctx.drawImage(img, 0, 0, width, height)
        }
        // 进行最小压缩
        var ndata = canvas.toDataURL('image/jpeg', quality)
        canvas.width = canvas.height = 0
        callback(ndata)
      }
    } else { // 小于200K不需要压缩 直接返回
      callback(result)
    }
  }
  reader.readAsDataURL(file)
}

/** 根据date URL 创建blob 用于上传 */
export function createBlob(result) {
  var arr = result.split(',')
  var mime = arr[0].match(/:(.*?)/)[1]
  var bstr = atob(arr[1])
  var n = bstr.length
  var u8arr = new Uint8Array(n)
  while (n--) {
    u8arr[n] = bstr.charCodeAt(n)
  }
  return new Blob([u8arr], {
    type: mime
  })
}

/** 获取file大小的名称 */
export function fileSize(value) {
  if (value == null || value == '') {
    return '0 Bytes'
  }
  if (typeof value == 'string') {
    value = parseInt(value)
  }
  var unitArr = ['Bytes', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB']
  var index = 0
  var srcsize = parseFloat(value)
  index = Math.floor(Math.log(srcsize) / Math.log(1024))
  var size = srcsize / Math.pow(1024, index)
  //  保留的小数位数
  size = size.toFixed(2)
  return size + unitArr[index]
}

/** 获取最大 z-index 的值 */
import {
  PopupManager
} from 'element-ui/lib/utils/popup'
export function getMaxIndex() {
  return PopupManager.nextZIndex()
}

/** 深拷贝 */
export function objDeepCopy(source) {
  if (!source) {
    return null
  }
  if (typeof source === 'object') {
    var sourceCopy = source instanceof Array ? [] : {}
    for (var item in source) {
      if (!source[item]) {
        sourceCopy[item] = source[item]
      } else {
        sourceCopy[item] = typeof source[item] === 'object' ? objDeepCopy(source[item]) : source[item]
      }
    }
    return sourceCopy
  }
  return source
}

/**
 * 获取文件类型图标
 * @param {*} file
 */
export function getFileTypeIcon(file) {
  if (file.type.indexOf('image') !== -1) {
    return getFileIconWithSuffix('png')
  } else if (file.type.indexOf('audio') !== -1) {
    return getFileIconWithSuffix('mp3')
  } else if (file.type.indexOf('video') !== -1) {
    return getFileIconWithSuffix('mp4')
  } else {
    const index = file.name.lastIndexOf('.')
    const ext = file.name.substr(index + 1) || ''

    return getFileIconWithSuffix(ext)
  }
}

/**
 * 根据文件名字判断是否能预览
 * @param {*} name
 */
export function canPreviewFile(name) {
  const temps = name ? name.split('.') : []
  var ext = ''
  if (temps.length > 0) {
    ext = temps[temps.length - 1]
  } else {
    ext = ''
  }

  if (['xlsx', 'xls'].includes(ext)) {
    return true
  } else if (['doc', 'docx'].includes(ext)) {
    return true
  } else if (ext === 'pdf') {
    return true
  } else if (['ppt', 'pptx'].includes(ext)) {
    return true
  } else if (['txt', 'text'].includes(ext)) {
    return true
  }

  return false
}

/**
 * 预览文件
 */
import axios from 'axios'
export function previewFile(path, name) {
  window.open(previewFileUrl(path, name))
}

export function previewFileUrl(path, name) {
  return `/file/onlinePreview?url=${encodeURIComponent(Base64.encode(`${path}${path.includes('?fullfilename=') ? '' : `?fullfilename=${name || ''}`}&c=${axios.defaults.headers['Admin-Token']}`))}`
}

export function getFileIconWithSuffix(ext) {
  const fileType = getFileTypeWithExt(ext)
  if (fileType) {
    return {
      image: require('@/assets/img/file/file_img.png'),
      tif: require('@/assets/img/file/file_tif.png'),
      video: require('@/assets/img/file/file_video.png'),
      audio: require('@/assets/img/file/file_music.png'),
      excel: require('@/assets/img/file/file_excle.png'),
      word: require('@/assets/img/file/file_word.png'),
      archive: require('@/assets/img/file/file_zip.png'),
      pdf: require('@/assets/img/file/file_pdf.png'),
      ppt: require('@/assets/img/file/file_ppt.png'),
      text: require('@/assets/img/file/file_txt.png')
    }[fileType]
  }

  return require('@/assets/img/file/file_unknown.png')
}

/**
 * 获取文件类型根据文件名
 * @param {*} file
 */
export function getFileTypeWithFileName(fileName) {
  if (fileName) {
    const index = fileName.lastIndexOf('.')
    const ext = fileName.substr(index + 1) || ''
    getFileTypeWithExt(ext)
  }
  return ''
}

import {
  Message
} from 'element-ui'
/**
 * 根据文件名验证文件是否通过
 * @param {*} fileName 文件名
 * @param {*} type 要求的文件类型
 * @param {*} messageShow 展示消息
 */
export function verifyFileTypeWithFileName(fileName, type = 'excel', messageShow = true) {
  let pass = true
  if (fileName) {
    const index = fileName.lastIndexOf('.')
    const ext = fileName.substr(index + 1) || ''
    const fileType = getFileTypeWithExt(ext)
    if (fileType != type) {
      pass = false
    }
  } else {
    pass = false
  }

  if (!pass && messageShow) {
    Message({
      message: '请选择正确的文件类型',
      type: 'error'
    })
  }
  return pass
}

/**
 * 根据后缀获取文件类型
 * @param {*} ext
 */
export function getFileTypeWithExt(ext) {
  if (ext) {
    ext = ext.toLowerCase()
    if (['jpg', 'png', 'jpeg', 'bmp', 'ico', 'gif'].includes(ext)) {
      return 'image'
    } else if (ext === 'psd') {
      return 'psd'
    } else if (ext === 'tif') {
      return 'tif'
    } else if (['mp4', 'm2v', 'mkv', 'rmvb', 'wmv', 'avi', 'flv', 'mov', '3gp'].includes(ext)) {
      return 'video'
    } else if (['mp3', 'wma', 'wav'].includes(ext)) {
      return 'audio'
    } else if (['xlsx', 'xls'].includes(ext)) {
      return 'excel'
    } else if (['doc', 'docx'].includes(ext)) {
      return 'word'
    } else if (['rar', 'zip', '7z', 'tar', 'iso', 'dmg'].includes(ext)) {
      return 'archive'
    } else if (ext === 'pdf') {
      return 'pdf'
    } else if (['ppt', 'pptx'].includes(ext)) {
      return 'ppt'
    } else if (['txt', 'text'].includes(ext)) {
      return 'text'
    }
  }
  return ''
}

/** 判断输入的是number */
export function regexIsNumber(nubmer) {
  var regex = /^[0-9]+.?[0-9]*/
  if (!regex.test(nubmer)) {
    return false
  }
  return true
}

/** 判断输入的是货币 货币的整数部分须少于15位，小数部分须少于2位*/
export function regexIsMoneyNumber(nubmer) {
  var regex = /^([-+]?\d{1,15})(\.\d{0,2})?$/
  if (!regex.test(nubmer)) {
    return false
  }
  return true
}

/** 判断输入的是电话*/
export function regexIsMobile(mobile) {
  var regex = /^1\d{10}$/
  if (!regex.test(mobile)) {
    return false
  }
  return true
}

/** 判断输入的是邮箱*/
export function regexIsEmail(email) {
  var regex = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/
  if (!regex.test(email)) {
    return false
  }
  return true
}

/**
 * 时间操作
 * @param
 */
/** 时间戳转date*/
import moment from 'moment'

export function formatTime(time) {
  const timeMoment = moment(time)
  const nowMoment = moment()
  const diff = nowMoment.diff(timeMoment, 'seconds')

  if (diff < 30) {
    return '刚刚'
  } else if (diff < 3600) {
    // less 1 hour
    return Math.ceil(diff / 60) + '分钟前'
  } else if (diff < 3600 * 24) {
    return Math.ceil(diff / 3600) + '小时前'
  } else if (diff < 3600 * 24 * 2) {
    return '1天前'
  }

  const timeYear = timeMoment.format('YYYY')
  const nowYear = nowMoment.format('YYYY')

  if (timeYear == nowYear) {
    return timeMoment.format('MM月DD日')
  } else {
    return timeMoment.format('YY年MM月DD日')
  }
}

export function getDateFromTimestamp(time) {
  var times = 0
  if (time.length === 13) {
    times = parseInt(time)
  } else {
    times = parseInt(time) * 1000
  }
  return new Date(times) // 如果date为13位不需要乘1000
}

/**
 *
 * @param {*} timestamp 时间戳
 * @param {*} format 格式化
 */
export function timestampToFormatTime(timestamp, format) {
  if (timestamp && timestamp.toString().length >= 10) {
    return moment(getDateFromTimestamp(timestamp.toString())).format(format)
  }
  return ''
}

// date 或者格式化时间
export function timeToFormatTime(time, format) {
  if (time) {
    return moment(time).format(format || 'YYYY-MM-DD')
  }
  return ''
}

/**
 *
 * @param {*} format 格式化字符串
 */
export function formatTimeToTimestamp(format) {
  if (format && format.length > 0) {
    var timeValue = moment(format)
      .valueOf()
      .toString()
    return timeValue.length > 10 ? timeValue.substr(0, 10) : timeValue
  }
  return ''
}

/** image 下载 */
import { fileDownloadAPI } from '@/api/common'
/**
 *
 * @param {*} data url
 * @param {*} filename 名称
 */
export function getImageData(code) {
  return new Promise((resolve, reject) => {
    fileDownloadAPI(code).then(res => {
      const blob = new Blob([res.data], {
        type: ''
      })

      var reader = new FileReader()
      reader.readAsDataURL(blob)
      reader.onload = (evt) => {
        resolve({
          blob: blob,
          src: evt.target.result
        })
      }
    }).catch(() => {
      reject()
    })
  })
}

export function dataURLtoBlob(dataurl) {
  // eslint-disable-next-line one-var
  var arr = dataurl.split(','),
    mime = arr[0].match(/:(.*?);/)[1],
    bstr = atob(arr[1]),
    n = bstr.length,
    u8arr = new Uint8Array(n)
  while (n--) {
    u8arr[n] = bstr.charCodeAt(n)
  }
  return new Blob([u8arr], {
    type: mime
  })
}

export function getBase64Image(img) {
  var canvas = document.createElement('canvas')
  canvas.width = img.width
  canvas.height = img.height
  var ctx = canvas.getContext('2d')
  ctx.drawImage(img, 0, 0, img.width, img.height)
  var ext = img.src.substring(img.src.lastIndexOf('.') + 1).toLowerCase()
  var dataURL = canvas.toDataURL('image/' + ext)
  return dataURL
}

// 获取绑定参数
export function guid() {
  function S4() {
    return (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1)
  }
  return (S4() + S4() + S4() + S4() + S4() + S4() + S4() + S4())
}

/**
 * 两个浮点数求和
 * @param num1
 * @param num2
 * @return {number}
 */
export function floatAdd(num1, num2) {
  let r1, r2
  try {
    r1 = num1.toString().split('.')[1].length
  } catch (e) {
    r1 = 0
  }
  try {
    r2 = num2.toString().split('.')[1].length
  } catch (e) {
    r2 = 0
  }
  const m = Math.pow(10, Math.max(r1, r2))
  return Math.round(num1 * m + num2 * m) / m
}

/**
 * 下载excel
 */
export function downloadExcelWithResData(res) {
  if (res && res.data.type === 'application/json') {
    const file = new FileReader()
    file.readAsText(res.data, 'utf-8')
    file.onload = function() {
      const obj = JSON.parse(file.result)
      if (obj.code != 200) {
        Message({
          message: obj.msg,
          type: 'error'
        })
      }
    }
    return
  }
  let fileName = res.headers['content-disposition'].split('filename=')[1]
  if (!fileName) {
    fileName = res.headers['content-disposition'].split('UTF-8\'\'')[1]
  }
  fileName = fileName ? fileName.replace(/\"/g, '') : 'file.xlsx'
  fileName = decodeURI(fileName) || ''
  downloadFileWithBuffer(res.data, fileName, 'application/vnd.ms-excel;charset=utf-8')
}

export function downloadFileWithBuffer(data, name, type) {
  var blob = new Blob([data], {
    type: type || ''
  })
  var downloadElement = document.createElement('a')
  var href = window.URL.createObjectURL(blob) // 创建下载的链接
  downloadElement.href = href
  downloadElement.download = name // 下载后文件名
  document.body.appendChild(downloadElement)
  downloadElement.click() // 点击下载
  document.body.removeChild(downloadElement) // 下载完成移除元素
  window.URL.revokeObjectURL(href) // 释放掉blob对象
}

/**
 * 获取百度地图
 */
export function getBaiduMap() {
  if (!global.BMap) {
    global.BMap = {}
    global.BMap._preloader = new Promise((resolve, reject) => {
      global._initBaiduMap = function() {
        resolve(global.BMap)
        global.document.body.removeChild($script)
        global.BMap._preloader = null
        global._initBaiduMap = null
      }
      const $script = document.createElement('script')
      global.document.body.appendChild($script)
      $script.src = `https://api.map.baidu.com/api?v=3.0&ak=${process.env.baiduKey}&callback=_initBaiduMap`
    })
    return global.BMap._preloader
  } else if (!global.BMap._preloader) {
    return Promise.resolve(global.BMap)
  } else {
    return global.BMap._preloader
  }
}
/** 将url转化为img对象 */
export function urltoImage(url, fn) {
  var img = new Image()
  img.src = url
  return img
}
/** img对象转化为canvas对象 */
export function imagetoCanvas(image) {
  var cvs = document.createElement('canvas')
  var ctx = cvs.getContext('2d')
  cvs.width = image.width
  cvs.height = image.height
  ctx.drawImage(image, 0, 0, cvs.width, cvs.height)
  return cvs
}

export function canvasToDataURL(canvas, format, quality) {
  return canvas.toDataURL(format || 'image/jpeg', quality || 1.0)
}

/**
 * file Path to blob
 */
export function filePathToBlob(filePath) {
  return new Promise((resolve, reject) => {
    const xhr = new XMLHttpRequest()
    xhr.open('get', filePath, true)
    xhr.responseType = 'blob'
    xhr.onload = function() {
      if (this.status == 200) {
        resolve(this.response)
      } else {
        reject()
      }
    }
    xhr.send()
  })
}

/**
 * 获取树形接口的值
 */
export function getTreeValue(id, treeList, key = 'id', children = 'children') {
  const resultList = []
  loopTree(id, treeList, resultList, key, children)
  return resultList.reverse()
}

function loopTree(id, treeList, resultList, key, children) {
  for (var i = 0; i < treeList.length; i++) {
    if (treeList[i][key] == id) {
      resultList.push(treeList[i][key])
      return true
    } else {
      if (treeList[i][children] && treeList[i][children].length > 0) {
        if (loopTree(id, treeList[i][children], resultList, key, children)) {
          resultList.push(treeList[i][key])
          return true
        }
      }
    }
  }
}

/**
 * 判断是手机
 */
export function isMobileDevice() {
  if (/(iPhone|iPad|iPod|iOS|Android)/i.test(navigator.userAgent)) {
    return true
  }
  return false
}

/**
 * 判断是微信
 */
export function isWeiXin() {
  var ua = window.navigator.userAgent.toLowerCase()
  if (ua.match(/MicroMessenger/i) == 'micromessenger') {
    return true
  } else {
    return false
  }
}
