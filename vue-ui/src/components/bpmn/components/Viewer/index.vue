<template>
  <div class="process-viewer">
    <el-row type="flex" justify="end">
      <el-button-group key="scale-control" size="medium">
        <el-button size="medium" type="default" :plain="true" :disabled="defaultZoom <= 0.3" icon="el-icon-zoom-out" @click="processZoomOut()" />
        <el-button size="medium" type="default" style="width: 90px;">{{ Math.floor(defaultZoom * 10 * 10) + "%" }}</el-button>
        <el-button size="medium" type="default" :plain="true" :disabled="defaultZoom >= 3.9" icon="el-icon-zoom-in" @click="processZoomIn()" />
        <el-button size="medium" type="default" icon="el-icon-c-scale-to-original" @click="processReZoom()" />
        <slot />
      </el-button-group>
    </el-row>
    <defs ref="customSuccessDefs">
      <marker id="sequenceflow-end-white-success" view-box="0 0 20 20" ref-x="11" ref-y="10" marker-width="10" marker-height="10" orient="auto">
        <path class="success-arrow" d="M 1 5 L 11 10 L 1 15 Z" style="stroke-width: 1px; stroke-linecap: round; stroke-dasharray: 10000, 1;" />
      </marker>
      <marker id="conditional-flow-marker-white-success" view-box="0 0 20 20" ref-x="-1" ref-y="10" marker-width="10" marker-height="10" orient="auto">
        <path class="success-conditional" d="M 0 10 L 8 6 L 16 10 L 8 14 Z" style="stroke-width: 1px; stroke-linecap: round; stroke-dasharray: 10000, 1;" />
      </marker>
    </defs>
    <!-- 自定义箭头样式，用于失败状态下流程连线箭头 -->
    <defs ref="customFailDefs">
      <marker id="sequenceflow-end-white-fail" view-box="0 0 20 20" ref-x="11" ref-y="10" marker-width="10" marker-height="10" orient="auto">
        <path class="fail-arrow" d="M 1 5 L 11 10 L 1 15 Z" style="stroke-width: 1px; stroke-linecap: round; stroke-dasharray: 10000, 1;" />
      </marker>
      <marker id="conditional-flow-marker-white-fail" view-box="0 0 20 20" ref-x="-1" ref-y="10" marker-width="10" marker-height="10" orient="auto">
        <path class="fail-conditional" d="M 0 10 L 8 6 L 16 10 L 8 14 Z" style="stroke-width: 1px; stroke-linecap: round; stroke-dasharray: 10000, 1;" />
      </marker>
    </defs>
    <div id="view" ref="processCanvas" :style="{height: '400px'}" ></div>
  </div>
</template>

<script>
import BpmnViewer from 'bpmn-js/lib/Viewer'
import MoveCanvasModule from 'diagram-js/lib/navigation/movecanvas'
import { getBusinessObject } from 'bpmn-js/lib/util/ModelUtil'

export default {
  name: 'BpmnViewer',
  props: {
    xml: String,
    processNodeInfo: {
      type: Object,
      default: () => {
        return {}
      }
    }
  },
  data() {
    return {
      defaultZoom: 1,
      bpmnModeler: null
    }
  },
  mounted() {
    this.createDiagram()
  },
  watch: {
  },
  methods: {
    initBpmnModeler() {
      if (this.$refs.processCanvas) {
        this.$refs.processCanvas.innerHTML = ''
      }
      if (this.bpmnModerler) {
        this.bpmnModerler.clear()
        this.bpmnModerler.destroy()
      }
      this.bpmnModerler && this.bpmnModerler.destroy() // 避免缓存
      this.bpmnModerler = null
      this.bpmnModerler = new BpmnViewer({
        additionalModules: [
          MoveCanvasModule
        ],
        container: this.$refs.processCanvas
      })
      this.bpmnModerler.on('element.click', ({ element }) => {
        this.onSelectElement(element)
      })
    },
    onSelectElement(element) {
      if (element.type === 'bpmn:UserTask') {
        const elementObj = getBusinessObject(element)
        console.log(elementObj)
        this.$emit('clickElement', elementObj.get('id'))
      }
    },
    createDiagram() {
      if (this.xml) {
        this.initBpmnModeler()
        this.bpmnModerler.importXML(this.xml).then(res => {
          const { warnings } = res
          if (warnings && warnings.length) {
            warnings.forEach(warn => console.warn(warn))
          }
          this.bpmnModerler.get('canvas').zoom('fit-viewport', 'auto')
          this.setProcessStatus()
        }).catch(err => {
          console.log(err)
        })
      }
    },
    processReZoom() {
      this.defaultZoom = 1
      this.bpmnModerler.get('canvas').zoom('fit-viewport', 'auto')
    },
    processZoomIn(zoomStep = 0.1) {
      const newZoom = Math.floor(this.defaultZoom * 100 + zoomStep * 100) / 100
      if (newZoom > 4) {
        throw new Error('[Process Designer Warn ]: The zoom ratio cannot be greater than 4')
      }
      this.defaultZoom = newZoom
      this.bpmnModerler.get('canvas').zoom(this.defaultZoom)
    },
    processZoomOut(zoomStep = 0.1) {
      const newZoom = Math.floor(this.defaultZoom * 100 - zoomStep * 100) / 100
      if (newZoom < 0.2) {
        throw new Error('[Process Designer Warn ]: The zoom ratio cannot be less than 0.2')
      }
      this.defaultZoom = newZoom
      this.bpmnModerler.get('canvas').zoom(this.defaultZoom)
    },
    setProcessStatus() {
      if (this.processNodeInfo == null || this.bpmnModerler == null) {
        return
      }
      const { finishedTaskSet, rejectedTaskSet, unfinishedTaskSet, finishedSequenceFlowSet } = this.processNodeInfo
      const canvas = this.bpmnModerler.get('canvas')
      const elementRegistry = this.bpmnModerler.get('elementRegistry')
      if (Array.isArray(finishedSequenceFlowSet)) {
        finishedSequenceFlowSet.forEach(item => {
          if (item != null) {
            canvas.addMarker(item, 'success')
            const element = elementRegistry.get(item)
            const conditionExpression = getBusinessObject(element).conditionExpression
            if (conditionExpression) {
              canvas.addMarker(item, 'condition-expression')
            }
          }
        })
      }
      if (Array.isArray(finishedTaskSet)) {
        finishedTaskSet.forEach(item => canvas.addMarker(item, 'success'))
      }
      if (Array.isArray(unfinishedTaskSet)) {
        unfinishedTaskSet.forEach(item => canvas.addMarker(item, 'primary'))
      }
      if (Array.isArray(rejectedTaskSet)) {
        rejectedTaskSet.forEach(item => {
          if (item != null) {
            const element = elementRegistry.get(item)
            if (element.type.includes('Task')) {
              canvas.addMarker(item, 'danger')
            } else {
              canvas.addMarker(item, 'warning')
            }
          }
        })
      }
    }
  }
}
</script>
<style lang="scss" scoped>
@import '@/components/bpmn/theme/viewer.scss';
</style>
