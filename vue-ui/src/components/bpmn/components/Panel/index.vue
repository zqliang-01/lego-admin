<template>
  <div class="bpmn-panel" ref="panel">
    <div class="panel-header">
      <bpmn-icon :name="bpmnIconName" />
      <p>{{ bpmnElementName }}</p>
      <p>{{ customTranslate(currentElementType || "Process") }}</p>
    </div>
    <el-collapse>
      <component v-for="cp in this.renderComponents" :key="cp.name" :is="cp" />
    </el-collapse>
  </div>
</template>

<script>
import { debounce } from 'min-dash'
import Logger from '@/utils/bpmn/Logger'
import { catchError } from '@/utils/bpmn/printCatch'
import EventEmitter from '@/utils/bpmn/EventEmitter'
import BpmnIcon from '../common/BpmnIcon'
import bpmnIcons from '../../bpmn-icons'
import getBpmnIconType from '../../bpmn-icons/getIconType'
import { customTranslate } from '../../additional-modules/Translate'
import { isUserTask } from '../../bo-utils/userTaskUtil'
import { isCanbeConditional } from '../../bo-utils/conditionUtil'
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

export default {
  name: 'BpmnPanel',
  components: {
    BpmnIcon,
    ElementUserTask,
    ElementGenerations,
    ElementDocumentations,
    ElementConditional,
    ElementJobExecution,
    ElementExtensionProperties,
    ElementExecutionListeners,
    ElementAsyncContinuations,
    ElementStartInitiator,
    ElementExtensionField
  },
  data() {
    return {
      bpmnElementName: 'Process',
      bpmnIconName: 'Process',
      currentElementType: undefined,
      currentElementId: undefined,
      customTranslate,
      renderComponents: []
    }
  },
  created() {
    EventEmitter.on('modeler-init', (modeler) => {
      // 导入完成后默认选中 process 节点
      modeler.on('import.done', () => {
        this.setCurrentElement(null)
      })
      // 监听选择事件，修改当前激活的元素以及表单
      modeler.on('selection.changed', ({ newSelection }) => {
        this.setCurrentElement(newSelection[0] || null)
      })
      modeler.on('element.changed', ({ element }) => {
        // 保证 修改 "默认流转路径" 等类似需要修改多个元素的事件发生的时候，更新表单的元素与原选中元素不一致。
        if (element && element.id === this.currentElementId) {
          this.setCurrentElement(element)
        }
      })
    })
  },
  mounted() {
    !this.currentElementId && this.setCurrentElement()
  },
  methods: {
    setCurrentElement: debounce(function(element) {
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
      this.$store.commit('setElement', { element: activatedElement, id: activatedElement.id })
      this.currentElementId = activatedElement.id
      this.currentElementType = activatedElement.type.split(':')[1]

      this.bpmnIconName = bpmnIcons[activatedElementTypeName]
      this.bpmnElementName = activatedElementTypeName

      this.setCurrentComponents(activatedElement)
      EventEmitter.emit('element-update', activatedElement)

      Logger.prettyPrimary('Selected element changed', `ID: ${activatedElement.id} , type: ${activatedElement.type}`)
      Logger.prettyInfo('Selected element businessObject', activatedElement.businessObject)
    }, 100),
    //
    setCurrentComponents(element) {
      this.renderComponents.splice(0, this.renderComponents.length) // 清空
      // 重设
      isUserTask(element) && this.renderComponents.push(ElementUserTask)
      isUserTask(element) && this.renderComponents.push(ElementForm)
      this.renderComponents.push(ElementGenerations)
      this.renderComponents.push(ElementDocumentations)
      isCanbeConditional(element) && this.renderComponents.push(ElementConditional)
      isJobExecutable(element) && this.renderComponents.push(ElementJobExecution)
      this.renderComponents.push(ElementExtensionProperties)
      isExecutable(element) && this.renderComponents.push(ElementExecutionListeners)
      isAsynchronous(element) && this.renderComponents.push(ElementAsyncContinuations)
      isStartInitializable(element) && this.renderComponents.push(ElementStartInitiator)
      this.renderComponents.push(ElementExtensionField)
    }
  }
}
</script>
