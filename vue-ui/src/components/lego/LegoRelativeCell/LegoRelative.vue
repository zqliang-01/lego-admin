<template>
  <div class="cr-contianer">
    <div class="title">{{ getTitle() }}</div>
    <div style="height: 100%;position: relative;">
      <lego-relative-table
        ref="legoRelativeTable"
        :radio="radio"
        :field-list="fieldList"
        :query-api-url="queryApiUrl"
        :selected-data="currentSelectedData"
        @changeCheckout="checkSelectInfos" />
    </div>
    <div
      class="dialog-footer">
      <el-button @click="closeView">取消</el-button>
      <el-button
        type="primary"
        @click="confirmClick">确定</el-button>
    </div>
  </div>
</template>

<script type="text/javascript">
import LegoRelativeTable from './LegoRelativeTable'
import { objDeepCopy } from '@/utils'

export default {
  name: 'LegoRelatieve', // 相关
  components: {
    LegoRelativeTable
  },
  props: {
    fieldList: {
      type: Array,
      default: () => {
        return []
      }
    },
    /** 多选框 只能选一个 */
    radio: {
      type: Boolean,
      default: true
    },
    /** 已选信息 */
    selectedData: {
      type: Array,
      default: () => {
        return []
      }
    },
    queryApiUrl: String
  },
  data() {
    return {
      currentSelectedData: []
    }
  },
  computed: {},
  watch: {
    selectedData: function(data) {
      this.handleCurrentSelectData(data)
    }
  },
  mounted() {
    this.currentSelectedData = objDeepCopy(this.selectedData)
  },
  methods: {
    /**
     * 目前选择值
     */
    handleCurrentSelectData(data) {
      if (data && data.length) {
        this.currentSelectedData = objDeepCopy(data)
        return
      }
      this.currentSelectedData = []
    },
    clearAll() {
      this.$refs.legoRelativeTable.clearAll()
    },
    /**
     * 当用户手动勾选全选 Checkbox 时触发的事件
     */
    selectAll() {},
    /**
     * 关闭操作
     */
    closeView() {
      this.$emit('close')
    },
    checkSelectInfos(data) {
      this.currentSelectedData = data
    },

    /**
     * 确定选择
     */
    confirmClick() {
      this.$emit('changeCheckout', this.currentSelectedData)
      this.$emit('close')
    },

    /**
     * 根据类型获取标题展示名称
     */
    getTitle() {
      return '关联相关信息'
    }
  }
}
</script>
<style lang="scss" scoped>
.cr-contianer {
  // height: 450px;
  position: relative;
  padding: 50px 0 10px 0;
}

.title {
  padding: 0 20px;
  font-size: 16px;
  font-weight: 600;
  line-height: 50px;
  height: 50px;
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  z-index: 3;
  border-bottom: 1px solid $xr-border-line-color;
}

.dialog-footer {
  text-align: right;
  margin-top: 10px;
  margin-right: 10px;
}

.cr-body-side {
  flex-shrink: 0;
  z-index: 3;
  position: absolute;
  left: 0;
  top: 0;
  bottom: 0;
  width: 150px;
  font-size: 12px;
  background-color: white;
  height: 100%;
  border-right: 1px solid $xr-border-line-color;
  .side-item {
    position: relative;
    height: 40px;
    line-height: 40px;
    padding: 0 20px;
    font-size: 14px;
    color: #333;
    cursor: pointer;

    &__num {
      color: #666;
      font-size: 12px;
      position: absolute;
      top: 0;
      right: 5px;
    }
  }

  .side-item::before {
    content: ' ';
    position: absolute;
    top: 0;
    left: 0;
    bottom: 0;
    width: 2px;
  }
}

.side-item-default {
  color: #333;
}

.side-item-select {
  background-color: $xr--background-color-base;
}

.side-item-select::before {
  background-color: $xr-color-primary;
}
</style>
