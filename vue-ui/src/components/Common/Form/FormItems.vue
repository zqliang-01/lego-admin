<template>
  <flexbox
    class="form-items"
    align="flex-start"
    wrap="wrap"
    justify="flex-start">
    <template v-for="(item, index) in fieldList">
      <form-item
        v-if="showField(item)"
        :key="index"
        :prop-prefix="propPrefix"
        :item="item"
        :index="index"
        :field-form="fieldForm"
        :disabled="disabled"
        @change="fieldChange"
      >
        <template slot-scope="{ index }">
          <slot :data="item" :index="index" />
        </template>
      </form-item>
    </template>
  </flexbox>
</template>

<script>
import FormItem from './FormItem'
import { isBoolean } from '@/utils/types'

export default {
  // 多块形式的form-item 用于字段库
  name: 'FormItems',

  components: {
    FormItem
  },

  props: {
    // 表单验证前缀
    propPrefix: {
      type: String,
      default: ''
    },
    fieldForm: {
      type: Object,
      default: () => {
        return {}
      }
    },
    fieldList: {
      type: Array,
      default: () => {
        return []
      }
    },
    disabled: Boolean
  },
  methods: {
    showField(item) {
      if (isBoolean(item.editable)) {
        return item.editable
      }
      return true
    },
    fieldChange(item, index, value, valueList) {
      this.$emit('change', item, index, value, valueList)
    }
  }
}
</script>

<style lang="scss" scoped>
.form-items {
  padding: 0 12px;
}
</style>
