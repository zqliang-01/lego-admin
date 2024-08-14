<template>
  <field-wrapper
    :activate="activate"
    :field="field"
    :control-flag="controlFlag"
    class="field-input"
    @click="emitClick"
    @action="handleAction">
    <lego-relative-cell
      v-if="field.formType === 'entity'"
      :value="field.relativeForm"
      :field-list="formFieldList"
      search-key="name"
      query-api="/back-end/sys-custom-form/list"
      @value-change="entityChange(field, $event)"/>
    <flexbox
      v-else
      align="center"
      class="box">
      <span class="default-val">
        <i v-if="fieldIcon" :class="fieldIcon | iconPre" class="item-icon" />
        {{ typeof field.defaultValue == 'string' ? field.defaultValue : '' }}
      </span>
    </flexbox>
  </field-wrapper>
</template>

<script>
import FieldWrapper from './FieldWrapper'
import LegoRelativeCell from '@/components/Lego/LegoRelativeCell'
import mixins from './mixins'
import { isEmpty } from '@/utils/types'

export default {
  name: 'FieldInput',
  components: {
    FieldWrapper,
    LegoRelativeCell
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
    margin-right: 8px;
  }
  .default-val {
    color: #333;
  }
}
</style>
