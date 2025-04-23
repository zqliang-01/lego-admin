<template>
  <div class="system-customer">
    <xr-header
      :icon-class="'double-gear'"
      :show-search="true"
      :show-save="false"
      icon-color="#1CBAF5"
      label="部署管理"
      placeholder="请输入部署名称搜索"
      @search="onSearch"/>
    <div class="customer-content">
      <lego-table
        :loading="loading"
        :data-list="dataList"
        :field-list="fieldList"
        :current-page="currentPage"
        :page-size="pageSize"
        :total="total"
        @onList="getList"
        @onEdit="handleTable"
        @onClickField="handleField">
        <template #default="scope">
          <el-button
            v-if="manage.workflow.definition.update"
            type="text"
            size="small"
            icon="el-icon-price-tag"
            @click="handleTable('version', scope.row, scope.$index)">版本管理</el-button>
          <el-button
            v-if="manage.workflow.definition.delete"
            type="text"
            size="small"
            icon="el-icon-delete"
            @click="handleTable('delete', scope.row, scope.$index)">删除</el-button>
        </template>
      </lego-table>
    </div>

    <version-dialog
      :visible="versionVisible"
      :definition-key="definitionKey"
      @save-success="handleSaveSuccess"
    />
    <el-dialog title="流程信息" :visible="processVisible" width="70%" append-to-body>
      <bpmn-viewer :key="definitionKey" :xml="definitionXml"/>
    </el-dialog>
  </div>
</template>

<script setup>
import {
  definitionBpmnXmlGetAPI,
  definitionDeleteAPI,
  definitionListAPI,
  definitionStartAPI
} from '@/api/admin/workflow/definition'
import BpmnViewer from '@/components/Bpmn/components/Viewer'
import LegoTable from '@/components/Lego/LegoTable'
import XrHeader from '@/components/XrHeader'
import { ElMessage } from 'element-plus'
import { computed, onMounted, ref } from 'vue'
import { useStore } from 'vuex'
import VersionDialog from './VersionDialog'

const store = useStore()
const loading = ref(false)
const versionVisible = ref(false)
const processVisible = ref(false)
const definitionXml = ref('')
const definitionKey = ref('')
const dataList = ref([])
const currentPage = ref(1)
const pageSize = ref(15)
const total = ref(0)
const search = ref('')

const fieldList = ref([
  [
    { fieldCode: 'key', name: '模型标识', formType: 'text', width: '150' },
    { fieldCode: 'name', name: '名称', formType: 'text', width: '150', clickable: true }
  ],
  [
    { fieldCode: 'deploymentTime', name: '发布时间', formType: 'text', width: '150', editable: false },
    { fieldCode: 'version', name: '版本', formType: 'text', width: '100', editable: false },
    { fieldCode: 'active', name: '是否激活', formType: 'boolean', width: '100', editable: false }
  ]
])

const manage = computed(() => store.getters.manage)

onMounted(() => {
  getList()
})

const getList = (pageSizeVal = pageSize.value, currentPageVal = currentPage.value) => {
  loading.value = true
  definitionListAPI({
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

const handleTable = (type, item, index) => {
  if (type === 'version') {
    definitionKey.value = item.key
    versionVisible.value = true
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
        getList()
      }).catch(() => {
        loading.value = false
      })
    })
    return
  }
  if (type === 'start') {
    loading.value = true
    definitionStartAPI({
      definitionId: item.id
    }).then(() => {
      loading.value = false
      ElMessage.success('任务发布成功！')
      getList()
    }).catch(() => {
      loading.value = false
    })
    return
  }
}

const handleField = (data, row) => {
  if (data && data.field.fieldCode === 'name') {
    definitionBpmnXmlGetAPI(row.id).then(res => {
      definitionKey.value = row.key
      definitionXml.value = res.data
      processVisible.value = true
    })
    return
  }
}

const onSearch = (value) => {
  search.value = value
  getList()
}

const handleSaveSuccess = () => {
  getList()
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
