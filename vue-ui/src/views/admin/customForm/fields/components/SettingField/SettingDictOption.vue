<template>
  <div class="setting-options">
    <div class="option-item">
      <el-select
        v-model="selectedType"
        filterable
        clearable
        placeholder="请选择字典类型">
        <el-option
          v-for="item in crmDictTypeArr"
          :key="item.code"
          :label="item.name"
          :value="item.code"/>
      </el-select>
    </div>

  </div>
</template>

<script>
export default {
  name: 'SettingDictOption',
  components: { },
  props: {
    field: {
      type: Object,
      required: true
    },
    crmDictTypeArr: {
      type: Array,
      required: true
    }
  },
  data() {
    return {
      selectedType: ''
    }
  },
  watch: {
    selectedType: {
      handler(value) {
        this.$set(this.field, 'optionDictType', value)
      }
    }
  },
  created() {
    if (this.field && this.field.optionDictType) {
      this.selectedType = this.field.optionDictType
    }
  }
}
</script>

<style scoped lang="scss">
.option-item {
  margin: 5px 0;

  .el-input__icon {
    font-size: 14px;
    color: #999999;
  }
  .drag-hook {
    cursor: move;
  }

  &.other-item {
    .wk-icon-bin {
      cursor: pointer;
    }
  }
}

.name {
  font-size: 13px;
  font-weight: 500;
  color: #333;
  margin: 10px 0;
}

.add-btn {
  width: 100%;
  height: 34px;
  font-size: 14px;
  color: #666666;
  border: 1px dashed $xr-border-color-base;
  border-radius: $xr-border-radius-base;
  background-color: #f8f8f8;
  cursor: pointer;
}

.add-other-btn {
  font-size: 14px;
  color: #666666;
  cursor: pointer;
  display: inline-block;
  margin-top: 8px;
}

.edit-dialog {
  .edit-tips {
    font-size: 14px;
    //color: #999999;
    color: #ecb971;
    background-color: #fafbfb;
    border-top: 1px solid #E6E6E6;
    border-bottom: 1px solid #E6E6E6;
    padding: 5px 20px;
  }
  .el-textarea {
    margin: 10px 0;
  }
  ::v-deep .el-textarea__inner {
    border: 0 none;
    padding: 10px 20px;
  }
  ::v-deep .el-dialog__body {
    padding: 0;
  }
}

.el-select {
  width: 100%;
}

</style>
