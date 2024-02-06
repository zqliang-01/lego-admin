<template>
  <xr-create
    :loading="loading"
    :title="createTitle"
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
  modelAddAPI,
  modelUpdateAPI
} from '@/api/admin/workflow/model'
import CreateMixin from '@/components/Mixins/LegoCreate'

export default {
  name: 'GenTableCreate',
  mixins: [CreateMixin],
  props: {
  },
  computed: {
    createTitle() {
      if (this.title) {
        return title
      }
      return this.action.type === 'update' ? '编辑模型' : '新建模型'
    },
    saveRequest() {
      return this.action.type === 'update' ? modelUpdateAPI : modelAddAPI
    }
  },
  created() {
    this.dataFieldList = this.fieldList
    this.detailData = this.action.detailData
    if (this.action.detailData) {
      this.dataFieldList.map(fields => {
        fields.map(field => {
          this.$set(field, 'disabled', false)
          if (field.fieldCode === 'key' && this.action.type === 'update') {
            this.$set(field, 'disabled', true)
          }
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
