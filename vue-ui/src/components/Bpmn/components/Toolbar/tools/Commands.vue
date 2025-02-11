<template>
  <el-button-group>
    <el-button v-r-popover:undo class="el-button__no-padding" @click="undo">
      <LegoIcon name="el-icon-arrow-left" :size="16" />
      <el-popover ref="undo" placement="bottom" trigger="hover" popper-class="button-popover" content="撤销" />
    </el-button>
    <el-button v-r-popover:redo class="el-button__no-padding" @click="redo">
      <LegoIcon name="el-icon-arrow-right" :size="16" />
      <el-popover ref="redo" placement="bottom" trigger="hover" popper-class="button-popover" content="恢复" />
    </el-button>
    <el-button v-r-popover:restart class="el-button__no-padding" @click="restart">
      <LegoIcon name="el-icon-delete" :size="16" />
      <el-popover ref="restart" placement="bottom" trigger="hover" popper-class="button-popover" content="擦除重做" />
    </el-button>
  </el-button-group>
</template>

<script>
import { mapGetters } from 'vuex'
import { createNewDiagram } from '@/utils/bpmn/xml'
import ResetPopover from '@/utils/bpmn/resetPopover'

export default {
  name: 'BpmnCommands',
  computed: {
    ...mapGetters(['getModeler'])
  },
  directives: {
    'r-popover': ResetPopover
  },
  methods: {
    getCommand() {
      return this.getModeler && this.getModeler.get('commandStack')
    },
    redo() {
      const command = this.getCommand()
      command && command.canRedo() && command.redo()
    },
    undo() {
      const command = this.getCommand()
      command && command.canUndo() && command.undo()
    },
    restart() {
      const command = this.getCommand()
      command && command.clear()
      this.getModeler && createNewDiagram(this.getModeler)
    }
  }
}
</script>
