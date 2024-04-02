<template>
  <el-popover
    v-model="showPopover"
    :disabled="disabled"
    placement="right-end"
    width="700"
    popper-class="no-padding-popover"
    trigger="click">
    <lego-relative
      v-if="!disabled && showSelectView"
      v-empty="!readAuth"
      xs-empty-icon="nopermission"
      xs-empty-text="无操作权限"
      ref="legoRelative"
      :radio="radio"
      :field-list="tableFieldList"
      :selected-data="selectedData"
      :query-api-url="queryApiUrl"
      @close="showPopover = false"
      @changeCheckout="checkInfos"/>
    <flexbox
      slot="reference"
      :class="[disabled ? 'is_disabled' : 'is_valid']"
      wrap="wrap"
      class="user-container xh-form-border"
      @click.native="showSelectView = true">
      <div
        v-for="(item, index) in dataValue"
        :key="index"
        class="user-item"
        @click.stop="deleteinfo(index)">{{ item.name }}
        <i class="delete-icon el-icon-close"/>
      </div>
      <div
        v-if="dataValue.length == 0"
        class="add-item">+添加</div>
    </flexbox>
  </el-popover>
</template>
<script type="text/javascript">
import { isEmpty } from '@/utils/types'
import { objDeepCopy } from '@/utils/index'
import LegoRelative from './LegoRelative'
import { tableHeaderFieldListAPI } from '@/api/admin/formField'
import { getMenuAuth } from '@/utils/auth'

export default {
  name: 'LegoRelativeCell',
  components: {
    LegoRelative
  },
  props: {
    index: Number,
    fieldList: {
      type: Array,
      default: () => {
        return []
      }
    },
    formCode: {
      type: String,
      default: ''
    },
    value: {
      type: Object,
      default: () => {
        return {}
      }
    },
    disabled: {
      type: Boolean,
      default: false
    },
    queryApi: String
  },
  data() {
    return {
      auth: {},
      showPopover: false,
      showSelectView: false,
      radio: true,
      dataValue: [],
      tableFieldList: []
    }
  },
  computed: {
    selectedData() {
      return this.dataValue
    },
    readAuth() {
      if (this.auth.read || !this.formCode) {
        return true
      }
      return false
    }
  },
  watch: {
    value: function(val) {
      if (isEmpty(val)) {
        this.dataValue = []
        this.checkInfos(this.dataValue)
      }
      if (!isEmpty(val) && val.code) {
        this.dataValue = [val]
        this.checkInfos(this.dataValue)
      }
    },
    formCode: function(val) {
      this.initFields()
    }
  },
  mounted() {
    console.log('RelativeCell', this.disabled)
    if (isEmpty(this.value)) {
      this.dataValue = []
      this.checkInfos(this.dataValue)
    }
    if (!isEmpty(this.value) && this.value.code) {
      this.dataValue = [this.value]
      this.checkInfos(this.dataValue)
    }
    this.initFields()
  },
  methods: {
    initFields() {
      if (this.formCode) {
        tableHeaderFieldListAPI(this.formCode).then(res => {
          this.auth = getMenuAuth(res.data.form.permission.code)
          this.queryApiUrl = res.data.form.queryApiUrl
          this.tableFieldList = res.data.fields
        })
      } else {
        this.queryApiUrl = this.queryApi
        this.tableFieldList = objDeepCopy(this.fieldList)
      }
    },
    /** 选中 */
    checkInfos(data) {
      this.dataValue = data
      const value = data && data.length > 0 ? data[0] : []
      this.$emit('value-change', value)
    },
    /** 删除 */
    deleteinfo(index) {
      if (this.disabled) return
      if (this.radio && this.$refs.legoRelative) {
        this.$refs.legoRelative.clearAll()
      }
      if (this.dataValue.length === 1) {
        this.dataValue = []
      } else {
        this.dataValue.splice(index, 1)
      }
      this.$emit('value-change', this.dataValue)
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
  .delete-icon {
    color: #999;
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
