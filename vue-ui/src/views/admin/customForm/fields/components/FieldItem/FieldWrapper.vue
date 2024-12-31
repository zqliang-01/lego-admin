<template>
  <div
    :style="{width: fieldWidth}"
    :class="{activate: activate, 'border': showBorder }"
    class="field-item"
    @click.stop="emitClick">
    <div v-if="!hiddenTitle" class="field-item_title">
      <span class="required">{{ field.required ? '*' : '' }}</span>
      <span>{{ field.name }}</span>
      <span
        v-if="field.inputTips"
        class="input-tips">
        （{{ field.inputTips }}）
      </span>
    </div>

    <div class="field-item_body">
      <slot />
    </div>

    <template v-if="activate">
      <div
        v-if="controlFlag.top"
        class="control-top control-btn"
        @click.stop="handleControl('top', $event)">
        <i :class="'icon-top' | iconPre" />
      </div>
      <div
        v-if="controlFlag.bottom"
        class="control-bottom control-btn"
        @click.stop="handleControl('bottom', $event)">
        <i :class="'icon-top bottom' | iconPre" />
      </div>
      <div
        v-if="controlFlag.left"
        class="control-left control-btn"
        @click.stop="handleControl('left', $event)">
        <i :class="'transfer' | iconPre" />
      </div>
      <div
        v-if="controlFlag.right"
        class="control-right control-btn"
        @click.stop="handleControl('right', $event)">
        <i :class="'transfer' | iconPre" />
      </div>
      <div class="edit-box">
        <div
          v-if="controlFlag.copy"
          class="control-copy control-btn"
          @click.stop="handleControl('copy', $event)">
          <el-tooltip content="复制">
            <i :class="'associated' | iconPre" />
          </el-tooltip>
        </div>
        <div
          v-if="controlFlag.delete"
          class="control-delete control-btn"
          @click.stop="handleControl('delete', $event)">
          <el-tooltip content="删除">
            <i :class="'s-delete' | iconPre" />
          </el-tooltip>
        </div>
      </div>
    </template>
  </div>
</template>

<script>
export default {
  name: 'FieldWrapper',
  props: {
    field: { // 字段信息
      type: Object,
      required: true
    },
    activate: { // 当前字段是否已激活
      type: Boolean,
      default: false
    },
    controlFlag: { // 字段控制按钮状态
      type: Object,
      default: () => {
        return {
          top: false,
          bottom: false,
          left: false,
          right: false,
          delete: false,
          copy: true
        }
      }
    },
    hiddenTitle: {
      type: Boolean,
      default: false
    },
    showBorder: {
      type: Boolean,
      default: false
    }
  },
  computed: {
    fieldWidth() {
      if (!this.field) return '100%'
      return this.field.stylePercent + '%'
    }
  },
  watch: {
    field: {
      handler() {
        if (this.field && !this.field.stylePercent) {
          this.field.stylePercent = 100
        }
      },
      deep: true,
      immediate: true
    }
  },
  methods: {
    emitClick(evt) {
      this.$emit('click', evt)
    },
    handleControl(action, evt) {
      this.$emit('action', action, evt)
    }
  }
}
</script>

<style scoped lang="scss">
.field-item {
  position: relative;
  padding: 0 10px 20px;
  background-color: white;
  cursor: move;

  &.border {
    border: 1px dashed #d9d9d9;
  }

  &.activate {
    //border-left: 2px solid $xr-color-primary;
    background-color: #f7f8fa;
  }

  .field-item_title {
    min-height: 34px;
    line-height: 1.5;
    font-size: 13px;
    width: 100%;
    padding: 10px 0 8px;
    word-wrap: break-word;
    word-break: break-all;

    .required {
      color: #F56C6C;
    }
    .input-tips {
      color: #999;
    }
  }

  .control-btn {
    position: absolute;
    z-index: 1;
    width: 25px;
    height: 25px;
    border-radius: 50%;
    box-shadow: 0 2px 4px 0 rgba(163,163,163,.5);
    background-color: white;
    cursor: pointer;
    display: flex;
    align-items: center;
    justify-content: center;

    .lego {
      color: #555;
      font-size: 14px;
    }

    .lego-icon-top {
      font-size: 12px;
      font-weight: bold;
      &.bottom {
        transform: rotate(180deg);
      }
    }

    &.control-top {
      top: -14px;
      left: 50%;
      transform: translateX(-50%);
    }
    &.control-bottom {
      bottom: -14px;
      left: 50%;
      transform: translateX(-50%);
    }
    &.control-left {
      left: -14px;
      top: 50%;
      transform: translateY(-50%);
    }
    &.control-right {
      right: -14px;
      top: 50%;
      transform: translateY(-50%);
    }
  }

  .edit-box {
    position: absolute;
    bottom: -14px;
    right: 5%;
    z-index: 1;
    .control-btn {
      position: unset;
      display: inline-flex;
      vertical-align: middle;
      margin: 0 2px;
    }
  }
}

</style>
