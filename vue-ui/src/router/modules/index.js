var modules = []
const files = require.context('.', true, /\.js$/)
files.keys().forEach((itemPath) => {
  var file = files(itemPath).default || files(itemPath)
  if (file.type) {
    modules.push(file)
  }
})
export default modules
