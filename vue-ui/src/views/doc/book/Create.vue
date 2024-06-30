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
  bookAddAPI,
  bookModifyAPI
} from '@/api/doc/book'
import CreateMixin from '@/components/lego/mixins/LegoCreate'
import { objDeepCopy } from '@/utils'

export default {
  name: 'DocBookCreate',
  mixins: [CreateMixin],
  computed: {
    createTitle() {
      if (this.title) {
        return title
      }
      return this.action.type === 'update' ? '编辑知识库' : '新建知识库'
    },
    saveRequest() {
      return this.action.type === 'update' ? bookModifyAPI : bookAddAPI
    }
  },
  watch: {
    visible(val) {
      if (val) {
        this.init()
      }
    }
  },
  methods: {
    init() {
      this.actionType = this.action.type
      this.detailData = this.action.detailData
      this.dataFieldList = objDeepCopy(this.fieldList)
      this.initValue()
      this.fieldForm.code = this.detailData.code
    }
  }
}
</script>

<style lang="scss" scoped>
</style>
