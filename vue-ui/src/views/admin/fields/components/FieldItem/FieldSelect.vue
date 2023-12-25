<template>
  <field-wrapper
    :activate="activate"
    :field="field"
    :control-flag="controlFlag"
    class="field-select"
    @click="emitClick"
    @action="handleAction">

    <el-radio-group
      v-if="field.precisions === 1"
      v-model="field.defaultValue">
      <el-radio
        v-for="(item, index) in options"
        :key="index"
        :label="item.code">
        {{ item.name }}
      </el-radio>
    </el-radio-group>

    <flexbox
      v-else
      class="select-box">
      <div :class="{placeholder: !Boolean(field.defaultValue)}">
        {{ defaultName }}
      </div>
      <i class="el-icon-arrow-down el-icon--right"/>
    </flexbox>
  </field-wrapper>
</template>

<script>
import FieldWrapper from './FieldWrapper'
import mixins from './mixins'
import { dictListAPI } from '@/api/crm/common'

export default {
  name: 'FieldSelect',
  components: {
    FieldWrapper
  },
  mixins: [mixins],
  data() {
    return {
      options: []
    }
  },
  computed: {
    defaultName() {
      const defaultValue = this.field.defaultValue
      const option = this.options.find(item => item.code === defaultValue)
      if (option) {
        return option.name
      }
      return '请选择'
    }
  },
  watch: {
    field: {
      handler() {
        const field = this.field
        if (!field.precisions) {
          this.$set(this.field, 'precisions', 2)
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
      if (field.appCode && field.optionDataType === 'dict') {
        dictListAPI(field.appCode, field.optionDictType).then(res => {
          this.options = res.data
        })
      }
    }
  }
}
</script>

<style scoped lang="scss">
.select-box {
  width: 100%;
  color: #333;
  border: 1px solid #dcdfe6;
  border-radius: $xr-border-radius-base;
  padding: 8px 10px;
  div {
    flex: 1;
  }
  .placeholder {
    color: #999;
  }
}

.el-radio-group {
  width: 100%;
  .el-radio {
    margin: 5px 30px 5px 0;
  }
}
</style>
