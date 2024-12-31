<template>
  <div class="distpicker-address-wrapper">
    <el-select v-model="model.province" filterable clearable :disabled="disabled" placeholder="省" @change="handleSelectProvince">
      <el-option
        v-for="(item, index) in provinces"
        :value="item.code"
        :key="index"
        :label="item.name"/>
    </el-select>
    <el-select v-model="model.city" filterable clearable :disabled="disabled" placeholder="市" @change="handleSelectCity">
      <el-option
        v-for="(item, index) in cities"
        :value="item.code"
        :key="index"
        :label="item.name"/>
    </el-select>
    <el-select v-model="model.area" filterable clearable :disabled="disabled" placeholder="区">
      <el-option
        v-for="(item, index) in areas "
        :value="item.code"
        :key="index"
        :label="item.name"/>
    </el-select>
    <el-input v-model="model.detail" :disabled="disabled" placeholder="详细地址"/>
  </div>
</template>

<script>
import DISTRICTS from '@/utils/data/address-data.js'

export default {
  name: 'Address',
  props: {
    value: {
      type: Object,
      default: () => {
        return {
          province: '',
          city: '',
          area: '',
          detail: ''
        }
      }
    },
    item: Object,
    disabled: Boolean
  },
  computed: {
    model: {
      get() {
        const that = this
        return new Proxy(this.value, {
          get(target, key) {
            return Reflect.get(target, key)
          },
          set(target, key, val) {
            Reflect.set(target, key, val)
            if (key === 'province') {
              Reflect.set(target, 'city', '')
              Reflect.set(target, 'area', '')
            } else if (key === 'city') {
              Reflect.set(target, 'area', '')
            }
            that.$emit('input', target)
            that.handleChange(target)
            return true
          }
        })
      },
      set(val) {
        this.emit('input', val)
      }
    }
  },
  data() {
    return {
      provinces: [],
      cities: [],
      areas: []
    }
  },
  created() {
    this.provinces = DISTRICTS
    this.cities = this.value && this.value.province ? this.getDataList(this.value.province, this.provinces) : []
    this.areas = this.value && this.value.city ? this.getDataList(this.value.city, this.cities) : []
  },
  methods: {
    handleSelectProvince(code) {
      this.cities = this.getDataList(code, this.provinces)
      this.cleanList('areas')
    },
    handleSelectCity(code) {
      this.areas = this.getDataList(code, this.cities)
    },
    getDataList(value, list) {
      for (let index = 0; index < list.length; index++) {
        const item = list[index]
        if (item.code === value) {
          return item.children
        }
      }
      return []
    },
    cleanList(name) {
      this[name] = []
    },
    handleChange(val) {
      this.$emit('change', val)
    }
  }
}
</script>

<style lang="scss">
.distpicker-address-wrapper {
  color: #9caebf;

  .el-select {
    width: 30%;
    margin-right: 5px;
  }
  .el-input {
    margin-top: 5px;
  }
}
</style>
