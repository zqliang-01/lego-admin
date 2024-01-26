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
  dataSourceAddAPI,
  dataSourceUpdateAPI
} from '@/api/admin/sharding/dataSource'
import CreateMixin from '@/components/Mixins/LegoCreate'

export default {
  name: 'ShardingDataSourceCreate',
  mixins: [CreateMixin],
  computed: {
    title() {
      return this.action.type === 'update' ? '编辑数据源' : '新建数据源'
    }
  },
  data() {
    return {
      addRequest: dataSourceAddAPI,
      updateRequest: dataSourceUpdateAPI
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
  }
}
</script>

<style lang="scss" scoped>
</style>
