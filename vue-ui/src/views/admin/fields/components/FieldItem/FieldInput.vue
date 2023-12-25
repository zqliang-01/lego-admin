<template>
  <field-wrapper
    :activate="activate"
    :field="field"
    :control-flag="controlFlag"
    class="field-input"
    @click="emitClick"
    @action="handleAction">

    <flexbox align="center" class="box">
      <span class="default-val">
        <i v-if="fieldIcon" :class="fieldIcon | iconPre" class="item-icon" />
        {{ typeof field.defaultValue == 'string' ? field.defaultValue : '' }}
      </span>
    </flexbox>

  </field-wrapper>
</template>

<script>
import FieldWrapper from './FieldWrapper'
import FieldTypeLib from '../../fieldTypeLib'
import mixins from './mixins'

export default {
  name: 'FieldInput',
  components: {
    FieldWrapper
  },
  mixins: [mixins],
  methods: {
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
