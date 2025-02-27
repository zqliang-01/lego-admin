const fs = require('fs')
const path = require('path')
const router = () => {
	var modules = []
	const appSrc = path.resolve(__dirname, './pages/app/')
	const files = findFile(appSrc, 'menu.js')
	files.forEach(file => {
		const menu = require('./pages/app' + file)
		if (menu.pages) {
			modules = modules.concat(menu.pages)
		}
	})
	return modules
}

function findFile(dir, filename, results = [], relativePath = '') {
	const files = fs.readdirSync(dir)
	for (const file of files) {
		const filePath = path.join(dir, file)
		const stats = fs.statSync(filePath)
		if (stats.isDirectory()) {
			findFile(filePath, filename, results, relativePath + '/' + file)
		} else if (file === filename) {
			results.push(relativePath + '/' + file)
		}
	}
	return results;
}

module.exports = (pagesJson, loader) => {
	const modules = router()
	pagesJson.pages = pagesJson.pages.concat(modules)
	return pagesJson
}