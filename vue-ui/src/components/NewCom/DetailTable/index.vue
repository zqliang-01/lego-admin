<template>
  <div class="detail-table">
    <template v-if="showType === 'default'">
      <div
        v-for="(children, index) in fieldList"
        :key="index"
        class="detail-item">
        <flexbox class="detail-item__head">
          <div class="detail-item__head-title">{{ title }}（{{ index+1 }}）</div>
          <el-button
            v-if="fieldList.length > 1"
            :icon="'icon-bin' | iconPre" type="text" @click="deleteClick(index)"/>
        </flexbox>
        <form-items
          :field-form="fieldForm[index]"
          :field-list="children"
          :prop-prefix="`${propPrefix || ''}[${index}].`"
          :disabled="disabled"
          @change="formChange"
        />
        <div class="add-btn">
          <el-button type="text" @click="addClick">
            <i :class="'l-plus' | iconPre" />
            {{ btnName }}
          </el-button>
        </div>
      </div>
    </template>
    <div
      v-else-if="showType === 'table'"
      class="detail-item">
      <table-items
        :field-form="fieldForm"
        :field-list="addFieldList"
        :prop-prefix="propPrefix"
        :disabled="disabled"
        @delete="deleteClick"
        @change="formChange"
      />
      <div class="add-btn">
        <el-button type="text" @click="addClick">
          <i :class="'l-plus' | iconPre" />
          {{ btnName }}
        </el-button>
      </div>
    </div>
  </div>
</template>

<script>
import TableItems from './TableItems'

import { objDeepCopy } from '@/utils'
import Emitter from 'element-ui/lib/mixins/emitter'

export default {
  // 明细表格
  name: 'DetailTable',

  components: {
    FormItems: () => import('../Form/FormItems'),
    TableItems
  },

  mixins: [Emitter],

  props: {
    title: String,
    showType: {
      type: String,
      default: 'defalut' // defalut table
    },
    propPrefix: String,
    btnName: String,
    addFieldList: Array,
    addFieldForm: Object,
    fieldForm: {
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
    formChange(item, index, value, valueList) {
      this.$emit('change', item, index, value, valueList)
      this.dispatch('ElFormItem', 'el.form.change', this.fieldForm)
    },

    addClick() {
      this.fieldList.push(objDeepCopy(this.addFieldList))
      this.fieldForm.push(objDeepCopy(this.addFieldForm))
    },

    deleteClick(index) {
      this.fieldList.splice(index, 1)
      this.fieldForm.splice(index, 1)
    }
  }
}
</script>

<style lang="scss" scoped>
.detail-table {
  font-size: 14px;
  line-height: inherit;

  .form-items {
    padding: 0;
  }

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

  .add-btn {
    text-align: right;
    padding-right: 10px;
    .lego-l-plus {
      font-size: 12px;
    }
  }
}
</style>
