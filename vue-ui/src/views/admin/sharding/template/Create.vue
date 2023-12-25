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
      />
    </el-form>
  </xr-create>
</template>

<script>
import {
  templateAddAPI,
  templateUpdateAPI
} from '@/api/admin/sharding/template'
import CreateMixin from '@/components/Mixins/LegoCreate'

export default {
  name: 'ShardingTemplateCreate',
  mixins: [CreateMixin],
  computed: {
    title() {
      return this.action.type === 'update' ? '编辑模板' : '新建模板'
    },
    saveRequest() {
      return this.action.type === 'update' ? templateUpdateAPI : templateAddAPI
    }
  },
  created() {
    if (this.action.detailData) {
      this.fieldList.map(field => {
        const value = this.action.detailData[field.fieldCode]
        field.value = value
      })
    }
    this.initField()
  },
  methods: {
  }
}
</script>

<style lang="scss" scoped>
</style>
