<template>
  <div class="bpmn-toolbar">
    <el-button-group>
      <el-button type="primary" @click="handleSave">
        保存
      </el-button>
      <el-button type="primary" @click="handleCancel">
        返回
      </el-button>
      <bpmn-import v-if="showImport"/>
      <bpmn-exports v-if="showExport"/>
      <bpmn-previews />
    </el-button-group>
    <bpmn-aligns />
    <bpmn-scales />
    <bpmn-commands />
    <bpmn-externals />
  </div>
</template>

<script setup>
import { ref } from 'vue'
import BpmnImport from './tools/Import'
import BpmnExports from './tools/Exports'
import BpmnPreviews from './tools/Previews'
import BpmnAligns from './tools/Aligns'
import BpmnScales from './tools/Scales'
import BpmnCommands from './tools/Commands'
import BpmnExternals from './tools/Externals'

const showImport = ref(false)
const showExport = ref(false)

const handleSave = () => {
  emit('save')
}

const handleCancel = () => {
  emit('cancel')
}

defineEmits(['save', 'cancel'])
</script>

<style scoped>
.bpmn-toolbar {
  padding: 8px 10px;
  background: #f8f8f8;
  border-bottom: 1px solid #e0e0e0;
  display: flex;
  align-items: center;
  flex-wrap: wrap;
}

.bpmn-toolbar .el-button-group {
  margin-right: 15px;
}
</style>
