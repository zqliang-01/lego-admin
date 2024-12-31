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
      <lego-create-sections title="基本信息">
        <form-items
          v-for="(children, index) in baseFieldList"
          :key="index"
          :field-form="fieldForm"
          :field-list="children"
        />
      </lego-create-sections>
      <lego-create-sections title="调度配置">
        <form-items
          v-for="(children, index) in scheduleFieldList"
          :key="index"
          :field-form="fieldForm"
          :field-list="children"
        />
      </lego-create-sections>
      <lego-create-sections title="任务配置">
        <form-items
          v-for="(children, index) in taskFieldList"
          :key="index"
          :field-form="fieldForm"
          :field-list="children"
        />
      </lego-create-sections>
      <lego-create-sections title="高级配置">
        <form-items
          v-for="(children, index) in advancedFiledList"
          :key="index"
          :field-form="fieldForm"
          :field-list="children"
        />
      </lego-create-sections>
    </el-form>
  </fade-view>
</template>

<script>
import {
  taskAddAPI,
  taskUpdateAPI,
  conditionListAPI
} from '@/api/job/task'
import CreateMixin from '@/components/Lego/mixins/LegoCreate'

export default {
  name: 'JobTaskCreate',
  mixins: [CreateMixin],
  props: {
    executorCode: [Number, String]
  },
  computed: {
    createTitle() {
      if (this.title) {
        return title
      }
      return this.action.type === 'update' ? '编辑任务' : '新建任务'
    },
    saveRequest() {
      return this.action.type === 'update' ? taskUpdateAPI : taskAddAPI
    },
    scheduleFieldList() {
      const fields = [
        { fieldCode: 'scheduleType', name: '调度类型', formType: 'select', required: true, setting: this.scheduleTypeList }
      ]
      if (this.fieldForm.scheduleType === 'CRON') {
        fields.push({ fieldCode: 'scheduleConf', name: 'Cron', formType: 'cronInput', required: true, tipType: 'tooltip', tips: 'cron执行表达式，格式：* * * * * ?' })
        return [fields]
      }
      if (this.fieldForm.scheduleType === 'FIX_RATE') {
        fields.push({ fieldCode: 'scheduleConf', name: '固定速度', formType: 'text', required: true, tipType: 'tooltip', tips: '固定时间间隔执行，单位：秒' })
        return [fields]
      }
      return [fields]
    },
    taskFieldList() {
      const fields = [
        [{ fieldCode: 'glueType', name: '运行模式', formType: 'select', required: true, unique: true, setting: this.glueTypeList, tipType: 'tooltip', tips: '执行器运行的程序，非BEAN模式则需编写脚本' }],
        [{ fieldCode: 'executorParam', name: '任务参数', formType: 'textarea', stylePercent: 100 }]
      ]
      if (this.fieldForm.glueType === 'BEAN') {
        fields[0].push({ fieldCode: 'executorHandler', name: 'JobHandle', formType: 'text', required: true, tipType: 'tooltip', tips: '执行器运行的程序BEAN，填写程序中定义的BEAN名称' })
        return fields
      }
      return fields
    }
  },
  watch: {
    visible(val) {
      if (val) {
        this.init()
      }
    },
    scheduleFieldList: {
      handler() {
        this.refreshRule()
      },
      deep: true,
      immediate: true
    },
    taskFieldList: {
      handler() {
        this.refreshRule()
      },
      deep: true,
      immediate: true
    }
  },
  data() {
    return {
      scheduleTypeList: [],
      glueTypeList: [],
      baseFieldList: [
        [
          { fieldCode: 'jobGroup', name: '执行器', formType: 'select', required: true, tipType: 'tooltip', tips: '调度执行的机器集群' },
          { fieldCode: 'jobDesc', name: '任务描述', formType: 'text', required: true }
        ],
        [
          { fieldCode: 'author', name: '负责人', formType: 'user', required: true, tipType: 'tooltip', tips: '任务负责人，异常任务推送站内信的收件人' },
          { fieldCode: 'alarmEmail', name: '报警邮件', formType: 'text', tipType: 'tooltip', tips: '填写邮箱地址，多个邮箱用逗号隔开' }
        ]
      ],
      advancedFiledList: [
        [
          { fieldCode: 'executorRouteStrategy', name: '路由策略', formType: 'select', required: true, tipType: 'tooltip', tips: '具体路由策略说明参考xxl-job中的定义说明' },
          { fieldCode: 'childJobId', name: '子任务ID', formType: 'text' }
        ],
        [
          { fieldCode: 'misfireStrategy', name: '调度过期策略', formType: 'select', required: true },
          { fieldCode: 'executorBlockStrategy', name: '阻塞处理策略', formType: 'select', required: true, tipType: 'tooltip', tips: '具体调度过期策略说明参考xxl-job中的定义说明' }
        ],
        [
          { fieldCode: 'executorTimeout', name: '任务超时时间', formType: 'text', tipType: 'tooltip', tips: '任务调度超过指定时间后超时，单位：秒' },
          { fieldCode: 'executorFailRetryCount', name: '失败重试次数', formType: 'number' }
        ]
      ]
    }
  },
  created() {
    this.dataFieldList = [...this.baseFieldList, ...this.scheduleFieldList, ...this.taskFieldList, ...this.advancedFiledList]
    conditionListAPI().then(res => {
      this.scheduleTypeList = res.data.scheduleTypes
      this.glueTypeList = res.data.glueTypes
      this.dataFieldList.forEach(fields => {
        fields.forEach(field => {
          if (field.fieldCode === 'jobGroup') {
            field.setting = res.data.jobGroupList.map(item => {
              return {
                code: item.id,
                name: item.title
              }
            })
          }
          if (field.fieldCode === 'executorRouteStrategy') {
            field.setting = res.data.executorRouteStrategies
          }
          if (field.fieldCode === 'misfireStrategy') {
            field.setting = res.data.misfireStrategies
          }
          if (field.fieldCode === 'executorBlockStrategy') {
            field.setting = res.data.executorBlockStrategies
          }
        })
      })
    })
  },
  methods: {
    init() {
      this.actionType = this.action.type
      this.detailData = this.action.detailData
      this.initValue()
      this.fieldForm.id = this.detailData.id
      this.$set(this.fieldForm, 'jobGroup', this.executorCode)
      this.$set(this.fieldForm, 'scheduleConf', this.detailData.scheduleConf)
      this.$set(this.fieldForm, 'executorHandler', this.detailData.executorHandler)
      this.dataFieldList.forEach(fields => {
        fields.forEach(field => {
          if ([
            'executorRouteStrategy', 'misfireStrategy', 'executorBlockStrategy', 'scheduleType', 'glueType'
          ].includes(field.fieldCode) && !this.fieldForm[field.fieldCode]) {
            this.$set(this.fieldForm, field.fieldCode, field.setting[0].code)
          }
        })
      })
    },
    refreshRule() {
      this.dataFieldList = [...this.baseFieldList, ...this.scheduleFieldList, ...this.taskFieldList, ...this.advancedFiledList]
      this.initRule()
      this.dataFieldList.forEach(fields => {
        fields.forEach(field => {
          if (field.fieldCode === 'author') {
            this.initSettingValue(field)
          }
          if (field.fieldCode === 'glueType') {
            this.$set(field, 'disabled', this.actionType === 'update')
          }
        })
      })
    }
  }
}
</script>

<style lang="scss" scoped>
</style>
