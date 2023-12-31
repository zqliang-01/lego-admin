import Vue from 'vue'
import Empty from './empty.vue'
import {
  addClass,
  removeClass,
  getStyle
} from 'element-ui/src/utils/dom'
const EmptyMask = Vue.extend(Empty)
/**
 * xs-empty-text
 * 在绑定了v-empty指令的元素上添加xs-empty-text属性，其值会被渲染为加载文案，并显示在加载图标的下方
 * xs-empty-icon
 * 定义了几个类型  none 就是不展示icon 其他或者无值 就是一种效果
 * xs-empty-background
 * 背景色
 * xs-empty-custom-class
 * 类选择器样式 多个以空格分开
 */
const emptyDirective = {}
emptyDirective.install = Vue => {
  if (Vue.prototype.$isServer) return
  const toggleEmpty = (el, binding) => {
    /** 如果是数组 判断数组长度  否则 判断是否存在 当做Boolean */
    if ((Object.prototype.toString.call(binding.value) === '[object Array]' && binding.value.length === 0) ||
      (Object.prototype.toString.call(binding.value) !== '[object Array]' && binding.value)) {
      Vue.nextTick(() => {
        el.emptyOriginalPosition = getStyle(el, 'position')
        insertDom(el, el, binding)
      })
    } else { // 移除效果
      el.emptyVisible = false
      removeClass(el, 'xs-empty-parent--relative')
      removeClass(el, 'xs-empty-parent--hidden')
      el.emptyInstance.visible = false
    }
  }
  const insertDom = (parent, el, binding) => {
    if (!el.emptyVisible && getStyle(el, 'display') !== 'none' && getStyle(el, 'visibility') !== 'hidden') {
      Object.keys(el.emptyMaskStyle).forEach(property => {
        el.emptyMask.style[property] = el.emptyMaskStyle[property]
      })

      if (el.emptyOriginalPosition !== 'absolute' && el.emptyOriginalPosition !== 'fixed') {
        addClass(parent, 'xs-empty-parent--relative')
      }
      el.emptyVisible = true

      parent.appendChild(el.emptyMask)
      Vue.nextTick(() => {
        el.emptyInstance.visible = true
      })
      el.emptyInserted = true
    }
  }

  Vue.directive('empty', {
    bind: function(el, binding, vnode) {
      const textExr = el.getAttribute('xs-empty-text')
      const iconExr = el.getAttribute('xs-empty-icon')
      const backgroundExr = el.getAttribute('xs-empty-background')
      const customClassExr = el.getAttribute('xs-empty-custom-class')
      const vm = vnode.context
      const mask = new EmptyMask({
        el: document.createElement('div'),
        data: {
          text: vm && vm[textExr] || textExr,
          icon: vm && vm[iconExr] || iconExr,
          background: vm && vm[backgroundExr] || backgroundExr,
          customClass: vm && vm[customClassExr] || customClassExr
        }
      })
      el.emptyInstance = mask
      el.emptyMask = mask.$el
      el.emptyMaskStyle = {}

      binding.value && toggleEmpty(el, binding)
    },

    update: function(el, binding) {
      el.emptyInstance.setText(el.getAttribute('xs-empty-text'))
      el.emptyInstance.setIcon(el.getAttribute('xs-empty-icon'))
      if (binding.oldValue !== binding.value) {
        toggleEmpty(el, binding)
      }
    },

    unbind: function(el, binding) {
      if (el.emptyInserted) {
        el.emptyMask &&
          el.emptyMask.parentNode &&
          el.emptyMask.parentNode.removeChild(el.emptyMask)
        toggleEmpty(el, {
          value: false,
          modifiers: binding.modifiers
        })
      }
    }
  })
}

export default emptyDirective
