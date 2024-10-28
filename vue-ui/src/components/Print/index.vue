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
        <span class="select-label">选择模版</span>
        <el-select
          v-model="templateCode"
          style="width: 170px;"
          class="handle-item-content"
          @change="getContent">
          <el-option
            v-for="item in templateList"
            :key="item.code"
            :label="item.name"
            :value="item.code"/>
        </el-select>
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
import {
  printTemplateSimpleListAPI,
  printTemplatePrintAPI,
  printTemplatePreviewAPI
} from '@/api/admin/printTemplate'
import { printLogAddAPI } from '@/api/printLog'
import { customFormGetAPI } from '@/api/admin/formField'
import { codeGetRequest } from '@/api/crm/common'
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
      templateList: [],
      templateCode: '',
      permissionCode: '',
      params: {},
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
    this.getDetail()
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
    getDetail() {
      this.loading = true
      customFormGetAPI(this.$route.params.formCode).then(res => {
        this.permissionCode = res.data.permissionCode
        codeGetRequest(res.data.detailApiUrl, this.$route.params.detailCode).then(res => {
          this.params = res.data
          this.getTemplateList()
        })
      }).catch(() => {
        this.loading = false
      })
    },
    /**
     * 获取模板配置
     */
    getTemplateList() {
      printTemplateSimpleListAPI({
        formCode: this.$route.params.formCode
      }).then(res => {
        this.templateList = res.data || []
        if (this.templateList.length) {
          this.templateCode = this.templateList[0].code
          this.getContent()
        } else {
          this.templateCode = ''
          this.loading = false
        }
      }).catch(() => {
        this.loading = false
      })
    },

    /**
     * 详情
     */
    getContent() {
      printTemplatePrintAPI({
        code: this.templateCode,
        params: this.params
      }).then(res => {
        this.loading = false
        this.content = res.data
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

    /**
     *保存打印记录
     */
    savePrintRecord() {
      if (this.templateCode) {
        printLogAddAPI({
          content: this.content,
          entityCode: this.$route.params.detailCode,
          templateCode: this.templateCode,
          permissionCode: this.permissionCode
        }).then(() => {})
      }
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
        this.savePrintRecord()
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
