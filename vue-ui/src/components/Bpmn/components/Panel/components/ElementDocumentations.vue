<template>
  <el-collapse-item name="element-documentations">
    <template #title>
      <collapse-title title="附加文档">
        <lucide-icon name="FileText" />
      </collapse-title>
    </template>
    <edit-item label="Documentation" :label-width="120">
      <el-input v-model="elementDoc" type="textarea" @change="updateElementDoc" />
    </edit-item>
  </el-collapse-item>
</template>

<script>
import { getDocumentValue, setDocumentValue } from '../../../bo-utils/documentationUtil'
import EditItem from '../../common/EditItem'
import LucideIcon from '../../common/LucideIcon'
import CollapseTitle from '../../common/CollapseTitle'
import EventEmitter from '@/utils/bpmn/EventEmitter'
import { getActive } from '../../../bpmn-utils/BpmnDesignerUtils'

export default {
  name: 'ElementDocumentations',
  components: {
    CollapseTitle,
    LucideIcon,
    EditItem
  },
  data() {
    return {
      elementDoc: ''
    }
  },

  watch: {
    getActive: {
      immediate: true,
      handler() {
        this.elementDoc = getDocumentValue(getActive()) || ''
      }
    }
  },
  mounted() {
    this.elementDoc = getDocumentValue(getActive()) || ''
    EventEmitter.on('element-update', () => {
      this.elementDoc = getDocumentValue(getActive()) || ''
    })
  },
  methods: {
    updateElementDoc(value) {
      setDocumentValue(getActive(), value)
    }
  }
}
</script>
