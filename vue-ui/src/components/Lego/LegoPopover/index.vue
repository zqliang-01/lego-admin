<template>
  <el-popover
    v-model="showPopover"
    :disabled="disabled"
    :placement="placement"
    :width="width"
    popper-class="no-padding-popover"
    trigger="click">
    <div
      v-if="!disabled && showSelectView"
      class="content-contianer">
      <div class="title">{{ title }}</div>
      <div style="height: 100%;position: relative;">
        <slot/>
      </div>
      <div class="popover-footer">
        <el-button @click="showPopover = false">取消</el-button>
        <el-button type="primary" @click="confirmClick">确定</el-button>
      </div>
    </div>
    <flexbox
      slot="reference"
      :class="[disabled ? 'is_disabled' : 'is_valid', { 'is_focus': showPopover }]"
      wrap="wrap"
      class="popover-container xh-form-border"
      @click.native="show">
      <div
        v-for="(item, index) in showDataValue"
        :key="index"
        class="popover-item"
        @click.stop="deleteItem(index)">{{ item.name }}
        <i class="delete-icon el-icon-close"/>
      </div>
      <i v-if="dataValue.length > maxShow" class="el-icon-more" />
      <i :class="['el-icon-arrow-up', { 'is-reverse' : showPopover}]"/>
      <div v-if="dataValue.length == 0" class="add-item text-one-line">{{ placeholder }}</div>
    </flexbox>
  </el-popover>
</template>
<script type="text/javascript">

export default {
  name: 'LegoPopover',
  components: {
  },
  props: {
    title: String,
    value: {
      type: Array,
      default: () => {
        return []
      }
    },
    width: {
      type: Number,
      default: 600
    },
    placeholder: {
      type: String,
      default: '请选择'
    },
    disabled: {
      type: Boolean,
      default: false
    },
    placement: {
      type: String,
      default: 'bottom-start'
    },
    maxShow: {
      type: Number,
      default: 3
    }
  },
  data() {
    return {
      showPopover: false,
      showSelectView: false,
      dataValue: []
    }
  },
  computed: {
    showDataValue() {
      if (this.dataValue.length > this.maxShow) {
        return this.dataValue.slice(0, this.maxShow)
      }
      return this.dataValue
    }
  },
  watch: {
    value: {
      handler() {
        this.dataValue = this.value.map(item => {
          return item
        })
      },
      deep: true,
      immediate: false
    }
  },
  mounted() {
    this.dataValue = this.value.map(item => {
      return item
    })
  },
  methods: {
    show() {
      this.showSelectView = true
      this.$emit('value-change', -1)
    },
    deleteItem(index) {
      if (this.disabled) {
        return
      }
      this.$emit('value-change', index)
    },
    confirmClick() {
      this.showPopover = false
      this.$emit('confirm', this.dataValue)
    }
  }
}
</script>
<style lang="scss" scoped>
.popover-container {
  min-height: 34px;
  position: relative;
  border-radius: 3px;
  font-size: 12px;
  border: 1px solid #ddd;
  color: #333333;
  line-height: 15px;
  cursor: pointer;
  .popover-item {
    padding: 5px;
    background-color: #e2ebf9;
    border-radius: 3px;
    margin: 3px;
    cursor: pointer;
  }
  .add-item {
    padding: 5px;
    color: $xr-color-text-placeholder;
    cursor: pointer;
  }
  .delete-icon {
    color: #999;
    cursor: pointer;
  }
  .el-icon-arrow-up {
    position: absolute;
    top: 10px;
    right: 5px;
    transition: transform .3s;
    color: #c0c4cc;
    font-size: 14px;
    transition: transform .3s;
    transform: rotate(180deg);
    cursor: pointer;
  }
  .el-icon-arrow-up.is-reverse {
    transform: rotate(0deg);
  }
  .el-icon-more {
    position: absolute;
    top: 5px;
    right: 20px;
    padding: 6px 10px;
    font-size: 12px;
    background-color: #F3F7FF;
    color: #666;
    border-radius: $xr-border-radius-base;
    &:hover {
      background-color: $xr-color-primary;
      color: white;
    }
  }
}
.popover-container.is_disabled {
  background-color: #f5f7fa;
  border-color: #e4e7ed;
  cursor: not-allowed;
  .popover-item {
    background-color: #f0f4f8ea;
    color: #c0c4cc;
    cursor: not-allowed;
  }
  .delete-icon {
    color: #c0c4cc;
    cursor: not-allowed;
  }
  .add-item {
    color: #c0c4cc;
    cursor: not-allowed;
  }
}
.popover-container.is_valid:hover {
  border-color: #c0c4cc;
}
.popover-container.is_focus {
  border-color: $xr-focus-border !important;
}

.popover-footer {
  background-color: #f7f8fa;
  padding: 0 10px;
  height: 40px;
  line-height: 40px;
  border-top: 1px solid $xr-border-line-color;

  .el-button {
    float: right;
    margin-top: 6px;
    padding: 6px 12px;
  }

  .el-button + .el-button {
    margin-left: 0;
    margin-right: 10px;
  }
}
.content-contianer {
  position: relative;
}
</style>
