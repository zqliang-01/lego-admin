import Loading from './loading.vue'
import {
	addClass,
	getStyle
} from '@/utils/dom'

const install = (Vue, vm) => {
	const Mask = Vue.extend(Loading)
	// 加载动画处理
	Vue.directive('loading', {
		bind: function(el, binding, vnode) {
			setTimeout(() => {
				const originalPosition = getStyle(el, 'position')
				if (originalPosition !== 'absolute' && originalPosition !== 'fixed') {
					addClass(el, 'lego-loading-parent--relative')
				}
				const mask = new Mask({
					el: document.createElement('view'),
					data: {}
				});
				el.instance = mask
				el.appendChild(mask.$el)
			})
		},
		update: function(el, binding) {
			setTimeout(() => {
				el.instance.visible = binding.value
			})
		},
		unbind: function(el, binding) {
			setTimeout(() => {
				if (el.domInserted) {
					el.mask &&
						el.mask.parentNode &&
						el.mask.parentNode.removeChild(el.mask);
				}
				el.instance && el.instance.$destroy();
			})
		},
	})
}

export default {
	install
}
