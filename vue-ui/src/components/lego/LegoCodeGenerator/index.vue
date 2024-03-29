<template>
  <el-popover
    v-model="showPopover"
    :disabled="disabled"
    placement="right-end"
    width="350"
    popper-class="no-padding-popover"
    trigger="click">
    <rule-edit
      v-loading="loading"
      v-if="showSelectView"
      :value="dataValue"
      @close="showPopover = false"
      @value-change="handleSumbit"
    />
    <flexbox
      slot="reference"
      :class="[disabled ? 'is_disabled' : 'is_valid']"
      wrap="wrap"
      class="user-container xh-form-border"
      @click.native="showSelectView = true">
      <div v-if="isEmptyValue" class="add-item">+配置自动编码</div>
      <div v-else class="user-item">
        {{ dataValue.name }}
      </div>
    </flexbox>
  </el-popover>
</template>
<script type="text/javascript">
import RuleEdit from './RuleEdit'
import { isEmpty } from '@/utils/types'

import {
  codeGeneratorGetAPI,
  codeGeneratorAddAPI,
  codeGeneratorUpdateAPI
} from '@/api/admin/codeGenerator'

export default {
  name: 'LegoCodeGenerator',
  components: {
    RuleEdit
  },
  props: {
    value: {
      type: Object,
      default: () => {
        return {}
      }
    },
    fieldCode: {
      type: String
    },
    disabled: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      loading: false,
      showPopover: false,
      showSelectView: false,
      dataValue: {}
    }
  },
  computed: {
    isEmptyValue() {
      return isEmpty(this.dataValue) || isEmpty(this.dataValue.code)
    }
  },
  watch: {
    value: function(val) {
      if (!isEmpty(val) && val.code) {
        this.refresh()
      }
    }
  },
  mounted() {
    if (!isEmpty(this.value) && this.value.code) {
      this.refresh()
    }
  },
  methods: {
    refresh() {
      codeGeneratorGetAPI(this.value.code).then(res => {
        this.dataValue = res.data
      })
    },
    handleSumbit(value) {
      this.loading = true
      let request = codeGeneratorAddAPI
      if (value && value.code) {
        request = codeGeneratorUpdateAPI
      }
      value.customFieldCode = this.fieldCode
      request(value).then(res => {
        this.$emit('value-change', res.data)
        this.loading = false
        this.showPopover = false
      }).catch(() => {
        this.loading = false
      })
    }
  }
}
</script>
<style lang="scss" scoped>
.user-container {
  min-height: 34px;
  position: relative;
  border-radius: 3px;
  font-size: 12px;
  border: 1px solid #ddd;
  color: #333333;
  line-height: 15px;
  cursor: pointer;
  .user-item {
    padding: 5px;
    background-color: #e2ebf9;
    border-radius: 3px;
    margin: 3px;
    cursor: pointer;
  }
  .add-item {
    padding: 5px;
    color: $xr-color-text-placeholder;
    cursor: pointer;
  }
}

.user-container.is_disabled {
  background-color: #f5f7fa;
  border-color: #e4e7ed;
  cursor: not-allowed;
  .user-item {
    background-color: #f0f4f8ea;
    color: #c0c4cc;
    cursor: not-allowed;
  }
  .delete-icon {
    color: #c0c4cc;
    cursor: not-allowed;
  }
  .add-item {
    color: #c0c4cc;
    cursor: not-allowed;
  }
}

.user-container.is_valid:hover {
  border-color: #c0c4cc;
}
</style>
