<template>
  <el-container>
    <el-header class="nav-container">
      <navbar :app-code="appCode"/>
    </el-header>
    <el-container>
      <sidebar
        :main-router="appCode"
        :items="currentRouters"
        :add-offset="quickAddOffset"
        :show-create-button="showQuickAddButton"
        create-button-title="快速创建">
        <div
          v-if="showQuickAddButton"
          slot="add"
          class="quick-add">
          <div class="quick-add-content">
            <p
              v-for="(item, index) in quickAddList"
              :key="index"
              @click="addSkip(item)">
            <i :class="iconPre(item.icon)"/><span>{{ item.title }}</span></p>
          </div>
        </div>
      </sidebar>
      <el-main id="main-container" style="padding: 0;">
        <app-main/>
      </el-main>
    </el-container>
    <lego-all-create
      :visible="createShow"
      :form-code="formCode"
      @update:visible="createShow = $event"
      @close="createShow = false"
      @handle="actionHandle"
    />
  </el-container>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { useStore } from 'vuex'
import { useRoute } from 'vue-router'
import { isObject } from '@/utils/types'
import { Navbar, Sidebar, AppMain, Welcome } from './components'

const props = defineProps({
  currentAppCode: String
})

const store = useStore()
const route = useRoute()

const formCode = ref('')
const appCode = ref('')
const quickAddList = ref([])
const createShow = ref(false)

// 计算属性
const menuRouters = computed(() => store.getters.menuRouters)
const navActiveIndex = computed(() => store.getters.navActiveIndex)
const allAuth = computed(() => store.getters.allAuth)

const currentRouters = computed(() => menuRouters.value[appCode.value])
const appName = computed(() => allAuth.value[appCode.value]?.title || '')
const quickAddOffset = computed(() => Math.round(quickAddList.value.length / 2) * 25)
const showQuickAddButton = computed(() => quickAddList.value.length > 0)

// 方法
const iconPre = (icon) => {
  if (!icon) return ''
  if (icon.startsWith('el-icon')) {
    return icon
  }
  if (icon.startsWith('lego-')) {
    return `lego ${icon}`
  }
  return `el-icon-${icon}`
}

const init = () => {
  const urls = route.path.split('/')
  appCode.value = route.params.model || navActiveIndex.value
  if (urls.length > 1 && urls[1] !== appCode.value) {
    appCode.value = urls[1]
  }
  if (props.currentAppCode) {
    appCode.value = props.currentAppCode
  }
  if (appCode.value) {
    quickAddList.value = []
    addQuickAddMenu(allAuth.value[appCode.value])
    quickAddList.value.sort((a, b) => a.sn - b.sn)
  }
}

const addSkip = (item) => {
  formCode.value = item.formCode
  createShow.value = true
}

const actionHandle = (data) => {
  if (data.type === 'save-success') {
    ElMessage.success('创建成功！')
    return
  }
}

const addQuickAddMenu = (obj) => {
  let hasChildren = false
  for (const key in obj) {
    if (isObject(obj[key])) {
      hasChildren = true
      addQuickAddMenu(obj[key])
    }
  }
  if (!hasChildren && obj?.formCode && obj.add) {
    quickAddList.value.push({
      sn: obj.sn,
      title: obj.title,
      icon: obj.icon,
      formCode: obj.formCode
    })
  }
}

// 监听路由变化
watch(route, () => {
  init()
})

// 初始化
onMounted(() => {
  init()
})
</script>

<style lang="scss" scoped>
@import './styles/common.scss';
.el-container {
  min-height: 0;
  height: 100%;
}
.aside-container {
  position: relative;
  background-color: #2d3037;
  box-sizing: border-box;
}

.nav-container {
  padding: 0;
  box-shadow: 0px 1px 2px #dbdbdb;
  z-index: 100;
  min-width: 1200px;
}
</style>
