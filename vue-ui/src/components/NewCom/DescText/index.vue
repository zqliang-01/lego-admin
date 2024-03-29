<template>
  <div class="desc-text">
    <tinymce
      v-bind="$attrs"
      :toolbar="[]" :init="{
        statusbar: false,
        placeholder: '描述文字内容',
        content_style: ' * {color: #262626; margin: 0;} body { font-size: 14px; }',
        quickbars_selection_toolbar: false,
        contextmenu: '',
        plugins: 'autoresize',
        autoresize_bottom_margin: 0
    }" />
  </div>
</template>

<script>
import Tinymce from '@/components/Tinymce'

export default {
  // 描述文字
  name: 'DescText',

  components: {
    Tinymce
  },

  inheritAttrs: false,

  props: {},

  data() {
    return {
    }
  },

  computed: {},

  watch: {},

  created() {},

  mounted() {},

  beforeDestroy() {},

  methods: {}
}
</script>

<style lang="scss">
.desc-text {
  .tox-tinymce {
    border: none;
  }

  .tox .tox-edit-area__iframe {
    background-color: unset;
  }
}
</style>
