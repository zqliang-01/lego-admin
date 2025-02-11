<template>
  <el-button-group>
    <el-button
      v-r-popover:popover="index"
      class="el-button__no-padding"
      v-for="(btn, index) in buttons"
      :key="btn.key"
      @click="alignElements(btn.key)"
    >
      <LegoIcon :name="btn.icon" :size="16" />
      <el-popover
        ref="popover"
        placement="bottom"
        trigger="hover"
        popper-class="button-popover"
        :content="btn.name"
      />
    </el-button>
  </el-button-group>
</template>

<script>
import { mapGetters } from 'vuex'
import EventEmitter from '@/utils/bpmn/EventEmitter'
import ResetPopover from '@/utils/bpmn/resetPopover'

export default {
  name: 'BpmnAligns',
  computed: {
    ...mapGetters(['getModeler'])
  },
  directives: {
    'r-popover': ResetPopover
  },
  data() {
    return {
      buttons: [
        { name: '左对齐', key: 'left', icon: 'el-icon-caret-left' },
        { name: '水平居中', key: 'center', icon: 'el-icon-s-data' },
        { name: '右对齐', key: 'right', icon: 'el-icon-caret-right' },
        { name: '上对齐', key: 'top', icon: 'el-icon-caret-top' },
        { name: '垂直居中', key: 'middle', icon: 'el-icon-d-caret' },
        { name: '下对齐', key: 'bottom', icon: 'el-icon-caret-bottom' }
      ]
    }
  },
  created() {
    EventEmitter.on('modeler-init', (modeler) => {
      this._modeling = modeler.get('modeling')
      this._selection = modeler.get('selection')
      this._align = modeler.get('alignElements')
    })
  },
  methods: {
    alignElements(tag) {
      if (!this._align) {
        return this.$message.error('当前模式不支持自动对齐')
      }
      if (this._modeling && this._selection) {
        const SelectedElements = this._selection.get()
        if (!SelectedElements || SelectedElements.length <= 1) {
          return this.$message.warning('请按住 Shift 键选择多个元素对齐')
        }
        this._align.trigger(SelectedElements, tag)
      }
    }
  }
}
</script>
