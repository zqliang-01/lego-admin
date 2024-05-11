<template>
  <div>
    <el-form-item prop="sqlText">
      <codemirror
        v-loading="sqlLoadding"
        v-model="fieldForm.sqlText"
        :options="options" />
      <div class="report-query-btn">
        <el-button type="primary" @click="handleRunSql()">脚本测试</el-button>
        <span class="script-tips">（脚本测试通过并且有结果返回时会自动生成输出结果字段）</span>
      </div>
      <div v-if="jsonData" class="json-view">
        <json-editor mode="code" v-model="jsonData"/>
      </div>
    </el-form-item>
  </div>
</template>
<script>
import { definitionOpenTestAPI } from '@/api/report/definition'
import JsonEditor from '@/components/Common/JsonEditor'
import { codemirror } from 'vue-codemirror'
import 'codemirror/lib/codemirror.css'
import 'codemirror/mode/sql/sql.js'
import 'codemirror/addon/display/fullscreen.js'

export default {
  name: 'DefinitionParamScription',
  components: {
    JsonEditor,
    codemirror
  },
  props: {
    fieldForm: Object,
    fieldRules: Object
  },
  data() {
    return {
      sqlLoadding: false,
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
        mode: 'sql',
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
      jsonData: null
    }
  },
  created() {
    this.$set(this.fieldRules, 'sqlText', [{ required: true, message: '数据脚本不能为空' }])
  },
  methods: {
    handleRunSql() {
      if (!this.fieldForm.sqlText) {
        this.$message.error('请填写数据脚本后再执行！')
        return
      }
      var param = {
        dataSource: this.fieldForm.dataSource,
        params: this.fieldForm.params,
        sqlText: this.fieldForm.sqlText
      }
      this.sqlLoadding = true
      definitionOpenTestAPI(param).then(res => {
        this.sqlLoadding = false
        this.jsonData = JSON.stringify(res.data.records)
        if (res.data.records.length > 0) {
          this.autoAddOutput(res.data.records[0])
        }
      }).catch(() => {
        this.sqlLoadding = false
      })
    },
    autoAddOutput(record) {
      if (this.fieldForm.titles.length > 0) {
        this.$confirm('当前输出结果已经设置，是否需要覆盖?', '提示', {
          type: 'warning'
        }).then(() => {
          this.$emit('resetOutput', record)
        })
        return
      }
      this.$emit('resetOutput', record)
    }
  }
}
</script>
<style lang="scss" scoped>
.el-form-item {
  margin-bottom: 0px;
}

::v-deep .CodeMirror {
  height: 200px;
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
.report-query-btn {
  margin-top: 15px;
  .script-tips {
    margin-left: 10px;
    color: #999;
  }
}

.json-view {
  margin-top: 15px;
  border-radius: 5px;
  border: 1px solid #dcdee2;
  overflow: hidden;
}
</style>
