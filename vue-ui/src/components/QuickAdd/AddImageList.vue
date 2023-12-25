<template>
  <section
    class="img-cont">
    <flexbox wrap="wrap">
      <div
        v-src:background-image="item.url || item.filePath"
        v-for="(item, index) in data"
        :key="index"
        class="img-item"
        @mouseover="mouseImgOver(item, index)"
        @mouseleave="mouseImgLeave(item, index)">
        <div
          v-if="item.showDelete"
          class="img-delete"
          @click="deleteItem(item, index)">×</div>
      </div>
      <div class="img-item-add">
        <input
          type="file"
          class="img-item-iput"
          accept="image/*"
          multiple
          @change="uploadFile">
      </div>
    </flexbox>
    <el-button type="text" @click="deleteAll">全部删除</el-button>
  </section>
</template>

<script>

export default {
  // 添加图片展示
  name: 'AddImageList',
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
    /**
     *  鼠标移入和移除 图片区域
     */
    mouseImgOver(item, index) {
      item.showDelete = true
      this.$set(this.data, index, item)
    },
    mouseImgLeave(item, index) {
      item.showDelete = false
      this.$set(this.data, index, item)
    },

    deleteItem(item, index) {
      this.$emit('delete', item, index)
    },

    deleteAll() {
      this.$emit('delete-all')
    },

    uploadFile(event) {
      this.$emit('upload', event)
    }
  }
}
</script>

<style lang="scss" scoped>
/** 图片  */
.img-cont {
  padding: 0 10px;
  margin-bottom: 15px;
  .img-item {
    width: 98px;
    height: 98px;
    border: 1px solid #ccc;
    display: inline-block;
    margin: 0 4px 4px 0;
    background-size: contain;
    background-repeat: no-repeat;
    background-position: center;
    position: relative;
    .img-delete {
      position: absolute;
      cursor: pointer;
      top: 0;
      right: 0;
      width: 20px;
      height: 20px;
      line-height: 20px;
      text-align: center;
      font-size: 17px;
      background-color: #666;
      color: white;
    }
  }
  .img-item-add {
    width: 98px;
    height: 98px;
    line-height: 98px;
    font-size: 60px;
    color: #ccc;
    text-align: center;
    margin: 0 4px 4px 0;
    cursor: pointer;
    display: inline-block;
    border-width: 1px;
    border-style: dashed;
    border-color: #ddd;
    position: relative;
    font-weight: 100;
    .img-item-iput {
      position: absolute;
      top: 0;
      right: 0;
      height: 98px;
      width: 98px;
      opacity: 0;
      cursor: pointer;
    }
  }
  .img-item-add:before {
    width: 2px;
    height: 39.5px;
    content: ' ';
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    background-color: $xr-color-primary;
  }
  .img-item-add:after {
    width: 39.5px;
    height: 2px;
    content: ' ';
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    background-color: $xr-color-primary;
  }
}

</style>
