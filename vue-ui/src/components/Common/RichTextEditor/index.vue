<template>
  <div class="setting-rich-text">
    <tinymce
      ref="createTinymce"
      v-model="content"
      :init="getEditConfig()"
      :height="250"
      :uploadAPI="uploadAPI"
      :previewUrl="previewUrl"
      class="rich-txt"/>
  </div>
</template>

<script>
import Tinymce from '@/components/Tinymce'

export default {
  name: 'RichTextEditor',
  components: {
    Tinymce
  },
  props: {
    value: {
      type: String,
      default: ''
    },
    disabled: {
      type: Boolean,
      default: false
    },
    uploadAPI: Function,
    previewUrl: String
  },
  data() {
    return {
      content: ''
    }
  },
  watch: {
    content() {
      this.editInputChange()
    }
  },
  mounted() {
    this.content = this.value
  },
  methods: {
    getEditConfig() {
      return {
        menubar: false,
        statusbar: false,
        toolbar_sticky: true,
        content_editable: !this.disabled,
        paste_data_images: true, // 允许粘贴图片
        paste_enable_default_filters: false,
        paste_retain_style_properties: 'border', // 粘贴内容时要保留的样式
        content_style: 'p { margin: 5px 0; line-height: 1.5;}',
        table_advtab: false,
        table_cell_advtab: false,
        table_row_advtab: false,
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
    },
    editInputChange() {
      this.$emit('input', this.content)
      this.$emit('change', this.content)
    }
  }
}
</script>

<style scoped>

</style>
