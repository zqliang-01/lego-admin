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
      />
    </el-form>
  </fade-view>
</template>

<script>
import {
  templateAddAPI,
  templateUpdateAPI
} from '@/api/admin/sharding/template'
import CreateMixin from '@/components/Lego/mixins/LegoCreate'

export default {
  name: 'ShardingTemplateCreate',
  mixins: [CreateMixin],
  computed: {
    createTitle() {
      return this.action.type === 'update' ? '编辑模板' : '新建模板'
    }
  },
  data() {
    return {
      addRequest: templateAddAPI,
      updateRequest: templateUpdateAPI
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
          })
        })
      }
      this.initValue()
    }
  }
}
</script>

<style lang="scss" scoped>
</style>
