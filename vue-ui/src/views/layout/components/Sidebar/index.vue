<template>
  <div class="sidebar-container">
    <div
      :style="{ 'padding-top': createButtonTitle != '' && showCreateButton ? '40px' : '25px', 'background-color':variables.menuBg }"
      class="create-button-container">
      <el-popover
        v-if="createButtonTitle != '' && showCreateButton"
        :offset="addOffset"
        :visible-arrow="false"
        :disabled="!$slots.add"
        placement="right"
        popper-class="no-padding-popover"
        trigger="hover">
        <slot name="add" />
        <div
          slot="reference"
          class="create-button"
          @click="quicklyCreate">
          <div
            v-show="!buttonCollapse"
            class="button-name">{{ createButtonTitle }}</div>
          <div
            v-show="!buttonCollapse"
            class="button-line" />
          <i
            :class="createButtonIcon"
            class="button-mark" />
        </div>
      </el-popover>
    </div>
    <el-scrollbar
      :style="{'border-right-color': variables.menuBg, 'padding-top': createButtonTitle != '' && showCreateButton ? '90px' : '40px'}"
      wrap-class="scrollbar-wrapper">
      <el-menu
        :default-active="activeMenu"
        :collapse="collapse"
        :unique-opened="true"
        :background-color="variables.menuBg"
        :text-color="variables.menuText"
        :active-text-color="variables.menuActiveText"
        :style="{ paddingBottom: paddingBottom}"
        mode="vertical"
        class="el-menu-vertical"
        @select="handleSelect">
        <sidebar-item
          v-for="(route, index) in items"
          :key="`${route.path}${index}`"
          :item="route"
          :collapse="collapse"
          :base-path="route.path"
          :active-menu="activeMenu" />
      </el-menu>
    </el-scrollbar>
    <slot name="bottom"/>
    <div
      :style="{ 'background-color':variables.menuBg }"
      class="sidebar-bottom">
      <slot name="sidebar-bottom"/>
      <div class="sidebar-bottom-content">
        <img
          :style="{ 'right': buttonCollapse ? '3px' : '0' }"
          :class="{ 'is-close': collapse }"
          class="collapse-button"
          src="@/assets/img/collapse_white.png"
          alt=""
          @click="toggleSideBarClick">
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, ref, watch } from 'vue'
import { useStore } from 'vuex'
import { useRoute } from 'vue-router'
import SidebarItem from './SidebarItem.vue'
import variables from './variables.module.scss'

defineProps({
  items: {
    type: Array,
    default: () => []
  },
  addOffset: {
    type: Number,
    default: 70
  },
  showCreateButton: {
    type: Boolean,
    default: true
  },
  createButtonTitle: {
    type: String,
    default: ''
  },
  createButtonIcon: {
    type: String,
    default: 'el-icon-plus'
  },
  paddingBottom: {
    type: String,
    default: '48px'
  }
})

const emit = defineEmits(['quicklyCreate', 'select'])

const store = useStore()
const route = useRoute()
const buttonCollapse = ref(false)

const collapse = computed(() => store.getters.collapse)

const activeMenu = computed(() => {
  const { meta, path } = route
  if (meta.activeMenu) {
    return meta.activeMenu
  }
  return path
})

watch(collapse, (val) => {
  if (val) {
    buttonCollapse.value = val
  } else {
    setTimeout(() => {
      buttonCollapse.value = val
    }, 300)
  }
}, { immediate: true })

const toggleSideBarClick = () => {
  store.commit('SET_COLLAPSE', !collapse.value)
}

const quicklyCreate = () => {
  emit('quicklyCreate')
}

const handleSelect = (key, keyPath) => {
  emit('select', key, keyPath)
}
</script>

<style lang="scss" scoped>
@import './variables.module.scss';

.sidebar-container {
  transition: width 0.28s;
  width: auto;
  height: 100%;
  position: relative;
  background-color: $menuBg;
  overflow: auto;
  flex-shrink: 0;

  :deep(*::-webkit-scrollbar) {
    width: 0px !important;
    height: 0px !important;
  }

  :deep(.scrollbar-wrapper) {
    overflow-x: hidden !important;
    margin: 0px !important;
  }

  .el-scrollbar {
    height: 100%;
  }

  a {
    display: inline-block;
    width: 100%;
    overflow: hidden;
  }
}

.el-menu-vertical:not(.el-menu--collapse) {
  width: 200px;
  min-height: 400px;
}

.el-menu-vertical {
  height: 100%;
  overflow-y: auto;
  overflow-y: overlay;
  overflow-x: hidden;
  padding-bottom: 48px;
  border-right-color: $menuBg;
  background-color: #001529;
}

.el-menu-vertical.el-menu--collapse {
  :deep(.el-submenu__icon-arrow) {
    display: none;
  }

  :deep(.el-submenu__title) {
    span {
      display: none;
    }
  }

  :deep(.menu-item-content) {
    text-overflow: clip;
  }
  :deep(.el-menu) {
    background-color: $menuBg !important;
  }
}

.create-button-container {
  padding: 15px 14px;
  color: white;
  font-size: 14px;
  cursor: pointer;
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  z-index: 2;

  .create-button {
    display: flex;
    align-items: center;
    justify-content: center;
    box-sizing: border-box;
    padding: 0 15px;
    height: 36px;
    border-radius: $xr-border-radius-base;
    background-color: rgba($color: #fff, $alpha: 0.1);
    color: #999;

    .button-name {
      flex: 1;
    }

    .button-line {
      height: 10px;
      background-color: white;
      width: 1px;
      margin: 0 20px 0 10px;
      opacity: 0.3;
    }

    .button-mark {
      width: 12px;
    }
  }

  .create-button:hover {
    color: white !important;
    background-color: $xr-color-primary !important;
  }
}

.sidebar-bottom {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;

  &-content {
    position: relative;
    height: 48px;
  }

  .copyright {
    color: white;
    font-size: 12px;
    height: 100%;
    padding-left: 20px;
    line-height: 3.5;
    img,span {
      vertical-align: middle;
    }
  }
}

.collapse-button {
  position: absolute;
  top: 0;
  padding: 18px 20px;
  cursor: pointer;
}

.collapse-button.is-close {
  transform: rotate(180deg);
}
</style>
