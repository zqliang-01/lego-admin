export default {
  props: {
    data: {
      type: Object,
      default: {
        title: ''
      }
    },
    param: {
      type: Object,
      default: () => {}
    }
  },
  data() {
    return {
      loading: false,
      canvaId: this.uuid(),
      color: [
        '#FF7474',
        '#F59561',
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
    data: {
      handler() {
        if (this.getData && !this.loading) {
          this.getData()
        }
      },
      deep: true,
      immediate: true
    },
    param: {
      handler() {
        if (this.getData && !this.loading) {
          this.getData()
        }
      },
      deep: true,
      immediate: true
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
  },
  beforeDestroy() {
    this.$bus.off('window-resize')
  },
  methods: {
    /**
     * 获取请求参数
     */
    getBaseParams() {
      const condition = {
        code: this.data.definition.code,
        param: {}
      }
      if (this.param && this.param.startTime) {
        condition.param.startTime = this.param.startTime
      }
      if (this.param && this.param.endTime) {
        condition.param.endTime = this.param.endTime
      }
      return condition
    },
    uuid() {
      const time = Date.now()
      const random = Math.floor(Math.random() * 1000000000)
      return 'lego_' + random + String(time)
    }
  }
}
