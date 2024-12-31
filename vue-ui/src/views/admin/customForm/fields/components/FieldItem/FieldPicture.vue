<template>
  <field-wrapper
    :activate="activate"
    :field="field"
    :control-flag="controlFlag"
    :show-border="showBorder"
    @click="emitClick"
    @action="handleAction">
    <flexbox
      align="center"
      class="box">
      <i class="item-icon el-icon-plus"/>
    </flexbox>
  </field-wrapper>
</template>

<script>
import FieldWrapper from './FieldWrapper'
import mixins from './mixins'

export default {
  name: 'FieldPicture',
  components: {
    FieldWrapper
  },
  mixins: [mixins]
}
</script>

<style scoped lang="scss">
.box {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  max-width: 100px;
  height: 100px;
  position: relative;
  overflow: hidden;
  display: inline-block;
  text-align: center;
  outline: none;
  .item-icon {
    font-size: 28px;
    color: #8c939d;
    line-height: 100px;
    text-align: center;
  }
}
</style>
