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
  executorAddAPI,
  executorUpdateAPI
} from '@/api/job/executor'
import { objDeepCopy } from '@/utils'
import CreateMixin from '@/components/Lego/mixins/LegoCreate'

export default {
  name: 'JobExecutorCreate',
  mixins: [CreateMixin],
  computed: {
    createTitle() {
      return this.action.type === 'update' ? '编辑执行器' : '新建执行器'
    }
  },
  data() {
    return {
      addRequest: executorAddAPI,
      updateRequest: executorUpdateAPI
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
      this.fieldForm.id = this.detailData.id
      if (!this.fieldForm.addressType) {
        this.fieldForm.addressType = 0
      }
      this.updateAddressTypeInput(this.fieldForm.addressType === 0)
    },
    handleFieldChange(field, index, value) {
      if (field.fieldCode === 'addressType') {
        this.updateAddressTypeInput(value === 0)
        return
      }
    },
    updateAddressTypeInput(disabled) {
      this.dataFieldList.forEach(fields => {
        fields.forEach(field => {
          if (field.fieldCode === 'addressList') {
            this.$set(field, 'disabled', disabled)
            this.$set(field, 'required', !disabled)
          }
        })
      })
      this.initRule()
    }
  }
}
</script>

<style lang="scss" scoped>
</style>
