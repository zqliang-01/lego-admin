import { debounce } from 'throttle-debounce'

import { detailFieldListAPI } from '@/api/admin/formField'
import { printTemplateSimpleListAPI } from '@/api/admin/printTemplate'
import { getMenuAuth } from '@/utils/auth'

import LegoCommonMixin from './LegoCommon'
import ImportInfo from '@/components/ImportInfo'
import SlideView from '@/components/SlideView'
import LegoDetailHead from '@/components/lego/LegoDetailHead'
import LegoEditBaseInfo from '@/components/lego/LegoEditBaseInfo'

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
      pageIndex: 0,
      menuCode: '',
      detailData: null,
      relativeEntity: {
        show: false
      },
      isCreate: false,
      importList: [],
      fieldList: [],
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
    detailCode: String,
    formCode: String,
    visible: {
      type: Boolean,
      default: false
    },
    pageCodes: {
      type: Array,
      default: function() {
        return []
      }
    }, // 用于详情切换
    // 监听的dom 进行隐藏详情
    listenerIDs: {
      type: Array,
      default: () => {
        return ['main-container']
      }
    },
    // 不监听
    noListenerIDs: {
      type: Array,
      default: () => {
        return []
      }
    },
    noListenerClass: {
      type: Array,
      default: () => {
        return ['el-table__body']
      }
    }
  },
  computed: {
    auth() {
      return getMenuAuth(this.menuCode)
    }
  },
  watch: {
    detailCode() {
      if (this.auth.detail) {
        this.detailData = null
        this.pageIndex = this.getRowIndex()
        this.getDetial()
        this.getTabsNum()
      }
    },
    formCode: function() {
      this.initFieldList()
    },
    detailData() {
      if (this.$refs.detailMain && this.$refs.detailMain.style.background == 'white') {
        this.$refs.detailMain.style.background = 'inherit'
      }
    }
  },
  mounted() {
    if (this.$refs.detailMain) {
      this.$refs.detailMain.style.background = 'white'
    }
    this.debouncedGetTabsNum = debounce(300, this.getTabsNum)
    this.initFieldList()
    if (this.detailCode) {
      this.pageIndex = this.getRowIndex()
      this.getDetial()
    }
  },

  beforeDestroy() {
  },

  methods: {
    /**
     * 详情
     */
    getDetial() {
      if (this.auth.detail && typeof this.detailRequest === 'function' && this.detailCode) {
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
      }
    },
    /**
     * 获取重要信息
     */
    getImportInfo() {
      this.loading = true
    },

    async initFieldList() {
      if (this.formCode) {
        await detailFieldListAPI(this.formCode).then(res => {
          this.menuCode = res.data.form.permission.code
          this.initRequest(res.data.form)
          this.fieldList = res.data.fields
          this.fieldList.forEach(field => {
            this.initSettingValue(field)
          })
        })
      }
    },
    /**
     * 详情页面切换下一行
     */
    pageChange(type) {
      let currentIndex = this.pageIndex
      if (type === 'left') {
        currentIndex--
      } else {
        currentIndex++
      }
      if (currentIndex >= 0 && currentIndex < this.pageCodes.length) {
        this.pageIndex = currentIndex
        this.$emit('update:detailCode', this.pageCodes[this.pageIndex])
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
    viewAfterEnter() {
      if (this.auth.detail) {
        this.getDetial()
        this.getTabsNum()
      }
    },

    /**
     * 获取tab数量
     */
    getTabsNum() {
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
      this.relativeEntity.formCode = data.field.relativeForm.code
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
      this.getDetial()
    },
    /**
     * 关闭
     */
    hideView() {
      this.$emit('hide-view')
    }
  },

  deactivated: function() { }

}
