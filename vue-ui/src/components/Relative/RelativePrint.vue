<template>
  <div v-loading="loading" class="rc-cont">
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
            :item="item"
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
              class="el-button--primary"
              @click.native="handleFile('preview', scope.row)">打印预览</el-button>
          </flexbox>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script type="text/javascript">
import {
  printLogListAPI
} from '@/api/printLog'
import FieldView from '../Common/Form/FieldView'

export default {
  name: 'RelativePrint',
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
        { fieldCode: 'name', name: '名称', formType: 'text', width: '100' },
        { fieldCode: 'creator', name: '创建人', formType: 'select', width: '100' },
        { fieldCode: 'template', name: '模板', formType: 'select', width: '100' },
        { fieldCode: 'createTime', name: '打印时间', formType: 'text', width: '100' }
      ],
      tableHeight: document.documentElement.clientHeight - 225
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
      if (val === 'RelativePrint') {
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
      printLogListAPI({
        entityCode: this.detailCode,
        permissionCode: this.menuCode
      }).then(res => {
        this.loading = false
        this.list = res.data
      }).catch(() => {
        this.loading = false
      })
    },
    handleFile(type, item) {
      if (type === 'preview') {
        const routeData = this.$router.resolve(`/reprint/${item.code}`)
        window.open(routeData.href, '_blank')
      }
    }
  }
}
</script>
<style lang="scss" scoped>
@import '@/styles/relative.scss';

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

