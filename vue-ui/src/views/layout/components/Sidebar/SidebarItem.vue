<template>
  <div v-if="!item.hidden" class="menu-wrapper">
    <div v-if="item.meta && !item.meta.isMenu">
      <sidebar-item
        v-for="child in item.children"
        :key="child.path"
        :is-nest="true"
        :item="child"
        :base-path="resolvePath(item.path)"
        :active-menu="activeMenu"
        class="nest-menu" />
    </div>
    <!-- 只有一个可视子菜单的菜单显示 -->
    <template v-else-if="!item.children || item.children.length === 0">
      <el-tooltip
        :disabled="!collapse"
        :content="item.meta.title"
        effect="dark"
        placement="right">
        <app-link
          v-if="item.meta"
          :to="resolvePath(item.path)">
          <el-menu-item
            :index="resolvePath(item.path)"
            :class="{ 'is-select': activeMenu == resolvePath(item.path)}">
            <item
              :icon="item.meta.icon"
              :title="item.meta.title"
              :num="item.meta.num"
              :count="item.count"
              :collapse="collapse" />
          </el-menu-item>
        </app-link>
      </el-tooltip>
    </template>

    <el-submenu
      v-else
      ref="subMenu"
      :index="resolvePath(item.path)"
      :class="{ 'is-select': activeMenu == resolvePath(item.path)}"
      popper-append-to-body>
      <template #title>
        <item
          v-if="item.meta"
          :icon="item.meta && item.meta.icon"
          :title="item.meta.title"
          :num="item.meta.num" />
      </template>
      <sidebar-item
        v-for="child in item.children"
        :key="child.path"
        :is-nest="true"
        :item="child"
        :base-path="resolvePath(item.path)"
        :active-menu="activeMenu"
        class="nest-menu" />
    </el-submenu>
  </div>
</template>

<script setup>
import { computed, watch } from 'vue'
import { useStore } from 'vuex'
import { useRoute } from 'vue-router'
import path from 'path'
import { isExternal } from '@/utils/validate'
import Item from './Item.vue'
import AppLink from './Link.vue'
import FixiOSBug from './FixiOSBug.js'

const props = defineProps({
  item: {
    type: Object,
    required: true
  },
  basePath: {
    type: String,
    default: ''
  },
  activeMenu: String,
  collapse: Boolean
})

const store = useStore()
const route = useRoute()
const name = computed(() => store.getters.name)

const resolvePath = (routePath) => {
  if (isExternal(routePath)) {
    return routePath
  }
  if (isExternal(props.basePath)) {
    return props.basePath
  }
  return path.resolve(props.basePath, routePath)
}

watch(() => route.path, (val) => {
  let path = props.item.path
  if (path && !path.startsWith('/')) {
    path = '/' + path
  }
  if (val.endsWith(path)) {
    let title = name.value
    const meta = props.item.meta
    if (meta?.title) {
      title += ' - ' + meta.title
    }
    document.title = title
  }
}, { immediate: true })
</script>

<style lang="scss" scoped>
@import './variables.module.scss';

.menu-wrapper {
  :deep(.el-submenu__title) {
    height: auto;
    line-height: normal;
    color: #bebec0;
  }

  :deep(.el-submenu.is-active) {
    .el-submenu__title {
      span,
      i:first-child {
        color: white;
      }
    }
  }
}

.el-menu-item {
  height: auto;
  line-height: normal;
  padding: 0 14px !important;
  background-color: $menuBg !important;
  color: #bebec0;
}

.el-menu-item:not(.is-select) {
  color: $menuText !important;
  height: auto;
}

.el-menu-item.is-select {
  height: auto;
  .menu-item-content {
    background-color: #2362fb !important;
    color: white !important;
    :deep i {
      color: white !important;
    }
  }
}

.el-menu-item:hover {
  .menu-item-content {
    background-color: rgba($color: #fff, $alpha: 0.1);
    color: white;
    :deep i {
      color: white !important;
    }
  }
}
</style>
