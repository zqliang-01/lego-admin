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
      <template slot="title">
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

<script>
import path from 'path'
import { mapGetters } from 'vuex'
import { isExternal } from '@/utils/validate'
import Item from './Item'
import AppLink from './Link'
import FixiOSBug from './FixiOSBug'

export default {
  name: 'SidebarItem',
  components: { Item, AppLink },
  mixins: [FixiOSBug],
  props: {
    // route object
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
  },
  computed: {
    ...mapGetters(['name'])
  },
  data() {
    // To fix https://github.com/PanJiaChen/vue-admin-template/issues/237
    // TODO: refactor with render function
    this.onlyOneChild = null
    return {}
  },
  watch: {
    activeMenu: {
      handler(val) {
        if (val.endsWith(this.item.path)) {
          let title = this.name
          const meta = this.item.meta
          if (meta.title) {
            title += ' - ' + meta.title
          } else if (params && params.title) {
            title += ' - ' + params.title
          }
          document.title = title
        }
      },
      immediate: true
    }
  },
  methods: {
    resolvePath(routePath) {
      if (isExternal(routePath)) {
        return routePath
      }
      if (isExternal(this.basePath)) {
        return this.basePath
      }
      return path.resolve(this.basePath, routePath)
    }
  }
}
</script>
<style lang="scss" scoped>
@import './variables.module.scss';

.menu-wrapper {
  ::v-deep .el-submenu__title {
    height: auto;
    line-height: normal;
    color: #bebec0;
  }

  ::v-deep .el-submenu.is-active {
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

// element自带的有问题 is-active 换成 is-select
.el-menu-item.is-select {
  height: auto;
  .menu-item-content {
    background-color: #2362fb !important;
    color: white !important;
    ::v-deep i {
      color: white !important;
    }
  }
}

.el-menu-item:hover {
  .menu-item-content {
    background-color: rgba($color: #fff, $alpha: 0.1);
    color: white;
    ::v-deep i {
      color: white !important;
    }
  }
}
</style>
