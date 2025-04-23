<template>
  <div class="select">
    <el-select
      v-if="showType === 'default'"
      v-model="dataValue"
      :disabled="disabled"
      :clearable="clearable"
      :placeholder="placeholder"
      :filterable="filterable"
      style="width: 100%;"
      @change="valueChange">
      <el-option
        v-for="(item, index) in options"
        :key="index"
        :label="item.name "
        :value="item.code"/>
    </el-select>
    <el-radio-group
      v-else-if="showType === 'tiled'"
      v-model="dataValue"
      @change="valueChange">
      <el-radio
        v-for="(item, index) in options"
        :key="index"
        :disabled="disabled"
        :label="item.code">
        {{ item.name }}
      </el-radio>
    </el-radio-group>
  </div>
</template>

<script>
import { isEmpty } from '@/utils/types'

export default {
  name: 'LegoSelect',
  props: {
    value: [String, Number, Object],
    item: Object,
    disabled: Boolean,
    clearable: { type: Boolean, default: () => { return true } },
    placeholder: String,
    filterable: {
      type: Boolean,
      default: false
    },
    options: {
      type: Array,
      default: () => {
        return []
      }
    },
    showType: {
      type: String,
      default: 'default' //  下拉 default 平铺 tiled
    }
  },

  data() {
    return {
      dataValue: ''
    }
  },

  computed: {
  },

  watch: {
    value: {
      handler(newVal, oldVal) {
        if (this.value !== this.dataValue) {
          if (isEmpty(this.value)) {
            this.dataValue = ''
          } else {
            this.dataValue = this.value
          }
        }
      },
      immediate: true
    }
  },

  created() {
  },

  mounted() {},

  beforeDestroy() {},

  methods: {
    /**
     * 值更新
     */
    valueChange() {
      this.$emit('input', this.dataValue)
      this.$emit('change', this.dataValue)
      this.dispatch('ElFormItem', 'el.form.change', this.dataValue)
    },

    /**
     * 失去焦点
     */
    inputBlur() {
      this.$emit('input', '')
      this.$emit('change', '')
      this.dispatch('ElFormItem', 'el.form.change', '')
    },
    dispatch(componentName, eventName, params) {
      let parent = this.$parent || this.$root
      let name = parent.$options.name

      while (parent && (!name || name !== componentName)) {
        parent = parent.$parent
        if (parent) {
          name = parent.$options.name
        }
      }
      if (parent) {
        parent.$emit.apply(parent, [eventName].concat(params))
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.select {
  .el-input {
    margin-top: 5px;
  }

  .el-radio {
    margin: 5px 30px 5px 0;
  }
}
</style>
