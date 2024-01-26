<template>
  <field-wrapper
    :activate="activate"
    :field="field"
    :control-flag="controlFlag"
    @click="emitClick"
    @action="handleAction">

    <div class="box">
      <div class="default-val">
        {{ typeof field.defaultValue == 'string' ? field.defaultValue : '' }}
      </div>
      <div class="max-tips">
        Json
      </div>
    </div>

  </field-wrapper>
</template>

<script>
import FieldWrapper from './FieldWrapper'
import mixins from './mixins'

export default {
  name: 'FieldJsonEditor',
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
  height: 80px;
  font-size: 14px;
  color: #333;
  background: white;
  border: 1px solid #dcdfe6;
  border-radius: $xr-border-radius-base;
  padding: 10px 10px 15px 10px;

  .default-val {
    width: 100%;
    height: 48px;
    word-break: break-all;
    overflow: hidden;
  }
  .max-tips {
    width: 100%;
    text-align: right;
    font-size: 12px;
    color: #999;
    word-break: break-all;
    overflow: hidden;
  }
}
</style>
