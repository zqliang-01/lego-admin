<template>
  <fade-view
    :visible.sync="visible"
    :loading="loading"
    :title="processName"
    confirm-button-text="发起流程"
    :show-confirm="action.type !== 'view' && hasAuth"
    @close="close"
    @save="saveClick">
    <div
      style="height: 100%;"
      v-empty="!hasAuth"
      xs-empty-icon="nopermission"
      xs-empty-text="无操作权限">
      <lego-create-sections
        v-if="dataFieldList.length > 0 && hasAuth"
        title="基本信息">
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
            :disabled="action.type === 'view'"
          />
        </el-form>
      </lego-create-sections>
    </div>
  </fade-view>
</template>

<script>
import { definitionStartAPI } from '@/api/admin/workflow/definition'
import CreateMixin from '@/components/Lego/mixins/LegoCreate'
import UserSelect from '@/components/Common/UserSelect'
import { getMenuAuth } from '@/utils/auth'

export default {
  name: 'StartDetail',
  mixins: [CreateMixin],
  components: {
    UserSelect
  },
  props: {
    processName: {
      type: String,
      default: ''
    },
    definitionId: {
      type: String,
      default: ''
    },
    detailCode: {
      type: String,
      default: ''
    }
  },
  computed: {
    hasAuth() {
      if (this.auth.detail && this.actionType === 'view') {
        return true
      }
      if (this.auth.update && this.actionType === 'update') {
        return true
      }
      if (this.auth.add && this.actionType === 'save') {
        return true
      }
      return false
    }
  },
  watch: {
    visible(val) {
      if (val && this.formCode) {
        this.init()
      }
    }
  },
  data() {
    return {
      auth: {},
      addRequest: {},
      updateRequest: {},
      detailRequest: {}
    }
  },
  mounted() {
    if (this.formCode) {
      this.init()
    }
  },
  methods: {
    init() {
      this.initField().then(res => {
        this.initRequest(res.data.form)
        this.dataFieldList = res.data.fields
        this.auth = getMenuAuth(res.data.form.permissionCode)
        if (this.detailCode && this.auth.detail) {
          this.detailRequest(this.detailCode).then(res => {
            this.detailData = res.data
            this.initValue()
          })
        } else {
          this.initValue()
        }
      })
    },
    doRequest() {
      this.loading = true
      definitionStartAPI({
        formKey: this.formCode,
        definitionId: this.definitionId,
        variables: this.fieldForm
      }).then(() => {
        this.loading = false
        this.$message.success('审批发起成功，请到“我的流程”中查看发起的审批！')
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
