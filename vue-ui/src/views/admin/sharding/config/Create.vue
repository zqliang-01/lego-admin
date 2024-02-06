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
  configAddAPI,
  configUpdateAPI
} from '@/api/admin/sharding/config'
import CreateMixin from '@/components/Mixins/LegoCreate'

export default {
  name: 'ShardingConfigCreate',
  mixins: [CreateMixin],
  computed: {
    title() {
      return this.action.type === 'update' ? '编辑配置' : '新建配置'
    }
  },
  data() {
    return {
      addRequest: configAddAPI,
      updateRequest: configUpdateAPI
    }
  },
  created() {
    this.dataFieldList = this.fieldList
    this.detailData = this.action.detailData
    if (this.action.detailData) {
      this.dataFieldList.map(fields => {
        fields.map(field => {
          this.$set(field, 'disabled', false)
        })
      })
    }
    this.initValue()
  },
  methods: {
  }
}
</script>

<style lang="scss" scoped>
</style>
