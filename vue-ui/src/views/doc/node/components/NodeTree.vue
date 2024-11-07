<template>
  <div class="node-tree">
    <div class="node-tree-head">
      <i class="lego lego-catalog" />
      <div class="node-tree-head-item">目录</div>
      <div class="node-tree-head-btn">
        <el-dropdown
          v-if="editable"
          placement="bottom-start"
          trigger="click"
          @command="handleCommandClick">
          <el-button
            type="text">
            <i class="el-icon-circle-plus" />
          </el-button>
          <el-dropdown-menu slot="dropdown">
            <el-tooltip effect="dark" content="创建可在线编辑的文档" placement="right">
              <el-dropdown-item command="page" icon="lego lego-doc">
                创建文档
              </el-dropdown-item>
            </el-tooltip>
            <el-tooltip effect="dark" content="创建文件夹管理文档与文件" placement="right">
              <el-dropdown-item command="folder" icon="lego lego-folder">
                创建文件夹
              </el-dropdown-item>
            </el-tooltip>
            <el-tooltip effect="dark" content="支持office、pdf、rar、png等格式文件上传" placement="right">
              <el-dropdown-item command="file" icon="lego lego-upload-simple">
                上传文件
              </el-dropdown-item>
            </el-tooltip>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </div>
    <div class="node-tree-filter-input">
      <el-input
        v-model="filterText"
        placeholder="输入关键字过滤"
        @input="handleSearchChange"/>
    </div>
    <el-tree
      ref="nodeTree"
      :loading="loading"
      :data="treeList"
      :props="treeConfig"
      :default-expanded-keys="[currentNodeCode]"
      :current-node-key="currentNodeCode"
      :filter-node-method="handleFilterTree"
      node-key="code"
      @node-click="handleNodeClick">
      <flexbox
        :id="data.code"
        slot-scope="{ node, data }"
        class="catalog-item">
        <i v-if="data.type === 'folder'" :class="'folder' | iconPre" />
        <i v-else-if="data.type === 'page'" :class="'doc' | iconPre" />
        <img v-else :src="handleFileImage(data.name)" class="file-icon">
        <flexbox-item class="text-one-line" style="width: 0;">
          {{ node.label }}
        </flexbox-item>
        <div v-if="editable">
          <el-dropdown
            placement="bottom-start"
            trigger="click"
            class="catalog-item__more"
            @command="handleCommandClick($event, data)">
            <el-button
              icon="el-icon-more"
              type="text"
              class="common-text-btn"
              @click.stop="" />
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="rename" icon="lego lego-icon-modify">重命名</el-dropdown-item>
              <el-dropdown-item command="delete" icon="lego lego-delete">删除</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
          <el-dropdown
            v-if="data.type === 'folder'"
            placement="bottom-start"
            trigger="click"
            class="catalog-item__add"
            @command="handleCommandClick($event, data)">
            <el-button
              icon="el-icon-plus"
              type="text"
              class="common-text-btn"
              @click.stop="" />
            <el-dropdown-menu slot="dropdown">
              <el-tooltip effect="dark" content="创建可在线编辑的文档" placement="right">
                <el-dropdown-item command="page" icon="lego lego-doc">
                  创建文档
                </el-dropdown-item>
              </el-tooltip>
              <el-tooltip
                effect="dark"
                content="创建文件夹管理文档与文件"
                placement="right">
                <el-dropdown-item command="folder" icon="lego lego-folder">
                  创建文件夹
                </el-dropdown-item>
              </el-tooltip>
              <el-tooltip
                effect="dark"
                content="支持office、pdf、rar、png等格式文件上传"
                placement="right">
                <el-dropdown-item command="file" icon="lego lego-upload-simple">
                  上传文件
                </el-dropdown-item>
              </el-tooltip>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </flexbox>
    </el-tree>
    <input
      ref="nodeFile"
      accept="*/*"
      type="file"
      class="file-input"
      @change="handleFileUpload">
  </div>
</template>
<script>
import {
  nodeAddAPI,
  nodeModifyAPI,
  nodeDisableAPI
} from '@/api/doc/node'
import { fileUploadAPI } from '@/api/doc/file'
import { getFileIconWithSuffix } from '@/utils'
import PinyinMatch from 'pinyin-match'

