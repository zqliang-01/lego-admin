<template>
  <fade-view
    :visible.sync="visible"
    :loading="loading"
    :title="createTitle"
    @close="close"
    @save="saveClick">
    <lego-create-sections title="基本信息">
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
    </lego-create-sections>
  </fade-view>
</template>

<script>
import {
  leadAddAPI,
  leadUpdateAPI
} from '@/api/crm/lead'
import CreateMixin from '@/components/Lego/mixins/LegoCreate'

export default {
  name: 'CrmLeadCreate',
  mixins: [CreateMixin],
  computed: {
    createTitle() {
      return this.action.type === 'update' ? '编辑线索' : '新建线索'
    }
  },
  data() {
    return {
      addRequest: leadAddAPI,
      updateRequest: leadUpdateAPI
    }
  },
  mounted() {
    this.initField().then(res => {
      this.appCode = res.data.form.appCode
      this.dataFieldList = res.data.fields
      this.detailData = this.action.detailData
      this.initValue()
    })
  }
}
</script>

<style lang="scss" scoped>
</style>
