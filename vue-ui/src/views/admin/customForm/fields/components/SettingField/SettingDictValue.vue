<template>
  <div v-loading="loading">
    <div class="content-body">
      <div class="handle-bar">
        <el-select
          v-model="currentTypeCode"
          clearable
          filterable
          placeholder="请选择字典类型"
          @change="getDetail">
          <el-option
            v-for="item in dictTypeList"
            :key="item.code"
            :label="item.name"
            :value="item.code">
          </el-option>
        </el-select>
        <el-tooltip content="修改字典类型" placement="top-start">
          <el-button
            class="type-btn"
            icon="el-icon-edit"
            @click.native="handleClick('modifyType')" />
        </el-tooltip>
        <el-tooltip content="新增字典类型" placement="top-start">
          <el-button
            class="type-btn"
            icon="el-icon-plus"
            @click.native="handleClick('addType')" />
        </el-tooltip>
        <el-tooltip content="刷新" placement="top-start">
          <el-button
            class="type-btn"
            icon="el-icon-refresh"
            @click.native="handleClick('refresh')" />
        </el-tooltip>
        <div style="float: right">
          <el-button
            type="primary"
            class="xr-btn--orange"
            icon="el-icon-plus"
            @click.native="handleClick('add')">
            新建字典数据
          </el-button>
        </div>
      </div>
      <div class="content">
        <el-table
          :data="valueList"
          :height="tableHeight"
          border
          header-align="center"
          highlight-current-row>
          <el-table-column
            v-for="(item, index) in titleList"
            :key="index"
            :prop="item.field"
            :label="item.name"
            :width="item.width"
            :align="item.align"
            header-align="center">
            <template slot-scope="{ row }">
              <template v-if="row.show && !item.readOnly">
                <el-switch
                  v-if="item.type === 'boolean'"
                  v-model="row[item.field]" />
                <el-input
                  v-else
                  v-model="row[item.field]" />
              </template>
              <template v-else>
                <el-switch
                  v-if="item.type === 'boolean'"
                  :disabled="true"
                  v-model="row[item.field]" />
                <span v-else>
                  {{ row[item.field] }}
                </span>
              </template>
            </template>
          </el-table-column>
          <el-table-column :resizable="false" label="操作" width="200" align="center">
            <template slot-scope="{ row }">
              <el-button
                :disabled="row.show"
                type="primary"
                class="xr-btn--orange"
                @click="handleClick('edit', row)">编辑</el-button>
              <el-button
                :disabled="!row.show"
                type="primary"
                @click="handleClick('save', row)">保存</el-button>
              <el-button
                :disabled="!row.show"
                type="info"
                @click="handleClick('cancel')">取消</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <el-dialog
        :visible.sync="showDialog"
        append-to-body
        :title="dialogTitle"
        width="500px">
        <flexbox
          class="nav-dialog-div"
          v-for="(item, index) in titleList"
          :key="index">
          <template v-if="item.field !== 'enable'">
            <label>{{ item.name }}：</label>
            <el-input
              :disabled="item.readOnly"
              v-model="submitForm[item.field]"
              placeholder="请输入内容" />
          </template>
        </flexbox>
        <span slot="footer" class="dialog-footer">
          <el-button @click="showDialog = false">取 消</el-button>
          <el-button type="primary" @click="submitDialog">确 定</el-button>
        </span>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import {
  dictListAPI,
  dictModifyAPI,
  dictAddAPI,
  dictTypeAddAPI,
  dictTypeModifyAPI
} from '@/api/dictionary'
import { objDeepCopy } from '@/utils'

