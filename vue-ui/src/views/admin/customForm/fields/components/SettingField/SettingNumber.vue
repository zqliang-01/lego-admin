<template>
  <div class="setting-number">
    <flexbox
      align="flex-start"
      justify="flex-start"
      direction="column"
      class="setting-number-item">
      <el-checkbox v-model="limitChecked" @change="limitChange">
        限制数值范围
      </el-checkbox>
      <flexbox
        v-if="limitChecked"
        align="center"
        justify="flex-start"
        class="number-range-body">
        <flexbox-item>
          <el-input-number
            v-model="minNumRestrict"
            :controls="false"
            placeholder="最小值"
            @change="handleChangeNumber('minNumRestrict')" />
        </flexbox-item>
        <div class="number-range-text">~</div>
        <flexbox-item>
          <el-input-number
            v-model="maxNumRestrict"
            :controls="false"
            placeholder="最大值"
            @change="handleChangeNumber('maxNumRestrict')" />
        </flexbox-item>
      </flexbox>
    </flexbox>
  </div>
</template>

<script>
import { isEmpty } from '@/utils/types'

export default {
  name: 'SettingNumber',
  props: {
    field: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      limitChecked: false,
      minNumRestrict: undefined, // 定义为 undefined 防止input number自动填充0
      maxNumRestrict: undefined
    }
  },
  watch: {
    field: {
      handler() {
        if ([
          'number',
          'float',
          'percent'
        ].includes(this.field.formType)) {
          if (!this.field.hasOwnProperty('minNumRestrict')) {
            this.field.minNumRestrict = null
          }
          if (!this.field.hasOwnProperty('maxNumRestrict')) {
            this.field.maxNumRestrict = null
          }
          this.minNumRestrict = isEmpty(this.field.minNumRestrict) ? undefined : Number(this.field.minNumRestrict)
          this.maxNumRestrict = isEmpty(this.field.maxNumRestrict) ? undefined : Number(this.field.maxNumRestrict)
          this.limitChecked = !isEmpty(this.minNumRestrict) || !isEmpty(this.maxNumRestrict)
        }
      },
      deep: true,
      immediate: true
    }
  },
  methods: {
    limitChange() {
      if (!this.limitChecked) {
        this.minNumRestrict = undefined
        this.maxNumRestrict = undefined

        this.field.minNumRestrict = ''
        this.field.maxNumRestrict = ''
      }
    },
    handleChangeNumber(type) {
      const current = this[type]
      const len = String(current || '')
        .replace('.', '')
        .replace('-', '')
        .length

      const maxlength = this.field.formType === 'percent' ? 10 : 15
      if (len > maxlength) {
        this.$message.error(`最多支持${maxlength}位数字`)
        this.field[type] = null
        return
      }

      const min = this.minNumRestrict
      const max = this.maxNumRestrict

      if (!isEmpty(min) && !isEmpty(max)) {
        if (Number(min) > Number(max)) {
          this.$message.error('请输入正确的数值范围')
          this.field[type] = null
        }
      }
      const minNum = isEmpty(min) ? '' : min
      const maxNum = isEmpty(max) ? '' : max
      this.field.minNumRestrict = this.minNumRestrict !== null ? String(minNum) : null
      this.field.maxNumRestrict = this.maxNumRestrict !== null ? String(maxNum) : null
    }
  }
}
</script>

<style>
.setting-number-tooltip {
  max-width: 250px;
}
</style>
<style scoped lang="scss">
.setting-number-item {
  height: 32px;
  &:nth-child(1) {
    height: auto;
    margin-top: 10px;
  }
  .el-checkbox {
    margin-right: 8px;
  }
  .el-tooltip {
    margin: 0 10px 0 5px;
  }
  .el-select {
    width: 70px;
  }
  .el-input-number {
    width: 100%;
  }
  .number-range-body {
    margin-top: 10px;
  }
  .number-range-text {
    padding: 0 10px;
  }
}
</style>
