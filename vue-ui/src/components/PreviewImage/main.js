import PreviewImageVue from './index.vue'

const PreviewImage = {}

PreviewImage.install = (Vue) => {
  const PreviewImageConstructor = Vue.extend(PreviewImageVue)
  const instance = new PreviewImageConstructor({
    el: document.createElement('div')
  })
  document.body.appendChild(instance.$el)
  Vue.prototype.$previewImage = instance
}

export default PreviewImage
