<template>
  <div id="app">
    <bpmn-toolbar v-if="getEditorConfig.toolbar" />
    <div class="main-content">
      <bpmn-designer :xml.sync="xmlString" />
      <bpmn-panel v-if="getEditorConfig.penalMode === 'custom'" />
      <div v-else class="camunda-panel" id="camunda-panel"></div>
    </div>
    <el-button class="add-btn" @click="deploy">发布</el-button>
    <el-button class="add-btn" @click="getStatus">当前状态</el-button>

    <bpmn-settings v-if="showSetting" />
  </div>
</template>

<script>
import BpmnDesigner from '@/components/bpmn/components/Designer'
import BpmnSettings from '@/components/bpmn/components/Settings'
import BpmnToolbar from '@/components/bpmn/components/Toolbar'
import BpmnPanel from '@/components/bpmn/components/Panel'
import 'highlight.js/styles/atom-one-dark-reasonable.css'
import '@/components/bpmn/theme/index.scss'
import '@/components/bpmn/bpmn-icons'
import { mapGetters } from 'vuex'
import { design, taskStatusGetAPI } from '@/api/admin/workflow'
import { getModeler } from '@/components/bpmn/bpmn-utils/BpmnDesignerUtils'

export default {
  name: 'WorkFlow',
  components: { BpmnPanel, BpmnToolbar, BpmnSettings, BpmnDesigner },
  data() {
    return {
      showSetting: false,
      xmlString: undefined
    }
  },
  computed: {
    ...mapGetters(['getEditorConfig'])
  },
  mounted() {
    document.body.addEventListener('contextmenu', function(ev) {
      ev.preventDefault()
    })
  },
  methods: {
    deploy() {
      console.log(this.xmlString)
      design({
        key: '234',
        bpmnXml: this.xmlString
      }).then(res => {
        console.log(res)
      })
    },
    getStatus() {
      taskStatusGetAPI('Process_1704883954718:2:4648c062-b027-11ee-b348-2cf05d6475fb').then(res => {
        this.bpmnXml = res.data
        getModeler().importXML(this.bpmnXml)
      })
    }
  }
}
</script>
