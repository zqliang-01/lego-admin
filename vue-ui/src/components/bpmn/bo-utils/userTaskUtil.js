import { is } from 'bpmn-js/lib/util/ModelUtil'
import { getModeler } from '../bpmn-utils/BpmnDesignerUtils'
import { getBusinessObject } from 'bpmn-js/lib/util/ModelUtil'
import { createModdleElement } from '../bpmn-utils/BpmnExtensionElements'

export function isUserTask(element) {
  return is(element, 'bpmn:UserTask')
}

export function setProperty(element, property) {
  const modeling = getModeler.get('modeling')
  modeling.updateProperties(element, property)
}

export function setModdleProperty(element, value, property) {
  const modeling = getModeler.get('modeling')
  modeling.updateModdleProperties(element, value, property)
}

export function createElement(element, name, value) {
  const parent = getBusinessObject(element)
  return createModdleElement(name, value, parent)
}

export function removeElement(element) {
  const modeling = getModeler.get('modeling')
  modeling.removeElements(element)
}
