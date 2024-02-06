<template>
  <xr-create
    :loading="loading"
    :title="createTitle"
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
    createTitle() {
      if (this.title) {
        return title
      }
      return this.action.type === 'update' ? '编辑数据表' : '新建数据表'
    },
    saveRequest() {
      return this.action.type === 'update' ? genTableModifyAPI : genTableAddAPI
    }
  },
  created() {
    this.dataFieldList = this.fieldList
    this.detailData = this.action.detailData
    if (this.action.detailData) {
      this.dataFieldList.map(fields => {
        fields.map(field => {
          this.$set(field, 'disabled', false)
          if (field.fieldCode === 'code') {
            field.setting = this.tableNameList
          }
        })
      })
    }
    this.initValue()
  },
  methods: {
    fieldChange(item, index, value, valueList) {
      if (item && item.fieldCode === 'code') {
        genTableInitGetAPI(value).then(res => {
          this.dataFieldList.map(fields => {
            fields.map(field => {
              if (field.fieldCode !== 'code') {
                this.$set(this.fieldFrom, field.fieldCode, res.data[field.fieldCode])
              }
            })
          })
        })
      }
    }
  }
}
</script>

<style lang="scss" scoped>
</style>
