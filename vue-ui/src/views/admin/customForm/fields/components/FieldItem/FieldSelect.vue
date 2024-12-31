<template>
  <field-wrapper
    :activate="activate"
    :field="field"
    :control-flag="controlFlag"
    :show-border="showBorder"
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
      <i v-if="fieldIcon" :class="fieldIcon | iconPre" class="item-icon" />
      <div class="placeholder">
        {{ defaultName }}
      </div>
      <i class="el-icon-arrow-down el-icon--right"/>
    </flexbox>
  </field-wrapper>
</template>

<script>
import FieldWrapper from './FieldWrapper'
import mixins from './mixins'
import { dictSimpleListAPI } from '@/api/dictionary'

export default {
  name: 'FieldSelect',
  components: {
    FieldWrapper
  },
  mixins: [mixins],
  props: {
  },
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
.select-box {
  width: 100%;
  color: #333;
  height: 32px;
  border: 1px solid #dcdfe6;
  border-radius: $xr-border-radius-base;
  padding: 8px 10px;
  div {
    flex: 1;
  }
  .item-icon {
    display: inline-block;
    color: #999;
    margin-right: 5px;
  }
  .placeholder {
    color: #999;
    line-height: 14px;
  }
}

.el-radio-group {
  width: 100%;
  .el-radio {
    margin: 5px 30px 5px 0;
  }
}
</style>
