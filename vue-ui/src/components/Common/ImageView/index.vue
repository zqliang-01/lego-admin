<template>
  <el-image
    v-if="src"
    :src="url"
    :style="{height: height, ...styleObj, cursor: 'pointer'}"
    fit="contain"
    @click="handleView"/>
</template>

<script>
import { filePreviewUrl } from '@/api/common'

export default {
  name: 'ImageView',

  components: {},

  props: {
    height: {
      type: String,
      default: '100%'
    },
    src: String
  },

  data() {
    return {
      styleObj: {}
    }
  },

  computed: {
    url() {
      return filePreviewUrl + this.src
    }
  },
  created() {
  },

  mounted() {},

  beforeDestroy() {},

  methods: {
    handleView() {
      this.$previewImage.preview({
        data: [this.src]
      })
    }
  }
}
</script>

<style lang="scss" scoped>

</style>
