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
  leadAddAPI,
  leadUpdateAPI
} from '@/api/crm/lead'
import CreateMixin from '@/components/Mixins/LegoCreate'

export default {
  name: 'CrmLeadCreate',
  mixins: [CreateMixin],
  computed: {
    title() {
      return this.action.type === 'update' ? '编辑线索' : '新建线索'
    },
    saveRequest() {
      return this.action.type === 'update' ? leadUpdateAPI : leadAddAPI
    }
  },
  created() {
    this.initField()
  }
}
</script>

<style lang="scss" scoped>
</style>
