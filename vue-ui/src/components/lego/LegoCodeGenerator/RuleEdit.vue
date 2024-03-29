<template>
  <div class="cr-contianer">
    <div class="title">自动编码器配置</div>
    <div style="height: 100%;position: relative;">
      <div class="rule-body">
        <div class="example">编号示例：{{ dataValue | exampleName }}</div>
        <div class="rule">
          <span>编码前缀</span>
          <el-input
            :value="dataValue.prefix"
            :maxlength="12"
            class="rule-input"
            @input="inputValue => dataValue.prefix = inputValue.replace(/\-+/g,'')" />
        </div>
        <div class="rule">
          <span>日期格式</span>
          <el-select v-model="dataValue.datePattern" class="rule-select" >
            <el-option
              v-for="item in timeOptions"
              :key="'time' + item.value"
              :label="item.label"
              :value="item.value"/>
          </el-select>
        </div>
        <div class="rule">
          <span>序号长度</span>
          <el-input-number
            v-model="dataValue.serialLength"
            :controls="false"
            class="rule-input"/>
        </div>
      </div>
    </div>
    <div class="dialog-footer">
      <el-button @click="close">取消</el-button>
      <el-button type="primary" @click="handleSumbit">确定</el-button>
    </div>
  </div>
</template>
<script type="text/javascript">
import { isEmpty } from '@/utils/types'
import { objDeepCopy } from '@/utils/index'
import moment from 'moment'

export default {
  name: 'RuleEdit',
  components: {
  },
  filters: {
    exampleName(value) {
      let name = ''
      if (value.prefix) {
        name = name + value.prefix
      }
      if (value.datePattern) {
        const formate = {
          yyyy: 'YYYY',
          yyyyMM: 'YYYYMM',
          yyyyMMdd: 'YYYYMMDD'
        }[value.datePattern]
        name = name + '-' + (formate ? moment().format(formate) : '')
      }
      if (value.serialLength) {
        let startShowValue = '1'
        for (let index = 1; index < value.serialLength; index++) {
          startShowValue = '0' + startShowValue
        }
        name = name + '-' + startShowValue
      }
      return name
    }
  },
  props: {
    value: {
      type: Object,
      default: () => {
        return {}
      }
    }
  },
  data() {
    return {
      dataValue: {
        name: '',
        prefix: '',
        datePattern: '',
        serialLength: 4
      },
      timeOptions: [
        {
          value: 'yyyy',
          label: 'yyyy（年）'
        }, {
          value: 'yyyyMM',
          label: 'yyyyMM（年月）'
        }, {
          value: 'yyyyMMdd',
          label: 'yyyyMMdd（年月日）'
        }
      ]
    }
  },
  computed: {
  },
  watch: {
    value: function(val) {
      if (!isEmpty(val) && val.code) {
        this.dataValue = objDeepCopy(val)
      }
    }
  },
  mounted() {
    if (!isEmpty(this.value) && this.value.code) {
      this.dataValue = objDeepCopy(this.value)
    }
  },
  methods: {
    handleSumbit() {
      this.dataValue.name = this.dataValue.prefix + '-' + this.dataValue.datePattern + '-' + this.dataValue.serialLength
      this.$emit('value-change', this.dataValue)
    },
    close() {
      this.$emit('close')
    }
  }
}
</script>
<style lang="scss" scoped>
.cr-contianer {
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

.rule-body {
  position: relative;
  margin-top: 10px;
  background: #FAFAFA;
  padding: 20px 20px 10px;
  border-radius: 4px;
  border: 1px solid #f6f6f6;

  .rule {
    &-type {
      width: 80px;
      margin-left: 15px;
    }

    &-select,
    &-input {
      width: 200px;
      margin-left: 15px;
    }

    .el-input-number {
      width: 140px;
      ::v-deep .el-input__inner {
        padding: 0 8px;
        text-align: left;
      }
    }

    &-code-input {
      width: 140px;
    }

    &-code-span {
      margin-left: 15px;
    }

    .el-icon-remove {
      color: #ff6767;
      cursor: pointer;
      margin-left: 2px;
      display: none;
    }
  }
  .rule + .rule {
    margin-top: 8px;
  }
  .rule:hover {
    .el-icon-remove {
      display: inline;
    }
  }
}

.example {
  color: #666;
  margin-bottom: 10px;
}
</style>
