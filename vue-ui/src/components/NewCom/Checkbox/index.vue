<template>
  <div>
    <el-select
      v-if="showType === 'default'"
      v-model="dataValue"
      :disabled="disabled"
      :clearable="clearable"
      :placeholder="placeholder"
      style="width: 100%;"
      multiple
      @change="valueChange">
      <el-option
        v-for="(item, index) in options"
        :key="index"
        :label="item.name "
        :value="item.code"/>
    </el-select>
    <el-checkbox-group
      v-else-if="showType === 'tiled'"
      v-model="dataValue"
      :disabled="disabled"
      @change="valueChange">
      <el-checkbox
        v-for="(item, index) in options"
        :key="index"
        :label="item.code">
        {{ item.name }}
      </el-checkbox>
    </el-checkbox-group>
  </div>
</template>

<script>
import { isEmpty } from '@/utils/types'
import Emitter from 'element-ui/lib/mixins/emitter'

export default {
  // 自定义字段库 多选
  name: 'Checkbox',

  components: {},

  mixins: [Emitter],

  props: {
    value: {},
    disabled: Boolean,
    clearable: Boolean,
    placeholder: String,
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
      dataValue: []
    }
  },

  computed: {
  },

  watch: {
    value: {
      handler(newVal, oldVal) {
        this.validValue()
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
     * 验证值
     */
    validValue() {
      if (isEmpty(this.value)) {
        this.dataValue = []
      } else {
        this.dataValue = this.value
      }
    },

    /**
     * 选项值
     */
    getValue(item) {
      return !this.isEmptyValue(item.value) ? item.value : item
    },

    /**
     * 判断是空值
     */
    isEmptyValue(value) {
      return value === null || value == undefined
    },

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
      this.valueChange()
    }
  }
}
</script>

<style lang="scss" scoped>
</style>
