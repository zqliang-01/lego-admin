<template>
  <xr-create
    :loading="loading"
    :title="taskName"
    confirm-button-text="通过"
    :showCancel="isView"
    :showConfirm="!isView"
    @close="close"
    @save="saveClick">
    <create-sections
      v-if="dataFieldList.length > 0"
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
          :disabled="isView"
        />
      </el-form>
    </create-sections>
    <create-sections title="审批信息">
      <el-form
        ref="otherFrom"
        :model="otherFieldForm"
        :rules="otherFieldRules"
        :validate-on-rule-change="false"
        class="form"
        label-position="top">
        <form-items
          v-for="(children, index) in otherFieldList"
          :key="index"
          :field-form="otherFieldForm"
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
      <el-button
        type="primary"
        icon="el-icon-paperclip"
        @click.native="handleSave">保存</el-button>
    </template>
    <user-select
      :visible.sync="showSelectEmployee"
      @selected="handleEmployeeSelect"/>
  </xr-create>
</template>

<script>
import {
  taskCompleteAPI,
  taskSaveAPI,
  taskRejectAPI,
  taskDelegateAPI,
  taskTransferAPI,
  taskFormDetailGetAPI
} from '@/api/admin/workflow/task'
import { createFieldListAPI } from '@/api/admin/formField'
import CreateMixin from '@/components/lego/mixins/LegoCreate'
import UserSelect from '@/components/NewCom/UserSelect'
import { showFormErrorMessage } from '@/components/NewCom/Form/utils'

export default {
  name: 'TaskDetail',
  mixins: [CreateMixin],
  components: { UserSelect },
  props: {
    taskId: String
  },
  computed: {
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
      taskName: '',
      selectUser: [],
      showSelectEmployee: false,
      otherFieldForm: {},
      otherFieldRules: {
        comment: [{ required: true, message: '审批意见不能为空', trigger: 'blur' }]
      },
      otherFieldList: [
        [
          { fieldCode: 'comment', name: '审批意见', formType: 'textarea', required: true, stylePercent: 100 }
        ]
      ],
      handleType: '',
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
      taskFormDetailGetAPI(this.taskId).then(taskResponse => {
        const task = taskResponse.data
        this.taskName = task.name
        this.$set(this.otherFieldForm, 'comment', task.comment)
        if (task.formKey) {
          createFieldListAPI(task.formKey).then(res => {
            this.dataFieldList = res.data.fields
            this.initRequest(res.data.form)
            if (task.code) {
              this.detailRequest(task.code).then(res => {
                this.actionType = 'update'
                this.detailData = res.data
                this.initValue()
              })
            } else if (!this.isView) {
              this.initValue()
            }
          })
        }
      })
    },
    doRequest() {
      this.$refs.otherFrom.validate(valid => {
        if (!valid) {
          showFormErrorMessage(this.$refs.otherFrom)
          return
        }
        this.loading = true
        this.otherFieldForm.id = this.taskId
        this.otherFieldForm.variables = this.fieldForm
        taskCompleteAPI(this.otherFieldForm).then(() => {
          this.loading = false
          this.$emit('handle', { type: 'save-success', msg: '任务已完工完成！' })
          this.close()
        }).catch(() => {
          this.loading = false
        })
      })
    },
    handleSave() {
      this.$refs.createForm.validate(valid => {
        if (!valid) {
          showFormErrorMessage(this.$refs.createForm)
          return
        }
        this.$refs.otherFrom.validate(validOther => {
          if (!validOther) {
            showFormErrorMessage(this.$refs.otherFrom)
            return
          }
          this.loading = true
          this.otherFieldForm.id = this.taskId
          this.otherFieldForm.variables = this.fieldForm
          taskSaveAPI(this.otherFieldForm).then(() => {
            this.loading = false
            this.actionType = 'update'
            this.$emit('handle', { type: 'save-success', msg: '任务保存！' })
          }).catch(() => {
            this.loading = false
          })
        })
      })
    },
    handleButton(type) {
      this.$refs.otherFrom.validate(valid => {
        if (!valid) {
          showFormErrorMessage(this.$refs.otherFrom)
          return
        }
        if (type !== 'reject') {
          this.showSelectEmployee = true
          this.handleType = type
          return
        }
        this.$confirm('此操作将拒绝任务【' + this.taskName + '】并回退到上一审批节点，是否继续?', '提示', {
          type: 'warning'
        }).then(() => {
          this.loading = true
          this.otherFieldForm.id = this.taskId
          taskRejectAPI(this.otherFieldForm).then(() => {
            this.loading = false
            this.$emit('handle', { type: 'reject-success', msg: '拒绝成功，任务回退到上一节点！' })
            this.close()
          }).catch(() => {
            this.loading = false
          })
        })
      })
    },
    handleEmployeeSelect(val) {
      if (this.handleType === 'delegate') {
        this.loading = true
        this.otherFieldForm.id = this.taskId
        this.otherFieldForm.employeeCode = val
        taskDelegateAPI(this.otherFieldForm).then(() => {
          this.loading = false
          this.$emit('handle', { type: 'delegate-success', msg: '任务委派成功！' })
          this.close()
        }).catch(() => {
          this.loading = false
        })
        return
      }
      if (this.handleType === 'transfer') {
        this.loading = true
        this.otherFieldForm.id = this.taskId
        this.otherFieldForm.employeeCode = val
        taskTransferAPI(this.otherFieldForm).then(() => {
          this.loading = false
          this.$emit('handle', { type: 'delegate-success', msg: '任务转办成功！' })
          this.close()
        }).catch(() => {
          this.loading = false
        })
        return
      }
    }
  }
}
</script>

<style lang="scss" scoped>
</style>
