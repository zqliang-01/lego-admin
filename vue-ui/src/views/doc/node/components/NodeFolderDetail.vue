<template>
  <div class="page-container" v-loading="loading">
    <node-detail-head
      :node-detail="nodeFolder"
      @onClickEdit="handleEdit"
      @onDeleteSuccess="handleDelete"
    />
    <div class="content">
      <div v-if="dataList.length > 0">
        <div class="files-head">
          <i class="el-icon-folder-opened" />
          <label v-text="node.name" /> 共<span>{{ dataList.length }}</span>个文件
        </div>
        <el-table
          :data="dataList"
          :height="tableHeight"
          highlight-current-row
          style="width: 100%">
          <template v-for="(fields, r) in fieldList">
            <el-table-column
              v-for="(item, index) in fields"
              :key="r + '-' + index"
              :min-width="item.width"
              :prop="item.fieldCode"
              :label="item.name"
              show-overflow-tooltip>
              <template slot-scope="{ row }">
                <div
                  v-if="item.fieldCode === 'name'"
                  @click="handleDetail(row)">
                  <flexbox class="file">
                    <i v-if="row.type === 'page'" :class="'doc' | iconPre" class="file-icon"/>
                    <i v-else-if="row.type === 'folder'" :class="'folder' | iconPre" class="file-icon"/>
                    <img v-else v-src="handleFileImage(row.name)" class="file-img">
                    <div class="file-name text-one-line">
                      {{ row.name }}
                      <span v-if="row.type === 'file'">
                        ({{ handleFileSize(row.file.size) }})
                      </span>
                    </div>
                  </flexbox>
                </div>
                <field-view
                  v-else
                  :item="item"
                  :form-type="item.formType"
                  :value="row[item.fieldCode]"/>
              </template>
            </el-table-column>
          </template>
        </el-table>
      </div>
      <div
        v-else
        v-empty="true"
        :xs-empty-text="'目录【' + node.name + '】暂无文件'"
        class="empty">
      </div>
    </div>
  </div>
</template>
<script>
import {
  nodeGetAPI,
  nodeModifyAPI,
  nodeChildrenListAPI
} from '@/api/doc/node'
import { getFileIconWithSuffix, fileSize } from '@/utils'
import NodeDetailHead from './NodeDetailHead'
import FieldView from '@/components/Common/Form/FieldView'

export default {
  name: 'DocNodeFile',
  components: {
    NodeDetailHead,
    FieldView
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
      dataList: [],
      nodeFolder: {
        name: '',
        createTime: '',
        creator: {}
      },
      fieldList: [
        [
          { fieldCode: 'name', name: '名称', formType: 'text', width: '150' },
          { fieldCode: 'createTime', name: '创建时间', formType: 'text', width: '120' }
        ],
        [
          { fieldCode: 'creator', name: '创建人', formType: 'select', width: '100' }
        ]
      ],
      tableHeight: document.documentElement.clientHeight - 245
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
      this.loading = true
      nodeGetAPI(this.node.code).then(res => {
        this.nodeFolder = res.data
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
      nodeChildrenListAPI({
        code: this.node.code
      }).then(res => {
        this.dataList = res.data
      })
    },
    handleFileSize(size) {
      return fileSize(size)
    },
    handleEdit() {
      this.$prompt('目录名称', '目录重命名', {
        inputValue: this.nodeFolder.name,
        inputErrorMessage: '请填写目录名称！',
        inputValidator: value => {
          if (!value) {
            return '请填写目录名称！'
          }
        }
      }).then(({ value }) => {
        this.loading = true
        nodeModifyAPI({
          code: this.nodeFolder.code,
          name: value
        }).then(res => {
          this.loading = false
          this.$message.success('重命名成功！')
          this.$emit('onRename', value)
        }).catch(() => {
          this.loading = false
        })
      })
    },
    handleDelete() {
      this.$emit('onDelete')
    },
    handleDetail(data) {
      this.$emit('onNodeClick', data)
    },
    handleFileImage(name) {
      var ext = ''
      const temps = name ? name.split('.') : []
      if (temps.length > 0) {
        ext = temps[temps.length - 1]
      }
      return getFileIconWithSuffix(ext)
    }
  }
}
</script>
<style scoped lang="scss">
.page-container {
  height: 100%;
  width: 100%;
  .content {
    width: 100%;
    height: calc(100% - 30px);
    overflow: auto;
    .empty {
      display: flex;
      align-items: center;
      justify-content: center;
      height: 60%;
    }

    .files-head {
      margin: 8px 0;
      padding: 0 5px;
      overflow: hidden;
      float: left;
      line-height: 32px;
      font-size: 14px;
      i {
        font-size: 22px;
        vertical-align: middle;
        color: #333;
      }
      label {
        font-size: 16px;
        font-weight: 550;
        margin-right: 5px;
        margin-left: 4px;
        vertical-align: middle;
      }
      span {
        font-size: 18px;
        font-weight: 550;
        color: #ff6161;
        margin: 0 3px;
      }
    }

    .file {
      padding: 5px;
      cursor: pointer;

      &-icon {
        flex-shrink: 0;
        width: 20px;
        height: 20px;
        line-height: 20px;
        font-size: 20px;
        color: $xr-color-text-regular;
      }
      &-img {
        flex-shrink: 0;
        width: 20px;
      }

      &-name {
        flex: 1;
        margin-left: 8px;
        color: $xr-color-primary;
      }
    }
  }
}
</style>
