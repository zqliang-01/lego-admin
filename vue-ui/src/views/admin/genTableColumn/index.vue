
<template>
  <div class="container">
    <xr-header
      ref="xrHeader"
      :icon-class="'user'"
      showBack
      label="代码生成字段设计"
      icon-color="#19B5F6">
      <template slot="ft">
        <el-button slot="ft" v-debounce="handleSync" icon="el-icon-refresh" v-if="manage.genTable.sync">初始化</el-button>
        <el-button slot="ft" @click="handlePreview" icon="el-icon-view">代码预览</el-button>
        <el-button @click="handleBack" type="primary">返回<i class="el-icon-d-arrow-right"></i></el-button>
      </template>
    </xr-header>
    <div class="content" v-loading="loading">
      <!-- 左边导航 -->
      <div class="nav">
        <div class="nav__hd">
          字段列表
          <el-button
            type="text"
            icon="el-icon-circle-plus"
            class="add-btn"
            @click="handleAdd">新增字段</el-button>
        </div>
        <div class="nav-box">
          <div
            v-for="(item, index) in dataList"
            :key="index"
            :class="{'is-select' : item.code == currentData.code}"
            class="menu-item"
            @click="handleClick(item)">
            {{ item.name }}
            <span v-if="item.required" class="require"></span>
            <span class="type">
              {{ item.comment }}({{ item.formType }})
            </span>
          </div>
        </div>
      </div>
      <div class="content-right">
        <Detail
          :currentData="currentData"
          :tableCode="tableCode"
          :selected="dataList"
          @success="init"/>
      </div>
    </div>
    <code-preview
      :visible.sync="previewVisible"
      :data="previewData"
      title="代码预览"
    />
    <TableSyncDialog
      :visible.sync="showSync"
      :tableCode="tableCode"
      :selected="dataList"
      @success="init"
    />
  </div>
</template>
<script>
import CodePreview from '../genTable/CodePreview'
import Detail from './components/Detail'
import TableSyncDialog from './components/TableSyncDialog'
import XrHeader from '@/components/XrHeader'
import { mapGetters } from 'vuex'
import { genTablePreviewAPI } from '@/api/admin/genTable'
import { genTableColumnListAPI } from '@/api/admin/genTableColumn'
export default {
  components: {
    Detail,
    TableSyncDialog,
    XrHeader,
    CodePreview
  },
  computed: {
    ...mapGetters(['manage'])
  },
  data() {
    return {
      loading: false,
      showSync: false,
      tableCode: '',
      previewVisible: false,
      previewData: [],
      currentData: {},
      dataList: []
    }
  },
  mounted() {
    this.tableCode = this.$route.params && this.$route.params.tableCode
    this.init()
  },
  methods: {
    init() {
      this.loading = true
      genTableColumnListAPI({ tableCode: this.tableCode }).then(res => {
        this.dataList = res.data
        if (!this.currentData.code && this.dataList.length > 0) {
          this.currentData = this.dataList[0]
        }
        if (this.dataList.length === 0) {
          setTimeout(this.handleSync, 1000)
        }
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    handleAdd() {
      this.currentData = {}
    },
    handleClick(value) {
      this.currentData = value
    },
    handleSync() {
      let msg = '初始化操作将重置所有表字段配置，是否继续?'
      if (this.dataList.length === 0) {
        msg = '当前数据表暂无字段信息，是否进行初始化?'
      }
      this.$confirm(msg, '提示', { type: 'warning' }).then(() => {
        this.showSync = true
      })
    },
    handlePreview() {
      this.loading = true
      genTablePreviewAPI(this.tableCode).then(response => {
        this.loading = false
        this.previewData = response.data
        this.previewVisible = true
      }).catch(() => {
        this.loading = false
      })
    },
    handleBack() {
      this.$router.go(-1)
    }
  }
}
</script>
<style lang="scss" scoped>
.container {
  padding: 0 15px;
  height: 100%;
  box-sizing: border-box;
  overflow: hidden;
}
.content {
  height: calc(100% - 60px);
  overflow: hidden;
  position: relative;
}
.require::before {
  content: "*";
  color: #F56C6C;
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

.content-right {
  background: #fff;
  border: 1px solid $xr-border-line-color;
  border-radius: $xr-border-radius-base;
  margin-left: 295px;
  height: 100%;
  overflow: hidden;
  padding-top: 10px;
  position: relative;
  overflow: auto;
}
.nav-box {
  line-height: 30px;
  overflow-y: auto;
  padding: 10px 0;
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
  text-overflow: ellipsis;
  overflow: hidden;
  white-space: nowrap;
  .type {
    color: #aaa;
    font-size: 12px;
    margin-right: 5px;
    float: right;
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

.nav-box .menu-item:hover .icon-close {
  display: block;
  float: right;
}

</style>
