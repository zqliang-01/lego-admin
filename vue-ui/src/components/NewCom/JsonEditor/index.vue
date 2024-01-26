<template>
  <div class="code-json-editor">
    <vue-json-editor
      v-model="jsonContent"
      :show-btns="false"
      :expanded-on-start="true"
      mode="code"
      lang="zh"
      @json-change="onJsonChange"
      @json-save="onJsonSave"
      @has-error="onError"
    />
  </div>
</template>

<script>
import vueJsonEditor from 'vue-json-editor'
export default {
  components: { vueJsonEditor },
  props: {
    value: {
      type: String,
      default: ''
    }
  },
  watch: {
    value() {
      this.jsonContent = JSON.parse(this.value ? this.value : '[]')
    }
  },
  data() {
    return {
      hasJsonFlag: true, // json是否验证通过
      jsonContent: {}
    }
  },
  mounted() {
    this.jsonContent = JSON.parse(this.value ? this.value : '[]')
  },
  methods: {
    onJsonChange(value) {
      this.onJsonSave(value)
    },
    onJsonSave(value) {
      this.jsonContent = value
      this.hasJsonFlag = true

      const textValue = JSON.stringify(value, null, 2)
      this.$emit('input', textValue)
      this.$emit('change', textValue)
    },
    onError(value) {
      this.hasJsonFlag = false
    }
  }
}
</script>

<style lang="scss" scoped>
.code-json-editor {
  ::v-deep .jsoneditor-poweredBy {
    display: none !important;
  }
}
</style>
