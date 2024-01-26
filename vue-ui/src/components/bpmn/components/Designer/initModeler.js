import Modeler from 'bpmn-js/lib/Modeler'
import EventEmitter from '@/utils/bpmn/EventEmitter'
import { catchError } from '@/utils/bpmn/printCatch'
import EnhancementContextmenu from '../../additional-components/EnhancementContextmenu'
import { unObserver } from '@/utils/bpmn/tool'

export default function(designerDom, moduleAndExtensions, context) {
  const options = {
    container: designerDom,
    additionalModules: moduleAndExtensions[0] || [],
    moddleExtensions: moduleAndExtensions[1] || {},
    ...moduleAndExtensions[2]
  }

  // 清除旧 modeler
  context.getModeler && context.getModeler.destroy()
  context.$store.commit('clearBpmnState')

  const modeler = new Modeler(options)

  context.$store.commit('setModeler', modeler)

  EventEmitter.emit('modeler-init', modeler)

  context.events?.forEach((event) => {
    modeler.on(event, function(eventObj) {
      const eventName = event.replace(/\./g, '-')
      const element = eventObj ? eventObj.element : null
      context.$emit(eventName, unObserver({ element, eventObj }))
    })
  })

  modeler.on('commandStack.changed', async(event) => {
    try {
      const { xml } = await modeler.saveXML({ format: true })

      context.$emit('update:xml', xml)
      context.$emit('command-stack-changed', event)
    } catch (error) {
      catchError(error)
    }
  })

  // 右键菜单
  EnhancementContextmenu(modeler, context.getEditor)

  // console.log(modeler)

  return modeler
}
