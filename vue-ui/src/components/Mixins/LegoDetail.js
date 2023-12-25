import {
  mapGetters
} from 'vuex'

import { debounce } from 'throttle-debounce'

import { tableHeaderFieldListAPI } from '@/api/admin/formField'
import ImportInfo from '../ImportInfo'
import SlideView from '../SlideView'
import LegoDetailHead from '../LegoDetailHead'
import LegoEditBaseInfo from '../LegoEditBaseInfo'

export default {
  components: {
    SlideView,
    LegoDetailHead,
    LegoEditBaseInfo,
    ImportInfo
  },
  data() {
    return {
      loading: false,
      visible: false,
      detailData: null,
      tabsNumber: {},
      importList: [],
      tabNumRequest: null,
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
    menuCode: String,
    unionKey: String,
    pageIndex: [String, Number],
    pageList: Array, // 用于详情切换
    // 监听的dom 进行隐藏详情
    listenerIDs: {
      type: Array,
      default: () => {
        return ['crm-main-container']
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
    ...mapGetters([
      'allAuth'
    ]),
    auth() {
      const menuList = this.menuCode.split(':')
      var auth = { ...this.allAuth }
      menuList.forEach(menu => {
        if (auth) {
          auth = auth[menu]
        }
      })
      return auth
    },
    // 能否查看详情
    canShowDetail() {
      return this.auth && this.auth.detail
    }
  },

  watch: {
    code: function() {
      if (this.canShowDetail) {
        this.detailData = null
        this.tabsNumber = {}
        this.getDetial()
        this.getTabsNum()
      }
    },

    detailData() {
      if (this.$refs.detailMain && this.$refs.detailMain.style.background == 'white') {
        this.$refs.detailMain.style.background = 'inherit'
      }
    }
  },

  mounted() {
    this.$refs.detailMain.style.background = 'white'
    this.debouncedGetTabsNum = debounce(300, this.getTabsNum)
  },

  beforeDestroy() {
    this.$bus.off('crm-tab-num-update', this.debouncedGetTabsNum)
  },

  methods: {
    /**
     * 详情
     */
    getDetial() {
      this.loading = true
      this.detailRequest(this.code)
        .then(res => {
          this.detailData = res.data
          this.headFieldList.map(detail => {
            detail.value = this.detailData[detail.fieldCode]
          })
          this.fieldList.map(field => {
            field.value = this.detailData[field.fieldCode]
          })
          this.systemFieldList.map(field => {
            field.value = this.detailData[field.fieldCode]
          })
          this.loading = false
        })
        .catch(() => {
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

    /**
     * 详情页面切换下一行
     */
    pageChange(type) {
      if (type === 'left') {
        if (this.pageIndex > 0) {
          let pageIndex = this.pageIndex
          this.$emit('update:pageIndex', --pageIndex)
          this.$emit('update:code', this.pageList[pageIndex][this.unionKey])
        } else {
          this.$message.error('没有更多了')
        }
      } else {
        if (this.pageIndex < this.pageList.length - 1) {
          let pageIndex = this.pageIndex
          this.$emit('update:pageIndex', ++pageIndex)
          this.$emit('update:code', this.pageList[pageIndex][this.unionKey])
        } else {
          this.$message.error('没有更多了')
        }
      }
    },

    viewAfterEnter() {
      if (this.canShowDetail) {
        this.getDetial()
        this.getTabsNum()
        this.$bus.on('crm-tab-num-update', this.debouncedGetTabsNum)
      }
    },

    /**
     * 获取tab数量
     */
    getTabsNum() {
      if (!this.tabNumRequest) {
        return
      }
      this.tabNumRequest({ code: this.code }).then(res => {
        this.tabsNumber = res.data || {}
      })
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
        }).catch(err => {
          this.loading = false
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
      const detailComponent = this.entityDetailList.find(detail => detail.code == data.field.fieldCode)
      if (detailComponent) {
        detailComponent.detailCode = data.value.code
        detailComponent.formCode = data.field.relativeForm.code
        tableHeaderFieldListAPI(data.field.relativeForm.code).then(res => {
          detailComponent.menuCode = res.data.permissionCode
          detailComponent.fieldList = res.data.fields
          this.visible = true
          detailComponent.show = true
        })
      }
    },
    closeEntityDetail(item) {
      item.show = false
      setTimeout(() => {
        this.visible = false
      }, 350)
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
