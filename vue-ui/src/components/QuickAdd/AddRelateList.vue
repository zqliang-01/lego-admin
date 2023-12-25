<template>
  <section
    class="c-cont">
    <flexbox class="c-header">
      <i :class="type | iconPre" />
      <div class="c-name">{{ typeName }}</div>
    </flexbox>
    <div class="c-body">
      <flexbox wrap="wrap">
        <flexbox
          v-for="(item, index) in data"
          :key="index"
          class="c-item">
          <div class="c-item-name">{{ getItemName(item) }}</div>
          <div
            class="c-item-close"
            @click="deleteItem(item, index)">×</div>
        </flexbox>
      </flexbox>
    </div>
  </section>
</template>

<script>
/**
 * 名称字段
 */
export default {
  // 添加相关业务信息展示
  name: 'AddRelateList',
  components: {},
  props: {
    data: Array,
    // 业务关键字
    type: {
      type: [String, Number],
      require: true
    },
    label: String
  },
  data() {
    return {
    }
  },
  computed: {
    typeName() {
      if (this.label) {
        return this.label
      }

      return {
        customer: '客户',
        contacts: '联系人',
        business: '商机',
        contract: '合同'
      }[this.type]
    }
  },
  watch: {},
  mounted() {},

  beforeDestroy() {},
  methods: {
    getItemName(data) {
      const key = {
        customer: 'customerName',
        contacts: 'name',
        business: 'businessName',
        contract: 'name'
      }[this.type]
      return data[key] || data.name
    },

    deleteItem(item, index) {
      this.$emit('delete', item, index, this.type)
    }
  }
}
</script>

<style lang="scss" scoped>
.c-cont {
  padding: 0 10px;
  margin: 0 10px 15px;
  border: 1px dashed #dfdfdf;
  .c-header {
    padding: 8px 0 15px;
    font-size: 13px;
    color: #333;

    .wk {
      font-size: 14px;
      color: #333;
      margin-right: 8px;
    }
    .c-name {
      font-weight: 600;
    }
  }

  .c-body {
    margin-bottom: 10px;
    .c-item {
      height: 24px;
      border-radius: 12px;
      padding: 0 8px;
      margin: 0 5px 5px 0;
      background-color: $xr-color-primary;
      color: white;
      width: auto;
      cursor: pointer;
      .c-item-name {
        font-size: 12px;
      }
      .c-item-close {
        padding-left: 5px;
        font-size: 17px;
      }
    }
  }
}
</style>
