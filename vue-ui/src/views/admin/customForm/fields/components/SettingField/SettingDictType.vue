<template>
  <div v-loading="loading" class="setting-options">
    <div class="option-item">
      <flexbox>
        <el-select
          v-model="selectedType"
          filterable
          clearable
          placeholder="请选择字典类型"
          @change="onChangeType">
          <el-option
            v-for="item in dictTypeList"
            :key="item.code"
            :label="item.name"
            :value="item.code"/>
        </el-select>
        <el-tooltip content="维护字典信息" placement="top-start">
          <el-button
            class="modify-btn"
            icon="el-icon-edit"
            @click="showEdit = true">
          </el-button>
        </el-tooltip>
      </flexbox>
    </div>
    <el-dialog
      :visible.sync="showEdit"
      title="维护字典数据"
      width="70%">
      <setting-dict-value
        :app-code="appCode"
        :selected-type="selectedType"
        :dict-type-list="dictTypeList"
        @onRefreshType="getDictType"
      />
    </el-dialog>
  </div>
</template>

<script>
import { dictTypeListAPI } from '@/api/dictionary'
import SettingDictValue from './SettingDictValue'

export default {
  name: 'SettingDictType',
  components: {
    SettingDictValue
  },
  props: {
    appCode: {
      type: String,
      required: true
    },
    field: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      loading: false,
      showEdit: false,
      selectedType: '',
      dictTypeList: []
    }
  },
  watch: {
    field: {
      handler() {
        this.handleSelected()
      },
      deep: true,
      immediate: true
    },
    appCode(value) {
      this.getDictType()
    }
  },
  created() {
    this.handleSelected()
    this.getDictType()
  },
  methods: {
    handleSelected() {
      if (this.field && this.field.optionDictType) {
        this.selectedType = this.field.optionDictType
      }
    },
    getDictType() {
      if (this.appCode) {
        this.loading = true
        dictTypeListAPI(this.appCode).then(res => {
          this.dictTypeList = res.data
          this.loading = false
        }).catch(() => {
          this.loading = false
        })
      }
    },
    onChangeType(value) {
      this.$set(this.field, 'optionDictType', value)
      this.$set(this.field, 'defaultValue', '')
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
    .lego-icon-bin {
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

.modify-btn {
  height: 34px;
  font-size: 14px;
  margin-left: 5px;
}

.add-btn {
  width: 100%;
  height: 34px;
  font-size: 14px;
  margin-top: 5px;
  color: #666666;
  border: 1px dashed $xr-border-color-base;
  border-radius: $xr-border-radius-base;
  background-color: #f8f8f8;
  cursor: pointer;
}

.el-select {
  width: 100%;
}

</style>
