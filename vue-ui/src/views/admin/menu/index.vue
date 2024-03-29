<template>
  <div class="menu-management">
    <xr-header
      :icon-class="'s-seas'"
      placeholder="请输入菜单名称"
      show-search
      icon-color="#26D4DA"
      label="菜单管理"
      @search="headerSearch"/>
    <div class="system-content">
      <!-- 左边导航栏 -->
      <div
        v-loading="loading"
        class="system-nav">
        <div class="nav__hd">
          菜单列表
          <el-switch
            v-model="filterRouteStatus"
            active-text="动态路由"
            class="route-type"
            @change="filterRouteType"/>
        </div>
        <div class="system-nav__content">
          <div class="section">
            <div class="section__content">
              <el-tree
                ref="menuTree"
                :props="menuTreeProps"
                :expand-on-click-node="false"
                :data="menuList"
                node-key="code"
                highlight-current
                @node-click="changeMenuClick">
                <flexbox slot-scope="{ node, data }" :class="{ 'is-current': data.code === currentMenu.code}" class="node-data">
                  <i v-if="data.type.code !== 'auth'" :class="data.icon | iconPre" />
                  <i v-else :class="'doc' | iconPre" />
                  <div class="node-data__label text-one-line">{{ node.label }}</div>
                  <i v-if="node.childNodes && node.childNodes.length"
                    :class="[{ 'is-close': !node.expanded }, {'lego lego-up-unfold': true}]"
                    @click="node.expanded = !node.expanded"/>
                </flexbox>
              </el-tree>
            </div>
          </div>
        </div>
      </div>
      <!-- 右边内容 -->
      <div class="system-view-table flex-index">
        <detail
          :menu-list="menuList"
          :menu-data="currentMenu"
          @success="saveSuccess"
          @deleteSuccess="deleteSuccess"/>
      </div>
    </div>
  </div>
</template>
<script>
import { mapGetters } from 'vuex'
import XrHeader from '@/components/XrHeader'
import Detail from './components/MenuDetail'
import { permissionListAPI } from '@/api/admin/permission'

export default {
  name: 'Menu',
  components: {
    XrHeader,
    Detail
  },
  computed: {
    ...mapGetters(['manage']),
    defaultExpandedKeys() {
      if (this.currentMenu && this.currentMenu.code) {
        return [this.currentMenu.code]
      }
      return []
    }
  },
  data() {
    return {
      loading: false,
      menuName: '', // 搜索
      filterRouteStatus: false,
      currentMenu: {},
      menuList: [],
      menuTreeProps: {
        label: 'name',
        children: 'childrens'
      }
    }
  },
  mounted() {
    this.listMenu()
  },
  methods: {
    filterRouteType(val) {
      if (val) {
        this.listMenu({
          routeType: 'Dynamic'
        })
        return
      }
      this.listMenu()
    },
    headerSearch(search) {
      this.menuName = search
    },
    listMenu(data) {
      permissionListAPI(data).then(res => {
        this.menuList = res.data
      })
    },
    changeMenuClick(data) {
      this.currentMenu = data
    },
    saveSuccess() {
      this.listMenu()
    },
    deleteSuccess() {
      this.currentMenu = {}
      this.listMenu()
    }
  }
}
</script>

<style lang="scss" scoped>
.menu-management {
  padding: 0 15px;
  height: 100%;
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
}
.system-content {
  position: relative;
  height: 100%;
  flex: 1;
  display: flex;
  overflow: hidden;
}
.system-nav {
  width: 280px;
  height: 100%;
  overflow: auto;
  margin-right: 10px;
  background: #fff;
  border: 1px solid $xr-border-line-color;
  border-radius: $xr-border-radius-base;

  &__title {
    padding: 15px;
    font-size: 16px;
    font-weight: 600;
    border-bottom: 1px solid $xr-border-line-color;
  }

  &__content {
    overflow: auto;
    height: calc(100% - 50px);
    overflow-y: overlay;
    overflow-x: overlay;
  }
}
.nav__hd {
  position: relative;
  padding: 15px;
  font-size: 16px;
  font-weight: 600;
  border-bottom: 1px solid $xr-border-line-color;

  .el-switch {
    position: absolute;
    top: 15px;
    right: 15px;
  }
}
// 菜单
.section {
  position: relative;
  &__title {
    position: relative;
    font-size: 14px;
    font-weight: 600;
    color: #333;
    padding: 15px;
    .add-btn {
      position: absolute;
      right: 10px;
      top: 10px;
    }
  }
}
.system-view-table {
  background: #fff;
  border: 1px solid $xr-border-line-color;
  border-radius: $xr-border-radius-base;
  position: absolute;
  top: 0;
  left: 295px;
  bottom: 0;
  right: 0;
}
/* 详情 */
.menu-management ::v-deep .el-dialog__wrapper {
  margin-top: 60px !important;
}

.el-tree ::v-deep .el-tree-node__content {
  height: 40px;

  .node-data {
    height: 40px;
    padding: 0 15px;
    position: relative;
    font-size: 14px;
    color: #8a94a6;
    flex-shrink: 0;
    border-radius: $xr-border-radius-base;

    &__mark {
      display: inline-block;
      width: 6px;
      height: 6px;
      border-radius: 50%;
      background-color: #e6e6e6;
      margin-right: 8px;
      flex-shrink: 0;
    }

    &__label {
      flex: 1;
      color: #333;
      font-size: 14px;
      margin: 0px 5px;
    }

    .lego-up-unfold {
      margin-left: 8px;
      transition: transform 0.3s;
    }

    .lego-up-unfold.is-close {
      transform: rotateZ(180deg);
    }
  }

  .node-data.is-current,
  .node-data:hover {
    background-color: $xr--background-color-base;
  }
}
.el-tree ::v-deep .el-tree-node__expand-icon {
  display: none;
}
.system-nav ::v-deep .el-tree-node > .el-tree-node__children {
  overflow: visible;
}
.system-nav ::v-deep .el-tree > .el-tree-node {
  min-width: 100%;
  display: inline-block !important;
}

.system-nav
  ::v-deep
  .el-tree--highlight-current
  .el-tree-node.is-current
  > .el-tree-node__content {
  background-color: white;
}

.system-nav ::v-deep .el-tree-node__content:hover {
  background-color: white;
}
/* 设置flex布局 */
.flex-index {
  display: flex;
  flex-direction: column;
}

</style>
