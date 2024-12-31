<template>
  <field-wrapper
    :activate="activate"
    :field="field"
    :control-flag="controlFlag"
    :show-border="showBorder"
    class="field-writing-sign"
    @click="emitClick"
    @action="handleAction">

    <div class="box" />

  </field-wrapper>
</template>

<script>
import FieldWrapper from './FieldWrapper'
import mixins from './mixins'

export default {
  name: 'FieldWritingSign',
  components: {
    FieldWrapper
  },
  mixins: [mixins],
  methods: {}
}
</script>

<style scoped lang="scss">
.box {
  width: 100%;
  height: 150px;
  border: 1px solid #e1e1e1;
  border-radius: 3px;
  background: white;
  padding: 10px 0;
}
</style>
