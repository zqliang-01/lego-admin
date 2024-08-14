<template>
  <div class="setting-default">
    <lego-code-generator
      v-if="field.unique"
      :value="field.codeGenerator"
      :field-code="field.code"
      @value-change="handleCodeGenerator($event)"/>

    <lego-relative-cell
      v-else-if="type === 'entity'"
      :value="field.relativeForm"
      :field-list="formFieldList"
      search-key="name"
      query-api="/back-end/sys-custom-form/list"
      @value-change="entityChange(field, $event)"/>
    <el-input
      v-else-if="type === 'text'"
      v-model="field.defaultValue"
      :maxlength="field.maxLength || 100"
      clearable
      @blur="inputBlur" />

    <el-input
      v-else-if="type === 'textarea'"
      v-model="field.defaultValue"
      :maxlength="field.maxLength || 800"
      clearable
      @blur="inputBlur" />

    <el-input
      v-else-if="type === 'jsonEditor'"
      v-model="field.defaultValue"
      clearable
      @blur="inputBlur" />

    <el-switch
      v-else-if="type === 'switch'"
      v-model="field.defaultValue" />

    <el-date-picker
      v-else-if="type === 'datePicker'"
      v-model="field.defaultValue"
      :type="field.formType === 'date' ? 'date' : 'datetime'"
      :value-format="field.formType === 'date' ? 'yyyy-MM-dd' : 'yyyy-MM-dd HH:mm:ss'"
      placeholder="请选择" />

    <el-date-picker
      v-else-if="type === 'date_interval'"
      v-model="field.defaultValue"
      :type="field.precisions === 1 ? 'daterange' : 'datetimerange'"
      :value-format="field.precisions === 1 ? 'yyyy-MM-dd' : 'yyyy-MM-dd HH:mm:ss'"
      start-placeholder="开始日期"
      end-placeholder="结束日期" />

    <el-select
      v-else-if="type === 'select'"
      v-model="field.defaultValue"
      :clearable="true"
      :multiple="field.formType === 'checkbox'"
      filterable
      placeholder="请选择">
      <el-option
        v-for="item in options"
        :key="item.code"
        :label="item.name"
        :value="item.code"/>
    </el-select>

    <template v-else-if="type === 'number'">
      <el-input
        v-model="field.defaultValue"
        @blur="inputBlur">
        <div
          v-if="field.formType === 'percent'"
          slot="suffix"
          class="el-input__icon">%</div>
      </el-input>
      <div class="input-tips">
        <span>*</span>
        数字的位数必须少于{{ field.formType === 'percent' ? 10 : 15 }}位
      </div>
    </template>
  </div>
</template>

<script>
import { dictSimpleListAPI } from '@/api/dictionary'
import LegoRelativeCell from '@/components/Lego/LegoRelativeCell'
import LegoCodeGenerator from '@/components/Lego/LegoCodeGenerator'

import { isEmpty } from '@/utils/types'
import { regexIsMobile, regexIsEmail } from '@/utils'

export default {
  name: 'SettingDefault',
  components: {
    LegoRelativeCell,
    LegoCodeGenerator
  },
  props: {
    field: {
      type: Object,
      required: true
    },
    appCode: {
      type: String
    }
  },
  data() {
    return {
      oldPrecisions: null,
      options: [],
      formFieldList: [
        { fieldCode: 'name', name: '模块', formType: 'text', width: '150' },
        { fieldCode: 'table', name: '数据表', formType: 'select', width: '150' },
        { fieldCode: 'createTime', name: '创建时间', formType: 'text', width: '150' }
      ]
    }
  },
  computed: {
    // 类型
    type() {
      const formType = this.field.formType
      if ([
        'date',
        'datetime'
      ].includes(formType)) return 'datePicker'
      if ([
        'number',
        'floatnumber',
        'percent'
      ].includes(formType)) return 'number'
      if ([
        'select',
        'checkbox'
      ].includes(formType)) return 'select'
      if ([
        'textarea',
        'jsonEditor'
      ].includes(formType)) return formType
      if (formType === 'boolean_value') return 'switch'
      if (formType === 'entity') {
        return 'entity'
      }
      return 'text'
    }
  },
  watch: {
    field: {
      handler() {
        if (this.type === 'select') {
          this.getOptions()
        }
      },
      deep: true,
      immediate: true
    }
  },
  created() {
    this.getOptions()
  },
  methods: {
    entityChange(field, value) {
      if (!isEmpty(value)) {
        this.$set(this.field, 'relativeForm', value)
        this.$set(this.field, 'relativeFormCode', value.code)
        return
      }
      this.$set(this.field, 'relativeForm', null)
      this.$set(this.field, 'relativeFormCode', '')
    },
    getOptions() {
      const field = this.field
      if (this.appCode && field.optionDataType === 'dict' && field.optionDictType) {
        dictSimpleListAPI(this.appCode, field.optionDictType).then(res => {
          this.options = res.data
        })
      }
    },
    inputBlur() {
      if (!this.field.defaultValue) return
      if (this.field.formType === 'mobile') {
        // 校验手机号
        if (!regexIsMobile(this.field.defaultValue)) {
          this.$message.error('输入的手机格式有误')
          this.field.defaultValue = ''
        }
      } else if (this.field.formType === 'email') {
        // 校验邮箱
        if (!regexIsEmail(this.field.defaultValue)) {
          this.$message.error('输入的邮箱格式有误')
          this.field.defaultValue = ''
        }
      } else if (this.type === 'number') {
        // 校验数字类型
        const num = Number(this.field.defaultValue) // 去0
        if (isNaN(num)) {
          this.field.defaultValue = null
          return
        }
        this.field.defaultValue = String(num)
        const arr = String(num).split('.')

        const len = String(num)
          .replace('.', '')
          .replace('-', '')
          .length
        const maxlength = this.field.formType === 'percent' ? 10 : 15
        if (len > maxlength) {
          this.$message.error(`最多支持${maxlength}位数字（包含小数位）`)
          this.field.defaultValue = null
          return
        }

        const min = isEmpty(this.field.minNumRestrict) ? -Infinity : Number(this.field.minNumRestrict || -Infinity)
        const max = isEmpty(this.field.maxNumRestrict) ? Infinity : Number(this.field.maxNumRestrict || Infinity)
        if (num < min) {
          this.$message.error('默认值不能小于最小值')
          this.field.defaultValue = null
          return
        }
        if (num > max) {
          this.$message.error('默认值不能大于最大值')
          this.field.defaultValue = null
          return
        }

        // null 不支持小数  0 不限制小数位
        if (isEmpty(this.field.precisions)) {
          this.field.defaultValue = arr[0]
          return
        }
        if (this.field.precisions === 0) return
        if (arr.length > 1 && arr[1].length > Number(this.field.precisions)) {
          this.$message.error(`默认值的小数位不能大于${this.field.precisions}`)
          this.field.defaultValue = null
        }
      }
    },
    handleCodeGenerator(value) {
      if (value && value.code) {
        this.$set(this.field, 'generatorCode', value.code)
        this.$set(this.field, 'codeGenerator', value)
      }
    }
  }
}
</script>

<style scoped lang="scss">
.el-date-editor {
  width: 100%;
}
.el-select {
  width: 100%;
}
.el-cascader {
  width: 100%;
}
.el-input__icon {
  color: #333333;
}
.input-tips {
  font-size: 12px;
  margin-top: 10px;
  color: #999;
  span {
    color: red;
  }
}
</style>
