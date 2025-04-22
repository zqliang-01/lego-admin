<template>
  <div class="system-customer">
    <xr-header
      :icon-class="'double-gear'"
      :show-search="true"
      :show-save="manage.workflow.model.update"
      icon-color="#1CBAF5"
      label="模型管理"
      placeholder="请输入模型名称搜索"
      @search="onSearch"
      @create="onCreate"/>
    <div class="customer-content">
      <lego-table
        :loading="loading"
        :data-list="dataList"
        :field-list="fieldList"
        :current-page="currentPage"
        :page-size="pageSize"
        :total="total"
        :edit-button-width="250"
        @onList="getList"
        @onEdit="handleTable"
        @onClickField="handleField">
        <template #default="scope">
          <el-button
            v-if="manage.workflow.model.update"
            type="text"
            size="small"
            icon="el-icon-edit"
            @click="handleTable('edit', scope.row, scope.$index)">编辑</el-button>
          <el-button
            v-if="manage.workflow.model.update"
            type="text"
            size="small"
            icon="el-icon-brush"
            @click="handleTable('design', scope.row, scope.$index)">设计流程</el-button>
          <el-button
            v-if="manage.workflow.model.deploy"
            type="text"
            size="small"
            icon="el-icon-video-play"
            @click="handleTable('depoly', scope.row, scope.$index)">部署</el-button>
          <el-button
            v-if="manage.workflow.model.delete"
            type="text"
            size="small"
            icon="el-icon-delete"
            @click="handleTable('delete', scope.row, scope.$index)">删除</el-button>
        </template>
      </lego-table>
    </div>
    <Create
      :visible="isCreate"
      :field-list="fieldList"
      :action="action"
      @handle="actionHandle"
      @update:visible="val => isCreate = val"
    />
    <el-dialog
      title="流程信息"
      v-model:visible="processVisible"
      width="70%"
      append-to-body>
      <bpmn-viewer :key="modelId" v-model:xml="processXml"/>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import Create from './Create'
import XrHeader from '@/components/XrHeader'
import LegoTable from '@/components/Lego/LegoTable'
import BpmnViewer from '@/components/Bpmn/components/Viewer'
import {
  modelListAPI,
  modelDeployAPI,
  modelDeleteAPI,
  modelBpmnXmlGetAPI
} from '@/api/admin/workflow/model'

const store = useStore()
const router = useRouter()
const loading = ref(false)
const isCreate = ref(false)
const processVisible = ref(false)
const processXml = ref('')
const modelId = ref('')
const dataList = ref([])
const currentPage = ref(1)
const pageSize = ref(15)
const total = ref(0)
const search = ref('')

const action = ref({
  type: 'update',
  detailData: {}
})

const fieldList = ref([
  [
    { fieldCode: 'id', name: '模型ID', formType: 'text', width: '260', unique: true },
    { fieldCode: 'key', name: '模型标识', formType: 'text', width: '150', required: true }
  ],
  [
    { fieldCode: 'name', name: '名称', formType: 'text', width: '150', required: true, clickable: true },
    { fieldCode: 'description', name: '备注', formType: 'text', width: '100' }
  ],
  [
    { fieldCode: 'version', name: '版本', formType: 'text', width: '100', show: false },
    { fieldCode: 'createTime', name: '创建时间', formType: 'text', width: '150', show: false }
  ]
])

const manage = computed(() => store.getters.manage)

onMounted(() => {
  refresh()
})

const refresh = () => {
  getList()
}

const getList = (pageSizeVal = pageSize.value, currentPageVal = currentPage.value) => {
  loading.value = true
  modelListAPI({
    name: search.value,
    pageIndex: currentPageVal,
    pageSize: pageSizeVal
  }).then(res => {
    dataList.value = res.data.result
    total.value = res.data.totalCount
    currentPage.value = res.data.pageIndex
    loading.value = false
  }).catch(() => {
    loading.value = false
  })
}

const handleSizeChange = (val) => {
  pageSize.value = val
  getList()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  getList()
}

const handleTable = (type, item, index) => {
  if (type === 'design') {
    router.push({
      name: 'modelDesign',
      params: {
        modelId: item.id
      }
    })
    return
  }
  if (type === 'depoly') {
    loading.value = true
    modelDeployAPI(item.id).then(() => {
      loading.value = false
      ElMessage.success('部署成功！')
      refresh()
    }).catch(() => {
      loading.value = false
    })
    return
  }
  if (type === 'delete') {
    ElMessageBox.confirm(`此操作将永久删除[${item.name}]的[${item.version}]版本，是否继续?`, '提示', {
      type: 'warning'
    }).then(() => {
      modelDeleteAPI(item.id).then(() => {
        ElMessage.success('删除成功！')
        refresh()
      })
    })
    return
  }
  action.value.type = 'update'
  action.value.detailData = item
  isCreate.value = true
}

const handleField = (data, row) => {
  if (data && data.field.fieldCode === 'name') {
    modelBpmnXmlGetAPI(row.id).then(res => {
      modelId.value = row.id
      processXml.value = res.data
      processVisible.value = true
    })
    return
  }
}

const onSearch = (value) => {
  search.value = value
  getList()
}

const onCreate = () => {
  action.value.type = 'save'
  action.value.detailData = {}
  isCreate.value = true
}

const actionHandle = (data) => {
  if (data.type === 'save-success') {
    ElMessage.success('模型保存成功！')
    refresh()
  }
}
</script>

<style lang="scss" scoped>
.system-customer {
  height: 100%;
}
.customer-content {
  border-top: 1px solid $xr-border-line-color;
  border-bottom: 1px solid $xr-border-line-color;
}
</style>
