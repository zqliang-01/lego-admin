<template>
  <xr-create
    :loading="loading"
    :title="createTitle"
    :showConfirm="action.type !== 'view'"
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
  taskCompleteAPI
} from '@/api/admin/workflow/task'
import CreateMixin from '@/components/Mixins/LegoCreate'

export default {
  name: 'TaskCompleteForm',
  mixins: [CreateMixin],
  props: {
    taskId: String,
    detailCode: String
  },
  computed: {
    createTitle() {
      return this.title
    }
  },
  data() {
    return {
      addRequest: {},
      updateRequest: {},
      detailRequest: {}
    }
  },
  created() {
    this.initField().then(fieldResponse => {
      this.initRequest(fieldResponse.data.form)
      if (this.detailCode) {
        this.detailRequest(this.detailCode).then(res => {
          this.dataFieldList = fieldResponse.data.fields
          this.detailData = res.data
          this.initValue()
        })
      } else {
        this.dataFieldList = fieldResponse.data.fields
        this.initValue()
      }
    })
  },
  methods: {
    doRequest() {
      this.loading = true
      taskCompleteAPI({
        id: this.taskId,
        variables: this.fieldFrom
      }).then(() => {
        this.loading = false
        this.$emit('handle', { type: 'save-success' })
        this.close()
      }).catch(() => {
        this.loading = false
      })
    }
  }
}
</script>

<style lang="scss" scoped>
</style>
