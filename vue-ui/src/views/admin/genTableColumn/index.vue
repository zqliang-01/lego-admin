<template>
  <flexbox
    v-loading="loading"
    align="flex-start"
    justify="flex-start"
    class="fields-index body">
    <div class="body-content">
      <flexbox
        align="flex-start"
        justify="flex-start"
        direction="column"
        class="body-content-warp">
        <el-header>
          <div class="title">编辑表字段</div>
          <div>
            <el-button v-debounce="handleSync" v-if="manage.genTable.sync" type="primary">初始化</el-button>
            <el-button v-debounce="handleSave" type="primary">保存</el-button>
            <el-button @click="handleCancel">返回</el-button>
          </div>
        </el-header>
        <el-table
          ref="dragTable"
          :data="dataList"
          :max-height="tableHeight"
          row-key="code">
          <el-table-column label="序号" prop="sn" min-width="5%" class-name="allowDrag">
            <template slot-scope="scope">
              <el-input v-model="scope.row.sn"/>
            </template>
          </el-table-column>
          <el-table-column :show-overflow-tooltip="true" label="字段列名" prop="name" min-width="10%" />
          <el-table-column label="字段描述" min-width="10%">
            <template slot-scope="scope">
              <el-input v-model="scope.row.comment"/>
            </template>
          </el-table-column>
          <el-table-column :show-overflow-tooltip="true" label="物理类型" prop="dataType" min-width="10%" />
          <el-table-column label="表单类型" min-width="11%">
            <template slot-scope="scope">
              <el-select v-model="scope.row.formType" @change="formTypeChange($event, scope.row)">
                <el-option
                  v-for="(item, index) in formTypeList"
                  :key="index"
                  :label="item.name"
                  :value="item.code" />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="java类型" min-width="10%">
            <template slot-scope="scope">
              <template v-if="scope.row.editJavaFiledType">
                <lego-relative-cell
                  v-if="scope.row.formType == 'entity'"
                  :value="scope.row.relativeTable"
                  :field-list="fieldList"
                  :action="{request: tableListRequest}"
                  @value-change="entityChange(scope.row, $event)"/>
                <el-input v-else v-model="scope.row.javaFieldType"/>
              </template>
              <span v-else>{{ scope.row.javaFieldType }}</span>
            </template>
          </el-table-column>
          <el-table-column label="java属性" min-width="10%">
            <template slot-scope="scope">
              <el-input v-model="scope.row.javaField"/>
            </template>
          </el-table-column>
          <el-table-column label="必填" min-width="5%">
            <template slot-scope="scope">
              <el-checkbox v-model="scope.row.required"/>
            </template>
          </el-table-column>
          <el-table-column label="唯一" min-width="5%">
            <template slot-scope="scope">
              <el-checkbox v-model="scope.row.unique"/>
            </template>
          </el-table-column>
        </el-table>
      </flexbox>
    </div>
  </flexbox>
</template>

<script>
import {
  genTableColumnListAPI,
  genTableModifyAPI
} from '@/api/admin/genTableColumn'
import {
  genTableSyncAPI,
  genTableListAPI
} from '@/api/admin/genTable'
import { customFieldTypeListAPI } from '@/api/admin/formField'
import { Loading } from 'element-ui'
import { mapGetters } from 'vuex'
import LegoRelativeCell from '@/components/Relative/LegoRelativeCell'

export default {
  name: 'GenEdit',
  components: {
    LegoRelativeCell
  },
  computed: {
    ...mapGetters(['manage'])
  },
  data() {
    return {
      loading: false,
      tableCode: '',
      tableHeight: document.documentElement.clientHeight - 165, // 表的高度
      dataList: [],
      // 字典信息
      formTypeList: [],
      tableListRequest: genTableListAPI,
      fieldList: [
        { fieldCode: 'code', name: '表名', formType: 'select', width: '150' },
        { fieldCode: 'name', name: '功能名称', formType: 'text', width: '150' },
        { fieldCode: 'comment', name: '描述', formType: 'text', width: '150' },
        { fieldCode: 'packageName', name: '包名', formType: 'text', width: '150' },
        { fieldCode: 'appCode', name: '模块编码', formType: 'text', width: '100' },
        { fieldCode: 'className', name: '类名', formType: 'text', width: '100' },
        { fieldCode: 'permissionCode', name: '菜单编码', formType: 'text', width: '100' },
        { fieldCode: 'path', name: '生成路径', formType: 'text', width: '150' }
      ]
    }
  },
  created() {
    this.tableCode = this.$route.params && this.$route.params.tableCode
    customFieldTypeListAPI({ tableCode: this.tableCode }).then(res => {
      this.formTypeList = res.data
      this.refreshData()
    })
  },
  methods: {
    refreshData() {
      this.loading = true
      genTableColumnListAPI({ tableCode: this.tableCode }).then(res => {
        this.dataList = res.data.map(item => {
          item.editJavaFiledType = false
          if (item.formType === 'entity' || item.formType === 'select') {
            item.editJavaFiledType = true
          }
          return item
        })
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    formTypeChange(value, row) {
      const item = this.formTypeList.find(formType => formType.code === value)
      if (item) {
        row.javaFieldType = item.type
      }
      if (value === 'entity' || value === 'select') {
        row.editJavaFiledType = true
        return
      }
      row.editJavaFiledType = false
    },
    handleSync() {
      this.$confirm('初始化操作将重置表字段配置，是否继续?', '提示', {
        type: 'warning'
      }).then(() => {
        const loading = Loading.service({ fullscreen: true, text: '初始化中，请稍后...' })
        genTableSyncAPI(this.tableCode).then(() => {
          loading.close()
          this.$message.success('初始化成功')
          this.refreshData()
        }).catch(() => {
          loading.close()
        })
      })
    },
    handleSave() {
      this.loading = true
      genTableModifyAPI(this.dataList).then(() => {
        this.$message.success('保存成功！')
        this.refreshData()
        this.loading = false
      }).catch(res => {
        this.loading = false
      })
    },
    handleCancel() {
      this.$router.go(-1)
    },
    entityChange(row, value) {
      row.relativeTableCode = value.code
    }
  }
}
</script>

<style scoped lang="scss">
@import '@/styles/mixin.scss';

.fields-index {
  &.body {
    position: relative;
    width: 100%;
    height: calc(100% + 15px);
    user-select: none;
    overflow: hidden;

    .body-content {
      padding: 15px;
      flex: 1;
      height: 100%;

      .body-content-warp {
        margin: 0 auto;
        height: 100%;
        box-shadow: 0 2px 12px 0 rgba($color: #000, $alpha: 0.1);
        border-radius: $xr-border-radius-base;
        overflow: hidden;
        background-color: white;
        .el-header {
          width: 100%;
          // border-bottom: 1px solid $xr-border-line-color;
          display: flex;
          justify-content: space-between;
          align-items: center;
          .title {
            font-size: 16px;
            font-weight: 600;
            color: #333;
          }
        }
        .body-content-main {
          width: 100%;
          height: 100%;
          overflow-y: auto;
          padding: 10px 16px 30px;
          .el-main {
            .no-list {
              margin: 200px 0;
              color: #ccc;
              @include center;
            }
          }
        }
      }
    }
  }
}
</style>
