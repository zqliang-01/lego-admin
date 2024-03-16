<template>
  <el-collapse-item name="element-user-task">
    <template #title>
      <collapse-title title="设置审批人">
        <lucide-icon name="user-round" />
        <i :class="'icon-my' | iconPre" />
      </collapse-title>
    </template>
    <div class="element-user-task">
      <el-row>
        <h4><b>审批人设置</b></h4>
        <el-radio-group v-model="taskForm.dataType" @change="changeDataType">
          <el-radio label="USERS">指定用户</el-radio>
          <el-radio label="ROLES">角色</el-radio>
          <el-radio label="DEPTS">部门</el-radio>
          <el-radio label="INITIATOR">发起人</el-radio>
        </el-radio-group>
      </el-row>
      <el-row class="data-value">
        <div v-if="taskForm.dataType === 'USERS'">
          <el-tag v-for="userName in selectedUser.names" :key="userName" effect="plain">
            {{ userName }}
          </el-tag>
          <div class="element-drawer__button">
            <el-select
              v-model="selectedUser.codes"
              multiple
              filterable
              placeholder="请选择 用户"
              style="width: 100%;"
              @change="changeSelectUser">
              <el-option
                v-for="optionItem in userOptions"
                :key="optionItem.code"
                :label="optionItem.name"
                :value="optionItem.code" />
            </el-select>
          </div>
        </div>
        <div v-if="taskForm.dataType === 'ROLES'">
          <el-select
            v-model="selectedRoles"
            multiple
            placeholder="请选择 角色"
            style="width: 100%;"
            @change="changeSelectRoles">
            <el-option
              v-for="item in roleOptions"
              :key="item.code"
              :label="item.name"
              :value="item.code"
              :disabled="item.status === 1"
            />
          </el-select>
        </div>
        <div v-if="taskForm.dataType === 'DEPTS'">
          <select-tree
            v-model="selectedDept"
            :options="deptOptions"
            filterable
            placeholder="请选择 部门"
            @change="checkedDeptChange"
          />
        </div>
      </el-row>
      <el-row>
        <div v-show="showMultiFlog">
          <el-divider />
          <h4><b>多实例审批方式</b></h4>
          <el-row>
            <el-radio-group v-model="multiLoopType" @change="changeMultiLoopType()">
              <el-row><el-radio label="Null">无</el-radio></el-row>
              <el-row><el-radio label="SequentialMultiInstance">会签（需所有审批人同意）</el-radio></el-row>
              <el-row><el-radio label="ParallelMultiInstance">或签（一名审批人同意即可）</el-radio></el-row>
            </el-radio-group>
          </el-row>
          <el-row v-if="multiLoopType !== 'Null'">
            <el-tooltip content="开启后，实例需按顺序轮流审批" placement="top-start" @click.stop.prevent>
              <i class="header-icon el-icon-info" />
            </el-tooltip>
            <span class="custom-label">顺序审批：</span>
            <el-switch v-model="isSequential" @change="changeMultiLoopType()" />
          </el-row>
        </div>
      </el-row>
    </div>
  </el-collapse-item>
</template>

<script>
import LucideIcon from '../../common/LucideIcon'
import CollapseTitle from '../../common/CollapseTitle'
import SelectTree from '@/components/NewCom/SelectTree'
import EventEmitter from '@/utils/bpmn/EventEmitter'
import { depSimpleListAPI } from '@/api/admin/dept'
import { employeeSimpleListAPI } from '@/api/admin/employee'
import { roleSimpleListAPI } from '@/api/admin/role'
import { getActive } from '../../../bpmn-utils/BpmnDesignerUtils'
import { getBusinessObject } from 'bpmn-js/lib/util/ModelUtil'
import { debounce } from 'min-dash'
import { setProperty, setModdleProperty, createElement } from '../../../bo-utils/userTaskUtil'

