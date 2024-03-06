<template>
  <xr-create
    :loading="loading"
    :title="createTitle"
    confirm-button-text="通过"
    :showConfirm="!isView"
    @close="close"
    @save="saveClick">
    <create-sections
      v-if="dataFieldList.length > 0"
      title="基本信息">
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
          :disabled="isView"
        />
      </el-form>
    </create-sections>
    <create-sections title="审批信息">
      <el-form
        ref="otherFrom"
        :model="otherFieldFrom"
        :rules="otherFieldRules"
        :validate-on-rule-change="false"
        class="form"
        label-position="top">
        <form-items
          v-for="(children, index) in otherFieldList"
          :key="index"
          :field-from="otherFieldFrom"
          :field-list="children"
          :disabled="isView"
        />
      </el-form>
    </create-sections>
    <template
      v-if="!isView"
      slot="footer">
      <el-button
        type="warning"
        icon="el-icon-chat-line-square"
        @click.native="handleButton('delegate')">委派</el-button>
      <el-button
        type="success"
        icon="el-icon-thumb"
        @click.native="handleButton('transfer')">转办</el-button>
      <el-button
        type="danger"
        icon="el-icon-circle-close"
        @click.native="handleButton('reject')">拒绝</el-button>
    </template>
  </xr-create>
</template>

<script>
import {
  taskCompleteAPI,
  taskRejectAPI
} from '@/api/admin/workflow/task'
import CreateMixin from '@/components/Mixins/LegoCreate'

export default {
  name: 'TaskCompleteForm',
  mixins: [CreateMixin],
  props: {
    taskId: String,
    detailCode: String,
    comment: String
  },
  computed: {
    createTitle() {
      return this.title
    },
    isView() {
      return this.action.type === 'view'
    }
  },
  watch: {
    taskId() {
      this.init()
    }
  },
  data() {
    return {
      otherFieldFrom: {},
      otherFieldRules: {
        comment: [{ required: true, message: '审批意见不能为空', trigger: 'blur' }]
      },
      otherFieldList: [
        [
          { fieldCode: 'comment', name: '审批意见', formType: 'textarea', required: true, stylePercent: 100 }
        ]
      ],
      addRequest: {},
      updateRequest: {},
      detailRequest: {}
    }
  },
  created() {
    this.init()
  },
  methods: {
    init() {
      this.initField().then(fieldResponse => {
        this.$set(this.otherFieldFrom, 'comment', this.comment)
        if (fieldResponse && fieldResponse.data) {
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
        }
      })
    },
    doRequest() {
      this.$refs.otherFrom.validate(valid => {
        if (!valid) {
          return
        }
        this.loading = true
        this.otherFieldFrom.id = this.taskId
        this.otherFieldFrom.variables = this.fieldFrom
        taskCompleteAPI(this.otherFieldFrom).then(() => {
          this.loading = false
          this.$emit('handle', { type: 'save-success', msg: '任务已完工完成！' })
          this.close()
        }).catch(() => {
          this.loading = false
        })
      })
    },
    handleButton(type) {
      this.$refs.otherFrom.validate(valid => {
        if (!valid) {
          return
        }
        if (type !== 'reject') {
          this.$message.error('暂未实现该操作！')
          return
        }
        this.loading = true
        this.otherFieldFrom.id = this.taskId
        taskRejectAPI(this.otherFieldFrom).then(() => {
          this.loading = false
          this.$emit('handle', { type: 'reject-success', msg: '拒绝成功，任务回退到上一节点！' })
          this.close()
        }).catch(() => {
          this.loading = false
        })
      })
    }
  }
}
</script>

<style lang="scss" scoped>
</style>
