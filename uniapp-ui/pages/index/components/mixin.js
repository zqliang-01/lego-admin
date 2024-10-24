import { getLastWeekDate } from '@/utils/date'

export default {
  props: {
    data: {
      type: Object,
      default: function() {
        return {
					name: 'hello'
				}
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
			open2d: true,
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
        if (this.init && !this.loading) {
          this.init()
        }
      },
      deep: true,
      immediate: true
    },
    param: {
      handler() {
        if (this.init && !this.loading) {
          this.init()
        }
      },
      deep: true,
      immediate: true
    }
  },
  onLoad() {
		if (this.init) {
			this.init()
		}
  },
  methods: {
    /**
     * 获取请求参数
     */
    getBaseParams() {
      const condition = {
        code: this.data.definition.code,
        param: {
          startTime: getLastWeekDate()[0],
          endTime: getLastWeekDate()[1]
        }
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
