<template>
  <!-- 预览界面 -->
  <el-dialog
    :title="title"
    :visible="visible"
    :fullscreen="isFull"
    width="80%"
    top="5vh"
    append-to-body class="scrollbar preview-box"
    @close="handleCancel">
    <div slot="title">
      <h2>代码预览 <el-button :icon="isFull ? 'shrink' : 'full' | iconPre" @click="isFull = !isFull"/></h2>
    </div>
    <div class="nav">
      <el-tree
        :props="treeProps"
        :expand-on-click-node="false"
        :default-expanded-keys="expandeKeys"
        :data="data"
        node-key="code"
        highlight-current
        class="code-tree"
        @node-click="changeCodeClick">
        <flexbox slot-scope="{ node }" :class="{ 'is-current': node.isCurrent}" class="node-data">
          <i
            v-if="node.childNodes && node.childNodes.length"
            :class="'department' | iconPre"/>
          <span v-else class="node-data__mark" />
          <div class="node-data__label text-one-line ">{{ node.label }}</div>
        </flexbox>
      </el-tree>
    </div>
    <div class="content-box">
      <div class="copy-button">
        <span>{{ currentFileName }}</span>
        <el-link v-clipboard:copy="currentValue" v-clipboard:success="clipboardSuccess" :underline="false" icon="el-icon-document-copy">复制</el-link>
      </div>
      <pre class="code-content"><code v-html="highlightedCode"/></pre>
    </div>
  </el-dialog>
</template>

<script>
import 'highlight.js/styles/github-gist.css'
const hljs = require('highlight.js/lib/core')
hljs.registerLanguage('java', require('highlight.js/lib/languages/java'))
hljs.registerLanguage('xml', require('highlight.js/lib/languages/xml'))
hljs.registerLanguage('html', require('highlight.js/lib/languages/xml'))
hljs.registerLanguage('vue', require('highlight.js/lib/languages/xml'))
hljs.registerLanguage('javascript', require('highlight.js/lib/languages/javascript'))
hljs.registerLanguage('sql', require('highlight.js/lib/languages/sql'))

export default {
  name: 'CustomField',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    data: Array,
    title: String
  },
  data() {
    return {
      isFull: false,
      expandeKeys: [],
      currentValue: '',
      currentFileName: '',
      treeProps: {
        label: 'code',
        children: 'childrens'
      }
    }
  },
  computed: {
    highlightedCode() {
      if (!this.currentFileName) {
        return ''
      }
      const fileName = this.currentFileName
      var language = fileName.substring(fileName.indexOf('.') + 1, fileName.length)
      const result = hljs.highlight(language, this.currentValue || '', true)
      return result.value || '&nbsp;'
    }
  },
  watch: {
    visible() {
      this.currentValue = ''
      this.currentFileName = ''
      this.expandeKeys = []
      if (this.visible) {
        this.data.forEach(element => {
          this.expandeKeys.push(element.code)
        })
      }
    }
  },
  methods: {
    changeCodeClick(data) {
      if (data.childrens.length == 0) {
        this.currentValue = data.value
        this.currentFileName = data.code
      }
    },
    clipboardSuccess() {
      this.$message.success('复制成功')
    },
    handleCancel() {
      this.$emit('update:visible', false)
    }
  }
}
</script>
<style rel="stylesheet/scss" lang="scss" scoped>

.nav {
  width: 280px;
  position: absolute;
  left: 0;
  height: 515px;
  overflow: auto;
}
.content-box {
  background: #fff;
  margin-left: 280px;
  position: relative;
  .copy-button {
    text-align: right;
    padding-right: 10px;
  }
  .code-content {
    overflow: auto;
    height: 500px;
  }
}
</style>
