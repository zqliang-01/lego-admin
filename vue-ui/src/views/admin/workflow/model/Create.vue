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
  modelAddAPI,
  modelUpdateAPI
} from '@/api/admin/workflow/model'
import CreateMixin from '@/components/lego/mixins/LegoCreate'

export default {
  name: 'WorkflowModelCreate',
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
            if (field.fieldCode === 'key' && this.action.type === 'update') {
              this.$set(field, 'disabled', true)
            }
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
