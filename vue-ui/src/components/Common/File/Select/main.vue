<template>
  <input
    ref="fileInput"
    :name="data.name"
    :multiple="data.multiple"
    :accept="data.accept"
    style="display: none;"
    type="file"
    @change="handleChange" >
</template>

<script>
import merge from '@/utils/merge'

const DefaultFileSelectData = {
  name: 'file',
  accept: '*.*',
  multiple: true
}

export default {
  // 附件选择
  name: 'File',

  components: {},

  props: {},

  data() {
    return {
      data: {},
      resolve: null
    }
  },

  computed: {},

  watch: {},

  created() {},

  mounted() {
  },

  beforeDestroy() {},

  methods: {
    handleChange(ev) {
      this.resolve(ev)
    },

    select(data) {
      this.data = merge({ ...DefaultFileSelectData }, data || {})
      this.$refs.fileInput.value = null
      this.$refs.fileInput.click()
      return new Promise((resolve, reject) => {
        this.resolve = resolve
      })
    }
  }
}
</script>

<style lang="scss" scoped>

</style>
