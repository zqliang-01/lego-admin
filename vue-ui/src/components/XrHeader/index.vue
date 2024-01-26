<template>
  <flexbox class="xr-header">
    <div
      v-if="!!iconClass"
      :style="{ backgroundColor: iconColor }"
      class="xr-header__icon">
      <i :class="iconClass | iconPre" />
    </div>

    <div class="xr-header__label">
      <slot v-if="$slots.label" name="label" />
      <template v-else>{{ label }}</template>
    </div>

    <el-input
      v-if="showSearch"
      :placeholder="placeholder"
      v-model="search"
      v-bind="inputAttr"
      :class="{'is-text': searchIconType === 'text'}"
      :style="{'margin-top': ftTop}"
      class="xr-header__search"
      @input="inputChange"
      @keyup.enter.native="searchClick">

      <el-button
        v-if="searchIconType === 'text'"
        slot="append"
        type="primary"
        @click.native="searchClick">搜索</el-button>
      <el-button
        v-else
        slot="append"
        icon="el-icon-search"
        @click.native="searchClick"/>
    </el-input>

    <el-button
      v-if="showSave"
      class="xr-btn--orange"
      icon="el-icon-plus"
      type="primary"
      @click="createClick">
      新建
    </el-button>

    <div :style="{ top: ftTop }" class="xr-header__ft">
      <slot name="ft" />
    </div>
  </flexbox>
</template>

<script>

export default {
  // 公共列表搜索头部
  name: 'XrHeader',
  components: {},
  // inheritAttrs: false,
  props: {
    iconClass: [String, Array],
    iconColor: String,
    label: String,
    // value: String,

    showSearch: {
      type: Boolean,
      default: false
    },
    showSave: {
      type: Boolean,
      default: false
    },
    // 图标形式的搜索
    searchIconType: {
      type: String,
      default: 'text' // icon
    },
    placeholder: {
      type: String,
      default: '请输入内容'
    },
    ftTop: {
      type: String,
      default: '15px'
    },

    content: [String, Number],

    inputAttr: {
      type: Object,
      default: () => {}
    }
  },
  data() {
    return {
      search: ''
    }
  },
  computed: {},
  watch: {
    content: {
      handler() {
        if (this.search != this.content) {
          this.search = this.content
        }
      },
      immediate: true
    }
  },
  mounted() {
  },

  beforeDestroy() {},
  methods: {
    inputChange() {
      this.$emit('update:content', this.search)
    },

    searchClick() {
      this.$emit('search', this.search)
    },

    createClick() {
      this.$emit('create')
    }
  }
}
</script>

<style lang="scss" scoped>
.xr-header {
  padding: 15px;
  position: relative;
  &__icon {
    width: 30px;
    height: 30px;
    text-align: center;
    line-height: 30px;
    border-radius: $xr-border-radius-base;
    margin-right: 10px;
    .wk {
      color: white;
      font-size: 18px;
    }
  }
  &__label {
    font-size: 16px;
    color: #333333;
    font-weight: 600;
  }

  &__search {
    width: 300px;
    margin: 0 0 0 -150px;
    position: absolute;
    left: 50%;
    top: 0;
  }

  &__search.is-text {
    ::v-deep .el-input-group__append {
      background-color: $xr-color-primary;
      border-color: $xr-color-primary;
      color: white;
    }
  }

  &__ft {
    position: absolute;
    right: 15px;
  }

  .xr-btn--orange {
    position: absolute;
    right: 20px;
  }
}
</style>
