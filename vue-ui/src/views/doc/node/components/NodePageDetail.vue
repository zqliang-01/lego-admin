<template>
  <div class="page-container" v-loading="loading">
    <node-detail-head
      :node-detail="nodePage"
      @onClickEdit="openEdit = !openEdit"
      @onDeleteSuccess="handleDelete"
      @onExport="handleExport"
    />
    <div class="content">
      <div
        v-if="!pageDetail.content"
        v-empty="!pageDetail.content"
        xs-empty-text="这个文档是空的"
        class="empty">
      </div>
      <tinymce
        v-else
        ref="createTinymce"
        :value="pageDetail.content"
        :toolbar="[]"
        :plugins="[]"
        :init="getEditConfig()"
        :uploadAPI="tinymceRequestAPI"
        :previewUrl="tinymcePreviewUrl"
        class="rich-txt" />
    </div>
    <node-page-editor
      v-if="openEdit"
      :node-page="pageDetail"
      @onBack="openEdit = !openEdit"
      @onSuccess="handleSuccess"
    />
  </div>
</template>
<script>
import { nodeGetAPI } from '@/api/doc/node'
import { pageGetAPI } from '@/api/doc/page'
import { fileUploadAPI, filePreviewUrl } from '@/api/doc/file'
import NodeDetailHead from './NodeDetailHead'
import NodePageEditor from './NodePageEditor'
import Tinymce from '@/components/Tinymce'
import { exportToWord } from './exportToWord'

export default {
  name: 'DocpageDetail',
  components: {
    NodeDetailHead,
    NodePageEditor,
    Tinymce
  },
  props: {
    node: {
      type: Object,
      required: true,
      default: function() {
        return {
          name: '',
          createTime: '',
          creator: {}
        }
      }
    }
  },
  data() {
    return {
      loading: false,
      openEdit: false,
      nodePage: {
        name: '',
        createTime: '',
        creator: {}
      },
      pageDetail: {
        name: '',
        content: ''
      },
      tinymceRequestAPI: fileUploadAPI,
      tinymcePreviewUrl: filePreviewUrl
    }
  },
  watch: {
    node: {
      handler(val) {
        this.init()
      },
      deep: true,
      immediate: true
    }
  },
  methods: {
    init() {
      nodeGetAPI(this.node.code).then(res => {
        this.nodePage = res.data
      })
      this.loading = true
      pageGetAPI(this.node.page.code).then(res => {
        this.pageDetail = res.data
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    handleSuccess() {
      this.openEdit = false
      this.init()
    },
    handleDelete() {
      this.$emit('onDelete')
    },
    handleExport() {
      this.loading = true
      exportToWord(this.pageDetail.name, this.pageDetail.content).then(() => {
        this.loading = false
      }).catch(res => {
        this.loading = false
        this.$message.error(res)
      })
    },
    getEditConfig() {
      return {
        menubar: false,
        toolbar_sticky: true,
        statusbar: false,
        content_editable: false,
        quickbars_selection_toolbar: false,
        content_style: ' * {color: #262626; outline: unset !important} img { max-width: 100%; height: auto; }',
        plugins: 'autoresize',
        contextmenu: false,
        mage_advtab: false,
        table_responsive_width: false,
        object_resizing: false,
        setup: (editor) => {
          // 文档禁止编辑
          editor.on('init', () => {
            editor.getBody().setAttribute('contenteditable', 'false')
          })
          editor.on('SetContent', function() {
          })
        }
      }
    }
  }
}
</script>
<style scoped lang="scss">
.page-container {
  height: 100%;
  width: 100%;
  .main-top {
    .title {
      color: $xr-color-text-secondary;
      span:first-child {
        margin-right: 4px;
      }
    }
    .control {
      .dropdown-btn {
        padding: 8px;
      }
      .el-dropdown {
        margin-left: 8px;
      }
      .control-btn {
        color: $xr-color-text-regular;
      }
      .control-btn.active {
        color: $xr-color-primary;
        ::v-deep i {
          color: $xr-color-primary;
        }
      }
      ::v-deep .lego-focus-on.collected {
        color: #fac23d;
      }
    }
  }
  .content {
    width: 100%;
    height: calc(100% - 30px);
    overflow-y: auto;
    .empty {
      display: flex;
      align-items: center;
      justify-content: center;
      height: 70%;
    }
  }
  ::v-deep .tox-tinymce {
    border: 0 none;
  }
}
</style>
