export default {
  props: {
    filterValue: {
      type: Object,
      default: () => {}
    }
  },
  data() {
    return {
      color: [
        '#6CA2FF',
        '#6AC9D7',
        '#72DCA2',
        '#48E78D',
        '#FECD51',
        '#DBB375',
        '#FF7474',
        '#F59561',
        '#A3AEBC',
        '#4C84FF',
        '#0DBEB4',
        '#00DEDE',
        '#FFAA00',
        '#C7C116',
        '#F7A57C',
        '#F661AC',
        '#8652EE'
      ]
    }
  },
  watch: {
    // 根据筛选条件获取统计数据
    filterValue: {
      handler() {
        if (this.getData) {
          this.getData()
        }
      },
      deep: true
    }
  },
  mounted() {
    // 根据窗口的大小变化实时调整 chart 大小
    this.$bus.on('window-resize', () => {
      if (this.chartObj) {
        this.chartObj.resize()
      }
    })
    if (this.initChart) {
      this.initChart()
    }
    if (this.getData) {
      this.getData()
    }
  },
  beforeDestroy() {
    this.$bus.off('window-resize')
  },
  methods: {
    /**
     * 获取请求参数
     */
    getBaseParams() {
      const params = {}
      if (this.filterValue.dataType !== 'custom') {
        params.dataType = this.filterValue.dataType
      } else {
        if (this.filterValue.strucs.length) {
          params.isUser = 0
          params.deptId = this.filterValue.strucs[0].id
        } else {
          params.isUser = 1
          params.userId = this.filterValue.users.length ? this.filterValue.users[0].userId : ''
        }
      }

      if (this.filterValue.timeLine.type) {
        if (this.filterValue.timeLine.type === 'custom') {
          params.startTime = this.filterValue.timeLine.startTime.replace(/\./g, '-')
          params.endTime = this.filterValue.timeLine.endTime.replace(/\./g, '-')
        } else {
          params.type = this.filterValue.timeLine.value || ''
        }
      }
      return params
    }
  }
}
