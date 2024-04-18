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
  noticeAddAPI,
  noticeModifyAPI
} from '@/api/admin/noticeTemplate'
import CreateMixin from '@/components/lego/mixins/LegoCreate'
import LegoPopover from '@/components/lego/LegoPopover'

export default {
  name: 'NoticeCreate',
  mixins: [CreateMixin],
  components: {
    LegoPopover
  },
  computed: {
    createTitle() {
      return this.action.type === 'update' ? '编辑公告' : '新建公告'
    }
  },
  data() {
    return {
      addRequest: noticeAddAPI,
      updateRequest: noticeModifyAPI
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
      this.initValue()
    }
  }
}
</script>

<style lang="scss" scoped>
</style>