export default {
  name: 'ElementUserTask',
  components: {
    SelectTree,
    CollapseTitle,
    LucideIcon
  },
  data() {
    return {
      showMultiFlog: false,
      isSequential: false,
      multiLoopType: 'Null',
      deptOptions: [],
      selectedDept: '',
      userOptions: [],
      selectedUser: {
        names: [],
        codes: []
      },
      roleOptions: [],
      selectedRoles: [],
      taskForm: {
        dataType: '',
        assignee: '',
        candidateUsers: '',
        candidateGroups: '',
        text: ''
      }
    }
  },
  mounted() {
    this.init()
    EventEmitter.on('element-update', () => {
      this.init()
    })
  },
  methods: {
    init: debounce(function() {
      const elementObj = getBusinessObject(getActive())
      const dataType = elementObj.get('dataType')
      this.showMultiFlog = ['ROLES', 'DEPTS'].includes(dataType)
      this.selectedDept = ''
      this.selectedUser = []
      this.selectedRoles = []

      if (dataType === 'USERS') {
        employeeSimpleListAPI().then(res => {
          this.userOptions = res.data
          const userCodes = elementObj.get('candidateUsers') || elementObj.get('assignee')
          const userText = elementObj.get('text') || ''
          if (userText) {
            this.selectedUser.names = userText.split(',')
          }
          if (userCodes && userCodes.toString().length > 0) {
            this.selectedUser.codes = userCodes.toString().split(',')
          }
          if (this.selectedUser.codes.length > 1) {
            this.showMultiFlog = true
          }
        })
      }
      if (dataType === 'ROLES') {
        roleSimpleListAPI().then(res => {
          this.roleOptions = res.data
          const roleCodes = elementObj.get('candidateGroups') || ''
          if (roleCodes && roleCodes.length > 0) {
            this.selectedRoles = roleCodes.split(',')
          }
          this.showMultiFlog = true
        })
      }
      if (dataType === 'DEPTS') {
        depSimpleListAPI().then(res => {
          this.deptOptions = res.data
          this.selectedDept = elementObj.get('candidateGroups') || ''
          this.showMultiFlog = true
        })
      }
      this.taskForm.dataType = dataType
      this.setElementLoop()
    }, 100),
    setElementLoop() {
      const elementObj = getBusinessObject(getActive())
      if (!elementObj.loopCharacteristics) {
        this.multiLoopType = 'Null'
        return
      }
      this.isSequential = elementObj.loopCharacteristics.isSequential
      if (elementObj.loopCharacteristics.completionCondition) {
        if (elementObj.loopCharacteristics.completionCondition.body === '${nrOfCompletedInstances >= nrOfInstances}') {
          this.multiLoopType = 'SequentialMultiInstance'
        } else {
          this.multiLoopType = 'ParallelMultiInstance'
        }
      }
    },
    changeDataType(dataType) {
      if (this.multiLoopType !== 'Null') {
        this.multiLoopType = 'Null'
        this.changeMultiLoopType()
      }
      if (dataType === 'USERS') {
        employeeSimpleListAPI().then(res => {
          this.userOptions = res.data
          this.resetTaskForm(dataType)
        })
      }
      if (dataType === 'ROLES') {
        roleSimpleListAPI().then(res => {
          this.roleOptions = res.data
          this.resetTaskForm(dataType)
        })
      }
      if (dataType === 'DEPTS') {
        depSimpleListAPI().then(res => {
          this.deptOptions = res.data
          this.resetTaskForm(dataType)
        })
      }
      if (dataType === 'INITIATOR') {
        this.resetTaskForm(dataType)
      }
    },
    resetTaskForm(dataType) {
      Object.keys(this.taskForm).forEach(key => {
        if (key !== 'dataType') {
          this.taskForm[key] = null
        }
      })
      this.showMultiFlog = ['ROLES', 'DEPTS'].includes(dataType) || (dataType === 'USERS' && this.selectedUser.codes.length > 1)
      if (this.showMultiFlog && this.multiLoopType !== 'Null') {
        this.taskForm.assignee = '${assignee}'
      }
      if (dataType === 'USERS') {
        if (this.selectedUser && this.selectedUser.codes && this.selectedUser.codes.length > 0) {
          if (this.selectedUser.codes.length === 1) {
            this.taskForm.assignee = this.selectedUser.codes[0]
          } else {
            this.taskForm.candidateUsers = this.selectedUser.codes.join()
          }
          if (this.selectedUser.names) {
            this.taskForm.text = this.selectedUser.names.join()
          }
        }
      }
      if (dataType === 'ROLES') {
        if (this.selectedRoles && this.selectedRoles.length > 0) {
          this.taskForm.candidateGroups = this.selectedRoles.join() || null
          const textArr = this.roleOptions.filter(k => this.selectedRoles.indexOf(`${k.code}`) >= 0)
          if (textArr) {
            this.taskForm.text = textArr.map(k => k.name).join()
          }
        }
      }
      if (dataType === 'DEPTS') {
        if (this.selectedDept) {
          this.taskForm.candidateGroups = this.selectedDept
        }
      }
      if (dataType === 'INITIATOR') {
        this.taskForm.assignee = '${initiator}'
        this.taskForm.text = '流程发起人'
      }
      if (dataType) {
        setProperty(getActive(), this.taskForm)
      }
    },
    changeSelectUser() {
      this.resetTaskForm(this.taskForm.dataType)
    },
    changeSelectRoles() {
      this.resetTaskForm(this.taskForm.dataType)
    },
    checkedDeptChange() {
      this.resetTaskForm(this.taskForm.dataType)
    },
    changeMultiLoopType() {
      const elementObj = getActive()
      // 取消多实例配置
      if (this.multiLoopType === 'Null') {
        setProperty(elementObj, { loopCharacteristics: null, assignee: null })
        return
      }
      const multiLoopInstance = createElement(elementObj, 'bpmn:MultiInstanceLoopCharacteristics', {
        isSequential: this.isSequential
      })
      // 更新多实例配置
      setProperty(elementObj, { loopCharacteristics: multiLoopInstance, assignee: '${assignee}' })
      // 完成条件
      let completionCondition = null
      // 会签
      if (this.multiLoopType === 'SequentialMultiInstance') {
        completionCondition = createElement(multiLoopInstance, 'bpmn:FormalExpression', { body: '${nrOfCompletedInstances >= nrOfInstances}' })
      }
      // 或签
      if (this.multiLoopType === 'ParallelMultiInstance') {
        completionCondition = createElement(multiLoopInstance, 'bpmn:FormalExpression', { body: '${nrOfCompletedInstances > 0}' })
      }
      setModdleProperty(elementObj, multiLoopInstance, {
        collection: '${multiInstanceHandler.getUserIds(execution)}',
        elementVariable: 'assignee',
        completionCondition
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.element-user-task {
  padding: 5px 15px;
  font-size: 15px;
}
.data-value {
  margin-top: 10px;
}
.el-row .el-radio-group {
  .el-radio {
    line-height: 28px;
    margin-right: 10px;
  }
}
.el-tag {
  margin-bottom: 10px;
  + .el-tag {
    margin-left: 10px;
  }
}
</style>
