// import { getImageData } from '@/utils'

window.dataCache = {}

const loadData = (el, binding) => {
  const loadingImg = require('@/assets/img/white.png')
  setSrc(el, binding, loadingImg)

  if (binding.value && (binding.value.indexOf('/file/downFile') == 0 ||
  binding.value.indexOf('/adminFile/down') == 0)) {
    const src = `${process.env.BASE_API}${binding.value}`
    setSrc(el, binding, src.replace('//', '/'))
    // if (window.dataCache[binding.value]) {
    //   setSrc(el, binding, window.dataCache[binding.value])
    // } else {
    //   getImageData(binding.value).then((data) => {
    //     setSrc(el, binding, data.src)
    //     window.dataCache[binding.value] = data.src
    //   }).catch(() => {})
    // }
  } else {
    setSrc(el, binding, binding.value)
  }
}

const setSrc = (el, binding, src) => {
  if (binding.arg) {
    el.style[binding.arg] = 'url("' + src + '")'
  } else {
    el.setAttribute('src', src)
  }
  // 如果不是img标签则手动触发 onload 事件，通知图片已经获取到
  if (el.tagName !== 'IMG') {
    const evt = document.createEvent('HTMLEvents')
    evt.initEvent('load', false, true)
    el.dispatchEvent(evt)
  }
}

export default {
  name: 'src',
  inserted(el, binding) {
    loadData(el, binding)
  },
  update: function(el, binding) {
    if (binding.oldValue != binding.value) {
      loadData(el, binding)
    }
  },

  unbind(el) {
  }
}

