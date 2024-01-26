import { unObserver } from '@/utils/bpmn/tool'
import { defaultSettings } from '@/components/bpmn/preset-configuration/editor.config'

const bpmn = {
  state: {
    editor: { ...defaultSettings },
    data: {},
    activeElement: {}
  },
  mutations: {
    setConfiguration(state, conf) {
      state.editor = { ...state.editor, ...conf }
    },
    clearBpmnState(state) {
      state.data = {}
      state.activeElement = {}
    },
    setModeler(state, modeler) {
      state.data._modeler = unObserver(modeler)
    },
    setElement(state, { element, id }) {
      state.activeElement = { element: unObserver(element), id }
    }
  }
}
export default bpmn
