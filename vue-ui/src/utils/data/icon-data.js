import data from './json/icon.json'

export default data.glyphs.sort((a, b) => a.name.localeCompare(b.name))
