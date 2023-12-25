
<template>
  <div class="role-authorization">
    <xr-header
      ref="xrHeader"
      :icon-class="'user'"
      show-search
      label="角色权限管理"
      icon-color="#19B5F6"
      @search="headerSearch" />
    <div class="role-box">
      <!-- 左边导航 -->
      <div v-loading="loading" class="nav">
        <div class="nav__hd">
          角色列表
          <el-button
            v-if="manage.role.add"
            type="text"
            icon="el-icon-circle-plus"
            class="add-btn"
            @click="addRole">创建角色</el-button>
        </div>
        <!-- 角色列表 -->
        <div class="role-nav-box">
          <div
            v-for="(item, index) in roleList"
            :key="index"
            :class="{'is-select' : item.code == currentRole.code}"
            class="menu-item"
            @click="roleMenuClick(item)">
            {{ item.name }}
            <div class="icon-close">
              <i
                v-if="manage.role.update"
                class="el-icon-edit"
                @click="roleHandleClick('edit', item)"/>
              <i
                v-if="manage.role.delete"
                class="el-icon-delete"
                @click="roleHandleClick('delete', item)"/>
            </div>
          </div>
        </div>
      </div>
      <div class="content-box">
        <tab-content
          :role-code="currentRole.code"
        />
      </div>
    </div>
    <role-dialog
      :visible.sync="roleCreateDialog"
      :role="currentRole"
      :creatable="creatable"
      @success="getRoleList"
    />
  </div>
</template>
<script>
import { mapGetters } from 'vuex'
import { roleSimpleListAPI } from '@/api/admin/role'
import { roleDeleteAPI } from '@/api/admin/role'
import XrHeader from '@/components/XrHeader'
import RoleDialog from './components/RoleDialog'
import TabContent from './components/TabContent'
export default {
  components: {
    XrHeader,
    RoleDialog,
    TabContent
  },
  computed: {
    ...mapGetters(['manage'])
  },
  data() {
    return {
      loading: false,
      creatable: true,
      roleCreateDialog: false,
      currentRole: {},
      roleList: []
    }
  },
  mounted() {
    this.getRoleList()
  },
  methods: {
    getRoleList() {
      roleSimpleListAPI().then(res => {
        this.roleList = res.data
        if (this.roleList && this.roleList.length > 0) {
          this.currentRole = this.roleList[0]
        }
      })
    },
    headerSearch() {
    },
    addRole() {
      this.creatable = true
      this.roleCreateDialog = true
    },
    roleMenuClick(value) {
      this.currentRole = value
    },
    roleHandleClick(value, item) {
      this.currentRole = item
      if (value === 'edit') {
        this.creatable = false
        this.roleCreateDialog = true
      }
      if (value === 'delete') {
        this.$confirm('此操作将永久删除[' + this.currentRole.name + ']是否继续?', '提示', {
          type: 'warning'
        }).then(() => {
          this.loading = true
          roleDeleteAPI(this.currentRole.code).then(res => {
            this.getRoleList()
            this.loading = false
            this.$message.success('删除成功')
          }).catch(() => {
            this.loading = false
          })
        })
      }
    }
  }
}
</script>
<style lang="scss" scoped>
.role-authorization {
  padding: 0 15px;
  height: 100%;
  box-sizing: border-box;
  overflow: hidden;
}
.role-box {
  height: calc(100% - 60px);
  overflow: hidden;
  position: relative;
}
.nav {
  width: 280px;
  background: #fff;
  border: 1px solid $xr-border-line-color;
  border-radius: $xr-border-radius-base;
  height: 100%;
  position: absolute;
  top: 0;
  left: 0;
}
.nav__hd {
  position: relative;
  padding: 15px;
  font-size: 16px;
  font-weight: 600;
  border-bottom: 1px solid $xr-border-line-color;

  .el-button {
    position: absolute;
    top: 10px;
    right: 15px;
  }
}

.content-box {
  background: #fff;
  border: 1px solid $xr-border-line-color;
  border-radius: $xr-border-radius-base;
  margin-left: 295px;
  height: 100%;
  overflow: hidden;
  padding-top: 10px;
  position: relative;
}
.role-authorization /deep/ .el-tree-node__expand-icon {
  display: none;
}
.role-nav-box {
  line-height: 30px;
  overflow-y: auto;
  padding: 20px 0;
  height: calc(100% - 50px);
}
// 菜单
.menu-item {
  color: #333;
  font-size: 13px;
  padding: 0 15px;
  height: 40px;
  line-height: 40px;
  cursor: pointer;
  position: relative;
  .icon-close {
    position: absolute;
    top: 0;
    right: 8px;
    z-index: 1;
    display: none;
    i {
      color: #aaa;
      font-size: 12px;
      margin-right: 5px;
    }
    i:hover {
      color: $xr-color-primary;
    }
  }
}

.menu-item:hover,
.menu-item.is-select {
  background-color: $xr--background-color-base;
}

.menu-item:hover::before,
.menu-item.is-select::before {
  content: ' ';
  position: absolute;
  top: 0;
  left: 0;
  bottom: 0;
  width: 2px;
  background-color: #5383ed;
}

.role-nav-box .menu-item:hover .icon-close {
  display: block;
  float: right;
}

.content-box {
  background: #fff;
  border: 1px solid $xr-border-line-color;
  border-radius: $xr-border-radius-base;
  margin-left: 295px;
  height: 100%;
  overflow: hidden;
  padding-top: 10px;
  position: relative;
}
@import '../styles/table.scss';
</style>
