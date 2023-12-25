<template>
  <transition
    name="el-drawer-fade">
    <div
      v-loading="loading"
      :style="{ 'z-index': zIndex, 'left': containerLeft }"
      class="nav-manager">

      <div class="nav-manager__content">

        <div class="nav-section">
          <div class="nav-section__title" title="123123">全部应用</div>
          <flexbox
            class="nav-section__content"
            wrap="wrap">
            <div
              v-for="(item, index) in headerModule"
              :key="index"
              class="nav-section-item"
              @click="selectClick(item)">
              <div class="nav-section-item__icon">
                <i :class="item.icon | iconPre" />
              </div>
              <div class="nav-section-item__label">{{ item.name }}</div>
              <i v-if="isEdit && topList.length < 8 && !item.future && !isHasSelect(item.code)" class="el-icon-circle-plus is-handle" @click.stop="moduleTopClick(item, index)" />
              <span v-if="item.future" class="span-future">{{ item.futureLabel }}</span>
            </div>
          </flexbox>
        </div>

        <div class="nav-section">
          <div class="nav-section__title">置顶应用<span class="nav-section__tips">（最多可设置8个置顶应用，可通过拖拽进行排列）</span>
            <el-button v-if="isEdit" type="primary" @click="saveClick">保存</el-button>
            <el-button v-else @click="isEdit = !isEdit">编辑</el-button>
          </div>
          <draggable
            v-model="topList"
            v-bind="{ dragClass: 'sortable-drag', forceFallback: false, disabled:!isEdit }"
            style="flex-wrap: wrap;"
            class="vux-flexbox nav-section__content">
            <div
              v-for="(item, index) in topList"
              :key="index"
              class="nav-section-item"
              @click="selectClick(item)">
              <div class="nav-section-item__icon">
                <i :class="item.icon | iconPre" />
              </div>
              <div class="nav-section-item__label">{{ item.name }}</div>
              <i v-if="isEdit" class="el-icon-remove is-handle" @click.stop="topModuleDelete(item, index)" />
            </div>
          </draggable>
        </div>
      </div>
    </div>
  </transition>
</template>

<script>
import { headerModelModifyAPI } from '@/api/config'

import { mapGetters } from 'vuex'
import { getMaxIndex } from '@/utils/index'
import Draggable from 'vuedraggable'
import { objDeepCopy } from '@/utils'

export default {
  // 模块管理
  name: 'NavManager',
  components: {
    Draggable
  },
  props: {
    collapse: Boolean,
    topModule: Array
  },
  data() {
    return {
      zIndex: getMaxIndex(),
      loading: false,
      isEdit: false,
      allList: [],
      topList: []
    }
  },
  computed: {
    ...mapGetters([
      'allAuth',
      'headerModule'
    ]),
    containerLeft() {
      if (this.collapse) {
        return '79px'
      }
      return '215px'
    },
    topModules() {
      return this.topList.map(item => {
        if (item.code != 'home') {
          return item.code
        }
      })
    }
  },
  watch: {
    topModule: {
      handler() {
        this.topList = objDeepCopy(this.topModule).filter(item => item.code != 'home')
      },
      deep: true,
      immediate: true
    }
  },
  methods: {
    /**
     * 保存
     */
    saveClick() {
      this.loading = true
      const codes = this.topList.map(item => item.code)
      headerModelModifyAPI(codes).then(res => {
        this.isEdit = false
        this.$message.success('操作成功')
        this.loading = false
        this.$emit('change')
      }).catch(() => {
        this.loading = false
      })
    },

    /**
     * 判断是否有选择
     */
    isHasSelect(module) {
      return this.topModules.includes(module)
    },

    /**
     * 删除
     */
    topModuleDelete(item, index) {
      if (this.topList.length > 1) {
        this.topList.splice(index, 1)
      }
    },

    /**
     * 模块添加
     */
    moduleTopClick(item, index) {
      if (this.topList.length < 8) {
        this.topList.push(item)
      }
    },

    /**
     * 选择模块
     */
    selectClick(item) {
      if (!this.isEdit) {
        this.$emit('select', item)
      }
    }
  }
}
</script>

<style lang="scss" scoped>

.nav-manager {
  position: absolute;
  top: 61px;
  right: 15px;
  background-color: white;
  box-shadow: 0px 8px 11px 1px rgba(0, 0, 0, 0.1);
  border-bottom-left-radius: 4px;
  border-bottom-right-radius: 4px;

  padding: 20px 15px 40px;

  &__content {
    position: relative;
  }

  &__handle {
    position: absolute;
    right: 20px;
    top: -5px;
  }

  .nav-section {
    &__title {
      font-weight: bold;
    }

    &__tips {
      color: #ccc;
      font-weight: normal;
    }

    &__content {
      margin-top: 20px;
    }
  }

  .nav-section + .nav-section {
    margin-top: 20px;
    border-top: 1px solid $xr-border-line-color;
    .nav-section__title {
      margin-top: 20px;
    }
  }

  .nav-section-item {
    text-align: center;
    width: 70px;
    height: 70px;
    margin: 10px;
    cursor: pointer;
    position: relative;

    &__icon {
      width: 40px;
      height: 40px;
      display: inline-block;
      line-height: 40px;
      border-radius: 4px;
      background-color: #F0F0F0;
      border-radius: 4px;

      i {
        font-size: 18px;
        color: $xr-color-primary;
      }
    }

    &__label {
      font-size: 13px;
      margin-top: 10px;
    }

    .el-icon-circle-plus,
    .el-icon-remove {
      color: #F94E4E;
      cursor: pointer;
    }
    .is-handle {
      position: absolute;
      right: 10px;
      top: -6px;
    }

    .span-future {
      position: absolute;
      top: -6px;
      left: 35px;
      font-size: 12px;
      background: #D9D9D9;
      display: inline-block;
      color: white;
      padding: 2px 3px;
      border-radius: 2px;
      white-space: nowrap;
      transform: scale(0.9);
    }

    &.is-future {
      cursor: initial;

      .nav-section-item__icon {
        i {
          color: #999;
        }
      }

      .nav-section-item__label {
        color: #666;
      }
    }

  }
}


</style>
