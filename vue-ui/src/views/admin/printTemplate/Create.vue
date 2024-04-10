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
      />
    </el-form>
  </fade-view>
</template>

<script>
import {
  printTemplateAddAPI,
  printTemplateModifyAPI
} from '@/api/admin/printTemplate'
import { customFormSimpleListAPI } from '@/api/admin/formField'
import CreateMixin from '@/components/lego/mixins/LegoCreate'

export default {
  name: 'PrintTemplateCreate',
  mixins: [CreateMixin],
  computed: {
    createTitle() {
      return this.action.type === 'update' ? '编辑模板' : '新建模板'
    }
  },
  data() {
    return {
      addRequest: printTemplateAddAPI,
      updateRequest: printTemplateModifyAPI
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
      this.dataFieldList = this.fieldList
      this.detailData = this.action.detailData
      this.dataFieldList.map(fields => {
        fields.map(field => {
          if (field.fieldCode === 'form') {
            customFormSimpleListAPI().then(res => {
              field.setting = res.data
            })
          }
        })
      })
      this.initValue()
    }
  }
}
</script>

<style lang="scss" scoped>
</style>
