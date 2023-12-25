<template>
  <xr-create
    :loading="loading"
    :title="title"
    @close="close"
    @save="saveClick">
    <create-sections title="基本信息">
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
        />
      </el-form>
    </create-sections>
  </xr-create>
</template>

<script>
import {
  customerAddAPI,
  customerUpdateAPI
} from '@/api/crm/customer'
import CreateMixin from '@/components/Mixins/LegoCreate'

export default {
  name: 'CrmCustomerCreate',
  mixins: [CreateMixin],
  computed: {
    title() {
      return this.action.type === 'update' ? '编辑客户' : '新建客户'
    },
    saveRequest() {
      return this.action.type === 'update' ? customerUpdateAPI : customerAddAPI
    }
  },
  created() {
    this.initField()
  }
}
</script>

<style lang="scss" scoped>
</style>
