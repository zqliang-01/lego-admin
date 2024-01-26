<template>
  <field-wrapper
    :activate="activate"
    :field="field"
    :control-flag="controlFlag"
    class="field-percent"
    @click="emitClick"
    @action="handleAction">

    <div class="box">
      <span class="default-val">{{ field.defaultValue || '' }}</span>
      <span class="rate">%</span>
    </div>

  </field-wrapper>
</template>

<script>
import FieldWrapper from './FieldWrapper'
import mixins from './mixins'

export default {
  name: 'FieldPercent',
  components: {
    FieldWrapper
  },
  mixins: [mixins],
  methods: {}
}
</script>

<style scoped lang="scss">
.box {
  position: relative;
  width: 100%;
  height: 32px;
  line-height: 32px;
  font-size: 14px;
  color: #333;
  border: 1px solid #dcdfe6;
  border-radius: $xr-border-radius-base;
  background: white;
  padding: 0 30px 0 10px;

  .rate {
    position: absolute;
    top: 50%;
    right: 10px;
    transform: translateY(-50%);
  }
}
</style>
