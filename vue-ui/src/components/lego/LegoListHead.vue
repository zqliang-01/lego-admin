<template>
  <div class="c-container">
    <flexbox v-if="!$slots.icon" class="title">
      <i :class="auth.icon | iconPre" class="menu-icon" />{{ title }}
    </flexbox>
    <slot v-else name="icon" />
    <el-input
      v-if="showSearch"
      :placeholder="placeholder"
      v-model="inputContent"
      class="sc-container"
      @input="inputChange"
      @keyup.enter.native="searchInput">
      <el-button
        slot="append"
        type="primary"
        @click.native="searchInput">搜索</el-button>
    </el-input>
    <div class="right-container">
      <el-button
        v-if="auth.add"
        class="xr-btn--orange"
        icon="el-icon-plus"
        type="primary"
        @click="createClick">{{ mainTitle }}</el-button>
      <el-button
        v-if="auth.export"
        :icon="'export' | iconPre"
        class="dup-check-btn"
        @click="handleTypeDrop('export')">导出</el-button>
      <el-dropdown
        v-if="moreTypes.length > 0"
        trigger="click"
        @command="handleTypeDrop">
        <el-button icon="el-icon-more"/>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item
            v-for="(item, index) in moreTypes"
            :key="index"
            :icon="item.icon | iconPre"
            :command="item.type">{{ item.name }}</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
  </div>
</template>

<script type="text/javascript">
import Message from 'element-ui'
import { getMenuAuth } from '@/utils/auth'

export default {
  name: 'LegoListHead',
  props: {
    title: {
      type: String,
      default: ''
    },
    placeholder: {
      type: String,
      default: '请输入内容'
    },
    mainTitle: {
      type: String,
      default: ''
    },
    menuCode: {
      type: String,
      default: ''
    },
    showSearch: {
      type: Boolean,
      default: true
    },
    search: String, // 用于联动
    // 自定义方法
    createFun: Function
  },
  data() {
    return {
      inputContent: '',
      /** 更多操作 */
      moreTypes: [],
      isCreate: false // 是创建
    }
  },
  computed: {
    auth() {
      return getMenuAuth(this.menuCode)
    }
  },
  mounted() {
    if (this.auth && this.auth.export) {
      // this.moreTypes.push({ type: 'export', name: '导出', icon: 'export' })
    }
  },
  methods: {
    handleTypeDrop(command) {
      if (command == 'export') {
        this.$confirm('此操作将导出全部数据，数据量大时将会比较慢，是否继续?', '提示', {
          type: 'warning'
        }).then(() => {
          this.$emit('on-export')
        })
      }
    },
    createClick() {
      if (this.createFun) {
        this.createFun()
      } else {
        Message({
          message: this.menuCode + '未实现创建流程！',
          duration: 1500,
          type: 'error'
        })
      }
    },
    inputChange() {
      this.$emit('update:search', this.inputContent)
    },
    // 进行搜索操作
    searchInput() {
      this.$emit('on-search', this.inputContent)
    },
    // 创建数据页面 保存成功
    createSaveSuccess(data) {
      // 回到保存成功
      this.$emit('on-handle', { type: 'save-success' })
    },
    hideView() {
      this.isCreate = false
    }
  }
}
</script>
<style lang="scss" scoped>
.c-container {
  height: 60px;
  position: relative;
  z-index: 100;
  .title-icon {
    width: 30px;
    height: 30px;
    margin-right: 10px;
  }
  .menu-icon {
    font-size: 20px;
    font-weight: 100;
    background: #2362fb;
    color: #fff;
    border-radius: 3px;
    width: 30px;
    text-align: center;
    height: 30px;
    padding: 4px;
    margin-right: 10px;
  }
  .title {
    float: left;
    width: auto;
    padding-left: 28px;
    font-size: 16px;
    font-weight: 600;
    height: 100%;
  }
  .sc-container {
    width: 300px;
    margin: -16px 0 0 -150px;
    position: absolute;
    left: 50%;
    top: 50%;
  }

  .el-input {
    ::v-deep .el-input-group__append {
      background-color: $xr-color-primary;
      border-color: $xr-color-primary;
      color: white;
    }
  }

  .right-container {
    margin-right: -10px;
    float: right;
    margin: 15px 20px 0 0;
    position: relative;

    .dup-check-btn,
    .xr-btn--orange {
      margin-left: 0;
      margin-right: 10px;
    }

    .right-item {
      float: left;
      margin-right: 10px;
      padding: 8px 10px;
      font-size: 13px;
      border-radius: 2px;
    }
  }
}
</style>
