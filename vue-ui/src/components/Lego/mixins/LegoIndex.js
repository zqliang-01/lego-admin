import { columnWidthModifyAPI } from '@/api/admin/formField'
import { permissionGetAPI } from '@/api/admin/permission'
import { tableHeaderFieldListAPI } from '@/api/admin/formField'

import LegoCommonMixin from './LegoCommon'
import LegoListHead from '../LegoListHead'
import LegoTableHead from '../LegoTableHead'
import LegoTableSort from '../LegoTableSort'
import Empty from '@/components/Empty'
import FieldView from '@/components/Common/Form/FieldView'

import Lockr from 'lockr'
import { Loading } from 'element-ui'
import { downloadExcelWithResData } from '@/utils'
import { isEmpty } from '@/utils/types'
import { getMenuAuth } from '@/utils/auth'

export default {
  components: {
    LegoListHead,
    LegoTableHead,
    LegoTableSort,
    Empty,
    FieldView
  },
  mixins: [LegoCommonMixin],
  data() {
    return {
      loading: false, // 加载动画
      tableHeight: document.documentElement.clientHeight - 216, // 表的高度
      pageList: [],
      pageCodes: [],
      relativeEntity: {
        show: false
      },
      formCode: '',
      fieldList: [],
      sortData: {}, // 字段排序
      currentPage: 1,
      pageSize: Lockr.get('legoPageSizes') || 15,
      pageSizes: [15, 30, 60, 100],
      total: 0,
      search: '', // 搜索内容
      filterList: [], // 高级筛选确定数据
      sceneData: [], // 场景条件集合
      selectionList: [], // 勾选数据 用于全局导出
      formErrorMsg: null
    }
  },
  computed: {
    auth() {
      return getMenuAuth(this.$route.params.menuCode || this.menuCode)
    }
  },
  watch: {
    $route: function(val) {
      this.formCode = ''
      if (this.$route.params.menuCode) {
        this.menuCode = this.$route.params.menuCode
      }
      this.getFieldList(true)
    }
  },
  mounted() {
    window.onresize = () => {
      this.updateTableHeight()
    }
    if (this.$route.params.menuCode) {
      this.menuCode = this.$route.params.menuCode
    }
    this.getFieldList()
  },

  methods: {
    /**
     * 获取列表数据
     */
    getList() {
      this.loading = true
      var params = {
        pageIndex: this.currentPage,
        pageSize: this.pageSize,
        search: this.search
      }
      if (this.sortData.order) {
        params.sortField = this.sortData.prop
        params.orderType = this.sortData.order
      }

      params.searchList = []
      if (this.filterList && this.filterList.length > 0) {
        params.searchList = this.filterList
      }

      if (this.sceneData) {
        params.searchList = params.searchList.concat(this.sceneData)
      }

      this.listRequest(params).then(res => {
        this.pageList = res.data.result
        this.pageCodes = res.data.result.map(item => item.code)

        if (res.data.totalCount && Math.ceil(res.data.totalCount / this.pageSize) < this.currentPage && this.currentPage > 1) {
          this.currentPage = this.currentPage - 1
          this.getList()
        } else {
          this.total = res.data.totalCount
          this.loading = false
        }

        this.$nextTick(() => {
          if (document.querySelector('.el-table__body-wrapper')) {
            document.querySelector('.el-table__body-wrapper').scrollTop = 1
          }
        })
      }).catch(() => {
        this.loading = false
      })
    },
    /**
     * 获取字段
     */
    getFieldList(force) {
      if (this.fieldList.length == 0 || force) {
        this.loading = true
        this.formErrorMsg = null
        permissionGetAPI(this.menuCode).then(res => {
          if (isEmpty(res.data.form) || isEmpty(res.data.form.code)) {
            this.loading = false
            this.formErrorMsg = '功能菜单[' + res.data.name + ']未关联表单！'
            this.$message.error(this.formErrorMsg)
            return
          }
          this.formCode = res.data.form.code
          tableHeaderFieldListAPI(this.formCode).then(res => {
            this.initRequest(res.data.form)
            this.fieldList = []
            res.data.fields.forEach(field => {
              field.minWidth = 100
              this.initSettingValue(field)
              this.fieldList.push(field)
            })
            this.getList()
          }).catch(res => {
            this.loading = false
            this.formErrorMsg = res.msg
          })
        })
      } else {
        // 获取好字段开始请求数据
        this.getList()
      }
    },

    /**
     * 搜索操作
     * @param {*} value
     */
    commonSearch(value) {
      this.currentPage = 1
      this.search = value
      if (this.fieldList.length) {
        this.getList()
      }
    },

    /**
     * 控制表格需要高亮的列
     */
    cellClassName({ row, column, rowIndex, columnIndex }) {
      if (column.property === this.unionKey) {
        return 'can-visit--underline can-visit--bold'
      } else {
        return ''
      }
    },

    /**
     * 列表点击某行操作
     */
    handleRowClick(row, column, event) {
      if (column.type === 'selection') {
        return
      }
      if (this.relativeEntity.show) {
        this.$store.commit('SET_COLLAPSE', this.relativeEntity.show)
      }
      // 只有点击高亮列才触发打开详情信息
      if (column.property === this.unionKey) {
        this.$set(this.relativeEntity, 'show', true)
        this.$set(this.relativeEntity, 'code', row[this.unionKey])
        this.$set(this.relativeEntity, 'formCode', this.formCode)
        this.$set(this.relativeEntity, 'pageCodes', this.pageCodes)
        return
      }
    },

    handleEntityClick(data) {
      this.$set(this.relativeEntity, 'show', true)
      this.$set(this.relativeEntity, 'code', data.value.code)
      this.$set(this.relativeEntity, 'formCode', data.field.relativeForm.code)
      this.$set(this.relativeEntity, 'pageCodes', [])
    },
    closeEntityDetail(item) {
      item.show = false
    },

    /**
     * 导出
     * @param {*} data
     */
    exportInfos() {
      var params = {
        search: this.search,
        formCode: this.formCode
      }

      params.searchList = []
      if (this.filterList && this.filterList.length > 0) {
        params.searchList = this.filterList
      }

      if (this.sceneData) {
        params.searchList = params.searchList.concat(this.sceneData)
      }

      const loading = Loading.service({ fullscreen: true, text: '导出中...' })
      this.exportAllRequest().then(res => {
        downloadExcelWithResData(res)
        loading.close()
      }).catch(() => {
        loading.close()
      })
    },

    /**
     * 筛选操作
     */
    handleFilter(data) {
      if (!data) {
        return
      }
      this.filterList = data
      var offsetHei = document.documentElement.clientHeight
      var removeHeight = this.filterList.length > 0 ? 266 : 216
      this.tableHeight = offsetHei - removeHeight
      this.currentPage = 1
      this.getList()
    },

    /**
     * 场景操作
     */
    handleScene(data) {
      this.sceneData = data
      this.currentPage = 1
      this.getList()
    },

    /**
     * 刷新数据
     */
    refreshList() {
      this.currentPage = 1
      this.getFieldList()
    },

    /**
     * 操作回调处理，包含修改或新增成功
     */
    actionHandle(data) {
      // 编辑是个动作，不是编辑成功。不执行操作
      if (['edit'].includes(data.type)) {
        return
      }
      if (data.type === 'save-success') {
        this.$message.success('操作成功')
        this.getList()
        return
      }
      if (data.type === 'clear-sort') {
        this.getMainTable().clearSort()
        this.sortChange()
        return
      }
      if (data.type === 'delete') {
        const codes = this.selectionList.map(item => {
          return item.code
        })
        const loading = Loading.service({ fullscreen: true, text: '删除中...' })
        this.deleteRequest(codes).then(res => {
          this.$message.success('删除成功')
          this.getMainTable().clearSelection()
          this.getList()
          loading.close()
        }).catch(() => {
          loading.close()
        })
      }
      if (data.type === 'export') {
        const codes = this.selectionList.map(item => {
          return item.code
        })
        const loading = Loading.service({ fullscreen: true, text: `导出[${codes.length}]条数据中...` })
        this.exportRequest(codes).then(res => {
          downloadExcelWithResData(res)
          this.$message.success('导出成功')
          this.getMainTable().clearSelection()
          this.getList()
          loading.close()
        })
          .catch(() => {
            loading.close()
          })
      }
    },

    /**
     * 获取table
     */
    getMainTable() {
      let table = null
      this.$children.forEach(item => {
        if (item.$options && item.$options.name === 'ElTable') {
          table = item
        }
      })
      return table
    },

    /** 自定义字段管理 */
    setSave() {
      this.getFieldList(true)
    },

    /**
     * 页面头部操作
     */
    listHeadHandle(data) {
      if (data.type === 'save-success') {
        // 重新请求第一页数据
        this.currentPage = 1
        this.getList()
      }
    },

    /**
     * 字段排序
     */
    sortChange(column, prop, order) {
      this.currentPage = 1
      this.sortData = column || {}
      this.getList()
    },

    /**
     * 勾选操作
     */
    handleSelectionChange(val) {
      this.selectionList = val // 勾选的行
      this.$refs.legoTableHead.headSelectionChange(val)
    },

    /**
     * 当拖动表头改变了列的宽度的时候会触发该事件
     */
    handleHeaderDragend(newWidth, oldWidth, column, event) {
      if (column.property && newWidth !== oldWidth) {
        var field = this.fieldList.find(field => field.fieldCode === column.property)
        if (field) {
          columnWidthModifyAPI({
            fieldCode: field.code,
            width: newWidth
          }).then(() => {})
        }
      }
    },

    /**
     * 更改每页展示数量
     */
    handleSizeChange(val) {
      Lockr.set('legoPageSizes', val)
      this.pageSize = val
      this.getList()
    },

    /**
     * 更改当前页数
     */
    handleCurrentChange(val) {
      this.currentPage = val
      this.getList()
    },

    /**
     * 更新表高
     */
    updateTableHeight() {
      var offsetHei = document.documentElement.clientHeight
      var removeHeight = this.filterList.length > 0 ? 266 : 216
      this.tableHeight = offsetHei - removeHeight
    }
  }
}
