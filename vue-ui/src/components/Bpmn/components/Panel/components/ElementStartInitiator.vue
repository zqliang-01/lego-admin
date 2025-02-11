<template>
  <el-collapse-item name="element-start-initiator">
    <template #title>
      <collapse-title title="启动器">
        <LegoIcon name="enable" />
      </collapse-title>
    </template>
    <div class="element-start-initiator">
      <edit-item label="Initiator">
        <el-input v-model="initiator" @change="setElementInitiator" />
      </edit-item>
    </div>
  </el-collapse-item>
</template>

<script>
import { getInitiatorValue, setInitiatorValue } from '../../../bo-utils/initiatorUtil'
import { getActive } from '../../../bpmn-utils/BpmnDesignerUtils'
import CollapseTitle from '../../common/CollapseTitle'
import EventEmitter from '@/utils/bpmn/EventEmitter'
import EditItem from '../../common/EditItem'

export default {
  name: 'ElementStartInitiator',
  components: {
    CollapseTitle,
    EditItem
  },
  data() {
    return {
      initiator: ''
    }
  },
  mounted() {
    this.getElementInitiator()

    EventEmitter.on('element-update', this.getElementInitiator)
  },
  methods: {
    getElementInitiator() {
      getInitiatorValue(getActive())
    },
    setElementInitiator(value) {
      setInitiatorValue(getActive(), value)
    }
  }
}
</script>
