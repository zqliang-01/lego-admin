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
        @change="handleFieldChange"
      />
    </el-form>
  </xr-create>
</template>

<script>
import {
  customFormModifyAPI,
  customFormAddAPI,
  customFormInitGetAPI
} from '@/api/admin/formField'
import CreateMixin from '@/components/Mixins/LegoCreate'

export default {
  name: 'CustomFieldCreate',
  mixins: [CreateMixin],
  computed: {
    title() {
      return this.action.type === 'update' ? '编辑表单' : '新建表单'
    }
  },
  data() {
    return {
      addRequest: customFormAddAPI,
      updateRequest: customFormModifyAPI
    }
  },
  created() {
    if (this.action.detailData) {
      this.fieldList.map(field => {
        field.value = this.action.detailData[field.fieldCode]
      })
    }
    this.initField()
  },
  methods: {
    handleFieldChange(field, index, value) {
      if (field.fieldCode === 'table') {
        customFormInitGetAPI(value).then(res => {
          this.fieldList.map(field => {
            if (field.fieldCode !== 'table') {
              this.$set(this.fieldFrom, field.fieldCode, res.data[field.fieldCode])
            }
            if (field.fieldCode === 'enable') {
              this.$set(this.fieldFrom, field.fieldCode, true)
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
