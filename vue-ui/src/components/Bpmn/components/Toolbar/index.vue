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

<script>
import BpmnImport from './tools/Import'
import BpmnExports from './tools/Exports'
import BpmnPreviews from './tools/Previews'
import BpmnAligns from './tools/Aligns'
import BpmnScales from './tools/Scales'
import BpmnCommands from './tools/Commands'
import BpmnExternals from './tools/Externals'
export default {
  name: 'BpmnToolbar',
  components: { BpmnExternals, BpmnCommands, BpmnScales, BpmnAligns, BpmnPreviews, BpmnExports, BpmnImport },
  data() {
    return {
      showImport: false,
      showExport: false
    }
  },
  methods: {
    handleSave() {
      this.$emit('save')
    },
    handleCancel() {
      this.$emit('cancel')
    }
  }
}
</script>
