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
      <h2>代码预览</h2>
    </div>
    <sash-form-layout height="510px" rightBackground="#e6e6e6">
      <template v-slot:left>
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
      </template>
      <template v-slot:right>
        <div class="content-box">
          <div class="copy-button">
            <span>{{ currentFileName }}</span>
            <el-link v-clipboard:copy="currentValue" v-clipboard:success="clipboardSuccess" :underline="false" icon="el-icon-document-copy">复制</el-link>
          </div>
          <pre class="code-content"><code v-html="highlightedCode"/></pre>
        </div>
      </template>
    </sash-form-layout>
  </el-dialog>
</template>

<script>
import hljs from '@/components/highlight'
import SashFormLayout from '@/components/Layout/SashFormLayout'

export default {
  name: 'CustomField',
  components: {
    SashFormLayout
  },
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
      codeLanguage: '',
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
<style lang="scss" scoped>
.content-box {
  padding: 5px;
  border-radius: 5px;
  .copy-button {
    right: 0px;
    padding: 5px;
    background: #e6e6e6;
    margin-right: 10px;
    position: absolute;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    a {
      margin-left: 10px;
    }
  }
}
</style>
