<template>
  <field-wrapper
    :activate="activate"
    :field="field"
    :control-flag="controlFlag"
    :show-border="showBorder"
    class="field-input"
    @click="emitClick"
    @action="handleAction">
    <flexbox
      align="center"
      class="box">
      <i v-if="fieldIcon" :class="fieldIcon | iconPre" class="item-icon" />
      <span class="default-val">
        {{ displayValue }}
      </span>
    </flexbox>
  </field-wrapper>
</template>

<script>
import FieldWrapper from './FieldWrapper'
import mixins from './mixins'
import { isEmpty } from '@/utils/types'

export default {
  name: 'FieldInput',
  components: {
    FieldWrapper
  },
  mixins: [mixins],
  data() {
    return {
      formFieldList: [
        { fieldCode: 'name', name: '模块', formType: 'text', width: '150' },
        { fieldCode: 'table', name: '数据表', formType: 'select', width: '150' },
        { fieldCode: 'createTime', name: '创建时间', formType: 'text', width: '150' }
      ]
    }
  },
  computed: {
    displayValue() {
      if (this.field.formType === 'entity') {
        return this.field.relativeForm ? this.field.relativeForm.name : ''
      }
      return this.field.defaultValue
    }
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
    }
  }
}
</script>

<style scoped lang="scss">
.box {
  width: 100%;
  height: 32px;
  font-size: 14px;
  border: 1px solid #dcdfe6;
  border-radius: $xr-border-radius-base;
  background: white;
  padding: 0 10px;
  .item-icon {
    display: inline-block;
    color: #999;
    margin-right: 5px;
  }
  .default-val {
    color: #999;
    line-height: 14px;
  }
}
</style>
