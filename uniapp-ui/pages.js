const fs = require('fs')
const path = require('path')
const router = () => {
	var modules = []
	const appSrc = path.resolve(__dirname, './pagesapp/')
	const files = findFile(appSrc, 'menu.js')
	files.forEach(file => {
		const menu = require('./pagesapp' + file)
		if (menu.pages) {
			modules = modules.concat(objDeepCopy(menu.pages))
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

function objDeepCopy(source) {
  if (!source) {
    return null
  }
  if (typeof source === 'object') {
    var sourceCopy = source instanceof Array ? [] : {}
    for (var item in source) {
      if (!source[item]) {
        sourceCopy[item] = source[item]
      } else {
        sourceCopy[item] = typeof source[item] === 'object' ? objDeepCopy(source[item]) : source[item]
      }
    }
    return sourceCopy
  }
  return source
}

function addSubPackages(subPackages, code, pages) {
	if (subPackages) {
		subPackages.forEach(subPackage => {
			if (subPackage.root === code) {
				subPackage.pages = subPackage.pages.concat(pages)
			}
		})
	}
}

module.exports = (pagesJson, loader) => {
	const modules = router()
	addSubPackages(pagesJson.subPackages, 'pagesapp', modules)
	return pagesJson
}