<template>
  <el-input
    v-if="item.formType == 'text'"
    v-model="fieldForm[item.fieldCode]"
    :disabled="item.disabled || disabled"
    :maxlength="100"
    :placeholder="item.placeholder"
    :type="item.formType"
    @input="commonChange(item, index, $event)"/>
  <el-input
    v-else-if="isTrimInput(item.formType)"
    v-model.trim="fieldForm[item.fieldCode]"
    :disabled="item.disabled || disabled"
    :prefix-icon="getInputIcon(item.formType) | iconPre"
    :maxlength="getInputMaxlength(item.formType)"
    :placeholder="item.placeholder"
    type="text"
    @input="commonChange(item, index, $event)"/>
  <json-editor
    v-else-if="item.formType == 'jsonEditor'"
    v-model="fieldForm[item.fieldCode]"
    :disabled="item.disabled || disabled"
    @input="commonChange(item, index, $event)"/>
  <el-input-number
    v-else-if="item.formType == 'number'"
    v-model="fieldForm[item.fieldCode]"
    :placeholder="item.placeholder"
    :disabled="item.disabled || disabled"
    :controls="false"
    @change="commonChange(item, index, $event)" />
  <el-input-number
    v-else-if="item.formType == 'floatnumber'"
    v-model="fieldForm[item.fieldCode]"
    :placeholder="item.placeholder"
    :disabled="item.disabled || disabled"
    :controls="false"
    @change="commonChange(item, index, $event)" />
  <percent-input
    v-else-if="item.formType == 'percent'"
    v-model="fieldForm[item.fieldCode]"
    :placeholder="item.placeholder"
    :disabled="item.disabled || disabled"
    :controls="false"
    @change="commonChange(item, index, $event)" />
  <el-input
    v-else-if="item.formType == 'textarea'"
    v-model="fieldForm[item.fieldCode]"
    :disabled="item.disabled || disabled"
    :rows="3"
    :autosize="{ minRows: 3}"
    :maxlength="item.maxlength || 800"
    :placeholder="item.placeholder"
    :type="item.formType"
    resize="none"
    @input="commonChange(item, index, $event)" />
  <lego-select
    v-else-if="['select', 'user'].includes(item.formType)"
    v-model="fieldForm[item.fieldCode]"
    :item="item"
    :disabled="item.disabled || disabled"
    :clearable="item.clearable"
    :placeholder="item.placeholder"
    :options="item.setting"
    :filterable="item.filterable"
    :show-type="item.precisions === 1 ? 'tiled' : 'default'"
    @change="commonChange(item, index, $event)"/>
  <select-tree
    v-else-if="['structure'].includes(item.formType)"
    v-model="fieldForm[item.fieldCode]"
    :options="item.setting"
    :disabled="item.disabled || disabled"
    filterable
  />
  <lego-checkbox
    v-else-if="['checkbox'].includes(item.formType)"
    v-model="fieldForm[item.fieldCode]"
    :disabled="item.disabled || disabled"
    :clearable="item.clearable"
    :placeholder="item.placeholder"
    :options="item.setting"
    :show-type="item.precisions === 1 ? 'tiled' : 'default'"
    :other-show-input="item.hasOwnProperty('optionsData')"
    @change="commonChange(item, index, $event)"/>
  <el-date-picker
    v-else-if="item.formType == 'date'"
    v-model="fieldForm[item.fieldCode]"
    :disabled="item.disabled || disabled"
    :picker-options="pickerOptions"
    clearable
    style="width: 100%;"
    type="date"
    value-format="yyyy-MM-dd"
    placeholder="选择日期"
    @change="commonChange(item, index, $event)"/>
  <el-date-picker
    v-else-if="item.formType == 'datetime'"
    v-model="fieldForm[item.fieldCode]"
    :disabled="item.disabled || disabled"
    clearable
    style="width: 100%;"
    type="datetime"
    value-format="yyyy-MM-dd HH:mm:ss"
    placeholder="选择日期"
    @change="commonChange(item, index, $event)"/>
  <el-switch
    v-else-if="item.formType == 'boolean_value'"
    v-model="fieldForm[item.fieldCode]"
    :disabled="item.disabled || disabled"
    @change="commonChange(item, index, $event)"/>
  <select-icon
    v-else-if="item.formType == 'icon'"
    v-model="fieldForm[item.fieldCode]"
    :disabled="item.disabled || disabled"
    @change="commonChange(item, index, $event)"/>
  <signature-pad
    v-else-if="item.formType == 'handwriting_sign'"
    v-model="fieldForm[item.fieldCode]"
    :disabled="item.disabled || disabled"/>
  <desc-text
    v-else-if="item.formType == 'desc_text'"
    :value="fieldForm[item.fieldCode]"/>
  <lego-relative-cell
    v-else-if="item.formType == 'entity'"
    :value="item.value"
    :form-code="relativeFormCode"
    :disabled="item.disabled"
    @value-change="entityChange(item, index, $event)"/>
  <div v-else>
    <slot :data="item" :index="index" />
  </div>
</template>

<script>
import LegoRelativeCell from '@/components/Relative/LegoRelativeCell'
import SignaturePad from '@/components/NewCom/SignaturePad'
import DescText from '@/components/NewCom/DescText'
import PercentInput from '@/components/NewCom/PercentInput'
import LegoSelect from '@/components/NewCom/Select'
import LegoCheckbox from '@/components/NewCom/Checkbox'
import JsonEditor from '@/components/NewCom/JsonEditor'
import VDistpicker from '@/components/VDistpicker'
import SelectTree from '@/components/NewCom/SelectTree'
import SelectIcon from '@/components/NewCom/SelectIcon'

import Mixin from './Mixin'

export default {
  name: 'Field',
  components: {
    SignaturePad,
    DescText,
    PercentInput,
    LegoSelect,
    LegoCheckbox,
    JsonEditor,
    VDistpicker,
    SelectTree,
    SelectIcon,
    LegoRelativeCell
  },
  mixins: [Mixin],
  props: {
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

  data() {
    return {
      pickerOptions: {}
    }
  },
  computed: {
    relativeFormCode() {
      if (this.item.relativeForm) {
        return this.item.relativeForm.code
      }
      return ''
    }
  },
  watch: {},
  created() {},
  mounted() {},
  beforeDestroy() {},
  methods: {
    entityChange(item, index, value) {
      const result = value ? value.code : ''
      this.$set(this.fieldForm, item.fieldCode, result)
      this.commonChange(item, index, value)
    }
  }
}
</script>

<style lang="scss" scoped>
.el-input-number {
  width: 100%;
  ::v-deep .el-input__inner {
    text-align: left;
    padding: 0 8px;
  }
}

.checkbox {
  zoom: 150%;
}
</style>
