<template>
  <div class="code">
    <flexbox class="code-header">
      <label>
        {{ title }}
      </label>
      <el-select
        v-model="version"
        clearable
        filterable
        placeholder="选择历史版本"
        value-key="code"
        class="code-header-item"
        @change="handleVersionChange">
        <el-option
          v-for="item in versionList"
          :key="item.code"
          :label="item.name "
          :value="item"/>
      </el-select>
      <el-button
        class="xr-btn--orange"
        icon="el-icon-download"
        type="primary"
        @click="handleSave">保存</el-button>
      <el-button
        type="primary"
        @click="handleGoBack">返回</el-button>
    </flexbox>
    <div class="code-content">
      <codemirror
        v-loading="loading"
        v-model="codeContent"
        :options="options" />
    </div>
  </div>
</template>
<script>
import {
  codeGetAPI,
  codeSaveAPI
} from '@/api/job/code'
import { codemirror } from 'vue-codemirror'
import 'codemirror/lib/codemirror.css'
import 'codemirror/mode/javascript/javascript.js'
import 'codemirror/addon/display/fullscreen.js'

export default {
  name: 'JobTaskScript',
  components: {
    codemirror
  },
  data() {
    return {
      loading: false,
      version: '',
      versionList: [],
      title: '',
      options: {
        line: true,
        tabSize: 4, // 制表符的宽度
        indentUnit: 4, // 一个块应该缩进多少个空格（无论这在编辑语言中意味着什么）。
        firstLineNumber: 1, // 从哪个数字开始计算行数。默认值为 1。
        readOnly: false, // 只读
        autorefresh: true,
        smartIndent: true, // 上下文缩进
        lineNumbers: true, // 是否显示行号
        styleActiveLine: true, // 高亮选中行
        viewportMargin: Infinity, // 处理高度自适应时搭配使用
        showCursorWhenSelecting: true, // 当选择处于活动状态时是否应绘制游标
        mode: 'javascript',
        extraKeys: {
          'F11': function(cm) {
            cm.setOption('fullScreen', !cm.getOption('fullScreen'))
          },
          'Esc': function(cm) {
            if (cm.getOption('fullScreen')) {
              cm.setOption('fullScreen', false)
            }
          }
        }
      },
      codeContent: ''
    }
  },
  computed: {
  },
  watch: {
    logId() {
      this.init()
    }
  },
  mounted() {
    this.init()
  },
  methods: {
    init() {
      this.loading = true
      codeGetAPI(this.$route.params.jobId).then(res => {
        this.title = res.data.jobInfo.jobDesc
        this.codeContent = res.data.jobInfo.glueSource
        this.versionList = res.data.jobLogGlues.map(item => {
          return {
            code: item.id,
            value: item.glueSource,
            name: item.glueRemark
          }
        })
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    handleVersionChange(item) {
      this.codeContent = item.value
    },
    handleSave() {
      this.$prompt('备注信息', '脚本保存', {
        inputErrorMessage: '请填写备注信息！',
        inputValidator: value => {
          if (!value) {
            return '请填备注信息！'
          }
        }
      }).then(({ value }) => {
        this.loading = true
        codeSaveAPI({
          id: this.$route.params.jobId,
          glueSource: this.codeContent,
          glueRemark: value
        }).then(res => {
          this.loading = false
          this.$message.success('脚本保存！')
          this.init()
        }).catch(() => {
          this.loading = false
        })
      }).catch(() => {})
    },
    handleGoBack() {
      this.$router.go(-1)
    }
  }
}
</script>
<style scoped lang="scss">
::v-deep .vue-codemirror {
  height: 100%;
}
::v-deep .CodeMirror {
  height: 100%;
  font-size: 15px;
  line-height: 20px;
}
::v-deep .CodeMirror-fullscreen {
  display: block;
  position: fixed;
  top: 0; left: 0;
  width: 100%;
  height: 100% !important;
  padding: 10px;
  z-index: 9999;
}
.code {
  height: 100%;
  label {
    width: 100%;
    font-size: 16px;
    color: #333333;
    font-weight: 600;
  }
  &-header {
    height: 60px;
    padding: 0px 20px;
    &-item {
      margin-right: 10px;
      width: 250px;
    }
  }
  &-content {
    margin: 0px 20px;
    overflow: auto;
    background-color: #fff;
    border: 1px solid #e6e6e6;
    border-radius: 4px;
    height: calc(100% - 70px);
    line-height: 20px;
  }
}
</style>
