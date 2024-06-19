<template>
  <div class="cell-div">
    <el-radio v-model="type_" :label="label" @change="change">
      <span class="cell-symbol">{{ tag_ }}</span>
      不固定
    </el-radio>
  </div>
</template>

<script>
import { UNFIXED } from '../../constant/filed'
import watchValue from '../../mixins/watchValue'

export default {
  mixins: [watchValue],
  props: {
    type: {
      type: String,
      default: UNFIXED
    },
    tag: {
      type: String,
      default: UNFIXED
    }
  },
  data() {
    return {
      label: UNFIXED,
      type_: this.type,
      proxy: this.tag
    }
  },
  computed: {
    tag_: {
      get() {
        return UNFIXED
      },
      set(newValue) {
        if (this.type_ !== UNFIXED) {
          return
        }
        this.proxy = UNFIXED
      }
    }
  },
  methods: {
    change() {
      this.$emit('type-changed', this.type_)
      this.$emit('tag-changed', this.tag_)
    }
  }
}
</script>
<style lang="scss" scoped>
.cell-symbol {
  color: #67C23A;
}
.cell-div {
    margin-bottom: 8px;
}
</style>
