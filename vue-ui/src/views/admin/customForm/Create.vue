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
        @change="handleFieldChange"
      />
    </el-form>
  </fade-view>
</template>

<script>
import {
  customFormModifyAPI,
  customFormAddAPI,
  customFormInitGetAPI
} from '@/api/admin/formField'
import CreateMixin from '@/components/Lego/mixins/LegoCreate'

export default {
  name: 'CustomFieldCreate',
  mixins: [CreateMixin],
  computed: {
    createTitle() {
      if (this.title) {
        return title
      }
      return this.action.type === 'update' ? '编辑表单' : '新建表单'
    }
  },
  data() {
    return {
      addRequest: customFormAddAPI,
      updateRequest: customFormModifyAPI
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
      if (this.action.detailData) {
        this.dataFieldList.map(fields => {
          fields.map(field => {
            this.$set(field, 'disabled', false)
            if (field.fieldCode === 'table' && this.action.type === 'update') {
              this.$set(field, 'disabled', true)
            }
          })
        })
      }
      this.initValue()
    },
    handleFieldChange(field, index, value) {
      if (field.fieldCode === 'table') {
        customFormInitGetAPI(value).then(res => {
          this.fieldList.map(fields => {
            fields.map(field => {
              if (field.fieldCode !== 'table') {
                this.$set(this.fieldForm, field.fieldCode, res.data[field.fieldCode])
              }
              if (field.fieldCode === 'enable') {
                this.$set(this.fieldForm, field.fieldCode, true)
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