export default {
  name: 'DocNode',
  props: {
    bookCode: String,
    treeList: {
      type: Array,
      default: function() {
        return []
      }
    },
    currentNodeCode: {
      type: String,
      default: ''
    },
    editable: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      loading: false,
      formName: '',
      filterText: '',
      currentAddNodeType: '',
      currentAddParentCode: '',
      treeConfig: {
        label: 'name',
        children: 'childrens'
      }
    }
  },
  methods: {
    handleFileImage(name) {
      const temps = name ? name.split('.') : []
      if (temps.length > 0) {
        return getFileIconWithSuffix(temps[temps.length - 1])
      } else {
        return getFileIconWithSuffix()
      }
    },
    handleFilterTree(value, data) {
      if (!value) {
        return true
      }
      return PinyinMatch.match(data.name, value)
    },
    handleSearchChange(val) {
      this.$refs.nodeTree.filter(val)
    },
    handleNodeClick(data) {
      this.$emit('onNodeClick', data)
    },
    handleCommandClick(command, data = null) {
      if (command === 'rename') {
        this.handleRename(data)
        return
      }
      if (command === 'delete') {
        this.handleDelete(data)
        return
      }
      if (data) {
        this.currentAddParentCode = data.code
      }
      this.currentAddNodeType = command
      if (command === 'file') {
        this.$refs.nodeFile.value = null
        this.$refs.nodeFile.click()
        return
      }
      var typeName = command === 'folder' ? '目录' : '文章'
      this.$prompt(typeName + '名称', '新建' + typeName, {
        inputErrorMessage: '请填写' + typeName + '名称！',
        inputValidator: value => {
          if (!value) {
            return '请填写' + typeName + '名称！'
          }
        }
      }).then(({ value }) => {
        this.handleAdd(value)
      })
    },
    handleAdd(name = null, fileCode = null) {
      this.loading = true
      nodeAddAPI({
        name: name,
        book: this.bookCode,
        fileCode: fileCode,
        type: this.currentAddNodeType,
        parentCode: this.currentAddParentCode
      }).then(() => {
        this.loading = false
        this.$message.success('创建成功！')
        this.$emit('onRefresh')
      }).catch(() => {
        this.loading = false
      })
    },
    handleDelete(data) {
      var typeName = data.type === 'folder' ? '目录' : '文章'
      this.$confirm(`请确认删除${typeName}【${data.name}】，是否继续?`, '提示', {
        type: 'warning'
      }).then(() => {
        this.loading = true
        nodeDisableAPI(data.code).then(() => {
          this.loading = false
          this.$message.success('删除成功！')
          this.$emit('onRefresh')
        }).catch(() => {
          this.loading = false
        })
      })
    },
    handleRename(data) {
      var typeName = data.type === 'folder' ? '目录' : '文章'
      this.$prompt(typeName + '名称', typeName + '重命名', {
        inputValue: data.name,
        inputErrorMessage: '请填写' + typeName + '名称！',
        inputValidator: value => {
          if (!value) {
            return '请填写' + typeName + '名称！'
          }
        }
      }).then(({ value }) => {
        this.loading = true
        nodeModifyAPI({
          code: data.code,
          name: value
        }).then(res => {
          this.loading = false
          this.$message.success('重命名成功！')
          this.$emit('onRefresh')
        }).catch(() => {
          this.loading = false
        })
      })
    },
    handleFileUpload(event) {
      var files = event.target.files
      for (let index = 0; index < files.length; index++) {
        this.$legoUploadFile.upload({
          request: fileUploadAPI,
          file: files[index]
        }).then(res => {
          this.handleAdd('', res.data)
        })
      }
    }
  }
}
</script>
<style lang="scss" scoped>
.node-tree {
  height: 100%;
  &-head {
    display: flex;
    align-items: center;
    text-align: left;
    flex-direction: row;
    width: 100%;
    height: 45px;
    padding: 0 20px;
    box-sizing: border-box;
    &-item {
      min-width: 20px;
      width: 0;
      flex: 1;
      margin-left: 8px;
    }
    &-btn {
      i {
        font-size: 18px;
      }
    }
  }
  &-filter-input {
    padding: 0px 10px;
  }
  .el-tree {
    overflow-y: auto;
    height: calc(100% - 85px);
  }
}
.common-text-btn {
  padding: 2px;
  color: $xr-color-text-regular;
}
.el-tree ::v-deep {
  padding: 5px;
  .el-tree-node {
    padding-top: 5px;
  }
  .el-tree-node__content {
    height: 35px;
    padding: 5px;
    border-radius: $xr-border-radius-base;

    &:hover {
      .el-dropdown {
        visibility: unset;
      }
    }
  }

  .el-tree-node__children {
    overflow: visible;
  }

  .el-tree-node__expand-icon {
    color: $xr-color-text-regular;
  }

  .is-leaf {
    visibility: hidden;
  }

  .el-tree--highlight-current,
  .el-tree-node.is-current > .el-tree-node__content {
    padding: 5px;
    color: $xr-color-primary;
    background-color: $xr--background-color-base !important;
    .lego {
      color: $xr-color-primary;
    }
  }

  .el-tree-node:focus > .el-tree-node__content {
    background-color: white;
  }
}
.catalog-item {
  padding-right: 4px;
  .el-dropdown {
    margin-left: 8px;
    visibility: hidden;
  }
  &.active, &:hover {
    background-color: #f6f9f9;
    .el-dropdown {
      visibility: unset;
    }
  }
  .file-icon {
    width: 11px;
    margin: 0 3px;
  }
  .lego {
    color: $xr-color-text-regular;
  }
}
.file-input {
  display: none;
}
</style>
