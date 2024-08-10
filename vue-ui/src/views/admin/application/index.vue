<template>
  <flexbox
    class="main"
    direction="column"
    align="stretch">
    <xr-header
      :icon-class="'all'"
      icon-color="#FD964A"
      label="应用管理" />
    <div
      v-loading="loading"
      class="body">
      <div
        v-for="(bigItem, bigIndex) in allList"
        :key="bigIndex"
        class="section">
        <flexbox class="section-header">
          {{ bigItem.name }}
        </flexbox>
        <flexbox
          wrap="wrap"
          class="section-body">
          <flexbox
            v-for="(item, index) in bigItem.sublist"
            :key="index"
            :class="['section-item']">
            <i :class="bigItem.status ? item.icon + ' item-icon' : item.icon + ' unactive-item-icon' | iconPre" />
            <span class="item-name">{{ item.name }}</span>
            <el-dropdown
              v-if="configSetAuth"
              class="more-menu"
              @command="handleMoreCommand($event, item)">
              <i class="el-icon-more"/>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item :command="bigItem.status ? 'disable' : 'enable'">{{ bigItem.status ? '停用' : '启用' }}</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </flexbox>
        </flexbox>
      </div>
    </div>
  </flexbox>
</template>

<script>
import {
  appListAPI,
  appModifyAPI
} from '@/api/admin/application'

import { mapGetters } from 'vuex'

import XrHeader from '@/components/XrHeader'


export default {
  /** 系统管理 的 应用管理 */
  name: 'SystemModule',
  components: {
    XrHeader
  },
  mixins: [],
  data() {
    return {
      loading: false,
      allList: [
        {
          name: '已启用应用',
          status: true,
          sublist: []
        },
        {
          name: '已停用应用',
          status: false,
          sublist: []
        }
      ]
    }
  },
  computed: {
    ...mapGetters(['manage']),
    configSetAuth() {
      return this.manage && this.manage.configSet && this.manage.configSet.update
    }
  },
  mounted() {
    this.getDetail()
  },
  methods: {
    /**
     * 详情
     */
    getDetail() {
      this.loading = true
      appListAPI()
        .then(res => {
          this.loading = false
          this.allList[0].sublist = res.data.validList
          this.allList[1].sublist = res.data.inValidList
        })
        .catch(() => {
          this.loading = false
        })
    },

    /**
     * 更多操作
     */
    handleMoreCommand(command, item) {
      this.getConfirmMessage(command, item, () => {
        this.loading = true
        appModifyAPI({
          code: item.code,
          enable: command != 'disable'
        })
          .then(res => {
            this.$message.success('设置成功')
            this.loading = false
            window.location.reload()
          })
          .catch(() => {
            this.loading = false
          })
      })
    },

    /**
     * 操作提示
     */
    getConfirmMessage(command, item, result) {
      if (command == 'enable') {
        result()
      } else {
        this.$confirm(`停用${item.name}后，用户将无法使用此功能。确定要停用吗？`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
          .then(() => {
            result()
          })
          .catch(() => {
            this.$message({
              type: 'info',
              message: '已取消操作'
            })
          })
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.main {
  padding: 0 15px;
  height: 100%;
}

.body {
  flex: 1;
  overflow-y: auto;
  padding-top: 20px;
  background-color: white;
  border: 1px solid $xr-border-line-color;
  border-radius: $xr-border-radius-base;
}

.section-header {
  padding: 3px 20px;
  font-size: 12px;
  color: #999;
}

.section-body {
  padding: 20px;
  .section-item {
    width: auto;
    min-width: 240px;
    position: relative;
    padding: 20px 35px 12px 20px;
    border: 1px solid #ebeef5;
    border-radius: $xr-border-radius-base;
    margin: 5px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    vertical-align: middle;
    .item-icon {
      width: 40px;
      height: 40px;
      font-size: 30px;
      margin-right: 10px;
      color: #2362fb;
    }
    .unactive-item-icon {
      width: 40px;
      height: 40px;
      font-size: 30px;
      margin-right: 10px;
      color: #c0c2c7;
    }
    .item-name {
      font-size: 14px;
      color: #333333;
    }
  }
}

.el-icon-more {
  color: #cdcdcd;
  transform: rotate(90deg);
  cursor: pointer;
}

.more-menu {
  position: absolute;
  top: 8px;
  right: 8px;
}
</style>
