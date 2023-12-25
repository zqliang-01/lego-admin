import { dictListAPI } from '@/api/crm/common'
import {
  tableHeaderFieldPermissionListAPI,
  columnWidthModifyAPI
} from '@/api/admin/formField'
import { employeeSimpleListAPI } from '@/api/admin/employee'
import { depSimpleListAPI } from '@/api/admin/dept'
import { tableHeaderFieldListAPI } from '@/api/admin/formField'

import LegoListHead from '../LegoListHead'
import LegoTableHead from '../LegoTableHead'
import Empty from '../Empty'
import FieldSet from '../FieldSet'
import FieldView from '../NewCom/Form/FieldView'

import { mapGetters } from 'vuex'
import Lockr from 'lockr'
import { Loading } from 'element-ui'
import { downloadExcelWithResData } from '@/utils'

export default {
  components: {
    LegoListHead,
    LegoTableHead,
    FieldSet,
    Empty,
    FieldView
  },
  data() {
    return {
      loading: false, // 加载动画
      tableHeight: document.documentElement.clientHeight - 235, // 表的高度
      list: [],
      formCode: '',
      fieldList: [],
      sortData: {}, // 字段排序
      currentPage: 1,
      pageSize: Lockr.get('legoPageSizes') || 15,
      pageSizes: [15, 30, 60, 100],
      total: 0,
      search: '', // 搜索内容
      /** 控制详情展示 */
      rowID: '', // 行信息
      showDetail: false,
      /** 高级筛选 */
      filterList: [], // 筛选确定数据
      sceneData: [], // 场景条件集合
      /** 勾选行 */
      selectionList: [], // 勾选数据 用于全局导出
      rowIndex: 0, // 行索引
      formErrorMsg: null
    }
  },
  computed: {
    ...mapGetters([
      'navActiveIndex',
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
    }
  },
  watch: {},
  mounted() {
    window.onresize = () => {
      this.updateTableHeight()
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

      this.listRequest(params)
        .then(res => {
          this.list = res.data.result

          if (res.data.totalCount && Math.ceil(res.data.totalCount / this.pageSize) < this.currentPage && this.currentPage > 1) {
            this.currentPage = this.currentPage - 1
            this.getList()
          } else {
            this.total = res.data.totalCount
            this.loading = false
          }

          this.$nextTick(() => {
            document.querySelector('.el-table__body-wrapper').scrollTop = 1
          })
        })
        .catch(() => {
          this.loading = false
        })
    },
    /**
     * 获取字段
     * @param {*} force
     */
    getFieldList(force) {
      if (this.fieldList.length == 0 || force) {
        this.loading = true
        this.formErrorMsg = null
        tableHeaderFieldPermissionListAPI(this.menuCode)
          .then(res => {
            this.formCode = res.data.formCode
            this.fieldList = []
            res.data.fields.forEach(field => {
              if (!field.width) {
                field.width = 100
              }
              this.initSettingValue(field)
              if (this.initRelativeAPI) {
                this.initRelativeAPI(field)
              }
              this.fieldList.push(field)
            })
            // 获取好字段开始请求数据
            this.getList()
          })
          .catch(res => {
            this.loading = false
            this.formErrorMsg = res.msg
          })
      } else {
        // 获取好字段开始请求数据
        this.getList()
      }
    },
    initSettingValue(field) {
      if (this.navActiveIndex &&
        field.optionDataType === 'dict' &&
        field.optionDictType) {
        dictListAPI(this.navActiveIndex, field.optionDictType).then(res => {
          field.setting = res.data
        })
      }
      if (field.formType == 'user') {
        employeeSimpleListAPI().then(res => {
          field.setting = res.data
        })
      }
      if (field.formType == 'structure') {
        depSimpleListAPI().then(res => {
          field.setting = res.data
        })
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
      if (column.property === this.highlightColumnKey) {
        return 'can-visit--underline can-visit--bold'
      } else {
        return ''
      }
    },

    /**
     * 列表点击某行操作
     * @param {*} row
     * @param {*} column
     * @param {*} event
     */
    handleRowClick(row, column, event) {
      if (column.type === 'selection') {
        return // 多选首列不能点击
      }
      if (this.showDetail) {
        this.$store.commit('SET_COLLAPSE', this.showDetail)
      }
      // 只有点击高亮列才触发打开详情信息
      if (column.property === this.highlightColumnKey) {
        this.rowID = row[this.unionKey]
        this.showDetail = true
        this.rowIndex = this.getRowIndex()
        return
      }
      this.showDetail = false
    },

    handleEntityClick(data) {
      const detailComponent = this.entityDetailList.find(detail => detail.code == data.field.fieldCode)
      if (detailComponent) {
        detailComponent.detailCode = data.value.code
        detailComponent.formCode = data.field.relativeForm.code
        tableHeaderFieldListAPI(data.field.relativeForm.code).then(res => {
          detailComponent.menuCode = res.data.permissionCode
          detailComponent.fieldList = res.data.fields
          detailComponent.show = true
        })
      }
    },
    closeEntityDetail(item) {
      item.show = false
    },

    /**
     * 获取点击行索引
     */
    getRowIndex() {
      for (let index = 0; index < this.list.length; index++) {
        const element = this.list[index]
        if (element[this.unionKey] === this.rowID) {
          return index
        }
      }
      return 0
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
      this.exportAllRequest(params)
        .then(res => {
          downloadExcelWithResData(res)
          loading.close()
        })
        .catch(() => {
          loading.close()
        })
    },

    /**
     * 筛选操作
     * @param {*} data
     */
    handleFilter(data) {
      if (!data) {
        return
      }
      this.filterList = data
      var offsetHei = document.documentElement.clientHeight
      var removeHeight = this.filterList.length > 0 ? 295 : 235
      this.tableHeight = offsetHei - removeHeight
      this.currentPage = 1
      this.getList()
    },

    /**
     * 场景操作
     * @param {*} data
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
        this.$confirm('此操作将永久删除[' + codes.length + ']，是否继续?', '提示', {
          type: 'warning'
        }).then(() => {
          const loading = Loading.service({ fullscreen: true, text: '删除中...' })
          this.deleteRequest(codes).then(res => {
            this.getMainTable().clearSelection()
            this.getList()
            loading.close()
          })
            .catch(() => {
              loading.close()
            })
        })
      }
      if (data.type === 'export') {
        const codes = this.selectionList.map(item => {
          return item.code
        })
        const loading = Loading.service({ fullscreen: true, text: `导出[${codes.length}]条数据中...` })
        this.exportRequest(codes).then(res => {
          downloadExcelWithResData(res)
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
      if (column.property) {
        var field = this.fieldList.find(field => field.fieldCode === column.property)
        if (!field) {
          return
        }
        if (!field.sortCode) {
          return
        }
        const params = {
          code: field.sortCode,
          width: newWidth
        }
        columnWidthModifyAPI(params).then(() => {})
      }
    },

    /**
     * 更改每页展示数量
     * @param {*} val
     */
    handleSizeChange(val) {
      Lockr.set('legoPageSizes', val)
      this.pageSize = val
      this.getList()
    },

    /**
     * 更改当前页数
     * @param {*} val
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
      var removeHeight = this.filterList.length > 0 ? 285 : 235
      this.tableHeight = offsetHei - removeHeight
    }
  }
}
