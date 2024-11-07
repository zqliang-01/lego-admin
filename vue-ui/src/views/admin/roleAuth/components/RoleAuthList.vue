<template>
  <div
    v-loading="loading"
    class="jurisdiction-box">
    <el-button
      v-if="manage.role.auth"
      size="medium"
      type="primary"
      class="jurisdiction-edit"
      @click="ruleSubmit"> 保存 </el-button>
    <el-tabs
      v-model="currentAppCode"
      class="app-tabs">
      <el-tab-pane
        v-for="(item, index) in menuList"
        :key="index"
        :label="item.name"
        :name="item.code">
        <div
          :style="{ height: treeHeight + 'px'}"
          class="jurisdiction-content">
          <div class="jurisdiction-content-checkbox">
            <el-tree
              :data="item.childrens"
              :indent="0"
              :ref="'tree' + item.code"
              :expand-on-click-node="false"
              :props="defaultProps"
              show-checkbox
              node-key="code"
              style="height: 0;"
              default-expand-all>
              <span
                slot-scope="{ node, data }"
                :class="data.menu ? 'node-label node-menu': 'node-auth'">
                {{ node.label }}
              </span>
            </el-tree>
          </div>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>
<script>
import {
  permissionListAPI,
  permissionAuthListAPI
} from '@/api/admin/permission'
import { roleAuthAPI } from '@/api/admin/role'
import { mapGetters } from 'vuex'
export default {
  props: {
    roleCode: String
  },
  computed: {
    ...mapGetters(['manage'])
  },
  data() {
    return {
      loading: false,
      menuList: [],
      authCodes: [],
      changeValue: null,
      currentAppCode: null,
      defaultProps: {
        children: 'childrens',
        label: 'name'
      },
      treeHeight: document.documentElement.clientHeight - 230
    }
  },
  watch: {
    roleCode() {
      this.refreshAuthList()
    }
  },
  mounted() {
    window.onresize = () => {
      this.treeHeight = document.documentElement.clientHeight - 230
    }
    this.getAllAuthList()
  },
  methods: {
    getAllAuthList() {
      permissionListAPI().then(res => {
        this.menuList = res.data
        if (this.currentAppCode == 0 && this.menuList.length > 0) {
          this.currentAppCode = this.menuList[0].code
        }
        this.updateLeafStyle()
        this.refreshAuthList()
      })
    },
    generateArr(menus, checkKeys) {
      let data = []
      menus.forEach(menu => {
        if (menu.childrens.length > 0) {
          const tmp = this.generateArr(menu.childrens, checkKeys)
          if (tmp) {
            data = [...data, ...tmp]
          }
        } else if (checkKeys.indexOf(menu.code) >= 0) {
          data.push(menu.code)
        }
      })
      return data
    },
    refreshAuthList() {
      permissionAuthListAPI({ roleCode: this.roleCode }).then(res => {
        const codes = this.generateArr(this.menuList, res.data)
        this.menuList.forEach(menu => {
          const treeRefs = this.$refs['tree' + menu.code]
          if (treeRefs) {
            if (Object.prototype.toString.call(treeRefs) == '[object Array]') {
              treeRefs[0].setCheckedKeys(codes, true)
            } else {
              treeRefs.setCheckedKeys(codes, true)
            }
          }
        })
      })
    },
    ruleSubmit() {
      this.loading = true
      let permissionCodes = []
      this.menuList.forEach(menu => {
        const treeRefs = this.$refs['tree' + menu.code]
        if (treeRefs) {
          let codes = []
          if (Object.prototype.toString.call(treeRefs) == '[object Array]') {
            codes = treeRefs[0].getCheckedKeys().concat(treeRefs[0].getHalfCheckedKeys())
          } else {
            codes = treeRefs.getCheckedKeys().concat(treeRefs.getHalfCheckedKeys())
          }
          permissionCodes = permissionCodes.concat(codes)
          if (codes && codes.length > 0) {
            permissionCodes.push(menu.code)
          }
        }
      })
      roleAuthAPI({ roleCode: this.roleCode, permissionCodes: permissionCodes }).then(res => {
        this.refreshAuthList()
        this.loading = false
        this.$message.success('角色授权成功！')
      }).catch(() => {
        this.loading = false
      })
    },
    updateLeafStyle() {
      this.$nextTick(() => {
        var levelName = document.getElementsByClassName('node-auth')
        for (var i = 0; i < levelName.length; i++) {
          levelName[i].parentNode.parentNode.style.display = 'inline-block'
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.jurisdiction-box {
  padding-bottom: 25px;
  overflow: hidden;
  position: relative;
}
.jurisdiction-content {
  position: relative;
  overflow: hidden;
}
.jurisdiction-content-checkbox {
  border-right: 1px dashed $xr-border-line-color;
  height: 100%;
  overflow-y: auto;
  padding: 20px;

  ::v-deep .el-tree-node__content:hover {
    background-color: white;
    color: $xr-color-primary;
  }
}
.jurisdiction-content-checkbox
  .el-tree
  ::v-deep
  .el-tree-node
  > .el-tree-node__content {
  margin-bottom: 20px;
  width: 150px;
}
.jurisdiction-content-checkbox ::v-deep .el-tree .el-tree-node {
  white-space: inherit;
  margin-bottom: 5px;
}
.jurisdiction-edit {
  text-align: right;
  padding: 10px 30px;
  position: absolute;
  top: 0;
  right: 20px;
  z-index: 3;
}
.node-label {
  font-weight: bold;
  font-size: 15px;
  position: relative;
  .el-button {
    position: absolute;
    top: -8px;
    right: -80px;
    ::v-deep span {
      margin-left: 3px;
    }
  }
}
.node-auth {
  display: inline-block;
}
.app-tabs {
  ::v-deep .el-tabs__header {
    padding-right: 110px !important;
  }
}
</style>
