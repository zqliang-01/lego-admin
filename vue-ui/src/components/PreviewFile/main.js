import PreviewFileVue from './index.vue'

const PreviewFile = {}

PreviewFile.install = (Vue) => {
  const PreviewFileConstructor = Vue.extend(PreviewFileVue)
  const instance = new PreviewFileConstructor({
    el: document.createElement('div')
  })
  document.body.appendChild(instance.$el)
  Vue.prototype.$previewFile = instance
}

export default PreviewFile
