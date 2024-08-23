<template>
  <div
    class="content">
    <div class="content-title">基本信息</div>
    <div
      v-for="(item , index) in showList"
      :key="index"
      class="detail-cell">
      <div class="detail-cell__label">
        {{ item.name }}
      </div>
      <div class="detail-cell__value">
        <field-view
          :item="item"
          :form-type="item.formType"
          :value="item.value"
        >
          <template slot-scope="{ data }">
            <span>{{ getCommonShowValue(data) }}</span>
          </template>
        </field-view>
      </div>
    </div>
  </div>
</template>

<script>
import FieldView from './Common/Form/FieldView'
import { getFormFieldShowValue } from './Common/Form/utils'

export default {
  //  重要信息 中的列表展示
  name: 'ImportInfo',
  components: {
    FieldView
  },
  props: {
    list: Array,
    detail: {
      type: Object,
      default: () => {
        return {}
      }
    }
  },
  data() {
    return {}
  },
  computed: {
    showList() {
      return this.list.filter(item => {
        return item.formType !== 'file' &&
        item.formType !== 'map_address'
      })
    }
  },
  methods: {
    /**
     * 获取非附件类型的展示值
     */
    getCommonShowValue(item) {
      return getFormFieldShowValue(item.formType, item.value, '', item)
    }
  }
}
</script>

<style lang="scss" scoped>
.import-info {
  overflow: auto;
  height: 100%;
}

.content {
  padding: 20px 10px;
  &-title {
    font-size: 13px;
    font-weight: 600;
  }

  .detail-cell {
    font-size: 12px;
    margin: 20px 0;
    &__label {
      color: #666;
    }
    &__value {
      margin-top: 5px;
      color: #333;
      line-height: 16px;
    }
  }
}
</style>
