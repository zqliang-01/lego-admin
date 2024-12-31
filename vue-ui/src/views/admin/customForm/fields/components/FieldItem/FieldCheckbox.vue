<template>
  <field-wrapper
    :activate="activate"
    :field="field"
    :control-flag="controlFlag"
    :show-border="showBorder"
    class="field-checkbox"
    @click="emitClick"
    @action="handleAction">

    <el-checkbox-group
      v-if="field.precisions === 1"
      v-model="field.defaultValue">
      <el-checkbox
        v-for="(item, index) in options"
        :key="index"
        :label="item.code"
        class="checkbox">
        {{ item.name }}
      </el-checkbox>
    </el-checkbox-group>

    <div
      v-else
      class="select-content">
      <el-select
        v-model="field.defaultValue"
        multiple
        placeholder="请选择">
        <el-option
          v-for="(item, index) in options"
          :key="index"
          :label="item.name"
          :value="item.code" />
      </el-select>
      <div class="mask" />
    </div>

  </field-wrapper>
</template>

<script>
import FieldWrapper from './FieldWrapper'
import mixins from './mixins'
import { dictSimpleListAPI } from '@/api/dictionary'

export default {
  name: 'FieldCheckbox',
  components: {
    FieldWrapper
  },
  mixins: [mixins],
  data() {
    return {
      options: []
    }
  },
  watch: {
    field: {
      handler() {
        const field = this.field
        if (!field.precisions) {
          this.$set(this.field, 'precisions', 1)
        }
        this.getOptions()
      },
      deep: true,
      immediate: true
    }
  },
  created() {
    this.getOptions()
  },
  methods: {
    getOptions() {
      const field = this.field
      if (field.optionDataType === 'dict') {
        dictSimpleListAPI(field.optionDictType).then(res => {
          this.options = res.data
        })
      }
    }
  }
}
</script>

<style scoped lang="scss">
.select-content {
  position: relative;
  width: 100%;
  color: #333;

  .mask {
    position: absolute;
    top: 0;
    left: 0;
    z-index: 100;
    width: 100%;
    height: 100%;
    background-color: transparent;
    display: block;
  }

  .el-select {
    width: 100%;
  }
}
</style>
