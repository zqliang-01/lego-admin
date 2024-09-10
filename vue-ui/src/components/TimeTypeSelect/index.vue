<template>
  <el-popover
    v-model="showTypePopover"
    :width="width"
    placement="bottom"
    class="time-type-select"
    popper-class="no-padding-popover"
    trigger="click">
    <div class="type-popper">
      <div class="type-content">
        <div
          v-for="(item, index) in options"
          :key="index"
          :class="{ 'selected' : selectType.value == item.value && !showCustomContent}"
          class="type-content-item"
          @click="typeSelectClick(item)">
          <div class="mark"/>{{ item.label }}
        </div>
        <div
          :class="{ 'selected' : showCustomContent}"
          class="type-content-item"
          @click="showCustomContent = true">
          <div class="mark"/>自定义
        </div>
      </div>
      <div
        v-if="showCustomContent"
        class="type-content-custom">
        <el-date-picker
          v-model="startTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="选择日期"/>
        <el-date-picker
          v-model="endTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="选择日期"/>
        <el-button @click="customSureClick">确定</el-button>
      </div>
    </div>
    <el-input
      slot="reference"
      v-model="typeShowValue"
      :readonly="true"
      :style="{width: width + 'px'}"
      class="type-select">
      <i
        slot="suffix"
        :class="['el-icon-arrow-up', { 'is-reverse' : showTypePopover}]"/>
    </el-input>
  </el-popover>
</template>

<script type="text/javascript">
import { isObject } from '@/utils/types'

const formatterData = require('./date.js')

export default {
  name: 'TimeTypeSelect', // 时间类型选择
  props: {
    defaultType: [String, Object],
    // 容器宽度，默认200px
    width: {
      type: [String, Number],
      default: 200
    },
    options: {
      type: Array,
      default: () => {
        return [
          { label: '今天', value: 'today', date: formatterData.getTodayDate() },
          { label: '昨天', value: 'yesterday', date: formatterData.getYesterday() },
          { label: '本周', value: 'week', date: formatterData.getWeekDate() },
          { label: '上周', value: 'lastWeek', date: formatterData.getLastWeekDate() },
          { label: '本月', value: 'month', date: formatterData.getMonth() },
          { label: '上月', value: 'lastMonth', date: formatterData.getLastMonth() },
          { label: '本年', value: 'year', date: formatterData.getYear() },
          { label: '去年', value: 'lastYear', date: formatterData.getLastYear() }
        ]
      }
    } // 数据源 如果存在 替换 默认
  },
  data() {
    return {
      selectType: { label: '本年', value: 'year' },
      showTypePopover: false,
      showCustomContent: false, // 展示自定义时间内容
      sureCustomContent: false, // 确定

      startTime: '',
      endTime: ''
    }
  },
  computed: {
    typeShowValue() {
      if (this.sureCustomContent) {
        if (this.startTime || this.endTime) {
          return (this.startTime || '') + '-' + (this.endTime || '')
        }
        return ''
      } else {
        return this.selectType.label
      }
    }
  }, // 时间类型选择
  watch: {
    defaultType() {
      if (this.selectType.value != this.defaultType) {
        this.selectType = this.getDefaultTypeValue(this.defaultType)
      }
    }
  },
  mounted() {
    if (this.defaultType) {
      if (typeof this.defaultType === 'string') {
        this.typeSelectClick(this.getDefaultTypeValue(this.defaultType))
      } else if (isObject(this.defaultType)) {
        if (this.defaultType.label) {
          this.typeSelectClick(this.defaultType)
        } else if (this.defaultType.type == 'default') {
          this.typeSelectClick(this.getDefaultTypeValue(this.defaultType.value))
        } else if (this.defaultType.type == 'custom') {
          this.sureCustomContent = true
          this.showCustomContent = true
          this.startTime = this.defaultType.startTime
          this.endTime = this.defaultType.endTime
        }
      }
    }
  },
  methods: {
    getDefaultTypeValue(type) {
      for (let index = 0; index < this.options.length; index++) {
        const element = this.options[index]
        if (element.value == type) {
          return element
        }
      }
      return { label: '本年', value: 'year', data: ['', ''] }
    },

    // 类型选择
    typeSelectClick(item) {
      this.showTypePopover = false
      this.sureCustomContent = false
      this.showCustomContent = false
      this.selectType = item
      this.$emit('change', {
        type: 'default',
        value: this.selectType.value,
        label: this.selectType.label,
        startTime: this.selectType.date[0],
        endTime: this.selectType.date[1]
      })
    },
    // 选择自定义时间 确定
    customSureClick() {
      if (this.startTime && this.endTime) {
        this.sureCustomContent = true
        this.showTypePopover = false

        this.$emit('change', {
          type: 'custom',
          startTime: this.startTime,
          endTime: this.endTime
        })
      }
    }
  }
}
</script>
<style lang="scss" scoped>
::v-deep .type-select {
  margin-right: 15px;
  .el-input__inner {
    height: inherit;
    cursor: pointer;
  }
}

// 时间选择
.type-popper {
  .type-content {
    max-height: 250px;
    overflow-y: auto;
    .type-content-item {
      height: 34px;
      line-height: 34px;
      padding: 0 20px;
      color: #606266;
      position: relative;
      cursor: pointer;
      .mark {
        display: inline-block;
        width: 6px;
        height: 6px;
        border-radius: 3px;
        margin-right: 8px;
        background-color: transparent;
      }
    }

    .selected {
      color: $xr-color-primary;
      font-weight: bold;
      .mark {
        background-color: $xr-color-primary;
      }
    }
    .type-content-item:hover {
      background-color: #f5f7fa;
    }
  }

  .type-content-custom {
    padding: 5px 20px 10px;
    position: relative;
    overflow: hidden;
    .el-date-editor {
      width: 100%;
      margin-bottom: 8px;
    }

    button {
      float: right;
    }
  }
}

.el-icon-arrow-up {
  position: absolute;
  top: 10px;
  right: 5px;
  color: #c0c4cc;
  font-size: 14px;
  transition: transform .3s;
  transform: rotate(180deg);
  cursor: pointer;
}
.el-icon-arrow-up.is-reverse {
  transform: rotate(0deg);
}
</style>
