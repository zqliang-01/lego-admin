<template>
  <div class="log">
    <flexbox class="log-header">
      <el-button
        type="primary"
        @click="handleGoBack">返回</el-button>
      <el-button
        class="xr-btn--orange"
        icon="el-icon-refresh"
        type="primary"
        @click="handleRefresh">刷新</el-button>
    </flexbox>
    <div class="log-content">
      <pre class="code-content"><code v-html="highlightedCode"/></pre>
      <span v-if="!isEnd">加载中...</span>
    </div>
  </div>
</template>
<script>
import { logDetailGetAPI } from '@/api/job/log'
import hljs from '@/components/highlight'

export default {
  name: 'LogDetail',
  data() {
    return {
      logContent: '',
      lineNum: 1,
      timer: null,
      isEnd: false
    }
  },
  computed: {
    highlightedCode() {
      if (!this.logContent) {
        return ''
      }
      const result = hljs.highlight('java', this.logContent || '', true)
      return result.value || '&nbsp;'
    }
  },
  watch: {
    logId() {
      this.init()
    }
  },
  mounted() {
    this.init()
    this.timer = setInterval(() => {
      this.init()
    }, 3000)
  },
  beforeDestroy() {
    if (this.timer != null) {
      clearInterval(this.timer)
    }
  },
  methods: {
    init() {
      logDetailGetAPI({
        logId: this.$route.params.logId,
        fromLineNum: this.lineNum
      }).then(res => {
        this.isEnd = res.data.end
        if (this.lineNum < res.data.toLineNum) {
          this.lineNum = res.data.toLineNum + 1
          this.logContent = this.logContent + res.data.logContent
        }
        if (this.isEnd && this.timer != null) {
          clearInterval(this.timer)
        }
      }).catch(res => {
        this.isEnd = true
        this.logContent = res.msg
        if (this.timer != null) {
          clearInterval(this.timer)
        }
      })
    },
    handleRefresh() {
      if (this.isEnd) {
        this.isEnd = false
        this.lineNum = 1
        this.logContent = ''
        if (this.timer != null) {
          clearInterval(this.timer)
        }
        this.timer = setInterval(() => {
          this.init()
        }, 3000)
      }
      this.init()
    },
    handleGoBack() {
      this.$router.go(-1)
    }
  }
}
</script>
<style scoped lang="scss">
.log {
  height: 100%;
  &-header {
    height: 60px;
    padding-left: 20px;
  }
  &-content {
    font-size: 12px;
    margin: 0px 20px;
    padding: 20px;
    overflow: auto;
    background-color: #fff;
    border: 1px solid #e6e6e6;
    border-radius: 4px;
    height: calc(100% - 70px);
    line-height: 20px;
  }
}
</style>
