<template>
  <el-input
    v-if="isTrimInput(item.formType)"
    v-model.trim="model"
    :disabled="disableStatus"
    :prefix-icon="getInputIcon(item.formType) | iconPre"
    :maxlength="getInputMaxlength(item.formType)"
    :placeholder="item.placeholder"
    type="text"
    @input="commonChange(item, index, $event)"/>
  <cover-upload
    v-else-if="item.formType == 'pictureEditor'"
    v-model="model"
    :disabled="disableStatus"
    :fileUploadAPI="uploadAPI"
    :filePreviewUrl="previewUrl"
    @value-change="commonChange(item, index, $event)"/>
  <image-upload
    v-else-if="item.formType == 'picture'"
    v-model="model"
    :disabled="disableStatus"
    @value-change="commonChange(item, index, $event)"/>
  <json-editor
    v-else-if="item.formType == 'jsonEditor'"
    v-model="model"
    :disabled="disableStatus"
    @input="commonChange(item, index, $event)"/>
  <rich-text-editor
    v-else-if="item.formType == 'richTextEditor'"
    v-model="model"
    :uploadAPI="item.uploadAPI"
    :previewUrl="item.previewUrl"
    :disabled="disableStatus"
    @input="commonChange(item, index, $event)"/>
  <el-input-number
    v-else-if="item.formType == 'number'"
    v-model="model"
    :placeholder="item.placeholder"
    :disabled="disableStatus"
    :controls="false"
    @change="commonChange(item, index, $event)" />
  <el-input-number
    v-else-if="item.formType == 'float'"
    v-model="model"
    :placeholder="item.placeholder"
    :disabled="disableStatus"
    :controls="false"
    @change="commonChange(item, index, $event)" />
  <percent-input
    v-else-if="item.formType == 'percent'"
    v-model="model"
    :placeholder="item.placeholder"
    :disabled="disableStatus"
    :controls="false"
    @change="commonChange(item, index, $event)" />
  <el-input
    v-else-if="item.formType == 'textarea'"
    v-model="model"
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
    v-model="model"
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
    v-model="model"
    :options="item.setting"
    :disabled="disableStatus"
    filterable
  />
  <dept-select
    v-else-if="['multipleStructure'].includes(item.formType)"
    v-model="model"
    :data="item.value"
    :disabled="disableStatus"
    @change="commonChange(item, index, $event)" />
  <lego-checkbox
    v-else-if="['checkbox', 'multipleUser'].includes(item.formType)"
    v-model="model"
    :disabled="disableStatus"
    :clearable="item.clearable"
    :placeholder="item.placeholder"
    :options="item.setting"
    :show-type="item.precisions === 1 ? 'tiled' : 'default'"
    :other-show-input="item.hasOwnProperty('optionsData')"
    @change="commonChange(item, index, $event)"/>
  <el-date-picker
    v-else-if="item.formType == 'date'"
    v-model="model"
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
    v-model="model"
    :disabled="disableStatus"
    clearable
    style="width: 100%;"
    type="datetime"
    value-format="yyyy-MM-dd HH:mm:ss"
    placeholder="选择日期"
    @change="commonChange(item, index, $event)"/>
  <el-switch
    v-else-if="item.formType == 'boolean'"
    v-model="model"
    :active-value="item.activeValue"
    :inactive-value="item.inactiveValue"
    :disabled="disableStatus"
    @change="commonChange(item, index, $event)"/>
  <select-icon
    v-else-if="item.formType == 'icon'"
    v-model="model"
    :disabled="disableStatus"
    @change="commonChange(item, index, $event)"/>
  <signature-pad
    v-else-if="item.formType == 'handwritingSign'"
    v-model="model"
    :disabled="disableStatus"/>
  <desc-text
    v-else-if="item.formType == 'descText'"
    v-model="item.defaultValue"
    :disabled="true"/>
  <lego-relative-cell
    v-else-if="item.formType == 'entity'"
    v-model="model"
    :data="item.value"
    :search-key="item.searchKey"
    :query-api="item.queryApi"
    :field-list="item.fieldList"
    :form-code="relativeFormCode"
    :disabled="disableStatus"
    @change="commonChange(item, index, $event)"/>
  <cron-input
    v-else-if="item.formType == 'cronInput'"
    v-model="model"
    :disabled="disableStatus"
    @change="commonChange(item, index, $event)"/>
  <Address
    v-else-if="item.formType == 'address'"
    v-model="model"
    :disabled="disableStatus"
    @change="commonChange(item, index, $event)"/>
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
import Address from '@/components/Common/Address'

import Mixin from './Mixin'
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
    CronInput,
    Address
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
    model: {
      get: function() {
        return this.item.fieldCode.split('.').reduce((data, key) => data ? data[key] : undefined, this.fieldForm)
      },
      set: function(val) {
        var list = this.item.fieldCode.split('.') || []
        let currentObj = this.fieldForm
        list.forEach((element, index) => {
          if (index === list.length - 1) {
            this.$set(currentObj, element, val)
          } else if (currentObj[element]) {
            currentObj = currentObj[element]
          } else {
            this.$set(currentObj, element, {})
            currentObj = currentObj[element]
          }
        })
      }
    },
    relativeFormCode() {
      if (this.item.relativeForm) {
        return this.item.relativeForm.code
      }
      return ''
    },
    uploadAPI() {
      if (this.item.uploadAPI) {
        return this.item.uploadAPI
      }
      return fileUploadAPI
    },
    previewUrl() {
      if (this.item.previewUrl) {
        return this.item.previewUrl
      }
      return filePreviewUrl
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
  mounted() {},
  beforeDestroy() {},
  methods: {
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
