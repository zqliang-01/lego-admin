<template>
  <xr-create
    :loading="loading"
    :title="createTitle"
    @close="close"
    @save="saveClick">
    <create-sections title="基本信息">
      <el-form
        ref="createForm"
        v-if="!loading"
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
    </create-sections>
  </xr-create>
</template>

<script>
import CreateMixin from './mixins/LegoCreate'

export default {
  name: 'LegoCreate',
  mixins: [CreateMixin],
  computed: {
    createTitle() {
      if (this.title) {
        return this.title
      }
      return this.action.type === 'update' ? `编辑信息` : `新建记录`
    }
  },
  data() {
    return {
      addRequest: {},
      updateRequest: {}
    }
  },
  mounted() {
    this.initField().then(res => {
      this.initRequest(res.data.form)
      this.dataFieldList = res.data.fields
      this.detailData = this.action.detailData
      this.initValue()
    })
  }
}
</script>

<style lang="scss" scoped>
</style>
