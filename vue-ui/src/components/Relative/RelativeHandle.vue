<template>
  <div
    v-loading="loading"
    v-empty="list"
    class="rc-cont"
    style="padding-right:10%;">
    <flexbox
      v-for="(item, index) in list"
      :key="index"
      class="ha-cont"
      align="stretch"
      justify="flex-start">
      <div class="ha-week">{{ item.createTime|filterTimestampToFormatTime('MM-DD dddd') }}</div>
      <div class="ha-circle"/>
      <div class="ha-time">{{ item.createTime|filterTimestampToFormatTime('HH:mm') }}</div>
      <xr-avatar
        :name="item.operator.name"
        :code="item.operator.code"
        :size="30"
        :src="item.imageCode"
        :disabled="false"
        class="ha-img" />
      <div class="ha-name">{{ item.operator.name }}</div>
      <div class="ha-content">
        <p>{{ item.description }}</p>
      </div>
      <div class="ha-line"/>
    </flexbox>
  </div>
</template>

<script type="text/javascript">
import { operationLogListAPI } from '@/api/common'

export default {
  name: 'RelativeHandle',
  components: {},
  props: {
    detailCode: [String, Number],
    menuCode: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      loading: false,
      list: []
    }
  },
  inject: ['rootTabs'],
  computed: {},
  watch: {
    detailCode(val) {
      this.list = []
      this.getDetail()
    },

    'rootTabs.currentName'(val) {
      if (val === 'RelativeHandle') {
        this.getDetail(false)
      }
    }
  },
  mounted() {
    this.getDetail()
  },
  methods: {
    getDetail(loading = true) {
      this.loading = loading
      operationLogListAPI({
        entityCode: this.detailCode,
        permissionCode: this.menuCode
      })
        .then(res => {
          this.loading = false
          this.list = res.data.map(item => {
            item.createTime = new Date(item.createTime).getTime()
            return item
          })
        })
        .catch(() => {
          this.loading = false
        })
    }
  }
}
</script>
<style lang="scss" scoped>
@import '@/styles/relative.scss';
.ha-cont {
  font-size: 12px;
  position: relative;
  line-height: 20px;
  min-height: 40px;
  padding-top: 3px;
  .ha-week {
    margin: 0 17px 0 10px;
    flex-shrink: 0;
    color: #777;
    width: 80px;
  }
  .ha-time {
    padding: 0 10px 0 24px;
    flex-shrink: 0;
    color: #aaa;
  }
  .ha-circle {
    flex-shrink: 0;
    width: 18px;
    height: 18px;
    z-index: 2;
    border-radius: 9px;
    background-color: white;
    border: 5px solid #a5ecd7;
  }
  .ha-img {
    flex-shrink: 0;
    margin: -3px 10px 0 10px;
    display: block;
  }
  .ha-name {
    padding: 0 10px;
    flex-shrink: 0;
    color: #333;
  }
  .ha-content {
    padding: 0px 10px 10px 10px;
    flex: 1;
    color: #333;
    p {
      white-space: pre-wrap;
    }
  }
  .ha-line {
    position: absolute;
    z-index: 1;
    width: 1px;
    top: 3px;
    bottom: -3px;
    left: 115px;
    background-color: #e6e6e6;
  }
}
</style>
