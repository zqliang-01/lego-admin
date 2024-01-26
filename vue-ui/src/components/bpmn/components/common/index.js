import BpmnIcon from './BpmnIcon'
import CollapseTitle from './CollapseTitle'
import EditItem from './EditItem'
import LucideIcon from './LucideIcon'
import NumberTag from './NumberTag'

const components = [BpmnIcon, CollapseTitle, EditItem, LucideIcon, NumberTag]

export default {
  install: (Vue) => {
    components.forEach((component) => {
      Vue.component(component.name, component)
    })
  }
}
