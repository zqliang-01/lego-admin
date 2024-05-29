<template>
  <div v-loading="loading" class="print-template-detail">
    <xr-header
      :icon-class="'print'"
      icon-color="#26d4da"
      :label="templateName" >
      <template slot="ft">
        <el-button
          type="text"
          @click="perviewClick">预览</el-button>
        <el-button
          v-debounce="saveClick"
          type="primary">保存</el-button>
        <el-button
          @click="goBackClick">返回</el-button>
      </template>
    </xr-header>
    <div class="print-template-detail__body">
      <!-- 客户管理导航 -->
      <div class="system-view-nav">
        <reminder
          content="点击字段插入值（光标所在位置）"
          class="xr-reminder"/>
        <div class="menus">
          <div
            v-for="(item, index) in fieldList"
            :key="index"
            class="menu-item"
            @click="fieldSelect(item, index)">
            {{ item.name }}
          </div>
        </div>
      </div>
      <div class="system-view-content" >
        <tinymce
          ref="createTinymce"
          v-model="content"
          :height="richHeight"
          :init="getEditConfig()"
          class="rich-txt" />
      </div>
    </div>
  </div>
</template>

<script>
import {
  printTemplateDesignAPI,
  printTemplateGetAPI
} from '@/api/admin/printTemplate'
import {
  customFieldSimpleListAPI
} from '@/api/admin/formField'

import XrHeader from '@/components/XrHeader'
import Reminder from '@/components/Reminder'
import Tinymce from '@/components/Tinymce'

