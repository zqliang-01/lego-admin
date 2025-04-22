<template>
  <div id="bpmn-app" v-loading="loading">
    <bpmn-toolbar
      v-if="editorConfig.toolbar"
      @save="handleSave"
      @cancel="handleBack"/>
    <div class="main-content">
      <bpmn-designer :xml.sync="xmlString" />
      <bpmn-panel v-if="editorConfig.penalMode === 'custom'" />
      <div v-else class="camunda-panel" id="camunda-panel"></div>
    </div>
    <bpmn-settings v-if="showSetting" />
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useStore } from 'vuex'
import { ElMessage } from 'element-plus'
import { debounce } from 'min-dash'
import BpmnDesigner from '@/components/Bpmn/components/Designer'
import BpmnSettings from '@/components/Bpmn/components/Settings'
import BpmnToolbar from '@/components/Bpmn/components/Toolbar'
import BpmnPanel from '@/components/Bpmn/components/Panel'
import '@/components/Bpmn/bpmn-icons'
import { modelDesignAPI, modelBpmnXmlGetAPI } from '@/api/admin/workflow/model'
import EventEmitter from '@/utils/bpmn/EventEmitter'

const store = useStore()
const route = useRoute()
const router = useRouter()

const loading = ref(false)
const showSetting = ref(true)
const xmlString = ref('')
const modelId = ref('')

const editorConfig = computed(() => store.getters.getEditorConfig)

const handleSave = () => {
  loading.value = true
  modelDesignAPI({
    id: modelId.value,
    bpmnXml: xmlString.value
  }).then(() => {
    loading.value = false
    ElMessage.success('保存成功！')
  }).catch(() => {
    loading.value = false
  })
}

const init = debounce((modeler) => {
  modelBpmnXmlGetAPI(modelId.value).then(res => {
    const xmlStr = res.data
    xmlString.value = xmlStr
    if (xmlStr) {
      modeler.importXML(xmlStr)
    }
  })
}, 100)

const handleBack = () => {
  router.go(-1)
}

onMounted(() => {
  document.body.addEventListener('contextmenu', (ev) => {
    ev.preventDefault()
  })
  
  modelId.value = route.params.modelId
  
  EventEmitter.on('modeler-init', (modeler) => {
    init(modeler)
  })
})
</script>

<style scoped>
.main-content {
  display: flex;
  height: calc(100vh - 50px);
}

.camunda-panel {
  width: 300px;
  border-left: 1px solid #e0e0e0;
}
</style>
