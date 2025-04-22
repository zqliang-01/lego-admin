<template>
  <div :class="['bpmn-designer', bgClass]" ref="designerRef"></div>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { useStore } from 'vuex'
import { debounce } from 'min-dash'
import { createNewDiagram } from '@/utils/bpmn/xml'
import { catchError } from '@/utils/bpmn/printCatch'
import moduleAndExtensions from './moduleAndExtensions'
import initModeler from './initModeler'

const props = defineProps({
  xml: {
    type: String,
    default: undefined
  },
  events: {
    type: Array,
    default: () => []
  }
})

const store = useStore()
const designerRef = ref(null)

const editor = computed(() => store.getters.getEditor)
const modeler = computed(() => store.getters.getModeler)
const modeling = computed(() => store.getters.getModeling)

const bgClass = computed(() => {
  const bg = editor.value.bg
  if (bg === 'grid-image') return 'designer-with-bg'
  if (bg === 'image') return 'designer-with-image'
  return ''
})

const reloadProcess = debounce(async (setting, oldSetting) => {
  const modelerModules = moduleAndExtensions(setting)

  const modelerInstance = initModeler(designerRef.value, modelerModules)
  if (!oldSetting || setting.processEngine !== oldSetting.processEngine) {
    await createNewDiagram(modelerInstance)
  } else {
    await createNewDiagram(modelerInstance, props.xml, setting)
  }
}, 100)

watch(editor, async (value, oldValue) => {
  try {
    await reloadProcess(value, oldValue)
  } catch (e) {
    catchError(e)
  }
}, { immediate: true, deep: true })
</script>

<style scoped>
.bpmn-designer {
  width: 100%;
  height: 100%;
  position: relative;
  overflow: hidden;
}

.designer-with-bg {
  background-image: url('~@/assets/img/grid.png');
  background-repeat: repeat;
}

.designer-with-image {
  background-image: url('~@/assets/img/background.jpg');
  background-repeat: no-repeat;
  background-size: 100% 100%;
}
</style>
