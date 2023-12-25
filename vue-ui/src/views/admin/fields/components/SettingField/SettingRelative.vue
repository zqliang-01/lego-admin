<template>
  <div class="setting-default">
    <lego-relative-cell
      :value="field.relativeForm"
      :field-list="fieldList"
      :action="{request: tableListRequest}"
      @value-change="entityChange(field, $event)"/>
  </div>
</template>

<script>
import { customFormListAPI } from '@/api/admin/formField'
import { isEmpty } from '@/utils/types'
import LegoRelativeCell from '@/components/Relative/LegoRelativeCell'

export default {
  name: 'SettingRelative',
  components: {
    LegoRelativeCell
  },
  props: {
    field: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      tableListRequest: customFormListAPI,
      fieldList: [
        { fieldCode: 'name', name: '模块', formType: 'text', width: '150' },
        { fieldCode: 'table', name: '数据表', formType: 'select', width: '150' },
        { fieldCode: 'permission', name: '归属菜单', formType: 'select', width: '150' },
        { fieldCode: 'createTime', name: '创建时间', formType: 'text', width: '150' }
      ]
    }
  },
  computed: {},
  watch: {},
  created() {},
  methods: {
    entityChange(field, value) {
      if (!isEmpty(value)) {
        this.$set(this.field, 'relativeFormCode', value.code)
        return
      }
      this.$set(this.field, 'relativeForm', null)
      this.$set(this.field, 'relativeFormCode', '')
    }
  }
}
</script>

<style scoped lang="scss">
.el-date-editor {
  width: 100%;
}
.el-select {
  width: 100%;
}
.el-cascader {
  width: 100%;
}
.el-input__icon {
  color: #333333;
}
.input-tips {
  font-size: 12px;
  margin-top: 10px;
  color: #999;
  span {
    color: red;
  }
}
</style>
