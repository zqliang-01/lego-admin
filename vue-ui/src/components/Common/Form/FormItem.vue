<template>
  <el-form-item
    v-if="getShowValue(item)"
    :key="index"
    :prop="`${propPrefix || ''}${item.fieldCode}`"
    :rules="item.rules"
    :class="[item.className || '', `is-${item.formType}`]"
    :style="{width: item.stylePercent ? `${item.stylePercent}%` : '50%'}"
    class="form-item">
    <template v-if="!item.simpleType" slot="label">
      {{ item.name }}
      <el-tooltip
        v-if="item.tipType == 'tooltip'"
        effect="dark"
        placement="top">
        <div slot="content" v-html="getTips(item)"/>
        <i :class="'help lego-help-tips' | iconPre"/>
      </el-tooltip>
      <span v-else style="color:#999;">
        {{ getTips(item) }}
      </span>
    </template>
    <field
      :item="item"
      :index="index"
      :field-form="fieldForm"
      :disabled="disabled"
      @change="fieldChange"
    >
      <template slot-scope="{ data, index }">
        <slot :data="data" :index="index" />
      </template>
    </field>
  </el-form-item>
</template>

<script>
import Field from './Field'

import Mixin from './Mixin'

export default {
  // item
  name: 'FormItem',

  components: {
    Field
  },

  mixins: [Mixin],

  props: {
    // 表单验证前缀
    propPrefix: {
      type: String,
      default: ''
    },
    item: Object,
    index: Number,
    fieldForm: {
      type: Object,
      default: () => {
        return {}
      }
    },
    disabled: Boolean
  },
  methods: {
    fieldChange(item, index, value, valueList) {
      this.$emit('change', item, index, value, valueList)
    }
  }
}
</script>

<style lang="scss">
.form-item {
  .el-form-item__label {
    line-height: 1.5;
    padding-bottom: 8px;
    word-break: break-all;
    word-wrap: break-word;
    color: #333;
  }

  .el-form-item__error {
    position: relative;
    top: auto;
    left: auto;
  }

  .el-form-item.is-desc_text {
    .el-form-item__label {
      display: none;
    }
  }
}
</style>


<style lang="scss" scoped>
.form-item {
  padding: 12px 12px 0;
  margin-bottom: 0;
}
</style>
