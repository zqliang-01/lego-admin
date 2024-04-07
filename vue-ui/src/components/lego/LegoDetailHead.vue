<template>
  <div class="container">
    <flexbox
      class="t-section"
      align="stretch">
      <i :class="auth.icon | iconPre" class="menu-icon" />
      <div class="t-section__bd">
        <div class="type-name">{{ typeName }}</div>
        <flexbox class="type-content">
          <el-tooltip
            :disabled="!name"
            :content="name"
            effect="dark"
            placement="top-start">
            <div class="name">{{ name }}</div>
          </el-tooltip>
          <el-button-group v-if="pageCodes && pageCodes.length > 1" class="lego-header-page-btn">
            <el-button icon="el-icon-arrow-left" @click="$emit('pageChange', 'left')"/>
            <el-button icon="el-icon-arrow-right" @click="$emit('pageChange', 'right')"/>
          </el-button-group>
        </flexbox>
      </div>
      <div class="t-section__ft">
        <el-button
          v-if="auth.update"
          :icon="'circle-edit' | iconPre"
          class="head-handle-button xr-btn--green"
          type="primary"
          @click.native="handleTypeClick('edit')">编辑</el-button>
        <el-button
          v-if="auth.update"
          :icon="'print' | iconPre"
          class="head-handle-button xr-btn--orange"
          type="primary"
          @click.native="handleTypeClick('print')">打印</el-button>
        <el-dropdown
          v-if="permissionMoreTypes && permissionMoreTypes.length > 0"
          trigger="click"
          @command="handleTypeClick">
          <el-button
            icon="el-icon-more"
            class="t-more" />
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item
              v-for="(item, index) in permissionMoreTypes"
              :key="index"
              :icon="item.icon | iconPre"
              :command="item.type">{{ item.name }}</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </flexbox>
    <flexbox
      class="h-section"
      align="stretch">
      <flexbox-item
        v-for="(item, index) in headFieldList"
        :key="index"
        class="h-item"
        span="200">
        <div class="h-title">{{ item.name }}</div>
        <div class="h-value text-one-line">
          <i
            v-if="item.showIcon"
            :style="item.style"
            :class="item.icon" />
          <field-view
            :props="item"
            :form-type="item.formType"
            :value="item.value" />
        </div>
      </flexbox-item>
    </flexbox>
    <slot />
  </div>
</template>
<script type="text/javascript">
import FieldView from '../NewCom/Form/FieldView'
import { getMenuAuth } from '@/utils/auth'

export default {
  name: 'LegoDetailHead',
  components: {
    FieldView
  },
  props: {
    typeName: String,
    menuCode: String,
    detail: {
      type: Object,
      default: () => {
        return {}
      }
    },
    headFieldList: {
      type: Array,
      default: () => {
        return []
      }
    },
    actionTypes: {
      type: Array,
      default: () => {
        return []
      }
    },
    pageCodes: Array
  },
  computed: {
    auth() {
      return getMenuAuth(this.menuCode)
    },
    name() {
      if (this.detail) {
        return this.detail.name
      }
      return ''
    },

    /**
     * 权限内的更多按钮
     */
    permissionMoreTypes() {
      return this.actionTypes.filter(item => {
        return this.auth[item.type]
      })
    }
  },
  methods: {
    /** 更多操作 */
    handleTypeClick(type) {
      this.$emit('handle', { type: type })
    },
    /**
     * 上下页切换
     */
    pageChange(type) {
      this.$emit('pageChange', type)
    }
  }
}
</script>
<style lang="scss" scoped>
.lego-header-page-btn {
  flex-shrink: 0;
  margin-left: 8px;
  .el-button + .el-button {
    margin-left: 0 !important;
  }
  .el-button {
    padding: 3px 6px;
    background-color: #F5F7FA;
    i {
      font-weight: bold;
    }
  }
  .el-button:hover, .el-button:focus {
    color: white;
    border-color: #ff6a00;
    background-color: #ff6a00;
  }
}
.container {
  position: relative;
  background-color: white;
  border-bottom: 1px solid $xr-border-line-color;
}
.menu-icon {
  font-size: 25px;
  width: 40px;
  height: 40px;
  background: #2362fb;
  color: #fff;
  border-radius: 4px;
  padding: 7px;
  margin-right: 10px;
}
.t-section {
  position: relative;
  padding: 30px 20px 5px;
  min-height: 60px;
  &__hd {
    display: block;
    width: 40px;
    height: 40px;
    margin-right: 15px;
  }
  &__bd {
    flex: 1;
    width: 0;
    .type-name {
      color: #999;
      font-size: 12px;
      margin-bottom: 5px;
    }

    .type-content {
      .name {
        color: #333;
        font-size: 16px;
        font-weight: 600;
        text-overflow: ellipsis;
        display: -webkit-box;
        -webkit-line-clamp: 1;
        -webkit-box-orient: vertical;
        overflow: hidden;
      }
    }
  }

  &__ft {
    flex-shrink: 0;
    margin-left: 20px;
  }

  .t-more {
    margin-left: 15px;
  }

  .t-close {
    display: block;
    width: 40px;
    height: 40px;
    margin-left: 20px;
    padding: 10px;
    cursor: pointer;
  }
}

.h-section {
  position: relative;
  padding: 8px 20px 15px;
  .h-item {
    .h-title {
      font-size: 12px;
      color: #666;
    }
    .h-value {
      min-height: 14px;
      margin-top: 8px;
      font-size: 13px;
      color: #333;
      padding-bottom: 2px;
    }
  }
}

.el-button + .el-button {
  margin-left: 15px;
}

.head-handle-button {
  ::v-deep i {
    font-size: 13px;
    margin-right: 5px;
  }
}
</style>
