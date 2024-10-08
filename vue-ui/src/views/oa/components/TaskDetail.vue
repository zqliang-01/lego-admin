<template>
  <fade-view
    :visible.sync="visible"
    :loading="loading"
    :title="taskName"
    :show-cancel="isView || !hasAuth"
    :show-confirm="!isView && hasAuth"
    confirm-button-text="审核通过"
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
            :disabled="isView"
          />
        </el-form>
      </lego-create-sections>
      <lego-create-sections title="审批信息">
        <el-form
          v-if="!isView"
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
        <task-comment
          :isView="isView"
          :commentList="commentList"/>
      </lego-create-sections>
    </div>
    <template
      v-if="!isView && hasAuth"
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
        v-if="dataFieldList.length > 0"
        plain
        type="primary"
        icon="el-icon-paperclip"
        @click.native="handleSave">保存草稿</el-button>
    </template>
    <user-select
      required
      :visible.sync="showSelectEmployee"
      @selected="handleEmployeeSelect"/>
  </fade-view>
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
import CreateMixin from '@/components/Lego/mixins/LegoCreate'
import UserSelect from '@/components/Common/UserSelect'
import TaskComment from './TaskComment'
import { showFormErrorMessage } from '@/components/Common/Form/utils'
import { getMenuAuth } from '@/utils/auth'

export default {
  name: 'TaskDetail',
  mixins: [CreateMixin],
  components: {
    UserSelect,
    TaskComment
  },
  props: {
    taskId: String
  },
  computed: {
    isView() {
      return this.actionType === 'view'
    },
    hasAuth() {
      if (this.dataFieldList.length === 0) {
        return true
      }
      if (this.auth.detail && this.isView) {
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
      if (val) {
        this.init()
      }
    }
  },
  data() {
    return {
      auth: {},
      taskName: '',
      selectUser: [],
      commentList: [],
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
    if (this.taskId) {
      this.init()
    }
  },
  methods: {
    init() {
      this.fieldForm = {}
      this.detailData = {}
      this.dataFieldList = []
      this.actionType = this.action.type
      this.$set(this.otherFieldForm, 'comment', '')
      taskFormDetailGetAPI(this.taskId).then(taskResponse => {
        const task = taskResponse.data
        this.taskName = task.name
        this.commentList = task.comments
        this.actionType = task.finished ? 'view' : this.action.type
        if (task.formKey) {
          createFieldListAPI(task.formKey).then(res => {
            this.dataFieldList = res.data.fields
            this.auth = getMenuAuth(res.data.form.permissionCode)
            this.initRequest(res.data.form)
            if (task.code && this.hasAuth) {
              this.actionType = task.finished ? 'view' : 'update'
              this.detailRequest(task.code).then(res => {
                this.detailData = res.data
                this.initValue()
              })
            } else {
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
      this.loading = true
      this.otherFieldForm.id = this.taskId
      this.otherFieldForm.employeeCode = val
      if (this.handleType === 'delegate') {
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
