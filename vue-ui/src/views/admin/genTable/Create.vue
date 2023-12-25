<template>
  <xr-create
    :loading="loading"
    :title="title"
    @close="close"
    @save="saveClick">
    <el-form
      ref="createForm"
      :model="fieldFrom"
      :rules="fieldRules"
      :validate-on-rule-change="false"
      class="form"
      label-position="top">
      <form-items
        v-for="(children, index) in dataFieldList"
        :key="index"
        :field-from="fieldFrom"
        :field-list="children"
        @change="fieldChange"
      />
    </el-form>
  </xr-create>
</template>

<script>
import {
  genTableAddAPI,
  genTableModifyAPI,
  genTableInitGetAPI
} from '@/api/admin/genTable'
import CreateMixin from '@/components/Mixins/LegoCreate'

export default {
  name: 'GenTableCreate',
  mixins: [CreateMixin],
  props: {
    tableNameList: {
      type: Array,
      default: () => {
        return []
      }
    }
  },
  computed: {
    title() {
      return this.action.type === 'update' ? '编辑数据表' : '新建数据表'
    },
    saveRequest() {
      return this.action.type === 'update' ? genTableModifyAPI : genTableAddAPI
    }
  },
  created() {
    if (this.action.detailData) {
      this.fieldList.map(field => {
        field.value = this.action.detailData[field.fieldCode]
        if (field.fieldCode === 'code') {
          field.setting = this.tableNameList
        }
      })
    }
    this.initField()
  },
  methods: {
    fieldChange(item, index, value, valueList) {
      if (item && item.fieldCode === 'code') {
        genTableInitGetAPI(value).then(res => {
          this.fieldList.map(field => {
            if (field.fieldCode !== 'code') {
              this.$set(this.fieldFrom, field.fieldCode, res.data[field.fieldCode])
            }
          })
        })
      }
    }
  }
}
</script>

<style lang="scss" scoped>
</style>
