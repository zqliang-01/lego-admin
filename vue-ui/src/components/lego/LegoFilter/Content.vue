<template>
  <div class="wrapper">
    <ul class="list">
      <li
        v-for="(item, index) in showObj.form"
        :key="index"
        class="list-item">
        <span v-if="item.formType == 'boolean_value'">
          {{ item.name +'&nbsp;' + getConditionName(item) }}“
          <el-switch :value="item.value" disabled/>”
        </span>
        <span v-else>
          {{ item.name + '&nbsp;' + getConditionName(item) + getValueContent(item) }}
        </span>
        <i
          class="el-icon-close icon"
          @click="handleDelete(item, index)"/>
      </li>
    </ul>
  </div>
</template>

<script>
import LegoConditionMixin from '../mixins/LegoCondition'
import { getConditionShowValue } from '@/components/Common/Form/utils'

export default {
  name: 'LegoFilterContent',
  mixins: [LegoConditionMixin],
  props: {
    filterObj: {
      type: Object,
      required: true,
      default: () => {
        return {}
      }
    }
  },
  data() {
    return {
      showObj: {}
    }
  },
  computed: {},
  watch: {
    filterObj: function(val) {
      this.showObj = val
    }
  },
  mounted() {
    this.showObj = this.filterObj
  },
  methods: {
    /**
     * 删除高级筛选条件
     * @param index
     */
    handleDelete(item, index) {
      if (this.showObj.filterList) {
        this.showObj.filterList.splice(index, 1)
      }
      this.showObj.form.splice(index, 1)
      this.$emit('delete', { item: item, index: index, filterObj: this.showObj })
    },

    /**
     * 获取条件名称
     */
    getConditionName(formItem) {
      let conditionName = ''
      this.getConditionByFormType(formItem.formType).forEach(item => {
        if (item.type === formItem.type) {
          conditionName = item.label
        }
      })
      return conditionName
    },

    /**
     * 值展示
     */
    getValueContent(data) {
      if (!data || ['isNull', 'isNotNull'].includes(data.type)) {
        return ''
      }
      const value = getConditionShowValue(data)
      return `“${value}”`
    }
  }
}
</script>

<style scoped lang="scss">
@mixin left() {
  display: flex;
  justify-content: flex-start;
  align-items: center;
}
@mixin center() {
  display: flex;
  justify-content: center;
  align-items: center;
}

.wrapper {
  width: 100%;
  min-height: 50px;
  background: white;
  border-top: 1px solid #e1e1e1;
  font-size: 13px;
  overflow-x: auto;
  color: #aaa;
  @include left;
  .list {
    width: 100%;
    padding: 0 20px;
    margin-bottom: 10px;
    flex-shrink: 0;
    @include left;
    .list-item {
      height: 30px;
      padding: 0 10px;
      margin: 10px 15px 0 0;
      border: 1px solid #e1e1e1;
      border-radius: 3px;
      flex-shrink: 0;
      @include center;
      .icon {
        margin-left: 20px;
        cursor: pointer;
      }
    }
  }
}
</style>
