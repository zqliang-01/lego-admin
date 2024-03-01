<template>
  <div id="bpmn-app" v-loading="loading">
    <bpmn-toolbar
      v-if="getEditorConfig.toolbar"
      @save="handleSave"
      @cancel="handleBack"/>
    <div class="main-content">
      <bpmn-designer :xml.sync="xmlString" />
      <bpmn-panel v-if="getEditorConfig.penalMode === 'custom'" />
      <div v-else class="camunda-panel" id="camunda-panel"></div>
    </div>
    <bpmn-settings v-if="showSetting" />
  </div>
</template>

<script>
import BpmnDesigner from '@/components/bpmn/components/Designer'
import BpmnSettings from '@/components/bpmn/components/Settings'
import BpmnToolbar from '@/components/bpmn/components/Toolbar'
import BpmnPanel from '@/components/bpmn/components/Panel'
import '@/components/bpmn/bpmn-icons'
import { mapGetters } from 'vuex'
import { modelDesignAPI, modelBpmnXmlGetAPI } from '@/api/admin/workflow/model'
import EventEmitter from '@/utils/bpmn/EventEmitter'
import { debounce } from 'min-dash'

export default {
  name: 'ModelDesign',
  components: { BpmnPanel, BpmnToolbar, BpmnSettings, BpmnDesigner },
  data() {
    return {
      loading: false,
      modelId: '',
      showSetting: true,
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
    EventEmitter.on('modeler-init', (modeler) => {
      this.modelId = this.$route.params.modelId
      this.init(modeler)
    })
  },
  methods: {
    handleSave() {
      this.loading = true
      modelDesignAPI({
        id: this.modelId,
        bpmnXml: this.xmlString
      }).then(res => {
        this.loading = false
        this.$message.success('保存成功！')
      }).catch(() => {
        this.loading = false
      })
    },
    init: debounce(function(modeler) {
      modelBpmnXmlGetAPI(this.modelId).then(res => {
        const xmlStr = res.data
        this.xmlString = xmlStr
        if (xmlStr) {
          modeler.importXML(xmlStr)
        }
      })
    }, 100),
    handleBack() {
      this.$router.go(-1)
    }
  }
}
</script>
