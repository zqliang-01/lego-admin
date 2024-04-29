<template>
    <div class="lego-form-item" :style="{ padding: padding }">
      <label
        class="lego-form-item-label lego-form-item-label-required"
        :class="{ required: required }"
      >
        {{ label }}
      </label>
      <div class="lego-form-item-other">
        <el-form-item :prop="code" :rules="itemRule">
          <slot />
        </el-form-item>
      </div>
    </div>
</template>
<script>
export default {
  name: 'LegoFormItem',
  props: {
    code: String,
    label: {
      type: String,
      default: ''
    },
    width: {
      type: Number,
      default: 80
    },
    padding: {
      type: String,
      default: '0'
    },
    required: {
      type: Boolean,
      default: false
    }
  },
  computed: {
    itemRule() {
      if (this.required) {
        return [{ required: true, message: this.label + '不能为空' }]
      }
      return []
    }
  },
  data() {
    return {}
  }
}
</script>
<style lang="scss" scoped>
$font-color2: rgba(0, 0, 0, 0.65);
.lego-form-item {
  display: inline-block;
  margin: 5px 20px 5px 0;

  &:after {
    content: "";
    display: block;
    width: 100%;
    height: 0;
    float: none;
    clear: both;
  }

  &:before {
    content: "";
    display: table;
  }

  &-label {
    float: left;
    text-align: right;
    vertical-align: middle;
    font-size: 14px;
    line-height: 40px;
    margin-right: 5px;
    color: $font-color2;
    box-sizing: border-box;
    cursor: default;
  }

  &-other {
    display: inline-block;
    position: relative;
    line-height: 32px;
    font-size: 14px;
  }

  &-label.required:before {
    content: "*";
    font-size: 16px;
    margin-right: 1px;
    color: #f00;
  }
}
</style>
