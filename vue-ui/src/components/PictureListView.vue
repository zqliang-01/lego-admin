<template>
  <div class="picture-list-view">
    <div
      v-for="(imgItem, k) in list"
      :key="k"
      class="img-list-item"
      @click="imgZoom(list, k)">
      <img
        v-src="imgItem.filePath || imgItem.url"
        :key="imgItem.filePath || imgItem.url">
    </div>
  </div>
</template>

<script>
/**
 * 图片文件列表
 * @props list {Array} 图片列表数据
 */
export default {
  name: 'PictureListView',
  props: {
    list: {
      type: Array,
      required: true
    }
  },
  methods: {
    // 放大图片
    imgZoom(val, k) {
      this.$previewFile.preview({
        index: k,
        data: val.map(function(item, index, array) {
          return {
            url: item.filePath || item.url,
            name: item.name
          }
        })
      })
    }
  }
}
</script>

<style scoped lang="scss">
  .picture-list-view {
    .img-list-item {
      display: inline-block;
      position: relative;
      margin-right: 10px;
      cursor: pointer;
      img {
        max-width: 80px;
        max-height: 60px;
      }
    }
  }
</style>
