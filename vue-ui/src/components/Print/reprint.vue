<template>
  <el-container
    v-loading="loading"
    :class="{ 'is-perview': isPreview }"
    class="lego-print-container">
    <el-header>
      <template v-if="isPreview">
        <el-button type="primary" @click="handleClick('download')">{{ `${fileTypeName} 下载` }}</el-button>
        <el-button type="primary" @click="handleClick('print')">打印</el-button>
        <el-button @click="handleClick('goback')">取消</el-button>
      </template>
      <template v-else>
        <span class="select-label">选择文件类型</span>
        <el-select
          v-model="fileType"
          style="width: 80px;"
          class="handle-item-content">
          <el-option
            v-for="item in fileTypeList"
            :key="item"
            :label="item"
            :value="item"/>
        </el-select>
        <el-button class="preview-button" type="primary" @click="printPreviewClick">打印预览</el-button>
      </template>
    </el-header>
    <el-main>
      <div class="main-container">
        <div class="main-content">
          <iframe
            v-show="isPreview"
            id="printFrame"
            :height="iframeHeight"
            :src="iframeUrl"
            style="width: 100%;margin-top: 54px;"
            scrolling="no"
            frameborder="0"/>
        </div>
        <div v-show="!isPreview" class="main-tips">您可以在当前页面对模版文字进行调整，确认无误后可进行打印</div>
        <div v-show="!isPreview" class="main-content">
          <tinymce
            ref="editor" :toolbar="[]" v-model="content" :init="{
              statusbar: false,
              plugins: 'print autoresize',
              extended_valid_elements: 'span[class|title|wktag|style|contenteditable]',
              content_style: ' body {padding: 60px !important;width: 595px; margin: 0 auto;} p { margin: 5px 0;}',
          }" class="rich-txt" />
        </div>
      </div>
    </el-main>
  </el-container>
</template>

<script>
import { printLogGetAPI } from '@/api/printLog'
import { printTemplatePreviewAPI } from '@/api/admin/printTemplate'

import Tinymce from '@/components/Tinymce'
import { downloadFileWithBuffer } from '@/utils'

export default {
  name: 'Print',
  components: {
    Tinymce
  },
  props: {},
  data() {
    return {
      loading: false,
      isPreview: false, // 是编辑 还是打印预览
      fileTypeList: ['pdf'],
      fileType: 'pdf',
      content: '',
      // 预览
      iframeUrl: '',
      iframeHeight: document.documentElement.clientHeight - 170,
      srcData: null
    }
  },
  computed: {
    fileTypeName() {
      return {
        pdf: 'PDF'
      }[this.fileType]
    }
  },
  created() {
    this.getContent()
    window.addEventListener('resize', this.changeIframeHeight)
  },
  beforeRouteUpdate(to, from, next) {
    this.content = ''
    next()
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.changeIframeHeight)
  },
  methods: {
    changeIframeHeight() {
      this.iframeHeight = document.documentElement.clientHeight - 170
    },
    /**
     * 详情
     */
    getContent() {
      printLogGetAPI(this.$route.params.code).then(res => {
        this.loading = false
        this.content = res.data.content
      }).catch(() => {
        this.loading = false
      })
    },

    /**
       * 打印
       */
    compactPrint() {
      this.$refs.editor.editor.execCommand('mcePrint')
      this.savePrintRecord()
    },

    /**
     * 打印预览
     */
    printPreviewClick() {
      this.loading = true
      printTemplatePreviewAPI({
        content: this.content,
        fileType: this.fileType
      }).then(res => {
        this.loading = false
        this.srcData = res.data
        this.iframeUrl = window.URL.createObjectURL(res.data)
        this.isPreview = true
      }).catch(() => {
        this.loading = false
      })
    },

    hidenView() {
      this.$emit('close')
    },

    /** 预览页面逻辑 */

    handleClick(type) {
      if (type == 'goback') {
        this.isPreview = false
      } else if (type == 'print') {
        document.getElementById('printFrame').contentWindow.print() // 调用浏览器的打印功能打印指定区域
      } else if (type == 'download') {
        if (this.srcData) {
          var ext = this.fileType === 'word' ? 'doc' : 'pdf'
          const blob = new Blob([this.srcData])
          downloadFileWithBuffer(blob, `文档.${ext}`)
        }
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.lego-print-container {
  .el-header {
    background-color: white;
    box-shadow: 0px 1px 2px #dbdbdb;
    line-height: 60px;
    padding: 0 70px;
    z-index: 1;
    position: relative;

    .select-label {
      margin-right: 15px;
    }

    .el-select + .select-label {
      margin-left: 30px;
    }
  }

  .preview-button {
    position: absolute;
    right: 70px;
    top: 16px;
  }

  .el-main {
    padding: 0;
  }

  &.is-perview {
    .el-header {
      text-align: right;
    }
  }
}

.main-container {
  height: 100%;
  overflow: auto;

  .main-tips {
    text-align: center;
    font-size: 13px;
    margin: 20px 0;
    color: #BFBFBF;
  }

  .main-content {
    width: 66%;
    margin: 0 auto;
  }
}

</style>
