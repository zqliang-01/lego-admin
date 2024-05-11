<template>
  <fade-view
    :visible.sync="visible"
    :loading="loading"
    :title="createTitle"
    @close="close"
    @save="saveClick">
    <el-form
      ref="createForm"
      :model="fieldForm"
      :rules="fieldRules"
      :validate-on-rule-change="false"
      class="form"
      label-position="top">
      <form-items
        v-for="(children, index) in dataFieldList"
        :key="index"
        :field-form="fieldForm"
        :field-list="children"
        @change="fieldChange"
      />
    </el-form>
  </fade-view>
</template>

<script>
import {
  genTableAddAPI,
  genTableModifyAPI,
  genTableInitGetAPI,
  genTableNameListAPI
} from '@/api/admin/genTable'
import CreateMixin from '@/components/lego/mixins/LegoCreate'
import { objDeepCopy } from '@/utils'

export default {
  name: 'GenTableCreate',
  mixins: [CreateMixin],
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
  watch: {
    action: {
      handler() {
        this.init()
      },
      deep: true,
      immediate: true
    }
  },
  methods: {
    init() {
      this.actionType = this.action.type
      this.detailData = this.action.detailData
      this.dataFieldList = objDeepCopy(this.fieldList)
      this.initValue()
    },
    fieldChange(item, index, value) {
      if (item && item.fieldCode === 'code') {
        genTableInitGetAPI({
          code: value,
          dataSource: this.fieldForm.dataSource
        }).then(res => {
          this.dataFieldList.map(fields => {
            fields.map(field => {
              if (!['code', 'dataSource'].includes(field.fieldCode)) {
                this.$set(this.fieldForm, field.fieldCode, res.data[field.fieldCode])
              }
            })
          })
        })
      }
      if (item && item.fieldCode === 'dataSource') {
        genTableNameListAPI({ dataSource: value }).then(res => {
          this.dataFieldList.map(fields => {
            fields.map(field => {
              if (field.fieldCode === 'code') {
                field.setting = res.data
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
