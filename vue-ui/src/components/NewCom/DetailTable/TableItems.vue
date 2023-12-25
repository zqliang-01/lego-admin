<template>
  <el-table
    :data="fieldFrom"
    :row-key="Date.now().toString()"
    class="table-items"
    style="width: 100%">
    <el-table-column
      label="序号"
      width="50">
      <template slot-scope="{ row, column, $index }">
        {{ $index + 1 }}
      </template>
    </el-table-column>
    <el-table-column
      v-for="(item, index) in fieldList"
      v-if="getShowValue(item)"
      :key="index"
      :prop="item.field"
      :min-width="getMinWidth(item.formType)">
      <template
        slot="header"
        slot-scope="scope">
        <span v-if="item.isNull == 1" class="red">*</span>{{ item.name }}
      </template>
      <template slot-scope="{ row, column, $index }">
        <form-item
          :prop-prefix="`${propPrefix || ''}[${$index}].`"
          :item="fieldList[index]"
          :index="$index"
          :field-from="fieldFrom[$index]"
          :disabled="disabled"
          @change="fieldChange"
        >
          <template slot-scope="{ data, index }">
            <slot :data="data" :index="$index" />
          </template>
        </form-item>
      </template>
    </el-table-column>
    <el-table-column
      :resizable="false"
      fixed="right"
      label="操作"
      width="60">
      <template slot-scope="{ row, column, $index }">
        <el-button
          :icon="'icon-bin' | iconPre" type="text" @click="deleteClick($index)"/>
      </template>

    </el-table-column>
  </el-table>
</template>

<script>

export default {
  // table 风格展示事项
  name: 'TableItems',

  components: {
    FormItem: () => import('../Form/FormItem')
  },

  props: {
    // 表单验证前缀
    propPrefix: {
      type: String,
      default: ''
    },
    fieldFrom: {
      type: Array,
      default: () => {
        return []
      }
    },
    fieldList: {
      type: Array,
      default: () => {
        return []
      }
    },
    disabled: Boolean
  },

  data() {
    return {
    }
  },

  computed: {},

  watch: {},

  created() {
  },

  mounted() {},

  beforeDestroy() {},

  methods: {
    deleteClick(index) {
      this.$emit('delete', index)
    },

    getMinWidth(formType) {
      if (formType === 'date_interval' ||
      formType === 'dateRange' ||
       formType === 'file' ||
       formType === 'location' ||
       formType === 'position') {
        return 250
      }
      return 150
    },

    /**
     * 判断展示
     */
    getShowValue(item) {
      if (item.hasOwnProperty('show')) {
        return item.show
      }
      return true
    },

    /**
     * 字段change
     */
    fieldChange(item, index, value, valueList) {
      this.$emit('change', item, index, value, valueList)
    }
  }
}
</script>

<style lang="scss">
.table-items {
  th {
    line-height: initial;
  }

  .form-item {
    padding: 8px 0 !important;
    margin-bottom: 0 !important;
    .el-form-item__label {
      display: none;
    }

    width: auto !important;
  }
}
</style>

<style lang="scss" scoped>
.red {
  color: #F56C6C;
  margin-right: 4px;
}
</style>
