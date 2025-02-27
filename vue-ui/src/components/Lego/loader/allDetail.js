var modules = []
const files = require.context('@/views/', true, /\/detail.js$/)
files.keys().forEach((itemPath) => {
  var components = files(itemPath).default || files(itemPath)
  components.forEach(component => {
    modules[component.name] = component
  })
})
export default modules
