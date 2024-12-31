<template>
  <field-wrapper
    :activate="activate"
    :field="field"
    :control-flag="controlFlag"
    :show-border="showBorder"
    class="field-map-position"
    @click="emitClick"
    @action="handleAction">
    <flexbox class="box-address">
      <flexbox class="box-select">
        <div class="placeholder">
          {{ address.province || '省' }}
        </div>
        <i class="el-icon-arrow-down el-icon--right"/>
      </flexbox>
      <flexbox class="box-select">
        <div class="placeholder">
          {{ address.city || '市' }}
        </div>
        <i class="el-icon-arrow-down el-icon--right"/>
      </flexbox>
      <flexbox class="box-select">
        <div class="placeholder">
          {{ address.area || '区' }}
        </div>
        <i class="el-icon-arrow-down el-icon--right"/>
      </flexbox>
    </flexbox>
    <div class="box-detail">
      <div class="placeholder">
        {{ address.detail || '详细地址' }}
      </div>
    </div>

  </field-wrapper>
</template>

<script>
import FieldWrapper from './FieldWrapper'
import mixins from './mixins'
import { getName } from '@/utils/address'

export default {
  name: 'FieldAddress',
  components: {
    FieldWrapper
  },
  mixins: [mixins],
  computed: {
    address() {
      const address = this.field.defaultValue ? this.field.defaultValue : {}
      const province = getName(address.province)
      const city = getName(address.city)
      const area = getName(address.area)
      const detail = address.detail ? address.detail : ''
      return {
        province: province,
        city: city,
        area: area,
        detail: detail
      }
    }
  },
  data() {
    return {
    }
  },
  watch: {
  },
  mounted() {
  },
  methods: {}
}
</script>

<style scoped lang="scss">
.placeholder {
  color: #999;
}
.box-address {
  width: 100%;
}
.box-select {
  width: 30%;
  margin-right: 5px;
  color: #333;
  border: 1px solid #dcdfe6;
  border-radius: $xr-border-radius-base;
  padding: 10px;
  div {
    flex: 1;
    white-space: nowrap;
    overflow: hidden;
  }
}
.box-detail {
  width: 100%;
  border: 1px solid #dcdfe6;
  border-radius: $xr-border-radius-base;
  margin-top: 10px;
  padding: 10px;
}
</style>
