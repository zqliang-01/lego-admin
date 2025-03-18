import { detailFieldListAPI } from '@/api/admin/formField'
import { printTemplateSimpleListAPI } from '@/api/admin/printTemplate'

import LegoCommonMixin from './LegoCommon'
import ImportInfo from '@/components/ImportInfo'
import SlideView from '@/components/Layout/SlideView'
import LegoDetailHead from '@/components/Lego/LegoDetailHead'
import LegoEditBaseInfo from '@/components/Lego/LegoEditBaseInfo'

export default {
  components: {
    SlideView,
    LegoDetailHead,
    LegoEditBaseInfo,
    ImportInfo
  },
  mixins: [LegoCommonMixin],
  data() {
    return {
      loading: false,
      menuCode: '',
      detailData: null,
      relativeEntity: {
        show: false
      },
      isCreate: false,
      importList: [],
      fieldList: [],
      formCode: '',
      detailCode: '',
      showImportInfo: false,
      tabCurrentName: 'LegoEditBaseInfo',
      headFieldList: [
        { fieldCode: 'code', name: '编码', formType: 'text' },
        { fieldCode: 'creator', name: '创建人', formType: 'user' },
        { fieldCode: 'createTime', name: '创建时间', formType: 'text' },
        { fieldCode: 'updateTime', name: '更新时间', formType: 'text' }
      ],
      systemFieldList: [
        { fieldCode: 'createTime', name: '创建时间', formType: 'text' },
        { fieldCode: 'updateTime', name: '更新时间', formType: 'text' },
        { fieldCode: 'creator', name: '创建人', formType: 'user' }
      ]
    }
  },
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    auth: {
      type: Object,
      default: function() {
        return {}
      }
    },
    detailForm: {
      type: Object,
      default: function() {
        return {}
      }
    },
    pageCodes: {
      type: Array,
      default: function() {
        return []
      }
    }
  },
  watch: {
    visible: {
      handler: function(newVal, oldVal) {
        if (this.visible) {
          this.detailCode = this.detailForm.detailCode
          if (!this.formCode || this.formCode !== this.detailForm.formCode) {
            this.formCode = this.detailForm.formCode
            this.initFieldList()
          } else {
            this.getDetail()
          }
        }
      },
      immediate: true
    }
  },
  methods: {
    /**
     * 详情
     */
    getDetail() {
      if (!this.auth.detail || typeof this.detailRequest !== 'function') {
        return
      }
      if (this.detailData && this.detailData.code === this.detailCode) {
        return
      }
      this.loading = true
      this.detailRequest(this.detailCode).then(res => {
        this.detailData = res.data
        this.headFieldList.map(field => {
          this.$set(field, 'value', this.detailData[field.fieldCode])
        })
        this.fieldList.map(field => {
          this.$set(field, 'isEdit', false)
          this.$set(field, 'value', this.detailData[field.fieldCode])
        })
        this.systemFieldList.map(field => {
          this.$set(field, 'value', this.detailData[field.fieldCode])
        })
        this.loading = false
      }).catch(() => {
        this.loading = false
        this.hideView()
      })
    },
    /**
     * 获取重要信息
     */
    getImportInfo() {
      this.loading = true
    },

    initFieldList() {
      if (this.formCode) {
        this.loading = true
        detailFieldListAPI(this.formCode).then(res => {
          this.menuCode = res.data.form.permissionCode
          this.initRequest(res.data.form)
          this.fieldList = res.data.fields
          this.fieldList.forEach(field => {
            this.initSettingValue(field)
          })
          this.getDetail()
          this.loading = false
        }).catch(() => {
          this.loading = false
        })
      }
    },
    /**
     * 详情页面切换下一行
     */
    pageChange(type) {
      let currentIndex = this.getRowIndex()
      if (type === 'left') {
        currentIndex--
      } else {
        currentIndex++
      }
      if (currentIndex >= 0 && currentIndex < this.pageCodes.length) {
        this.detailCode = this.pageCodes[currentIndex]
        this.getDetail()
      } else {
        this.$message.error('没有更多了')
      }
    },
    /**
     * 获取当前code的下标
     */
    getRowIndex() {
      for (let index = 0; index < this.pageCodes.length; index++) {
        if (this.pageCodes[index] === this.detailCode) {
          return index
        }
      }
      return 0
    },

    /**
     * 触发事件按钮
     */
    actionHandle(data) {
      if (data.type === 'edit') {
        this.isCreate = true
        return
      } else if (data.type === 'save-success') {
        // 但字段编辑成功刷新
        this.editSaveSuccess()
        return
      } else if (data.type === 'update') {
        this.loading = true
        this.updateRequest(data.data).then(res => {
          this.loading = false
          this.editSaveSuccess()
        }).catch(() => {
          this.loading = false
        })
        return
      } else if (data.type === 'print') {
        printTemplateSimpleListAPI({
          formCode: this.formCode
        }).then(res => {
          if (res.data.length === 0) {
            this.$message.error('未找到可用的打印模板，请先维护打印模板后再操作打印！')
            return
          }
          const routeData = this.$router.resolve(`/print/${this.formCode}/${this.detailCode}`)
          window.open(routeData.href, '_blank')
        })
        return
      }
      if (this.doActionHandler) {
        this.doActionHandler(data)
        return
      }
      this.$emit('handle', data)
    },

    handleEntityClick(data) {
      this.relativeEntity.code = data.value.code
      if (data.field.relativeForm) {
        this.relativeEntity.formCode = data.field.relativeForm.code
      }
      this.relativeEntity.menuCode = data.field.menuCode
      this.relativeEntity.componentName = data.field.componentName
      this.relativeEntity.show = true
    },
    closeEntityDetail() {
      this.relativeEntity.show = false
    },

    /**
     * 编辑成功
     */
    editSaveSuccess() {
      this.$emit('handle', { type: 'save-success' })
      this.detailData = null
      this.getDetail()
    },
    /**
     * 关闭
     */
    hideView() {
      this.$emit('hide-view')
      this.$store.commit('SET_ACTIVEINDEX', -50)
    }
  },

  deactivated: function() { }

}
