<template>
  <div class="node">
    <flexbox class="node-header">
      <img v-src="bookCover" class="corver-img" alt="封面图">
      <div class="title">
        <span class="title-name">{{ bookDetail.name }}</span>
        <span class="title-description">{{ bookDetail.description }}</span>
      </div>
      <div class="back-btn">
        <el-button type="primary" @click="handleGoBack">返回</el-button>
      </div>
    </flexbox>
    <div class="node-body">
      <div class="node-content">
        <div class="node-body-side">
          <node-tree
            :book-code="bookCode"
            :tree-list="treeList"
            :editable="bookDetail.editable"
            :current-node-code="currentNodeCode"
            @onNodeClick="handleNodeClick"
            @onRefresh="handleTreeRefresh" />
        </div>
        <div class="node-body-content">
          <node-page-detail
            v-if="currentNode.type === 'page'"
            :node="currentNode"
            @onDelete="handleNodeDelete"
          />
          <node-file-detail
            v-if="currentNode.type === 'file'"
            :node="currentNode"
            @onDelete="handleNodeDelete"
            @onRename="handleRename"
          />
          <node-folder-detail
            v-if="currentNode.type === 'folder'"
            :node="currentNode"
            @onNodeClick="handleNodeClick"
            @onDelete="handleNodeDelete"
            @onRename="handleRename"
          />
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import { nodeListAPI } from '@/api/doc/node'
import { bookGetAPI } from '@/api/doc/book'
import { filePreviewUrl } from '@/api/doc/file'
import NodeTree from './components/NodeTree'
import NodePageDetail from './components/NodePageDetail'
import NodeFileDetail from './components/NodeFileDetail'
import NodeFolderDetail from './components/NodeFolderDetail'

export default {
  name: 'DocBook',
  components: {
    NodeTree,
    NodePageDetail,
    NodeFileDetail,
    NodeFolderDetail
  },
  computed: {
    bookCover() {
      if (this.bookDetail && this.bookDetail.cover) {
        return filePreviewUrl + this.bookDetail.cover
      }
      return this.coverImage
    },
    bookCode() {
      return this.$route.params.bookCode
    },
    currentNodeCode() {
      if (this.currentNode && this.currentNode.code) {
        return this.currentNode.code
      }
      if (this.$route.params.nodeCode) {
        return this.$route.params.nodeCode
      }
      return ''
    }
  },
  data() {
    return {
      loading: false,
      bookDetail: {
        name: '',
        creator: {
          name: ''
        },
        description: ''
      },
      treeList: [],
      currentNode: {},
      coverImage: require('@/assets/img/doc/book_cover.png')
    }
  },
  created() {
    this.onRefresh()
  },
  methods: {
    onRefresh() {
      bookGetAPI(this.bookCode).then(res => {
        this.bookDetail = res.data
      })
      this.handleTreeRefresh()
    },
    handleTreeRefresh(code) {
      nodeListAPI({
        bookCode: this.bookCode
      }).then(res => {
        this.treeList = res.data
        if (this.$route.params.nodeCode || code) {
          this.setCurrentNode(this.treeList, code || this.$route.params.nodeCode)
        }
      })
    },
    handleRename(name) {
      this.$set(this.currentNode, 'name', name)
    },
    setCurrentNode(tree, currentCode) {
      tree.forEach(element => {
        if (element.code === currentCode) {
          this.currentNode = element
        } else if (element.childrens) {
          this.setCurrentNode(element.childrens, currentCode)
        }
      })
    },
    handleNodeDelete() {
      this.currentNode = {}
      this.handleTreeRefresh()
    },
    handleNodeClick(data) {
      this.currentNode = data
    },
    handleGoBack() {
      this.$router.go(-1)
    }
  }
}
</script>
<style lang="scss" scoped>
.node {
  height: 100%;
  &-header {
    padding-left: 25px;
    height: 60px;
    .corver-img {
      flex-shrink: 0;
      width: 100px;
      height: 50px;
      overflow: hidden;
      border-radius: 3px;
    }
    span {
      float: left;
    }
    .title {
      width: 100%;
      margin-left: 5px;
      display: inline-grid;
      &-name {
        color: #333;
        font-size: 16px;
        font-weight: 600;
      }
      &-description {
        color: #999;
        font-size: 12px;
        margin-top: 5px;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
      }
    }
    .back-btn {
      text-align: right;
      width: 100px;
      margin-right: 20px;
    }
  }
  &-body {
    position: relative;
    height: calc(100% - 75px);
    padding-left: 15px;
    .node-content {
      position: relative;
      height: 100%;
      .node-body-side {
        width: 240px;
        font-size: 14px;
        background-color: white;
        position: absolute;
        top: 0;
        left: 0;
        bottom: 0;
        z-index: 1;
        border: 1px solid #e6e6e6;
        border-radius: 4px;
      }
      .node-body-content {
        margin-left: 250px;
        height: 100%;
        padding: 10px;
        overflow: hidden;
        background-color: white;
        border: 1px solid #e6e6e6;
        border-radius: 4px 0px 0px 4px;
      }
    }
  }
}
</style>
