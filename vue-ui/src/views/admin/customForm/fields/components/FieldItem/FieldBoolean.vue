<template>
  <field-wrapper
    :activate="activate"
    :field="field"
    :control-flag="controlFlag"
    :show-border="showBorder"
    class="field-boolean"
    @click="emitClick"
    @action="handleAction">
    <flexbox
      align="center"
      class="box">
      <el-switch v-model="field.defaultValue"/>
    </flexbox>
  </field-wrapper>
</template>

<script>
import FieldWrapper from './FieldWrapper'
import mixins from './mixins'

export default {
  name: 'FieldBoolean',
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
  height: 32px;
  font-size: 14px;
  padding: 10px 0;
}
</style>
