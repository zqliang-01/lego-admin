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
  algorithmAddAPI,
  algorithmUpdateAPI
} from '@/api/admin/sharding/algorithm'
import CreateMixin from '@/components/Mixins/LegoCreate'

export default {
  name: 'ShardingAlgorithmCreate',
  mixins: [CreateMixin],
  computed: {
    title() {
      return this.action.type === 'update' ? '编辑分片算法' : '新建分片算法'
    }
  },
  data() {
    return {
      addRequest: algorithmAddAPI,
      updateRequest: algorithmUpdateAPI
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