export default {
  name: 'SettingDictValue',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    appCode: {
      type: String,
      required: true
    },
    selectedType: {
      type: String,
      required: true
    },
    dictTypeList: {
      type: Array,
      required: true
    }
  },
  computed: {
    request() {
      if (this.editType === 'addType') {
        return dictTypeAddAPI
      }
      if (this.editType === 'modifyType') {
        return dictTypeModifyAPI
      }
      if (this.editType === 'add') {
        return dictAddAPI
      }
      return dictModifyAPI
    },
    dialogTitle() {
      if (this.editType === 'addType') {
        return '新增字典类型'
      }
      if (this.editType === 'modifyType') {
        return '修改字典类型'
      }
      return '新增字典数据'
    }
  },
  watch: {
    selectedType(value) {
      this.currentTypeCode = this.selectedType
      this.getDetail()
    }
  },
  data() {
    return {
      loading: false,
      tableHeight: document.documentElement.clientHeight - 295,
      valueList: [],
      currentTypeCode: '',
      showDialog: false,
      submitForm: {},
      editType: 'add',
      titleList: [
        {
          field: 'sn',
          name: '序号',
          width: '50',
          type: 'number',
          align: 'center',
          readOnly: false
        },
        {
          field: 'code',
          name: '编码',
          width: '100',
          type: 'text',
          align: 'left',
          readOnly: true
        },
        {
          field: 'name',
          name: '名称',
          width: '150',
          type: 'text',
          align: 'left',
          readOnly: false
        },
        {
          field: 'enable',
          name: '状态',
          width: '100',
          type: 'boolean',
          align: 'center',
          readOnly: false
        }
      ]
    }
  },
  created() {
    this.currentTypeCode = this.selectedType
    this.getDetail()
    window.onresize = () => {
      this.tableHeight = document.documentElement.clientHeight - 295
    }
  },
  methods: {
    getDetail() {
      if (!this.currentTypeCode) {
        this.valueList = []
        return
      }
      this.loading = true
      dictListAPI(this.appCode, this.currentTypeCode).then(res => {
        this.loading = false
        this.valueList = res.data || []
      }).catch(() => {
        this.loading = false
      })
    },
    getCurrentType() {
      return this.dictTypeList.find(type => type.code === this.currentTypeCode)
    },
    getMaxSN(items) {
      var max = Math.max.apply(null, items.map(item => item.sn))
      if (max > 0) {
        return max + 1
      }
      return 1
    },
    handleClick(type, row) {
      this.editType = type
      if (type === 'refresh') {
        this.$emit('onRefreshType')
        this.getDetail()
      } else if (type == 'edit') {
        this.$set(row, 'show', true)
      } else if (type == 'save') {
        this.loading = true
        dictModifyAPI(this.appCode, row).then(() => {
          this.loading = false
          this.$message.success('修改成功')
          this.getDetail()
        }).catch(() => {
          this.loading = false
        })
      } else if (type == 'cancel') {
        this.getDetail()
      } else if (type == 'add') {
        this.submitForm = {
          name: '',
          typeCode: this.currentTypeCode,
          sn: this.getMaxSN(this.valueList)
        }
        this.showDialog = true
      } else if (type == 'addType') {
        this.submitForm = {
          name: '',
          sn: this.getMaxSN(this.dictTypeList)
        }
        this.showDialog = true
      } else if (type == 'modifyType') {
        this.submitForm = objDeepCopy(this.getCurrentType())
        this.showDialog = true
      }
    },
    submitDialog() {
      this.loading = true
      this.request(this.appCode, this.submitForm).then(res => {
        this.$message.success('操作成功')
        this.showDialog = false
        if (this.editType === 'modifyType') {
          this.$emit('onRefreshType')
        }
        if (this.editType === 'add') {
          this.getDetail()
        }
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    }
  }
}
</script>

<style scoped lang="scss">
.content-body {
  height: calc(100% - 57px);
  padding: 10px;
  overflow-y: auto;
}

.handle-bar {
  padding-bottom: 10px;
  .el-date-editor {
    width: 150px;
    margin-right: 15px;
  }
  .type-btn {
    height: 34px;
    margin-left: 5px;
  }
}

.content ::v-deep .el-table {
  border: 1px solid #e6e6e6;
}

.nav-dialog-div {
  margin-bottom: 20px;
  label {
    width: 90px;
    display: block;
  }
}
.nav-dialog-div {
  .el-input,
  .el-select,
  .user-select {
    flex: 1;
  }
}
</style>
