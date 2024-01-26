<template>
  <div v-loading="loading" class="rc-cont">
    <flexbox
      class="rc-head"
      direction="row-reverse">
      <el-button
        class="rc-head-item"
        type="primary"
        @click.native="addFile">上传附件</el-button>
      <input
        id="file"
        type="file"
        class="rc-head-file"
        accept="*/*"
        multiple
        @change="uploadFile">
    </flexbox>
    <el-table
      :data="list"
      :height="tableHeight"
      class="file-table"
      stripe
      style="width: 100%;border: 1px solid #E6E6E6;">
      <el-table-column
        v-for="(item, index) in fieldList"
        :key="index"
        :min-width="item.width"
        :prop="item.fieldCode"
        :label="item.name"
        show-overflow-tooltip>
        <template slot-scope="{ row }">
          <field-view
            :props="item"
            :form-type="item.formType"
            :value="row[item.fieldCode]" />
        </template>
      </el-table-column>
      <el-table-column
        label="操作"
        width="200">
        <template slot-scope="scope">
          <flexbox>
            <el-button
              type="text"
              @click.native="handleFile('preview', scope)">预览</el-button>
            <el-button
              type="text"
              @click.native="handleFile('download', scope)">下载</el-button>
            <el-button
              type="text"
              @click.native="handleFile('edit', scope)">重命名</el-button>
            <el-button
              type="text"
              @click.native="handleFile('delete', scope)">删除</el-button>
          </flexbox>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog
      :append-to-body="true"
      :close-on-click-modal="false"
      :visible.sync="editDialog"
      title="编辑"
      width="30%">
      <el-form :model="editForm">
        <el-form-item
          label="新名称"
          label-width="100">
          <el-input
            v-model="editForm.name"
            autocomplete="off" />
        </el-form-item>
      </el-form>
      <div
        slot="footer"
        class="dialog-footer">
        <el-button @click="editDialog = false">取 消</el-button>
        <el-button
          type="primary"
          @click="confirmEdit">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script type="text/javascript">

import FieldView from '../NewCom/Form/FieldView'
import {
  fileListAPI,
  fileDeleteAPI,
  fileDownloadAPI,
  fileModifyAPI
} from '@/api/common'
import { fileSize, downloadFileWithBuffer } from '@/utils'

export default {
  name: 'RelativeFile',
  components: {
    FieldView
  },
  props: {
    detailCode: String,
    menuCode: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      loading: false,
      list: [],
      fieldList: [
        { fieldCode: 'name', name: '附件名称', formType: 'text', width: '200' },
        { fieldCode: 'size', name: '附件大小', formType: 'text', width: '100' },
        { fieldCode: 'creator', name: '上传人', formType: 'user', width: '100' },
        { fieldCode: 'createTime', name: '上传时间', formType: 'text', width: '100' }
      ],
      tableHeight: document.documentElement.clientHeight - 285,
      /** 重命名 弹窗 */
      editDialog: false,
      /** 编辑信息 */
      editForm: { name: '', data: {}}
    }
  },
  inject: ['rootTabs'],
  computed: {},
  watch: {
    detailCode(val) {
      this.list = []
      this.getList()
    },
    'rootTabs.currentName'(val) {
      if (val === 'RelativeFile') {
        this.getList(false)
      }
    }
  },
  mounted() {
    this.getList()
  },
  methods: {
    getList(loading = true) {
      this.loading = loading
      fileListAPI({
        entityCode: this.detailCode,
        permissionCode: this.menuCode
      }).then(res => {
        this.loading = false
        this.list = res.data.map(item => {
          item.size = fileSize(item.size)
          return item
        })
      })
        .catch(() => {
          this.loading = false
        })
    },
    addFile() {
      document.getElementById('file').click()
    },
    /** 图片选择出发 */
    uploadFile(event) {
      var files = event.target.files
      for (let index = 0; index < files.length; index++) {
        this.$legoUploadFile.upload({
          file: files[index],
          params: {
            entityCode: this.detailCode,
            permissionCode: this.menuCode
          }
        }).then(completeData => {
          this.getList()
        })
      }

      event.target.value = ''
    },
    handleFile(type, item) {
      if (type === 'preview') {
        this.$previewFile.preview({
          index: item.$index,
          data: this.list
        })
      } else if (type === 'delete') {
        this.$confirm('您确定要删除该文件吗?', '提示', { type: 'warning' }).then(() => {
          fileDeleteAPI(item.row.code).then(res => {
            this.list.splice(item.$index, 1)
            this.$bus.emit('crm-tab-num-update')
            this.$message.success('操作成功')
          })
        })
      } else if (type === 'download') {
        fileDownloadAPI(item.row.code).then(res => {
          downloadFileWithBuffer(res.data, item.row.name)
        }).catch(() => {})
      } else {
        this.editForm.data = item
        this.editForm.name = item.row.name
        this.editDialog = true
      }
    },
    confirmEdit() {
      if (this.editForm.name) {
        this.loading = true
        fileModifyAPI(this.editForm.data.row.code, {
          permissionCode: this.menuCode,
          name: this.editForm.name
        }).then(res => {
          this.$message.success('编辑成功')
          this.editDialog = false
          this.loading = false
          var item = this.list[this.editForm.data.$index]
          item.name = this.editForm.name
        })
          .catch(() => {
            this.loading = false
          })
      }
    }
  }
}
</script>
<style lang="scss" scoped>
@import '@/styles/relative.scss';

.h-item {
  font-size: 13px;
  color: #409eff;
  margin: 0 5px;
  cursor: pointer;
}

.rc-head-file {
  position: absolute;
  top: 0;
  right: 0;
  height: 98px;
  width: 98px;
  opacity: 0;
  z-index: -1;
  cursor: pointer;
}
</style>

