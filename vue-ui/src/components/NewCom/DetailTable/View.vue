<template>
  <div v-if="fieldForm && fieldForm.length > 0" class="detail-table-view">
    <template v-if="showType === 'default'">
      <div
        v-for="(children, sectionIndex) in fieldList"
        :key="sectionIndex"
        class="detail-item">
        <flexbox class="detail-item__head">
          <div class="detail-item__head-title">{{ title }}（{{ sectionIndex+1 }}）</div>
        </flexbox>
        <flexbox
          class="form-items"
          align="flex-start"
          wrap="wrap"
          justify="flex-start">
          <template v-for="(item, index) in children">
            <div
              v-if="getShowValue(item)"
              :key="index"
              :class="[`is-${item.formType}`]"
              :label="item.name"
              :style="{width: item.stylePercent ? `${item.stylePercent}%` : 'auto'}"
              class="form-item">
              <div class="form-item__label">{{ item.name }}</div>
              <field-view
                :props="item"
                :form-type="item.formType"
                :value="fieldForm[sectionIndex][item.field]"
              >
                <template slot-scope="{ data }">
                  <slot :data="data" />
                </template>
              </field-view>
            </div>
          </template>
        </flexbox>
      </div>
    </template>
    <div
      v-else-if="showType === 'table'"
      class="detail-item">
      <el-table
        :data="fieldForm"
        :row-key="Date.now().toString()"
        class="table-items"
        style="width: 100%">
        <el-table-column
          v-for="(item, index) in addFieldList"
          v-if="getShowValue(item)"
          :key="index"
          :prop="item.field"
          :label="item.name"
          :min-width="getMinWidth(item.formType)">
          <template slot-scope="{ row }">
            <field-view
              :props="item"
              :form-type="item.formType"
              :value="row[item.field]"
            >
              <template slot-scope="{ data }">
                <slot :data="data" />
              </template>
            </field-view>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
export default {
  // 明细表格详情
  name: 'DetailTableView',

  components: {
    FieldView: () => import('@/components/NewCom/Form/FieldView')
  },

  props: {
    title: String,
    showType: {
      type: String,
      default: 'defalut' // defalut table
    },
    addFieldList: Array, // 表头展示用
    fieldForm: {
      type: Array,
      default: () => {
        return []
      }
    },
    fieldList: { // 块布局用
      type: Array,
      default: () => {
        return []
      }
    }
  },

  data() {
    return {

    }
  },

  computed: {},

  watch: {},

  created() {},

  mounted() {},

  beforeDestroy() {},

  methods: {
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
    }
  }
}
</script>

<style lang="scss" scoped>
.detail-table-view {
  font-size: 13px;
  line-height: normal;

  .detail-item {
    border-radius: 3px;
    border: 1px solid #e1e1e1;
    background-color: white;
     &__head {
      padding: 10px 20px;
      background-color: #f5f5f5;
      &-title {
        height: auto;
        font-size: 12px;
        color: #333;
        flex: 1;
        line-height: normal;
      }
      .el-button {
        padding: 0;
      }
    }
  }

  .detail-item + .detail-item {
    margin-top: 10px;
  }

  .form-items {
    padding-bottom: 10px;
  }

  .form-item {
    padding: 12px 12px 0;
    margin-bottom: 0;

    &__label {
      line-height: 1.5;
      padding-bottom: 5px;
      word-break: break-all;
      word-wrap: break-word;
      color: #666;
    }

    &.is-desc_text {
      .form-item__label {
        display: none;
      }
    }
  }
}
</style>
