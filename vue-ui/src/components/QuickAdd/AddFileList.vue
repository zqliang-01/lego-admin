<template>
  <section class="file-cont">
    <flexbox class="f-header">
      <i :class="'file' | iconPre"/>
      <div class="f-name">附件</div>
    </flexbox>
    <div class="f-body">
      <flexbox
        v-for="(item, index) in data"
        :key="index"
        class="f-item">
        <img
          :src="item.icon"
          class="f-img">
        <div class="f-name">{{ item.name }}<span class="f-size">{{ `（${item.size}）` }}</span></div>
        <div
          class="close-button"
          @click="deleteItem(item, index)">×</div>
      </flexbox>
    </div>
    <el-button type="text" @click="deleteAll">全部删除</el-button>
  </section>
</template>

<script>

export default {
  // 添加附件展示
  name: 'AddFileList',
  components: {},
  props: {
    data: Array
  },
  data() {
    return {}
  },
  computed: {},
  watch: {},
  mounted() {},

  beforeDestroy() {},
  methods: {
    deleteItem(item, index) {
      this.$emit('delete', item, index)
    },

    deleteAll() {
      this.$emit('delete-all')
    }
  }
}
</script>

<style lang="scss" scoped>
/** 附件  */
.file-cont {
  padding: 0 10px;
  margin: 0 10px 15px;
  border: 1px dashed #dfdfdf;
  .f-header {
    padding: 8px 0 15px;
    color: #333;
    font-size: 13px;
    .wk {
      font-size: 13px;
      margin-right: 8px;
    }
    .f-name {
      font-weight: 600;
    }
  }

  .f-body {
    .f-item {
      padding: 3px 0;
      height: 25px;
      font-size: 12px;
      color: #333;

      .f-img {
        position: block;
        width: 12px;
        margin-right: 8px;
      }
      .f-size {
        color: #999;
      }
    }
  }

}

// 关闭按钮
.close-button {
  width: 30px;
  line-height: 16px;
  cursor: pointer;
  color: #ccc;
  height: 16px;
  font-size: 17px;
  text-align: center;
}

.close-button:hover {
  color: $xr-color-primary;
}
</style>
