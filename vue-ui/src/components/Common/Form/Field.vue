<template>
  <el-input
    v-if="isTrimInput(item.formType)"
    v-model.trim="fieldForm[item.fieldCode]"
    :disabled="disableStatus"
    :prefix-icon="getInputIcon(item.formType) | iconPre"
    :maxlength="getInputMaxlength(item.formType)"
    :placeholder="item.placeholder"
    type="text"
    @input="commonChange(item, index, $event)"/>
  <cover-upload
    v-else-if="isCoverUpload"
    v-model="fieldForm[item.fieldCode]"
    :disabled="disableStatus"
    :fileUploadAPI="coverUploadAPI"
    :filePreviewUrl="coverPreviewUrl"
    @value-change="commonChange(item, index, $event)"/>
  <image-upload
    v-else-if="item.formType == 'picture'"
    v-model="fieldForm[item.fieldCode]"
    :disabled="disableStatus"
    @value-change="commonChange(item, index, $event)"/>
  <json-editor
    v-else-if="item.formType == 'jsonEditor'"
    v-model="fieldForm[item.fieldCode]"
    :disabled="disableStatus"
    @input="commonChange(item, index, $event)"/>
  <rich-text-editor
    v-else-if="item.formType == 'rich_text_editor'"
    v-model="fieldForm[item.fieldCode]"
    :disabled="disableStatus"
    @input="commonChange(item, index, $event)"/>
  <el-input-number
    v-else-if="item.formType == 'number'"
    v-model="fieldForm[item.fieldCode]"
    :placeholder="item.placeholder"
    :disabled="disableStatus"
    :controls="false"
    @change="commonChange(item, index, $event)" />
  <el-input-number
    v-else-if="item.formType == 'floatnumber'"
    v-model="fieldForm[item.fieldCode]"
    :placeholder="item.placeholder"
    :disabled="disableStatus"
    :controls="false"
    @change="commonChange(item, index, $event)" />
  <percent-input
    v-else-if="item.formType == 'percent'"
    v-model="fieldForm[item.fieldCode]"
    :placeholder="item.placeholder"
    :disabled="disableStatus"
    :controls="false"
    @change="commonChange(item, index, $event)" />
  <el-input
    v-else-if="item.formType == 'textarea'"
    v-model="fieldForm[item.fieldCode]"
    :disabled="disableStatus"
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
    :disabled="disableStatus"
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
    :disabled="disableStatus"
    filterable
  />
  <dept-select
    v-else-if="['multiple_structure'].includes(item.formType)"
    :value="item.value"
    :disabled="disableStatus"
    @value-change="valueChange(item, index, $event)" />
  <lego-checkbox
    v-else-if="['checkbox', 'multiple_user'].includes(item.formType)"
    v-model="fieldForm[item.fieldCode]"
    :disabled="disableStatus"
    :clearable="item.clearable"
    :placeholder="item.placeholder"
    :options="item.setting"
    :show-type="item.precisions === 1 ? 'tiled' : 'default'"
    :other-show-input="item.hasOwnProperty('optionsData')"
    @change="commonChange(item, index, $event)"/>
  <el-date-picker
    v-else-if="item.formType == 'date'"
    v-model="fieldForm[item.fieldCode]"
    :disabled="disableStatus"
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
    :disabled="disableStatus"
    clearable
    style="width: 100%;"
    type="datetime"
    value-format="yyyy-MM-dd HH:mm:ss"
    placeholder="选择日期"
    @change="commonChange(item, index, $event)"/>
  <el-switch
    v-else-if="item.formType == 'boolean_value'"
    v-model="fieldForm[item.fieldCode]"
    :active-value="item.activeValue"
    :inactive-value="item.inactiveValue"
    :disabled="disableStatus"
    @change="commonChange(item, index, $event)"/>
  <select-icon
    v-else-if="item.formType == 'icon'"
    v-model="fieldForm[item.fieldCode]"
    :disabled="disableStatus"
    @change="commonChange(item, index, $event)"/>
  <signature-pad
    v-else-if="item.formType == 'handwriting_sign'"
    v-model="fieldForm[item.fieldCode]"
    :disabled="disableStatus"/>
  <desc-text
    v-else-if="item.formType == 'desc_text'"
    :value="item.defaultValue"
    :disabled="true"/>
  <lego-relative-cell
    v-else-if="item.formType == 'entity'"
    :value="item.value"
    :form-code="relativeFormCode"
    :disabled="disableStatus"
    @value-change="entityChange(item, index, $event)"/>
  <cron-input
    v-else-if="item.formType == 'cron_input'"
    :cron-value="fieldForm[item.fieldCode]"
    :disabled="disableStatus"
    @change="valueChange(item, index, $event)"/>
  <div v-else>
    <slot :data="item" :index="index" />
  </div>
</template>

<script>
import LegoRelativeCell from '@/components/Lego/LegoRelativeCell'
import SignaturePad from '@/components/Common/SignaturePad'
import DescText from '@/components/Common/DescText'
import PercentInput from '@/components/Common/PercentInput'
import LegoSelect from '@/components/Common/Select'
import LegoCheckbox from '@/components/Common/Checkbox'
import JsonEditor from '@/components/Common/JsonEditor'
import SelectTree from '@/components/Common/SelectTree'
import SelectIcon from '@/components/Common/SelectIcon'
import RichTextEditor from '@/components/Common/RichTextEditor'
import DeptSelect from '@/components/Common/DeptSelect'
import CoverUpload from '@/components/Common/CoverUpload'
import ImageUpload from '@/components/Common/ImageUpload'
import CronInput from '@/components/Common/CronInput'

import Mixin from './Mixin'
import {
  fileUploadAPI as docFileUploadAPI,
  filePreviewUrl as docFilePreviewUrl
} from '@/api/doc/file'
import { fileUploadAPI, filePreviewUrl } from '@/api/common'

export default {
  name: 'Field',
  components: {
    SignaturePad,
    DescText,
    PercentInput,
    LegoSelect,
    LegoCheckbox,
    JsonEditor,
    SelectTree,
    SelectIcon,
    LegoRelativeCell,
    RichTextEditor,
    DeptSelect,
    CoverUpload,
    ImageUpload,
    CronInput
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
      disableStatus: false,
      pickerOptions: {}
    }
  },
  computed: {
    isCoverUpload() {
      return ['doc_cover'].includes(this.item.formType)
    },
    coverUploadAPI() {
      if (this.item.formType === 'doc_cover') {
        return docFileUploadAPI
      }
      return fileUploadAPI
    },
    coverPreviewUrl() {
      if (this.item.formType === 'doc_cover') {
        return docFilePreviewUrl
      }
      return filePreviewUrl
    },
    relativeFormCode() {
      if (this.item.relativeForm) {
        return this.item.relativeForm.code
      }
      return ''
    }
  },
  watch: {
    item: {
      handler() {
        this.disableStatus = this.item.disabled || this.disabled
      },
      deep: true,
      immediate: true
    }
  },
  created() {
    this.disableStatus = this.item.disabled || this.disabled
  },
  mounted() {},
  beforeDestroy() {},
  methods: {
    entityChange(item, index, value) {
      const result = value ? value.code : ''
      this.$set(this.fieldForm, item.fieldCode, result)
      this.commonChange(item, index, value)
    },
    valueChange(item, index, value) {
      this.$set(this.fieldForm, item.fieldCode, value)
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
