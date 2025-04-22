<template>
  <el-dialog
    title="部署版本信息"
    :model-value="visible"
    :close-on-click-modal="false"
    :modal-append-to-body="true"
    append-to-body
    width="60%"
    @close="handleCancel">
    <el-table
      v-loading="loading"
      :data="dataList"
      :height="tableHeight"
      highlight-current-row
      style="width: 100%">
      <el-table-column
        v-for="(item, index) in fieldList"
        :key="index"
        :min-width="item.width"
        :prop="item.fieldCode"
        :label="item.name"
        show-overflow-tooltip>
        <template #default="scope">
          <field-view
            :item="item"
            :form-type="item.formType"
            :value="scope.row[item.fieldCode]"
            @clickValue="handleField($event, scope.row, scope.$index)"/>
        </template>
      </el-table-column>
      <el-table-column
        fixed="right"
        label="操作"
        width="150">
        <template #default="scope">
          <el-button
            v-if="manage.workflow.definition.update"
            type="text"
            size="small"
            @click="handleTable('edit', scope.row, scope.$index)">{{ scope.row.active ? '挂起' : '激活' }}</el-button>
          <el-button
            v-if="manage.workflow.definition.delete"
            type="text"
            size="small"
            icon="el-icon-delete"
            @click="handleTable('delete', scope.row, scope.$index)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="p-contianer">
      <el-pagination
        :current-page="currentPage"
        :page-sizes="pageSizes"
        :page-size="pageSize"
        :total="total"
        :pager-count="5"
        class="p-bar"
        background
        layout="prev, pager, next, sizes, total, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"/>
    </div>
    <el-dialog
      title="流程信息"
      :model-value="processVisible"
      width="70%"
      append-to-body
      @close="processVisible = false">
      <bpmn-viewer :key="definitionId" v-model:xml="definitionXml"/>
    </el-dialog>
  </el-dialog>
</template>

<script setup>
import { ref, computed, watch, onMounted } from 'vue'
import { useStore } from 'vuex'
import { ElMessage, ElMessageBox } from 'element-plus'
import FieldView from '@/components/Common/Form/FieldView'
import BpmnViewer from '@/components/Bpmn/components/Viewer'
import {
  definitionDeleteAPI,
  definitionActiveAPI,
  definitionSuspendAPI,
  definitionHistoryListAPI,
  definitionBpmnXmlGetAPI
} from '@/api/admin/workflow/definition'

const props = defineProps({
  visible: {
    type: Boolean,
    default: false
  },
  definitionKey: String
})

const emit = defineEmits(['update:visible', 'save-success'])

const store = useStore()
const loading = ref(false)
const processVisible = ref(false)
const definitionXml = ref('')
const definitionId = ref('')
const dataList = ref([])
const currentPage = ref(1)
const pageSize = ref(15)
const pageSizes = ref([15, 30, 60, 100])
const total = ref(0)
const tableHeight = ref(document.documentElement.clientHeight - 400)

const fieldList = ref([
  { fieldCode: 'name', name: '名称', formType: 'text', width: '150', required: true, clickable: true },
  { fieldCode: 'key', name: '模型标识', formType: 'text', width: '150', required: true },
  { fieldCode: 'version', name: '版本', formType: 'text', width: '50', required: true, editable: false },
  { fieldCode: 'active', name: '是否激活', formType: 'boolean', width: '100', editable: false }
])

const manage = computed(() => store.getters.manage)

watch(() => props.definitionKey, () => {
  refresh()
})

onMounted(() => {
  window.onresize = () => {
    tableHeight.value = document.documentElement.clientHeight - 140
  }
  refresh()
})

const refresh = () => {
  loading.value = true
  definitionHistoryListAPI({
    key: props.definitionKey,
    pageIndex: currentPage.value,
    pageSize: pageSize.value
  }).then(res => {
    dataList.value = res.data.result
    total.value = res.data.totalCount
    currentPage.value = res.data.pageIndex
    loading.value = false
  }).catch(() => {
    loading.value = false
  })
}

const handleCancel = () => {
  emit('update:visible', false)
}

const handleSizeChange = (val) => {
  pageSize.value = val
  refresh()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  refresh()
}

const handleTable = (type, item, index) => {
  if (type === 'edit') {
    loading.value = true
    if (item.active) {
      definitionSuspendAPI(item.id).then(() => {
        loading.value = false
        ElMessage.success('挂起成功！')
        refresh()
        emit('save-success')
      }).catch(() => {
        loading.value = false
      })
    } else {
      definitionActiveAPI(item.id).then(() => {
        loading.value = false
        ElMessage.success('激活成功！')
        refresh()
        emit('save-success')
      }).catch(() => {
        loading.value = false
      })
    }
    return
  }
  if (type === 'delete') {
    ElMessageBox.confirm(`此操作将永久删除[${item.name}]的[${item.version}]版本，是否继续?`, '提示', {
      type: 'warning'
    }).then(() => {
      loading.value = true
      definitionDeleteAPI(item.id).then(() => {
        loading.value = false
        ElMessage.success('删除成功！')
        refresh()
        emit('save-success')
      }).catch(() => {
        loading.value = false
      })
    })
    return
  }
}

const handleField = (data, row) => {
  if (data && data.field.fieldCode === 'name') {
    definitionBpmnXmlGetAPI(row.id).then(res => {
      definitionId.value = row.id
      definitionXml.value = res.data
      processVisible.value = true
    })
    return
  }
}
</script>

<style lang="scss" scoped>
.p-contianer {
  position: relative;
  background-color: white;
  height: 44px;
  .p-bar {
    float: right;
    margin: 5px 100px 0 0;
    font-size: 14px !important;
  }
}
</style>
