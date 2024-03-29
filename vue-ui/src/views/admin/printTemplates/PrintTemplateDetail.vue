<template>
  <div v-loading="loading" class="print-template-detail">
    <xr-header
      :icon-class="'print'"
      icon-color="#26d4da"
      label="新建打印模板" >
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
        <el-tabs v-model="activeTab" @tab-click="tabsClick">
          <el-tab-pane
            v-for="(item, index) in tabs"
            :key="index"
            :label="item.label"
            :name="item.key"/>
        </el-tabs>
        <reminder
          content="点击字段插入值（光标所在位置）"
          class="xr-reminder"/>
        <div class="menus">
          <div
            v-for="(item, index) in menuList"
            :key="index"
            :class="{'is-select' : item.fieldName == menuIndex}"
            class="menu-item"
            @click="menuSelect(item, index)">
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
  printQueryFieldsAPI,
  printAddTemplateAPI,
  printUpdateTemplateAPI,
  printTemplateByIdAPI
} from '@/api/admin/printTemplate'

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
      activeTab: '',
      tabs: [],
      tabData: {},
      menuIndex: '',
      richHeight: document.documentElement.clientHeight - 135,
      content: '',

      // 控制选择的内容
      selectNodes: null
    }
  },

  computed: {
    menuList() {
      return this.tabData[this.activeTab] || []
    },

    // 模板类型
    type() {
      return this.$route.query.type
    },

    isCreate() {
      return this.$route.query.handle == 'create'
    },

    editor() {
      return this.$refs.createTinymce.editor
    }
  },

  created() {
    this.getFields(this.type)
    if (!this.isCreate) {
      this.getDetail()
    }
  },

  methods: {
    /** *
     * 获取详情
     */
    getDetail() {
      this.loading = true
      printTemplateByIdAPI({
        templateId: this.$route.query.templateId
      }).then(res => {
        const data = res.data || {}
        this.content = data.content

        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },

    /**
     *获取字段数据
     */
    getFields(type) {
      this.loading = true
      printQueryFieldsAPI({
        type
      }).then(res => {
        const data = res.data || {}
        this.tabData = data

        const tabs = []
        tabs.push({
          key: 1,
          label: 1
        })

        const defaultSort = ['customer', 'contacts', 'product', 'business', 'contract', 'receivables', 'supplier']
        defaultSort.forEach(key => {
          if (key != currentKey && data[key]) {
            const temp = { key }
            temp.label = 1
            tabs.push(temp)
          }
        })
        this.tabs = tabs
        if (tabs.length > 0) {
          this.activeTab = tabs[0].key
        } else {
          this.activeTab = ''
        }
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },

    /** 左侧操作 */
    tabsClick() {
      this.menuIndex = ''
    },

    menuSelect(item, index) {
      // console.log(this.editor.selection.getNode())
      this.menuIndex = item.fieldName
      // 整单折扣 产品总金额 不能插入
      if (this.activeTab == 'product' && item.fieldName != 'discount_rate' && item.fieldName != 'total_price') {
        const tableParent = this.getCurrentParentByTag('table[data-lego-table-tag="table"]')
        if (tableParent) {
          const headerTr = this.editor.dom.select('tr[data-lego-table-tr-tag="header"]', tableParent)
          const valueTr = this.editor.dom.select('tr[data-lego-table-tr-tag="value"]', tableParent)
          if (headerTr && valueTr) {
            this.editor.dom.add(headerTr, 'td', { 'data-lego-table-td-tag': 'name' }, item.name)
            this.editor.dom.add(valueTr, 'td', { 'data-lego-table-td-tag': 'value' }, this.getSpanNode(item, 'table-value'))
            this.content = this.editor.getContent({ format: 'html' })
          }
        } else {
          if (!this.getCurrentParentByTag('table')) {
            this.editor.insertContent(this.getProductTable(item))
          }
        }
      } else {
        const tableParent = this.getCurrentParentByTag('table[data-lego-table-tag="table"]')
        if (!tableParent) {
          // 产品下字段 整单折扣 产品总金额 用商机和合同的

          // const parentPNode = this.getCurrentParentByTag('p')
          if (this.activeTab == 'product' && (item.fieldName == 'discount_rate' || item.fieldName == 'total_price')) {
            this.editor.insertContent(this.getSpanNode(item, '', 1))
          } else {
            this.editor.insertContent(this.getSpanNode(item))
          }
          this.content = this.editor.getContent({ format: 'html' })
        }
      }
    },


    /**
     * 根据选择器获取父元素
     */
    getCurrentParentByTag(tag) {
      return this.editor.dom.getParent(this.editor.selection.getNode(), tag)
    },


    /**
     * 获取产品表
     */
    getProductTable(item) {
      return `<table data-lego-table-tag="table" style="border-collapse: collapse; width: 100%;" border="1" >
      <tbody>
        <tr data-lego-table-tr-tag="header"><td data-lego-table-td-tag="name">${item.name}</td></tr>
        <tr data-lego-table-tr-tag="value"><td data-lego-table-td-tag="value">${this.getSpanNode(item, 'table-value')}</td></tr>
      </tbody>
      </table>`
    },

    /**
     * 获取基础span属性
     */
    getSpanNodeAttrs(item, tag = '') {
      return this.getSpanNodeAttrsByType(item, tag, this.activeTab)
    },

    getSpanNodeAttrsByType(item, tag = '', type) {
      const attrs = {}
      const dataKey = `data-lego${tag ? `-${tag}` : ''}-tag`
      const dataValue = `${type}.${item.fieldName}`

      attrs[dataKey] = dataValue
      attrs.class = `lego-print-tag ${this.getSpanColorClass()}`
      attrs.contenteditable = false
      return attrs
    },

    getSpanNode(item, tag = '', type = this.activeTab) {
      return `<span data-lego${tag ? `-${tag}` : ''}-tag="${type}.${item.fieldName}" class="lego-print-tag ${this.getSpanColorClass()}" contenteditable="false">{${item.name}}</span>`
    },

    /**
     * span 颜色
     */
    getSpanColorClass() {
      const color = ['customer', 'contacts', 'business', 'contract', 'receivables', 'product', 'supplier'].includes(this.activeTab) ? this.activeTab : 'common'
      return `lego-tiny-color--${color}`
    },

    /** 顶部操作 */

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
      const request = this.isCreate ? printAddTemplateAPI : printUpdateTemplateAPI
      const data = this.$route.query
      const params = {
        templateName: data.templateName,
        type: data.type,
        content: this.content
      }

      if (!this.isCreate) {
        params.templateId = data.templateId
      }

      request(params)
        .then(res => {
          this.loading = false
          if (this.isCreate) {
            this.$message({
              type: 'success',
              message: '新建成功'
            })
            this.$router.go(-1)
          } else {
            this.$message({
              type: 'success',
              message: '保存成功'
            })
          }
        })
        .catch(() => {
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
        content_css: ['./static/tinymce/css/tiny-colors.css', './static/tinymce/css/tiny-word.css'],
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
                // console.log('上下左右')
                this.cancelSpanEdit()
              }
            }
          })

          // editor.on('focusin', function(e) {
          //   console.log('focusin')
          // })

          // editor.on('focusout', function(e) {
          //   console.log('focusout')
          // })

          // editor.on('click', function(e) {
          //   console.log('click')
          // })

          // editor.on('blur', (e) => {
          //   console.log('blur')
          // })

          editor.on('mousedown', (e) => {
            this.cancelSpanEdit()
          })

          editor.on('mouseup', (e) => {
            const selection = editor.selection.getSel()
            // console.log(selection.isCollapsed, selection.anchorNode == selection.focusNode, selection.anchorOffset == selection.focusOffset)

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
