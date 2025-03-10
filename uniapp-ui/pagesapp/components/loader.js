var modules = []
const files = require.context('../', true, /\/menu.js$/)
files.keys().forEach((itemPath) => {
  var menu = files(itemPath).default || files(itemPath)
	if (menu.app) {
		modules.push(menu.app)
	}
})
export default modules
