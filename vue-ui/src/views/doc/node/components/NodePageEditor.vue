<template>
  <div v-loading="loading">
    <flexbox
      :style="{zIndex: maxIndex}"
      align="flex-start"
      justify="flex-start"
      class="page-editor">
      <xr-header
        icon-class="doc"
        icon-color="#26d4da"
        :label="nodePage.name" >
        <template slot="ft">
          <el-button
            type="text"
            @click="perviewClick">预览</el-button>
          <el-button
            v-debounce="saveClick"
            type="primary">发布</el-button>
          <el-button
            @click="goBackClick">返回</el-button>
        </template>
      </xr-header>
      <div class="page-editor__body">
        <tinymce
          ref="createTinymce"
          v-model="pageContent"
          :toolbar="['undo redo | bold italic underline strikethrough | charmap emoticons | fontselect fontsizeselect formatselect | rowspacingtop rowspacingbottom | lineheight | alignleft aligncenter alignright alignjustify | outdent indent |  numlist bullist | table | forecolor backcolor removeformat | pagebreak | fullscreen  preview save print | insertfile image media link anchor codesample | ltr rtl | paste | uploadFile']"
          :init="getEditConfig()"
          :height="richHeight"
          :uploadAPI="tinymceRequestAPI"
          :previewUrl="tinymcePreviewUrl"
          class="rich-txt" />
      </div>
    </flexbox>
  </div>
</template>

<script>
import { pageModifyAPI } from '@/api/doc/page'
import { fileUploadAPI, filePreviewUrl } from '@/api/doc/file'
import XrHeader from '@/components/XrHeader'
import Reminder from '@/components/Reminder'
import Tinymce from '@/components/Tinymce'
import { getMaxIndex } from '@/utils/index'

export default {
  name: 'DocNodePageEditor',
  components: {
    XrHeader,
    Reminder,
    Tinymce
  },
  props: {
    nodePage: {
      type: Object,
      required: true,
      default: function() {
        return {
          name: '',
          content: ''
        }
      }
    }
  },
  data() {
    return {
      loading: false,
      richHeight: document.documentElement.clientHeight - 135,
      pageContent: '',
      tinymceRequestAPI: fileUploadAPI,
      tinymcePreviewUrl: filePreviewUrl
    }
  },
  computed: {
    editor() {
      return this.$refs.createTinymce.editor
    },
    maxIndex() {
      return getMaxIndex()
    }
  },
  created() {
    this.pageContent = this.nodePage.content
  },
  methods: {
    perviewClick() {
      this.editor.execCommand('mcePreview')
    },
    saveClick() {
      this.loading = true
      pageModifyAPI({
        code: this.nodePage.code,
        content: this.pageContent
      }).then(() => {
        this.loading = false
        this.$message.success('保存成功')
        this.$emit('onSuccess')
      }).catch(() => {
        this.loading = false
      })
    },
    goBackClick() {
      this.$emit('onBack')
    },
    getEditConfig() {
      return {
        menubar: false,
        toolbar_sticky: true,
        statusbar: false,
        paste_data_images: true, // 允许粘贴图片
        paste_enable_default_filters: false,
        placeholder: '点击开始输入正文',
        content_style: ' * {color: #262626;}',
        paste_retain_style_properties: 'border', // 粘贴内容时要保留的样式
        toolbar_mode: 'floating',
        setup: (editor) => {
          // 处理点击附件名称操作
          let url = ''
          let name = ''
          editor.ui.registry.addContextToolbar('customToolbar', {
            items: 'previewFile ｜ downloadFile',
            predicate: (node) => {
              if (node.tagName == 'SPAN' && node.getAttribute('data-wk-file-type') == 'file') {
                url = node.getAttribute('data-wk-file-url')
                name = node.innerText
              }
              return node.tagName == 'SPAN' && node.getAttribute('data-wk-file-type') == 'file'
            }
          })

          editor.ui.registry.addButton('previewFile', {
            text: '<span style="cursor: pointer;">预览</span>',
            onAction: () => {
              this.preview({
                url,
                name
              })
            }
          })

          editor.ui.registry.addButton('downloadFile', {
            text: '<span style="cursor: pointer;">下载</span>',
            onAction: () => {
              this.downloadFile({
                path: url,
                name
              })
            }
          })

          // toolbar添加附件上传按钮
          editor.ui.registry.addButton('uploadFile', {
            text: `<i class="lego lego-file" style="font-size: 22px"></i>`,
            tooltip: '上传附件',
            onAction: () => {
              // this.$refs.fileInput.click()
              this.$message.error('暂未支持附件功能')
            }
          })
        },
        // paste_word_valid_elements: 'b,strong,i,em,h1,h2',
        paste_preprocess: function(plugin, args) {
          // 删除部分标签
          var delTag = ['b', 'strong', 'i', 'em']
          delTag.forEach(tag => {
            var reg = new RegExp(`(<${tag}>)|(</${tag}>)]`, 'g')
            args.content = args.content.replace(reg, '')
          })
          // 替换部分标签
          var replaceTag = ['h1', 'h2', 'h3', 'h4', 'h5', 'h6']
          replaceTag.forEach(tag => {
            var reg1 = new RegExp(`<${tag}>`, 'g')
            var reg2 = new RegExp(`</${tag}>`, 'g')
            args.content = args.content.replace(reg1, '<p>')
            args.content = args.content.replace(reg2, '</p>')
          })
          // 删除所有font标签
          args.content = args.content.replace(/<\/font>/ig, '').replace(/<font[^>]+>/ig, '')
        },
        paste_postprocess: function(plugin, args) {
          var doms = Array.from(args.node.querySelectorAll('*'))
          // 删除字体样式
          doms.forEach(dom => {
            dom.style.color = ''
            dom.style.fontWeight = ''
            dom.style.fontFamily = ''
            dom.style.fontSize = ''
            dom.style.background = ''
          })
        }
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.page-editor {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  min-width: 1250px;
  padding: 0px 50px 10px;
  background-color: white;
  box-sizing: border-box;
  flex-direction: column;

  &__body {
    flex: 1;
    border: 1px solid $xr-border-line-color;
    border-radius: $xr-border-radius-base;
    background: #fff;
    display: flex;
    flex-direction: column;
    overflow-x: auto;
    ::v-deep .tox-tinymce {
      border: none;
      height: 100% !important;
    }
  }
}
</style>