export default {
  name: 'PrintTemplateDetail',
  components: {
    XrHeader,
    Reminder,
    Tinymce
  },
  data() {
    return {
      loading: false,
      templateName: '',
      fieldList: [],
      richHeight: document.documentElement.clientHeight - 135,
      content: '',
      // 控制选择的内容
      selectNodes: null
    }
  },
  computed: {
    editor() {
      return this.$refs.createTinymce.editor
    }
  },
  created() {
    this.getDetail()
  },
  methods: {
    /** *
     * 获取详情
     */
    getDetail() {
      this.loading = true
      printTemplateGetAPI(this.$route.params.templateCode).then(res => {
        const data = res.data || {}
        this.content = data.content
        this.templateName = data.name
        this.getFields(data.form.code)
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },

    /**
     *获取字段数据
     */
    getFields(formCode) {
      customFieldSimpleListAPI({ formCode: formCode }).then(res => {
        this.fieldList = res.data
      })
    },

    fieldSelect(item, index) {
      this.editor.insertContent(this.getSpanNode(item))
    },


    /**
     * 根据选择器获取父元素
     */
    getCurrentParentByTag(tag) {
      return this.editor.dom.getParent(this.editor.selection.getNode(), tag)
    },

    /**
     * 获取基础span属性
     */
    getSpanNodeAttrs(item, tag = '') {
      return this.getSpanNodeAttrsByType(item, tag, '')
    },

    getSpanNodeAttrsByType(item, tag = '', type) {
      const attrs = {}
      const dataKey = `data-lego${tag ? `-${tag}` : ''}-tag`
      const dataValue = `${item.code}`

      attrs[dataKey] = dataValue
      attrs.class = `lego-print-tag ${this.getSpanColorClass()}`
      attrs.contenteditable = false
      return attrs
    },

    getSpanNode(item, tag = '', type) {
      return `<span data-lego${tag ? `-${tag}` : ''}-tag="${item.code}" class="lego-print-tag ${this.getSpanColorClass()}" contenteditable="false">{${item.name}}</span>`
    },

    /**
     * span 颜色
     */
    getSpanColorClass() {
      return `lego-tiny-color--common`
    },

    /**
     * 预览
     */
    perviewClick() {
      this.editor.execCommand('mcePreview')
    },

    /**
     * 保存
     */
    saveClick() {
      this.loading = true
      printTemplateDesignAPI({
        code: this.$route.params.templateCode,
        content: this.content
      }).then(() => {
        this.loading = false
        this.$message.success('保存成功')
      }).catch(() => {
        this.loading = false
      })
    },

    /**
     * 保存
     */
    goBackClick() {
      this.$router.go(-1)
    },

    /** 富文本配置 */
    getEditConfig() {
      return {
        menubar: false,
        toolbar_sticky: true,
        statusbar: false,
        // extended_valid_elements: 'span[class|title|legotag|style|contenteditable]',
        content_style: 'p { margin: 5px 0; line-height: 1.5;}',
        content_css: ['./tinymce/css/tiny-colors.css', './tinymce/css/tiny-word.css'],
        table_advtab: false,
        table_cell_advtab: false,
        table_row_advtab: false,
        setup: (editor) => {
          editor.on('init', function(e) {
            editor.execCommand('mceFocus')
          })
          editor.on('keydown', (e) => {
            this.selectNodes = null
            if (e.keyCode === 8 /* Backspace key */ || /* del key */ e.keyCode == 46) {
              const currentNode = editor.selection.getNode()
              if (currentNode.getAttribute('data-lego-table-td-tag') === 'value') {
                e.preventDefault()
                return false
              } else if (currentNode.nodeName == 'SPAN') {
                if (currentNode.hasAttribute('contenteditable')) {
                  e.preventDefault()
                  editor.dom.remove(currentNode)
                  return false
                } else {
                  const farterSpan = this.getCurrentParentByTag('span[contenteditable]')
                  if (farterSpan) {
                    e.preventDefault()
                    editor.dom.remove(farterSpan)
                    return false
                  }
                }
              }
            } else if (e.keyCode == '65' && (e.metaKey || e.ctrlKey)) { // 全选
              this.selectNodes = editor.selection.getNode()
              this.setSpanEditAttr(this.selectNodes, true)
            } else {
              // table 里面不允许写内容
              const currentNode = editor.selection.getNode()
              if (currentNode.getAttribute('data-lego-table-td-tag') === 'value') {
              // 只允许输入上下左右
                if (e.keyCode !== 37 && e.keyCode !== 38 && e.keyCode !== 39 && e.keyCode !== 40) {
                  e.preventDefault()
                  return false
                }
              } else if (currentNode.nodeName == 'SPAN') {
                if (currentNode.hasAttribute('contenteditable')) {
                  if (currentNode.getAttribute('contenteditable')) {
                    e.preventDefault()
                    currentNode.setAttribute('contenteditable', false)
                    return false
                  }
                  return false
                } else {
                  const farterSpan = this.getCurrentParentByTag('span[contenteditable]')
                  if (farterSpan) {
                    if (farterSpan.getAttribute('contenteditable')) {
                      e.preventDefault()
                      farterSpan.setAttribute('contenteditable', false)
                      return false
                    }
                    return false
                  }
                }
              }

              if (e.keyCode == 37 && e.keyCode == 38 && e.keyCode == 39 && e.keyCode == 40) {
                this.cancelSpanEdit()
              }
            }
          })

          editor.on('mousedown', (e) => {
            this.cancelSpanEdit()
          })

          editor.on('mouseup', (e) => {
            const selection = editor.selection.getSel()
            if (e.target.hasAttribute('contenteditable') && selection.anchorOffset == 1 && selection.anchorOffset == 1) {
              // 忽略
            } else if (e.target.hasAttribute('contenteditable') && selection.isCollapsed) {
              this.cancelSpanEdit()
            } else {
              if (!selection.isCollapsed) {
                this.selectNodes = editor.selection.getNode()
                this.setSpanEditAttr(this.selectNodes, true)
              } else {
                this.cancelSpanEdit()
              }
            }
          })
        }
      }
    },

    /**
     * 取消编辑
     */
    cancelSpanEdit() {
      if (this.selectNodes) {
        setTimeout(() => {
          const selection = this.editor.selection.getSel()
          if (!selection.isCollapsed) {
            this.setSpanEditAttr(this.selectNodes, false)
            this.selectNodes = null
          }
        }, 300)
      }
    },

    /**
     * 操控span是否可编辑
     */
    setSpanEditAttr(node, canEdit) {
      if (node && node.hasAttribute('contenteditable')) {
        if (node.getAttribute('contenteditable') != canEdit) {
          node.setAttribute('contenteditable', canEdit)
        }
      }

      if (node && node.children) {
        for (let index = 0; index < node.children.length; index++) {
          const element = node.children[index]
          if (element.children) {
            this.setSpanEditAttr(element, canEdit)
          } else if (element.hasAttribute('contenteditable')) {
            if (node.getAttribute('contenteditable') != canEdit) {
              element.setAttribute('contenteditable', canEdit)
            }
          }
        }
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.print-template-detail {
  height: 100%;
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  padding: 0 15px;

  &__body {
    flex: 1;
    position: relative;
    display: flex;
    overflow: hidden;
  }
}

.system-view-nav {
  width: 300px;
  position: relative;
  background: #fff;
  margin-right: 10px;
  padding-top: 10px;
  border: 1px solid $xr-border-line-color;
  border-radius: $xr-border-radius-base;

  ::v-deep .el-tabs {
    padding: 0 5px;
    border-bottom: 1px solid #E4E7ED;

    .el-tabs__nav-wrap::after {
      display: none;
    }
    .el-tabs__header {
      margin-bottom: 0;
    }
  }
}
.system-view-content {
  flex: 1;
  border: 1px solid $xr-border-line-color;
  border-radius: $xr-border-radius-base;
  background: #fff;
  display: flex;
  flex-direction: column;
  overflow-x: auto;
  ::v-deep .tox-tinymce {
    border: none;
  }
}

// 菜单
.menus {
  height: calc(100% - 90px);
  overflow-y: auto;
}
.menu-item {
  color: #333;
  font-size: 13px;
  padding: 0 15px;
  height: 40px;
  line-height: 40px;
  cursor: pointer;
  position: relative;
  .icon-close {
    position: absolute;
    top: 0;
    right: 8px;
    z-index: 1;
    display: none;
  }
}

.menu-item:hover,
.menu-item.is-select {
  background-color: $xr--background-color-base;
}

.menu-item:hover::before,
.menu-item.is-select::before {
  content: ' ';
  position: absolute;
  top: 0;
  left: 0;
  bottom: 0;
  width: 2px;
  background-color: #5383ed;
}

.xr-reminder {
  ::v-deep .reminder-body {
    align-items: stretch;
    width: 100%;
    height: 35px;
    line-height: 35px;
    border-radius: 0;
  }
}
</style>
