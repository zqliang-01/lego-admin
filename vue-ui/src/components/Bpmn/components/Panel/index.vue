<template>
  <div class="bpmn-panel" ref="panel">
    <div class="panel-header">
      <bpmn-icon :name="bpmnIconName" />
      <p>{{ bpmnElementName }}</p>
      <p>{{ customTranslate(currentElementType || "Process") }}</p>
    </div>
    <el-collapse accordion>
      <component v-for="cp in renderComponents" :key="cp.name" :is="cp" />
    </el-collapse>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useStore } from 'vuex'
import { debounce } from 'min-dash'
import Logger from '@/utils/bpmn/Logger'
import { catchError } from '@/utils/bpmn/printCatch'
import EventEmitter from '@/utils/bpmn/EventEmitter'
import BpmnIcon from '../common/BpmnIcon'
import bpmnIcons from '../../bpmn-icons'
import getBpmnIconType from '../../bpmn-icons/getIconType'
import { customTranslate } from '../../additional-modules/Translate'
import { isUserTask } from '../../bo-utils/userTaskUtil'
import { isCanbeConditional, isExtendStartEvent } from '../../bo-utils/conditionUtil'
import { isJobExecutable } from '../../bo-utils/jobExecutionUtil'
import { isExecutable } from '../../bo-utils/executionListenersUtil'
import { isAsynchronous } from '../../bo-utils/asynchronousContinuationsUtil'
import { isStartInitializable } from '../../bo-utils/initiatorUtil'
import { getModeler } from '../../bpmn-utils/BpmnDesignerUtils'
import ElementUserTask from './components/ElementUserTask'
import ElementForm from './components/ElementForm'
import ElementGenerations from './components/ElementGenerations'
import ElementDocumentations from './components/ElementDocumentations'
import ElementConditional from './components/ElementConditional'
import ElementJobExecution from './components/ElementJobExecution'
import ElementExtensionProperties from './components/ElementExtensionProperties'
import ElementExecutionListeners from './components/ElementExecutionListeners'
import ElementAsyncContinuations from './components/ElementAsyncContinuations'
import ElementStartInitiator from './components/ElementStartInitiator'
import ElementExtensionField from './components/ElementExtensionField'

const store = useStore()
const panel = ref(null)

const bpmnElementName = ref('Process')
const bpmnIconName = ref('Process')
const currentElementType = ref()
const currentElementId = ref()
const renderComponents = ref([])

const setCurrentElement = debounce((element) => {
  let activatedElement = element
  let activatedElementTypeName = ''
  if (!activatedElement) {
    const modeler = getModeler()
    activatedElement =
      modeler.get('elementRegistry')?.find((el) => el.type === 'bpmn:Process') ||
      modeler.get('elementRegistry')?.find((el) => el.type === 'bpmn:Collaboration')
    if (!activatedElement) {
      return catchError('No Element found!')
    }
  }
  activatedElementTypeName = getBpmnIconType(activatedElement)
  store.commit('setElement', { element: activatedElement, id: activatedElement.id })
  currentElementId.value = activatedElement.id
  currentElementType.value = activatedElement.type.split(':')[1]

  bpmnIconName.value = bpmnIcons[activatedElementTypeName]
  bpmnElementName.value = activatedElementTypeName

  setCurrentComponents(activatedElement)
  EventEmitter.emit('element-update', activatedElement)

  Logger.prettyPrimary('Selected element changed', `ID: ${activatedElement.id} , type: ${activatedElement.type}`)
  Logger.prettyInfo('Selected element businessObject', activatedElement.businessObject)
}, 100)

const setCurrentComponents = (element) => {
  renderComponents.value = [ElementGenerations]
  isUserTask(element) && renderComponents.value.push(ElementUserTask)
  isUserTask(element) && renderComponents.value.push(ElementForm)
  isExtendStartEvent(element) && renderComponents.value.push(ElementForm)
  renderComponents.value.push(ElementDocumentations)
  isCanbeConditional(element) && renderComponents.value.push(ElementConditional)
  isJobExecutable(element) && renderComponents.value.push(ElementJobExecution)
  renderComponents.value.push(ElementExtensionProperties)
  isExecutable(element) && renderComponents.value.push(ElementExecutionListeners)
  isAsynchronous(element) && renderComponents.value.push(ElementAsyncContinuations)
  isStartInitializable(element) && renderComponents.value.push(ElementStartInitiator)
  renderComponents.value.push(ElementExtensionField)
}

onMounted(() => {
  EventEmitter.on('modeler-init', (modeler) => {
    modeler.on('import.done', () => {
      setCurrentElement(null)
    })
    modeler.on('selection.changed', ({ newSelection }) => {
      setCurrentElement(newSelection[0] || null)
    })
    modeler.on('element.changed', ({ element }) => {
      if (element && element.id === currentElementId.value) {
        setCurrentElement(element)
      }
    })
  })
  !currentElementId.value && setCurrentElement()
})
</script>

<style scoped>
.bpmn-panel {
  width: 300px;
  height: 100%;
  overflow-y: auto;
  border-left: 1px solid #e0e0e0;
}

.panel-header {
  padding: 8px 10px;
  border-bottom: 1px solid #e0e0e0;
  display: flex;
  align-items: center;
}

.panel-header p {
  margin: 0 0 0 5px;
  font-size: 14px;
  color: #333;
}
</style>
