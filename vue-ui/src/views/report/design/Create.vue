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
        @change="handleChangeValue"
      />
    </el-form>
    <template
      v-if="action.type === 'update'"
      slot="footer">
      <el-button
        type="danger"
        icon="el-icon-delete"
        @click.native="handleDelete">删除</el-button>
    </template>
  </fade-view>
</template>

<script>
import {
  designGetAPI,
  designAddAPI,
  designModifyAPI,
  designDeleteAPI
} from '@/api/report/design'
import {
  definitionGetAPI,
  definitionSimpleListAPI
} from '@/api/report/definition'
import CreateMixin from '@/components/Lego/mixins/LegoCreate'

const allCharts = require.context('../../home/charts', true, /\.vue$/)
const res_charts = []
allCharts.keys().forEach((item) => {
  const comp = allCharts(item)
  const code = comp.default.name
  const name = comp.default.data().prop.title
  res_charts.push({ code: code, name: name })
})

export default {
  name: 'ReportDesignCreate',
  mixins: [CreateMixin],
  computed: {
    createTitle() {
      if (this.action.position === 'top') {
        return this.action.type === 'update' ? '编辑顶部数据源' : '新建顶部数据源'
      }
      return this.action.type === 'update' ? '编辑数据源' : '新建数据源'
    }
  },
  data() {
    return {
      definitionList: [],
      definitionTitleList: [],
      addRequest: designAddAPI,
      updateRequest: designModifyAPI
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
    async init() {
      this.loading = true
      this.actionType = this.action.type
      await definitionSimpleListAPI({ type: 'dashBroad' }).then(res => {
        this.definitionList = res.data
      })
      if (this.actionType === 'update') {
        await designGetAPI(this.action.detailCode).then(res => {
          this.detailData = res.data
        })
      }
      if (this.detailData.definition && this.detailData.definition.code) {
        await definitionGetAPI(this.detailData.definition.code).then(res => {
          this.definitionTitleList = res.data.titles.map(title => {
            return {
              code: title.sqlKey,
              name: title.name
            }
          })
        })
      }
      this.dataFieldList = this.buildFieldList()
      this.initValue()
      this.fieldForm.position = this.action.position
      this.loading = false
    },
    buildFieldList() {
      if (this.action.position === 'top') {
        return [
          [
            { fieldCode: 'name', name: '名称', formType: 'text', required: true },
            { fieldCode: 'definition', name: '报表定义', formType: 'select', required: true, clearable: true, setting: this.definitionList },
            { fieldCode: 'sn', name: '序号', formType: 'number', required: true },
            { fieldCode: 'enable', name: '是否生效', formType: 'boolean_value' }
          ]
        ]
      }
      return [
        [
          { fieldCode: 'name', name: '名称', formType: 'text', required: true },
          { fieldCode: 'definition', name: '报表定义', formType: 'select', required: true, clearable: true, setting: this.definitionList },
          { fieldCode: 'chartType', name: '图表类型', formType: 'select', required: true, setting: res_charts },
          { fieldCode: 'dataCategories', name: '数据值字段', formType: 'checkbox', required: true, setting: this.definitionTitleList },
          { fieldCode: 'dataDimension', name: '维度字段', formType: 'select', required: true, setting: this.definitionTitleList },
          { fieldCode: 'sn', name: '序号', formType: 'number', required: true },
          { fieldCode: 'enable', name: '是否生效', formType: 'boolean_value' }
        ]
      ]
    },
    handleChangeValue(field, index, value) {
      if (field.fieldCode === 'definition' && value) {
        definitionGetAPI(value).then(res => {
          this.definitionTitleList = res.data.titles.map(title => {
            return {
              code: title.sqlKey,
              name: title.name
            }
          })
          this.dataFieldList.forEach(fields => {
            fields.forEach(field => {
              if (['dataCategories', 'dataDimension'].includes(field.fieldCode)) {
                field.setting = this.definitionTitleList
              }
            })
          })
        })
      }
    },
    handleDelete() {
      this.loading = true
      designDeleteAPI({ code: this.detailData.code }).then(() => {
        this.loading = false
        this.$message.success('删除成功')
        this.close()
        this.$emit('handle', { type: 'save-success' })
      }).catch(() => {
        this.loading = false
      })
    }
  }
}
</script>

<style lang="scss" scoped>
</style>
