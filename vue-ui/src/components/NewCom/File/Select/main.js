import FileVue from './main.vue'

const File = {}

File.install = (Vue) => {
  const FileConstructor = Vue.extend(FileVue)
  const instance = new FileConstructor({
    el: document.createElement('div')
  })
  document.body.appendChild(instance.$el)


  Vue.prototype.$dile = instance
}

export default File
