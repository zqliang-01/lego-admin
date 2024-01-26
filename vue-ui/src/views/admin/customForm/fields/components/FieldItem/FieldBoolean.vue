<template>
  <field-wrapper
    :activate="activate"
    :field="field"
    :control-flag="controlFlag"
    class="field-boolean"
    @click="emitClick"
    @action="handleAction">
    <el-switch v-model="field.defaultValue"/>
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
  font-size: 14px;
  padding: 10px 0;
  border: 1px solid #e1e1e1;
  border-radius: 3px;
  background: white;
  text-align: center;
}
</style>
