<template>
  <div v-if="showAttribute" class="ext-attribute">
    <lego-create-sections title="扩展信息">
      <el-tooltip slot="tips" effect="dark" placement="top">
        <span slot="content">
          特殊字段类型需定义扩展信息属性，一般脱离单表操作的字段才需设置
        </span>
        <i :class="'help lego-help-tips' | iconPre"/>
      </el-tooltip>
      <AttributeAddress
        v-if="fieldForm.formType === 'address'"
        v-bind="$attrs"
        :fieldForm="fieldForm"  />
    </lego-create-sections>
  </div>
</template>
<script>
import AttributeAddress from './AttributeAddress'
import LegoCreateSections from '@/components/Lego/LegoCreateSections'
export default {
  components: {
    AttributeAddress,
    LegoCreateSections
  },
  props: {
    fieldForm: {
      type: Object,
      default: () => {
        return {}
      }
    }
  },
  computed: {
    showAttribute() {
      return ['address'].includes(this.fieldForm.formType)
    }
  },
  data() {
    return {
    }
  },
  watch: {
    'fieldForm.formType'(value) {
      if (!this.showAttribute) {
        this.$set(this.fieldForm, 'attributes', undefined)
      }
    }
  },
  mounted() {
  },
  methods: {
  }
}
</script>

<style lang="scss" scoped>
.ext-attribute {
  padding-top: 10px;
}
</style>
