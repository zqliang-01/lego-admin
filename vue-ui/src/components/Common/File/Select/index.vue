<template>
  <div class="file-select" @click="handleClick">
    <slot />
  </div>
</template>

<script>

export default {
  // 用this.$file.select()替代
  name: 'FileSelect',
  components: {},
  props: {
    name: {
      type: String,
      default: 'file'
    },
    disabled: Boolean,
    multiple: Boolean,
    accept: {
      type: String,
      default: '*.*' // *.* image/*
    }
  },
  data() {
    return {}
  },
  computed: {},
  watch: {},
  mounted() {},

  beforeDestroy() {},
  methods: {
    handleChange(ev) {
      const files = ev.target.files

      if (!files) return
      this.$emit('change', files, ev)
    },

    handleClick() {
      if (!this.disabled) {
        this.$file.select({
          name: this.name,
          multiple: this.multiple,
          accept: this.accept
        }).then(ev => {
          this.handleChange(ev)
        })
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.file-select {
  &__input {
    display: none;
  }
}
</style>
